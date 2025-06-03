package at.spengergasse.projekt.controller;

import at.spengergasse.projekt.model.CsvManager;
import at.spengergasse.projekt.model.User;
import at.spengergasse.projekt.view.AdminViewFX;
import at.spengergasse.projekt.view.LoginViewFX;
import at.spengergasse.projekt.view.MainViewFX;
import javafx.event.ActionEvent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Controller für den Login-Vorgang.
 * Entscheidet nach erfolgreichem Login über Benutzer- oder Admin-Oberfläche.
 */
public class LoginControllerFX {

    private final LoginViewFX view;
    private final Stage stage;

    /**
     * Konstruktor des LoginControllers.
     *
     * @param view  die Login-Oberfläche
     * @param stage das JavaFX-Hauptfenster
     */
    public LoginControllerFX(LoginViewFX view, Stage stage) {
        this.view = view;
        this.stage = stage;

        view.getLoginButton().setOnAction(this::handleLogin);
    }

    /**
     * Verarbeitet den Login-Klick.
     *
     * @param event das ausgelöste Event
     */
    private void handleLogin(ActionEvent event) {
        String username = view.getUsername().trim();
        String password = view.getPassword().trim();

        if (username.isEmpty() || password.isEmpty()) {
            view.setError("Bitte fülle alle Felder aus.");
            return;
        }

        User user = CsvManager.validateLogin(username, password);
        if (user == null) {
            view.setError("Login fehlgeschlagen! Benutzer oder Passwort falsch.");
            view.clearPassword();
        } else {
            if (user.getRole().equalsIgnoreCase("admin")) {
                AdminViewFX adminView = new AdminViewFX();
                new AdminControllerFX(adminView, stage);
                stage.setScene(new Scene(adminView, 800, 600));
                stage.setTitle("Admin-Modus – SchulManager");
                stage.show();
            } else {
                MainViewFX mainView = new MainViewFX(user);
                new MainControllerFX(mainView, user);
                stage.setScene(new Scene(mainView, 800, 600));
                stage.setTitle("SchulManager – " + user.getUsername());
                stage.show();
            }
        }
    }
}
