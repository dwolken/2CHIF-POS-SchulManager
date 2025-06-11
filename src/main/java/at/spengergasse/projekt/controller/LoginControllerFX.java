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
 * Der {@code LoginControllerFX} steuert den Login- und Registrierungsprozess
 * innerhalb der Benutzeroberfläche.
 * <p>
 * Er verarbeitet Benutzeraktionen wie Klicks und Tastatureingaben,
 * überprüft die Anmeldedaten gegen gespeicherte Daten und startet
 * entsprechende Views (AdminView oder MainView) basierend auf der Rolle.
 */
public class LoginControllerFX {

    private final LoginViewFX view;
    private final Stage stage;

    /**
     * Konstruktor für den LoginControllerFX.
     * Lädt gespeicherte Pfade und speichert View + Stage zur Weiterverwendung.
     *
     * @param view  Die Login-Oberfläche (LoginViewFX)
     * @param stage Das aktuelle JavaFX-Window (Stage)
     */
    public LoginControllerFX(LoginViewFX view, Stage stage) {
        PfadManager.loadPfade();
        this.view = view;
        this.stage = stage;
    }

    /**
     * Initialisiert das Event-Handling für Buttons und die ENTER-Taste.
     * Klick auf Login oder Register wird mit entsprechender Methode verknüpft.
     * ENTER löst Login aus, wenn Felder befüllt sind – sonst Registrierung.
     *
     * @param scene Die JavaFX-Szene, in der das Event-Handling eingebunden wird.
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
     * Führt den Loginvorgang durch.
     * Überprüft Benutzername und Passwort und öffnet bei Erfolg
     * die Admin- oder Main-Ansicht basierend auf der Benutzerrolle.
     *
     * @param e Das auslösende ActionEvent
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
            System.setProperty("aktuellerUser", username);

            if (role.equalsIgnoreCase("admin")) {
                AdminViewFX adminView = new AdminViewFX();
                Scene adminScene = new Scene(adminView, 600, 700);
                adminScene.getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());
                newStage.setTitle("SchulManager - Willkommen admin");
                newStage.setScene(adminScene);
                newStage.show();
            } else {
                new MainViewFX(newStage, username);
            }

            stage.close();

        } catch (IOException ex) {
            showError("Fehler beim Laden der Benutzerdaten.");
        }
    }

    /**
     * Führt die Registrierung eines neuen Benutzers durch.
     * Benutzername "admin" ist reserviert und darf nicht verwendet werden.
     *
     * @param e Das auslösende ActionEvent
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
     * Zeigt eine Fehlermeldung im zugehörigen Label an.
     *
     * @param message Die anzuzeigende Fehlermeldung.
     */
    private void showError(String message) {
        Label errorLabel = view.getErrorLabel();
        errorLabel.setText(message);
        errorLabel.setVisible(true);
    }
}
