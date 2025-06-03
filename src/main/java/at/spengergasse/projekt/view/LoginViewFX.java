package at.spengergasse.projekt.view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;

/**
 * LoginViewFX ist die GUI-Komponente für den Login-Bereich.
 * Sie enthält Eingabefelder für Benutzername und Passwort sowie Buttons für Login und Registrierung.
 */
public class LoginViewFX extends BorderPane {

    private final TextField usernameField;
    private final PasswordField passwordField;
    private final Button loginButton;
    private final Button registerButton;
    private final Label errorLabel;

    /**
     * Konstruktor erstellt die Login-Oberfläche.
     */
    public LoginViewFX() {
        Label title = new Label("SchulManager Login");
        title.setFont(new Font("Arial", 24));

        usernameField = new TextField();
        usernameField.setPromptText("Benutzername");

        passwordField = new PasswordField();
        passwordField.setPromptText("Passwort");

        loginButton = new Button("Login");
        registerButton = new Button("Registrieren");

        errorLabel = new Label();
        errorLabel.setStyle("-fx-text-fill: red");

        VBox centerBox = new VBox(10);
        centerBox.setPadding(new Insets(30));
        centerBox.setAlignment(Pos.CENTER);
        centerBox.getChildren().addAll(title, usernameField, passwordField, loginButton, registerButton, errorLabel);

        this.setCenter(centerBox);
    }

    /**
     * Gibt den eingegebenen Benutzernamen zurück.
     */
    public String getUsername() {
        return usernameField.getText();
    }

    /**
     * Gibt das eingegebene Passwort zurück.
     */
    public String getPassword() {
        return passwordField.getText();
    }

    /**
     * Gibt Zugriff auf den Login-Button.
     */
    public Button getLoginButton() {
        return loginButton;
    }

    /**
     * Gibt Zugriff auf den Registrieren-Button.
     */
    public Button getRegisterButton() {
        return registerButton;
    }

    /**
     * Setzt eine Fehlermeldung im Fehler-Label.
     *
     * @param msg die anzuzeigende Nachricht
     */
    public void setError(String msg) {
        errorLabel.setText(msg);
    }

    /**
     * Löscht das Passwortfeld.
     */
    public void clearPassword() {
        passwordField.clear();
    }

    /**
     * Leert die Fehlermeldung.
     */
    public void clearError() {
        errorLabel.setText("");
    }
}
