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
 * Dieser Controller verwaltet die Admin-Oberfläche zur Benutzerverwaltung.
 * Funktionen: Benutzer anzeigen, hinzufügen, Passwort ändern, löschen und Logout.
 */
public class AdminControllerFX {

    /**
     * Liste aller Benutzer als ObservableList.
     */
    private final ObservableList<String[]> benutzerListe;

    /**
     * Tabelle zur Anzeige der Benutzer.
     */
    private final TableView<String[]> table;

    /**
     * Eingabefeld für den Benutzernamen.
     */
    private final TextField benutzernameField = new TextField();

    /**
     * Eingabefeld für ein neues Passwort (PasswordField).
     */
    private final PasswordField neuesPasswortField = new PasswordField();

    /**
     * Auswahlfeld für die Benutzerrolle (user/admin).
     */
    private final ComboBox<String> rolleBox = new ComboBox<>();

    /**
     * Button zum Ändern des Passworts eines bestehenden Benutzers.
     */
    private final Button speichernButton = new Button("Passwort ändern");

    /**
     * Button zum Löschen eines Benutzers.
     */
    private final Button löschenButton = new Button("Löschen");

    /**
     * Button zum Abmelden (zurück zur LoginView).
     */
    private final Button logoutButton = new Button("Abmelden");

    /**
     * Konstruktor initialisiert Tabelle und lädt Benutzerliste.
     */
    public AdminControllerFX() {
        this.benutzerListe = FXCollections.observableArrayList();
        this.table = createTable();
        loadBenutzer();
    }

    /**
     * Gibt die Benutzer-Tabelle zurück.
     * @return TableView mit Benutzerdaten
     */
    public TableView<String[]> getTable() {
        return table;
    }

    /**
     * Erstellt HBox mit Buttons für "Passwort ändern" und "Löschen".
     * @return HBox mit Aktionen
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
            }
        });

        table.setRowFactory(tv -> {
            TableRow<String[]> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (row.isEmpty()) {
                    table.getSelectionModel().clearSelection();
                    speichernButton.setDisable(true);
                    löschenButton.setDisable(true);
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
     * Erstellt das Eingabeformular für Benutzername, Passwort und Rolle.
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
     * Gibt den Logout-Button zurück. Schließt aktuelles Fenster und öffnet Login.
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
     * Erstellt die Tabelle für Benutzername und Rolle.
     * @return konfigurierte TableView
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
     * Handhabt das Anlegen eines neuen Benutzers.
     */
    private void handleNeuAnlegen() {
        String name = benutzernameField.getText().trim();
        String pass = neuesPasswortField.getText().trim();
        String rolle = rolleBox.getValue();

        if (name.isEmpty() || pass.isEmpty() || rolle == null) {
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
            benutzernameField.clear();
            neuesPasswortField.clear();
            rolleBox.setValue(null);
        } catch (IOException e) {
            showFehler("Fehler beim Speichern.");
        }
    }

    /**
     * Handhabt das Zurücksetzen des Passworts eines Benutzers.
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
        } catch (IOException e) {
            showFehler("Fehler beim Aktualisieren.");
        }
    }

    /**
     * Handhabt das Löschen eines Benutzers nach Bestätigung.
     */
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

    /**
     * Lädt die Benutzerdaten aus der CSV-Datei.
     */
    private void loadBenutzer() {
        try {
            benutzerListe.setAll(CsvManager.loadBenutzer());
        } catch (IOException e) {
            showFehler("Fehler beim Laden der Benutzer.");
        }
    }

    /**
     * Zeigt eine Fehlermeldung als Alert an.
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
