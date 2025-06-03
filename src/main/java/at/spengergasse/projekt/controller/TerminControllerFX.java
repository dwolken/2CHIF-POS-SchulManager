package at.spengergasse.projekt.controller;

import at.spengergasse.projekt.model.Termin;
import at.spengergasse.projekt.model.CsvManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.HBox;
import javafx.util.StringConverter;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Optional;

/**
 * Controller zur Verwaltung von Terminen, inklusive Anzeige, Hinzufügen, Löschen und Editieren.
 */
public class TerminControllerFX {

    private final String username;
    private String pfad;
    private final TableView<Termin> table;
    private final ObservableList<Termin> termine;

    private final TextField titelField = new TextField();
    private final DatePicker datumPicker = new DatePicker();
    private final ComboBox<String> artBox = new ComboBox<>();
    private final TextField notizField = new TextField();
    private final Button speichernButton = new Button("Speichern");
    private final Button löschenButton = new Button("Löschen");

    /**
     * Erstellt Controller für Terminverwaltung eines bestimmten Benutzers.
     * @param username Benutzername
     * @param pfad Pfad zur CSV-Datei
     */
    public TerminControllerFX(String username, String pfad) {
        this.username = username;
        this.pfad = pfad;
        this.termine = FXCollections.observableArrayList();
        this.table = createTable();
        loadTermine();
    }

    public TableView<Termin> getTable() {
        return table;
    }

    public HBox getFormular() {
        titelField.setPromptText("Titel");
        datumPicker.setPromptText("Datum");
        artBox.getItems().addAll("Prüfung", "Hausaufgabe", "Event", "Sonstiges");
        artBox.setPromptText("Art");
        notizField.setPromptText("Notiz");

        titelField.setPrefWidth(120);
        datumPicker.setPrefWidth(120);
        artBox.setPrefWidth(110);
        notizField.setPrefWidth(150);
        speichernButton.setPrefWidth(100);

        speichernButton.setOnAction(e -> handleSpeichern());

        HBox box = new HBox(10, titelField, datumPicker, artBox, notizField, speichernButton);
        box.setPadding(new Insets(10));
        return box;
    }

    public HBox getAktionen() {
        löschenButton.setDisable(true);
        löschenButton.setOnAction(e -> handleLöschen());

        table.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            löschenButton.setDisable(newVal == null);
        });

        HBox box = new HBox(10, löschenButton);
        box.setPadding(new Insets(10));
        return box;
    }

    private TableView<Termin> createTable() {
        TableView<Termin> table = new TableView<>(termine);
        table.setEditable(true);

        TableColumn<Termin, String> titelCol = new TableColumn<>("Titel");
        titelCol.setCellValueFactory(data -> data.getValue().titelProperty());

        TableColumn<Termin, LocalDate> datumCol = new TableColumn<>("Datum");
        datumCol.setCellValueFactory(data -> data.getValue().datumProperty());

        TableColumn<Termin, String> artCol = new TableColumn<>("Art");
        artCol.setCellValueFactory(data -> data.getValue().artProperty());

        TableColumn<Termin, String> notizCol = new TableColumn<>("Notiz");
        notizCol.setCellValueFactory(data -> data.getValue().notizProperty());
        notizCol.setCellFactory(TextFieldTableCell.forTableColumn());
        notizCol.setOnEditCommit(e -> {
            Termin t = e.getRowValue();
            t.setNotiz(e.getNewValue());
            saveTermine();
        });

        table.getColumns().addAll(titelCol, datumCol, artCol, notizCol);
        table.setPrefHeight(300);

        table.setOnMouseClicked(e -> {
            if (table.getSelectionModel().getSelectedItem() == null) {
                löschenButton.setDisable(true);
            }
        });

        return table;
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
        Termin ausgewählt = table.getSelectionModel().getSelectedItem();
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