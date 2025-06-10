package at.spengergasse.projekt.controller;

import at.spengergasse.projekt.model.Termin;
import at.spengergasse.projekt.model.CsvManager;
import at.spengergasse.projekt.model.PfadManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.HBox;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Optional;

/**
 * Controller zur Verwaltung und Anzeige von Terminen in der SchulManager-App.
 * Bietet Funktionen zum Speichern, Bearbeiten und Löschen von Terminen.
 */
public class TerminControllerFX {

    private final String username;
    private final ObservableList<Termin> termine;
    private final TableView<Termin> tableView;

    private final TextField titelField = new TextField();
    private final DatePicker datumPicker = new DatePicker();
    private final ComboBox<String> artBox = new ComboBox<>();
    private final TextField notizField = new TextField();
    private final Button speichernButton = new Button("Speichern");
    private final Button löschenButton = new Button("Löschen");

    /**
     * Erstellt einen neuen Controller und lädt die Termine für den Benutzer.
     *
     * @param username Aktueller Benutzername.
     */
    public TerminControllerFX(String username) {
        this.username = username;
        this.termine = FXCollections.observableArrayList();
        this.tableView = createTable();
        loadTermine();
    }

    /**
     * Gibt die Tabelle mit den Terminen zurück.
     *
     * @return TableView mit Termin-Objekten.
     */
    public TableView<Termin> getTable() {
        return tableView;
    }

    /**
     * Erstellt das Eingabeformular zur Erstellung eines neuen Termins.
     *
     * @return HBox mit Eingabefeldern und Speichern-Button.
     */
    public HBox getFormular() {
        titelField.setPromptText("Titel");
        datumPicker.setPromptText("Datum");
        artBox.getItems().setAll("Prüfung", "Hausaufgabe", "Event", "Sonstiges");
        artBox.setPromptText("Art");
        notizField.setPromptText("Notiz (optional)");

        speichernButton.setOnAction(e -> handleSpeichern());

        HBox box = new HBox(10, titelField, datumPicker, artBox, notizField, speichernButton);
        box.setPadding(new Insets(10));
        box.setAlignment(Pos.CENTER);
        return box;
    }

    /**
     * Erstellt die Aktionsleiste mit dem Löschen-Button und zugehörigem Verhalten.
     *
     * @return HBox mit Lösch-Funktionalität.
     */
    public HBox getAktionen() {
        löschenButton.setDisable(true);
        löschenButton.setOnAction(e -> handleLöschen());

        tableView.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            löschenButton.setDisable(newVal == null);
        });

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

    /**
     * Erstellt und konfiguriert die Tabelle zur Anzeige der Termine.
     *
     * @return Eine vollständig konfigurierte TableView.
     */
    private TableView<Termin> createTable() {
        TableView<Termin> table = new TableView<>(termine);
        table.setEditable(true);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

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

        table.getColumns().addAll(titelCol, datumCol, artCol, notizCol);
        table.setPrefHeight(300);
        table.setMaxWidth(Double.MAX_VALUE);

        return table;
    }

    /**
     * Verarbeitet das Speichern eines neuen Termins nach Validierung.
     * Zeigt Warnung bei vergangenem Datum.
     */
    private void handleSpeichern() {
        String titel = titelField.getText().trim();
        LocalDate datum = datumPicker.getValue();
        String art = artBox.getValue();
        String notiz = notizField.getText().trim();

        if (titel.isEmpty() || datum == null || art == null) {
            showFehler("Bitte füllen Sie alle Pflichtfelder aus.");
            return;
        }

        if (datum.isBefore(LocalDate.now())) {
            Alert warnung = new Alert(Alert.AlertType.CONFIRMATION);
            warnung.setTitle("Achtung: Vergangenes Datum");
            warnung.setHeaderText("Der Termin liegt in der Vergangenheit.");
            warnung.setContentText("Willst du ihn trotzdem speichern?");

            ButtonType speichern = new ButtonType("Trotzdem speichern");
            ButtonType abbrechen = new ButtonType("Abbrechen", ButtonBar.ButtonData.CANCEL_CLOSE);
            warnung.getButtonTypes().setAll(speichern, abbrechen);

            Optional<ButtonType> result = warnung.showAndWait();
            if (result.isEmpty() || result.get() == abbrechen) {
                return;
            }
        }

        Termin neu = new Termin(titel, datum, art, notiz);
        termine.add(neu);
        saveTermine();

        titelField.clear();
        datumPicker.setValue(null);
        artBox.setValue(null);
        notizField.clear();
    }

    /**
     * Löscht den aktuell ausgewählten Termin aus der Tabelle.
     */
    private void handleLöschen() {
        Termin ausgewählt = tableView.getSelectionModel().getSelectedItem();
        if (ausgewählt != null) {
            termine.remove(ausgewählt);
            saveTermine();
        }
    }

    /**
     * Lädt alle gespeicherten Termine des aktuellen Benutzers.
     */
    private void loadTermine() {
        try {
            String pfad = PfadManager.getTerminPfad(username);
            termine.setAll(CsvManager.loadTermine(pfad));
        } catch (IOException e) {
            showFehler("Fehler beim Laden der Termine.");
        }
    }

    /**
     * Speichert alle aktuellen Termine in die entsprechende CSV-Datei.
     */
    private void saveTermine() {
        try {
            String pfad = PfadManager.getTerminPfad(username);
            CsvManager.saveTermine(termine, pfad);
        } catch (IOException e) {
            showFehler("Fehler beim Speichern der Termine.");
        }
    }

    /**
     * Zeigt eine Fehlermeldung in einem Alert-Dialog.
     *
     * @param msg Die anzuzeigende Fehlermeldung.
     */
    private void showFehler(String msg) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Fehler");
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }
}
