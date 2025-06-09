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
 * Controller für Adminfunktionen: Benutzer anzeigen, erstellen, löschen und abmelden.
 */
public class AdminControllerFX {

    private final ObservableList<String[]> benutzerListe;
    private final TableView<String[]> table;
    private final TextField benutzernameField = new TextField();
    private final PasswordField passwortField = new PasswordField();
    private final ComboBox<String> rolleBox = new ComboBox<>();
    private final Button löschenButton = new Button("Löschen");
    private final Button anlegenButton = new Button("Benutzer anlegen");
    private final Button logoutButton = new Button("Abmelden");

    public AdminControllerFX() {
        this.benutzerListe = FXCollections.observableArrayList();
        this.table = createTable();
        loadBenutzer();
    }

    public TableView<String[]> getTable() {
        return table;
    }

    public HBox getAktionen() {
        löschenButton.setDisable(true);
        löschenButton.setOnAction(e -> handleLöschen());

        table.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            löschenButton.setDisable(newVal == null);
        });

        table.setRowFactory(tv -> {
            TableRow<String[]> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (row.isEmpty()) {
                    table.getSelectionModel().clearSelection();
                    löschenButton.setDisable(true);
                }
            });
            return row;
        });

        HBox box = new HBox(10, löschenButton);
        box.setAlignment(Pos.CENTER);
        box.setPadding(new Insets(10));
        return box;
    }

    public HBox getFormular() {
        benutzernameField.setPromptText("Benutzername");
        passwortField.setPromptText("Passwort");
        rolleBox.getItems().setAll("user", "admin");
        rolleBox.setPromptText("Rolle wählen");

        anlegenButton.setOnAction(e -> handleAnlegen());

        HBox box = new HBox(10, benutzernameField, passwortField, rolleBox, anlegenButton);
        box.setAlignment(Pos.CENTER);
        box.setPadding(new Insets(10));
        return box;
    }

    public Button getLogoutButton() {
        logoutButton.setOnAction(e -> {
            Stage currentStage = (Stage) logoutButton.getScene().getWindow();
            currentStage.close();
            new LoginViewFX(new Stage());
        });
        return logoutButton;
    }

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

    private void handleAnlegen() {
        String name = benutzernameField.getText().trim();
        String pass = passwortField.getText().trim();
        String rolle = rolleBox.getValue();

        if (name.isEmpty() || pass.isEmpty() || rolle == null) {
            showFehler("Bitte alle Felder ausfüllen.");
            return;
        }

        try {
            CsvManager.saveUser(name, pass, rolle);
            loadBenutzer();
            benutzernameField.clear();
            passwortField.clear();
            rolleBox.setValue(null);
        } catch (IOException e) {
            showFehler("Fehler beim Speichern.");
        }
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
            benutzerListe.setAll(CsvManager.loadBenutzer());
        } catch (IOException e) {
            showFehler("Fehler beim Laden der Benutzer.");
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
