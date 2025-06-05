package at.spengergasse.projekt.controller;

import at.spengergasse.projekt.model.Termin;
import at.spengergasse.projekt.model.CsvManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.HBox;

import java.io.IOException;
import java.time.LocalDate;

public class TerminControllerFX {

    private final String username;
    private final String pfad;
    private final TableView<Termin> tableView;
    private final ScrollPane table;
    private final ObservableList<Termin> termine;

    private final TextField titelField = new TextField();
    private final DatePicker datumPicker = new DatePicker();
    private final ComboBox<String> artBox = new ComboBox<>();
    private final TextField notizField = new TextField();
    private final Button speichernButton = new Button("Speichern");
    private final Button löschenButton = new Button("Löschen");

    public TerminControllerFX(String username, String pfad) {
        this.username = username;
        this.pfad = pfad;
        this.termine = FXCollections.observableArrayList();
        this.tableView = new TableView<>(termine);
        this.table = createTable();
        loadTermine();
    }

    public ScrollPane getTable() {
        return table;
    }

    public HBox getFormular() {
        titelField.setPromptText("Titel");
        datumPicker.setPromptText("Datum");
        artBox.getItems().setAll("Prüfung", "Hausaufgabe", "Event", "Sonstiges");
        artBox.setPromptText("Art");
        notizField.setPromptText("Notiz");

        speichernButton.setOnAction(e -> handleSpeichern());

        HBox box = new HBox(10, titelField, datumPicker, artBox, notizField, speichernButton);
        box.setPadding(new Insets(10));
        box.setAlignment(Pos.CENTER);
        return box;
    }

    public HBox getAktionen() {
        löschenButton.setDisable(true);
        löschenButton.setOnAction(e -> handleLöschen());

        tableView.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            löschenButton.setDisable(newVal == null);
        });

        // RICHTIGE LÖSUNG: Auswahl löschen, wenn ins Leere geklickt wird
        tableView.setRowFactory(tv -> {
            TableRow<Termin> row = new TableRow<>();

            row.setOnMouseClicked(event -> {
                if (row.isEmpty()) {
                    tableView.getSelectionModel().clearSelection();
                    löschenButton.setDisable(true);
                }
            });

            return row;
        });

        HBox box = new HBox(10, löschenButton);
        box.setPadding(new Insets(10));
        box.setAlignment(Pos.CENTER);
        return box;
    }



    private ScrollPane createTable() {
        tableView.setEditable(true);
        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        TableColumn<Termin, String> titelCol = new TableColumn<>("Titel");
        titelCol.setCellValueFactory(data -> data.getValue().titelProperty());
        titelCol.setStyle("-fx-alignment: CENTER;");

        TableColumn<Termin, LocalDate> datumCol = new TableColumn<>("Datum");
        datumCol.setCellValueFactory(data -> data.getValue().datumProperty());
        datumCol.setStyle("-fx-alignment: CENTER;");

        TableColumn<Termin, String> artCol = new TableColumn<>("Art");
        artCol.setCellValueFactory(data -> data.getValue().artProperty());
        artCol.setStyle("-fx-alignment: CENTER;");

        TableColumn<Termin, String> notizCol = new TableColumn<>("Notiz");
        notizCol.setCellValueFactory(data -> data.getValue().notizProperty());
        notizCol.setCellFactory(TextFieldTableCell.forTableColumn());
        notizCol.setOnEditCommit(e -> {
            Termin t = e.getRowValue();
            t.setNotiz(e.getNewValue());
            saveTermine();
        });
        notizCol.setStyle("-fx-alignment: CENTER;");

        tableView.getColumns().addAll(titelCol, datumCol, artCol, notizCol);
        tableView.setPrefHeight(300);
        tableView.setMaxWidth(Double.MAX_VALUE);


        ScrollPane scrollPane = new ScrollPane(tableView);
        scrollPane.setFitToWidth(true);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.setPrefHeight(300);
        scrollPane.setStyle("-fx-hbar-policy: never;");

        return scrollPane;
    }

    private void handleSpeichern() {
        String titel = titelField.getText().trim();
        LocalDate datum = datumPicker.getValue();
        String art = artBox.getValue();
        String notiz = notizField.getText().trim();

        if (titel.isEmpty() || datum == null || art == null) {
            showFehler("Bitte füllen Sie alle Pflichtfelder aus.");
            return;
        }

        Termin neu = new Termin(titel, datum, art, notiz);
        termine.add(neu);
        saveTermine();

        titelField.clear();
        datumPicker.setValue(null);
        artBox.setValue(null);
        notizField.clear();
    }

    private void handleLöschen() {
        Termin ausgewählt = tableView.getSelectionModel().getSelectedItem();
        if (ausgewählt != null) {
            termine.remove(ausgewählt);
            saveTermine();
        }
    }

    private void loadTermine() {
        try {
            termine.setAll(CsvManager.loadTermine(pfad));
        } catch (IOException e) {
            showFehler("Fehler beim Laden der Termine.");
        }
    }

    private void saveTermine() {
        try {
            CsvManager.saveTermine(termine, pfad);
        } catch (IOException e) {
            showFehler("Fehler beim Speichern der Termine.");
        }
    }

    private void showFehler(String msg) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Fehler");
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }
}
