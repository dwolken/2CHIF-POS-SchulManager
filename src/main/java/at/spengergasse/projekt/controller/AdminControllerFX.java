package at.spengergasse.projekt.controller;

import at.spengergasse.projekt.model.CsvManager;
import at.spengergasse.projekt.model.PfadManager;
import at.spengergasse.projekt.view.LoginViewFX;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Optional;

/**
 * Der {@code AdminControllerFX} verwaltet die Benutzeroberfläche und Logik für die
 * Administration von Benutzerkonten. Es ermöglicht:
 * <ul>
 *   <li>Erstellen neuer Benutzer</li>
 *   <li>Löschen bestehender Benutzer</li>
 *   <li>Passwortänderung für bestehende Benutzer</li>
 *   <li>Benutzernamen umzubenennen</li>
 * </ul>
 * Die Benutzerinformationen werden als CSV-Dateien gespeichert und verwaltet.
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
     * Konstruktor. Initialisiert die Benutzerliste und Tabelle.
     */
    public AdminControllerFX() {
        this.benutzerListe = FXCollections.observableArrayList();
        this.table = createTable();
        loadBenutzer();
    }

    /**
     * Gibt die Tabelle mit den Benutzerdaten zurück.
     *
     * @return TableView mit Benutzerdaten
     */
    public TableView<String[]> getTable() {
        return table;
    }

    /**
     * Gibt die HBox mit den Buttons zum Ändern und Löschen von Benutzern zurück.
     * Enthält die zugehörige Logik bei Benutzer-Auswahl und Button-Aktionen.
     *
     * @return HBox mit Action-Buttons
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
                                CsvManager.updateBenutzername(selected[0], neuerName.trim());

                                String alterTerminPfad = PfadManager.getTerminPfad(selected[0]);
                                String alterZielePfad = PfadManager.getZielePfad(selected[0]);

                                String neuerTerminPfad = alterTerminPfad.replace(selected[0], neuerName.trim());
                                String neuerZielePfad = alterZielePfad.replace(selected[0], neuerName.trim());

                                renameFileIfExists(alterTerminPfad, neuerTerminPfad);
                                renameFileIfExists(alterZielePfad, neuerZielePfad);

                                String alteConfig = System.getProperty("user.home") + "/SchulManager/data/" + selected[0] + "_config.properties";
                                String neueConfig = System.getProperty("user.home") + "/SchulManager/data/" + neuerName.trim() + "_config.properties";
                                renameFileIfExists(alteConfig, neueConfig);

                                PfadManager.setTerminPfad(neuerName.trim(), neuerTerminPfad);
                                PfadManager.setZielePfad(neuerName.trim(), neuerZielePfad);

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
     * Erstellt das Formular zur Erstellung neuer Benutzer.
     *
     * @return HBox mit Eingabefeldern und Button
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
     * Erstellt die Logout-Schaltfläche.
     *
     * @return Button für Logout
     */
    public void handleLogout(Event e) {
        Stage stage;

        if (e instanceof ActionEvent) {
            stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
        } else if (e instanceof WindowEvent) {
            stage = (Stage) ((WindowEvent) e).getSource();
        } else {
            throw new IllegalArgumentException("Unbekannter Event-Typ beim Logout");
        }

        stage.close();
        new LoginViewFX(new Stage());
    }


    /**
     * Erstellt die Tabelle zur Anzeige aller Benutzer.
     *
     * @return TableView mit Name und Rolle
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
     * Verarbeitet das Erstellen eines neuen Benutzers.
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
     * Verarbeitet die Änderung des Passworts für den ausgewählten Benutzer.
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
     * Verarbeitet das Löschen eines Benutzers (optional mit allen Dateien).
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
            Alert detail = new Alert(Alert.AlertType.CONFIRMATION);
            detail.setTitle("Dateien löschen?");
            detail.setHeaderText("Benutzerdateien entfernen?");
            detail.setContentText("Sollen auch alle zugehörigen Dateien (Termine, Ziele, Einstellungen) gelöscht werden?");

            ButtonType nurBenutzer = new ButtonType("Nur Benutzer");
            ButtonType allesLöschen = new ButtonType("Alles löschen");
            ButtonType abbrechen = new ButtonType("Abbrechen", ButtonBar.ButtonData.CANCEL_CLOSE);

            detail.getButtonTypes().setAll(allesLöschen, nurBenutzer, abbrechen);

            Optional<ButtonType> auswahl = detail.showAndWait();

            if (auswahl.isPresent() && auswahl.get() != abbrechen) {
                try {
                    CsvManager.deleteUser(selected[0]);

                    if (auswahl.get() == allesLöschen) {
                        Files.deleteIfExists(Paths.get(PfadManager.getTerminPfad(selected[0])));
                        Files.deleteIfExists(Paths.get(PfadManager.getZielePfad(selected[0])));
                        Files.deleteIfExists(Paths.get(System.getProperty("user.home") + "/SchulManager/data/" + selected[0] + "_config.properties"));
                    }

                    loadBenutzer();
                    clearFields();
                } catch (IOException e) {
                    showFehler("Fehler beim Löschen.");
                }
            }
        }
    }

    /**
     * Lädt alle Benutzer aus der CSV-Datei und aktualisiert die Tabelle.
     */
    private void loadBenutzer() {
        try {
            benutzerListe.setAll(CsvManager.loadBenutzer());
        } catch (IOException e) {
            showFehler("Fehler beim Laden der Benutzer.");
        }
    }

    /**
     * Setzt alle Eingabefelder zurück.
     */
    private void clearFields() {
        benutzernameField.clear();
        neuesPasswortField.clear();
        rolleBox.setValue(null);
    }

    /**
     * Zeigt eine Fehlermeldung in einem Alert-Fenster.
     *
     * @param msg Fehlermeldung
     */
    private void showFehler(String msg) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Fehler");
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }

    /**
     * Benennt eine Datei um, sofern sie existiert.
     *
     * @param altPfad Alter Dateipfad
     * @param neuPfad Neuer Dateipfad
     * @throws IOException falls Umbenennen fehlschlägt
     */
    private void renameFileIfExists(String altPfad, String neuPfad) throws IOException {
        File altDatei = new File(altPfad);
        if (altDatei.exists()) {
            Files.move(altDatei.toPath(), new File(neuPfad).toPath());
        }
    }
}
