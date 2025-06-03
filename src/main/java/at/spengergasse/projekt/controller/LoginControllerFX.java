package at.spengergasse.projekt.controller;

import at.spengergasse.projekt.model.CsvManager;
import at.spengergasse.projekt.model.User;
import at.spengergasse.projekt.view.AdminViewFX;
import at.spengergasse.projekt.view.LoginViewFX;
import at.spengergasse.projekt.view.MainViewFX;
import at.spengergasse.projekt.view.RegisterViewFX;
import javafx.scene.Scene;
import javafx.event.ActionEvent;
import javafx.stage.Stage;

/**
 * Controller für den Login-Vorgang. Er verarbeitet die Login-Eingaben,
 * entscheidet über den Wechsel zur Benutzer- oder Admin-Oberfläche und
 * kann ein Registrierungsfenster öffnen.
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
        view.getRegisterButton().setOnAction(e -> openRegisterWindow());
    }

    /**
     * Öffnet das Registrierungsfenster in einem neuen Stage.
     */
    private void openRegisterWindow() {
        RegisterViewFX registerView = new RegisterViewFX();
        Stage registerStage = new Stage();
        new RegisterControllerFX(registerView, registerStage);
        registerStage.setScene(new Scene(registerView, 400, 300));
        registerStage.setTitle("Benutzer registrieren");
        registerStage.show();
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
