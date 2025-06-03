package at.spengergasse.projekt.controller;

import at.spengergasse.projekt.model.CsvManager;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.util.Optional;

/**
 * Controller für Adminfunktionen: Benutzer anzeigen, bearbeiten und löschen.
 */
public class AdminControllerFX {

    private final TableView<String[]> table;
    private final ObservableList<String[]> daten;

    /**
     * Initialisiert Controller und lädt Benutzerdaten.
     */
    public AdminControllerFX() {
        daten = FXCollections.observableArrayList();
        table = new TableView<>(daten);
        table.setPlaceholder(new Label("Keine Benutzer gefunden."));
        loadBenutzer();

        TableColumn<String[], String> nameCol = new TableColumn<>("Benutzername");
        nameCol.setCellValueFactory(param -> new SimpleStringProperty(param.getValue()[0]));

        TableColumn<String[], String> rolleCol = new TableColumn<>("Rolle");
        rolleCol.setCellValueFactory(param -> new SimpleStringProperty(param.getValue()[1]));

        table.getColumns().addAll(nameCol, rolleCol);
        table.setPrefHeight(300);
    }

    /**
     * Gibt die Benutzer-Tabelle zurück.
     */
    public TableView<String[]> getUserTable() {
        return table;
    }

    /**
     * Aktionen wie Löschen und Bearbeiten
     */
    public HBox getAktionen() {
        Button löschenButton = new Button("Löschen");
        löschenButton.setOnAction(e -> handleLöschen());
        HBox box = new HBox(10, löschenButton);
        box.setPadding(new Insets(10));
        return box;
    }

    /**
     * Formular zur Neuanlage eines Benutzers
     */
    public VBox getNeuesBenutzerFormular() {
        TextField nameField = new TextField();
        nameField.setPromptText("Benutzername");
        PasswordField pwField = new PasswordField();
        pwField.setPromptText("Passwort");

        Button anlegenButton = new Button("Benutzer anlegen");
        anlegenButton.setOnAction(e -> {
            String name = nameField.getText().trim();
            String pw = pwField.getText().trim();
            if (!name.isEmpty() && !pw.isEmpty()) {
                try {
                    CsvManager.saveUser(name, pw, "user");
                    loadBenutzer();
                    nameField.clear();
                    pwField.clear();
                } catch (IOException ex) {
                    showFehler("Fehler beim Speichern.");
                }
            }
        });

        VBox form = new VBox(8, new Label("Neuen Benutzer erstellen:"), nameField, pwField, anlegenButton);
        form.setPadding(new Insets(10));
        return form;
    }

    private void handleLöschen() {
        String[] selected = table.getSelectionModel().getSelectedItem();
        if (selected == null) return;

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Löschen bestätigen");
        confirm.setHeaderText(null);
        confirm.setContentText("Benutzer '" + selected[0] + "' wirklich löschen?");

        Optional<ButtonType> result = confirm.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                CsvManager.deleteUser(selected[0]);
                loadBenutzer();
            } catch (IOException e) {
                showFehler("Fehler beim Löschen.");
            }
        }
    }

    private void loadBenutzer() {
        try {
            daten.setAll(CsvManager.loadBenutzer());
        } catch (IOException e) {
            showFehler("Fehler beim Laden der Benutzer.");
        }
    }

    private void showFehler(String msg) {
        Alert a = new Alert(Alert.AlertType.ERROR);
        a.setTitle("Fehler");
        a.setHeaderText(null);
        a.setContentText(msg);
        a.showAndWait();
    }
}
