package at.spengergasse.projekt.controller;

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

            String rolle = CsvManager.getRolle(username); // aus CSV holen

            if (rolle.equalsIgnoreCase("admin")) {
                // Direkt AdminView starten
                Stage adminStage = new Stage();
                Scene adminScene = new Scene(new at.spengergasse.projekt.view.AdminViewFX(), 1120, 650);
                adminScene.getStylesheets().add(getClass().getResource("/styles.css").toExternalForm()); // CSS nicht vergessen
                adminStage.setTitle("SchulManager - Willkommen admin");
                adminStage.setScene(adminScene);
                adminStage.show();
            } else {
                // Normale MainView starten
                new MainViewFX(new Stage(), username);
            }

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
