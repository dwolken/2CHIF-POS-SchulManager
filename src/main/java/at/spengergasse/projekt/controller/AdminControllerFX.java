package at.spengergasse.projekt.controller;

import at.spengergasse.projekt.model.CsvManager;
import at.spengergasse.projekt.view.LoginViewFX;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Optional;

/**
 * Controller für die Admin-Benutzerverwaltung.
 * Ermöglicht das Anzeigen, Bearbeiten und Löschen von Benutzern.
 * Unterstützt Passwortänderungen und Benutzernamenänderungen per Doppelklick.
 */
public class AdminControllerFX {

    private final ObservableList<String[]> benutzerListe;
    private final TableView<String[]> table;
    private final TextField benutzernameField = new TextField();
    private final PasswordField neuesPasswortField = new PasswordField();
    private final ComboBox<String> rolleBox = new ComboBox<>();
    private final Button speichernButton = new Button("Passwort ändern");
    private final Button löschenButton = new Button("Löschen");
    private final Button logoutButton = new Button("Abmelden");

    /**
     * Konstruktor: Initialisiert Benutzerliste und Tabelle.
     */
    public AdminControllerFX() {
        this.benutzerListe = FXCollections.observableArrayList();
        this.table = createTable();
        loadBenutzer();
    }

    /**
     * Gibt die Tabelle mit Benutzerdaten zurück.
     * @return Tabelle mit Benutzern
     */
    public TableView<String[]> getTable() {
        return table;
    }

    /**
     * Erstellt die Aktionsbuttons (Passwort ändern, Löschen) und deren Verhalten.
     * @return HBox mit Buttons
     */
    public HBox getAktionen() {
        speichernButton.setDisable(true);
        löschenButton.setDisable(true);

        speichernButton.setOnAction(e -> handlePasswortAendern());
        löschenButton.setOnAction(e -> handleLöschen());

        table.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            boolean selected = newVal != null;
            speichernButton.setDisable(!selected);
            löschenButton.setDisable(!selected);
            if (selected) {
                benutzernameField.setText(newVal[0]);
                rolleBox.setValue(newVal[1]);
                neuesPasswortField.clear();
            } else {
                clearFields();
            }
        });

        table.setRowFactory(tv -> {
            TableRow<String[]> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (row.isEmpty()) {
                    table.getSelectionModel().clearSelection();
                    speichernButton.setDisable(true);
                    löschenButton.setDisable(true);
                    clearFields();
                    return;
                }

                if (event.getClickCount() == 2 && !row.isEmpty()) {
                    String[] selected = row.getItem();
                    TextInputDialog dialog = new TextInputDialog(selected[0]);
                    dialog.setTitle("Benutzername ändern");
                    dialog.setHeaderText(null);
                    dialog.setContentText("Neuer Benutzername:");

                    Optional<String> result = dialog.showAndWait();
                    result.ifPresent(neuerName -> {
                        if (!neuerName.trim().isEmpty() && !neuerName.equals(selected[0])) {
                            try {
                                String pass = neuesPasswortField.getText().trim();
                                if (pass.isEmpty()) pass = "admin";
                                CsvManager.deleteUser(selected[0]);
                                CsvManager.saveUser(neuerName.trim(), pass, selected[1]);
                                loadBenutzer();
                                clearFields();
                            } catch (IOException ex) {
                                showFehler("Fehler beim Umbenennen.");
                            }
                        }
                    });
                }
            });
            return row;
        });

        HBox box = new HBox(10, speichernButton, löschenButton);
        box.setAlignment(Pos.CENTER);
        box.setPadding(new Insets(10));
        return box;
    }

    /**
     * Erstellt das Formular zur Eingabe neuer Benutzer.
     * @return HBox mit Formularfeldern
     */
    public HBox getFormular() {
        benutzernameField.setPromptText("Benutzername");
        neuesPasswortField.setPromptText("Neues Passwort setzen");
        rolleBox.getItems().setAll("user", "admin");
        rolleBox.setPromptText("Rolle wählen");

        Button neuButton = new Button("Neu anlegen");
        neuButton.setOnAction(e -> handleNeuAnlegen());

        HBox box = new HBox(10, benutzernameField, neuesPasswortField, rolleBox, neuButton);
        box.setAlignment(Pos.CENTER);
        box.setPadding(new Insets(10));
        return box;
    }

    /**
     * Gibt den Logout-Button mit Verhalten zurück.
     * @return Logout-Button
     */
    public Button getLogoutButton() {
        logoutButton.setOnAction(e -> {
            Stage currentStage = (Stage) logoutButton.getScene().getWindow();
            currentStage.close();
            new LoginViewFX(new Stage());
        });
        return logoutButton;
    }

    /**
     * Erstellt die Tabelle mit Benutzername und Rolle.
     * @return Benutzer-Tabelle
     */
    private TableView<String[]> createTable() {
        TableView<String[]> tableView = new TableView<>(benutzerListe);
        tableView.setEditable(false);
        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        TableColumn<String[], String> nameCol = new TableColumn<>("Benutzername");
        nameCol.setCellValueFactory(param -> new SimpleStringProperty(param.getValue()[0]));
        nameCol.setStyle("-fx-alignment: CENTER;");

        TableColumn<String[], String> rolleCol = new TableColumn<>("Rolle");
        rolleCol.setCellValueFactory(param -> new SimpleStringProperty(param.getValue()[1]));
        rolleCol.setStyle("-fx-alignment: CENTER;");

        tableView.getColumns().addAll(nameCol, rolleCol);
        return tableView;
    }

    /**
     * Erzeugt einen neuen Benutzer.
     */
    private void handleNeuAnlegen() {
        String name = benutzernameField.getText().trim();
        String pass = neuesPasswortField.getText().trim();
        String rolle = rolleBox.getValue();

        if (name.isEmpty() || pass.isEmpty() || rolle == null || rolle.isEmpty()) {
            showFehler("Bitte alle Felder ausfüllen.");
            return;
        }

        try {
            if (CsvManager.userExists(name)) {
                showFehler("Benutzer existiert bereits.");
                return;
            }
            CsvManager.saveUser(name, pass, rolle);
            loadBenutzer();
            clearFields();
        } catch (IOException e) {
            showFehler("Fehler beim Speichern.");
        }
    }

    /**
     * Ändert das Passwort eines Benutzers.
     */
    private void handlePasswortAendern() {
        String[] selected = table.getSelectionModel().getSelectedItem();
        if (selected == null) return;

        String name = selected[0];
        String neuesPass = neuesPasswortField.getText().trim();
        String rolle = selected[1];

        if (neuesPass.isEmpty()) {
            showFehler("Bitte neues Passwort eingeben.");
            return;
        }

        try {
            CsvManager.deleteUser(name);
            CsvManager.saveUser(name, neuesPass, rolle);
            loadBenutzer();
            table.getSelectionModel().clearSelection();
            clearFields();
        } catch (IOException e) {
            showFehler("Fehler beim Aktualisieren.");
        }
    }

    /**
     * Löscht den ausgewählten Benutzer (außer den eingeloggten).
     */
    private void handleLöschen() {
        String[] selected = table.getSelectionModel().getSelectedItem();
        if (selected == null) return;

        String aktuellerUser = System.getProperty("aktuellerUser");
        if (selected[0].equals(aktuellerUser)) {
            showFehler("Du kannst dich nicht selbst löschen.");
            return;
        }

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Löschen bestätigen");
        confirm.setHeaderText(null);
        confirm.setContentText("Benutzer '" + selected[0] + "' wirklich löschen?");

        Optional<ButtonType> result = confirm.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                CsvManager.deleteUser(selected[0]);
                loadBenutzer();
                clearFields();
            } catch (IOException e) {
                showFehler("Fehler beim Löschen.");
            }
        }
    }

    /**
     * Lädt Benutzerliste neu aus CSV-Datei.
     */
    private void loadBenutzer() {
        try {
            benutzerListe.setAll(CsvManager.loadBenutzer());
        } catch (IOException e) {
            showFehler("Fehler beim Laden der Benutzer.");
        }
    }

    /**
     * Leert alle Eingabefelder.
     */
    private void clearFields() {
        benutzernameField.clear();
        neuesPasswortField.clear();
        rolleBox.setValue(null);
    }

    /**
     * Zeigt eine Fehlermeldung als Dialog.
     * @param msg Text der Fehlermeldung
     */
    private void showFehler(String msg) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Fehler");
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }
}
