package at.spengergasse.projekt.controller;

import at.spengergasse.projekt.view.AdminViewFX;
import at.spengergasse.projekt.view.LoginViewFX;
import at.spengergasse.projekt.view.MainViewFX;
import at.spengergasse.projekt.model.CsvManager;
import javafx.event.ActionEvent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Dieser Controller verarbeitet die Logik für Login und Registrierung.
 * Er übernimmt Event-Handling und startet die MainView bei Erfolg.
 */
public class LoginControllerFX {

    private final LoginViewFX view;
    private final Stage stage;

    public LoginControllerFX(LoginViewFX view, Stage stage) {
        this.view = view;
        this.stage = stage;
    }

    /**
     * Verknüpft Button- und Tastatur-Events mit ihrer Funktion.
     * @param scene aktuelle JavaFX-Scene
     */
    public void setupEventHandling(Scene scene) {
        view.getLoginButton().addEventHandler(ActionEvent.ACTION, this::handleLogin);
        view.getRegisterButton().addEventHandler(ActionEvent.ACTION, this::handleRegistration);

        scene.addEventHandler(KeyEvent.KEY_PRESSED, event -> {
            if (event.getCode() == KeyCode.ENTER) {
                if (!view.getUsernameField().getText().isEmpty() && !view.getPasswordField().getText().isEmpty()) {
                    handleLogin(new ActionEvent());
                } else {
                    handleRegistration(new ActionEvent());
                }
            }
        });
    }

    private void handleLogin(ActionEvent e) {
        String username = view.getUsernameField().getText().trim();
        String password = view.getPasswordField().getText().trim();

        if (username.isEmpty() || password.isEmpty()) {
            showError("Bitte alle Felder ausfüllen.");
            return;
        }

        try {
            if (!CsvManager.userExists(username)) {
                showError("Benutzer existiert nicht.");
                return;
            }

            if (!CsvManager.isPasswordCorrect(username, password)) {
                showError("Falsches Passwort.");
                return;
            }

            String role = CsvManager.getUserRole(username);

            Stage newStage = new Stage();
            if (role.equalsIgnoreCase("admin")) {
                AdminViewFX adminView = new AdminViewFX();
                Scene adminScene = new Scene(adminView, 600, 700);

                adminScene.getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());

                newStage.setTitle("SchulManager - Willkommen admin");
                newStage.setScene(adminScene);
                newStage.show();

            } else {
                new MainViewFX(newStage, username); // funktioniert, weil dort das Stage selbst gesetzt wird
            }

            view.getStage().close();


            view.getStage().close();

        } catch (IOException ex) {
            showError("Fehler beim Laden der Benutzerdaten.");
        }
    }



    private void handleRegistration(ActionEvent e) {
        String username = view.getUsernameField().getText().trim();
        String password = view.getPasswordField().getText().trim();

        if (username.isEmpty() || password.isEmpty()) {
            showError("Bitte alle Felder ausfüllen.");
            return;
        }
        if (username.equalsIgnoreCase("admin")) {
            showError("Der Benutzername 'admin' ist reserviert.");
            return;
        }

        try {
            if (CsvManager.userExists(username)) {
                showError("Benutzername bereits vergeben.");
                return;
            }

            CsvManager.saveUser(username, password, "user");
            new MainViewFX(new Stage(), username);
            view.getStage().close();

        } catch (IOException ex) {
            showError("Fehler beim Speichern des Benutzers.");
        }
    }

    private void showError(String message) {
        Label errorLabel = view.getErrorLabel();
        errorLabel.setText(message);
        errorLabel.setVisible(true);
    }
}
