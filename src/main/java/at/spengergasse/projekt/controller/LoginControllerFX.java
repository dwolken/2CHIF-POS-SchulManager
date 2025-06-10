package at.spengergasse.projekt.controller;

import at.spengergasse.projekt.model.CsvManager;
import at.spengergasse.projekt.model.PfadManager;
import at.spengergasse.projekt.view.AdminViewFX;
import at.spengergasse.projekt.view.LoginViewFX;
import at.spengergasse.projekt.view.MainViewFX;
import javafx.event.ActionEvent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Dieser Controller verarbeitet die Logik für Login und Registrierung.
 * Er übernimmt Event-Handling und startet die MainView oder AdminView bei Erfolg.
 */
public class LoginControllerFX {

    private final LoginViewFX view;
    private final Stage stage;

    /**
     * Konstruktor für den LoginControllerFX.
     * Initialisiert die View und lädt Pfade aus Datei.
     *
     * @param view  Die LoginViewFX, die die Benutzeroberfläche enthält.
     * @param stage Das aktuelle JavaFX-Stage-Fenster.
     */
    public LoginControllerFX(LoginViewFX view, Stage stage) {
        PfadManager.loadPfade();
        this.view = view;
        this.stage = stage;
    }

    /**
     * Verknüpft Buttons und Tastaturereignisse (ENTER-Taste) mit der entsprechenden Logik.
     *
     * @param scene Die aktuelle JavaFX-Scene, auf die die Events angewendet werden.
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

    /**
     * Führt den Loginvorgang durch. Prüft Benutzerdaten und öffnet je nach Rolle
     * die AdminView oder MainView.
     *
     * @param e Das auslösende ActionEvent.
     */
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
                System.setProperty("aktuellerUser", username);
                AdminViewFX adminView = new AdminViewFX();
                Scene adminScene = new Scene(adminView, 600, 700);
                adminScene.getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());
                newStage.setTitle("SchulManager - Willkommen admin");
                newStage.setScene(adminScene);
                newStage.show();
            } else {
                System.setProperty("aktuellerUser", username);
                new MainViewFX(newStage, username);
            }

            stage.close();

        } catch (IOException ex) {
            showError("Fehler beim Laden der Benutzerdaten.");
        }
    }

    /**
     * Führt die Registrierung eines neuen Benutzers durch.
     * Der Benutzername "admin" ist reserviert.
     *
     * @param e Das auslösende ActionEvent.
     */
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
            stage.close();

        } catch (IOException ex) {
            showError("Fehler beim Speichern des Benutzers.");
        }
    }

    /**
     * Zeigt eine Fehlermeldung im Fehler-Label der Login-View an.
     *
     * @param message Die anzuzeigende Fehlermeldung.
     */
    private void showError(String message) {
        Label errorLabel = view.getErrorLabel();
        errorLabel.setText(message);
        errorLabel.setVisible(true);
    }
}
