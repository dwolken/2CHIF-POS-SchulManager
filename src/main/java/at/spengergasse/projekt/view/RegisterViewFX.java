package at.spengergasse.projekt.view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;

/**
 * RegisterViewFX stellt die grafische Oberfläche zur Benutzerregistrierung dar.
 */
public class RegisterViewFX extends BorderPane {

    private final TextField usernameField;
    private final PasswordField passwordField;
    private final PasswordField repeatPasswordField;
    private final Button registerButton;
    private final Button cancelButton;
    private final Label errorLabel;

    /**
     * Konstruktor: Erstellt das Layout für die Registrierung.
     */
    public RegisterViewFX() {
        Label title = new Label("Neuen Benutzer registrieren");
        title.setFont(new Font("Arial", 20));

        usernameField = new TextField();
        usernameField.setPromptText("Benutzername");

        passwordField = new PasswordField();
        passwordField.setPromptText("Passwort");

        repeatPasswordField = new PasswordField();
        repeatPasswordField.setPromptText("Passwort wiederholen");

        registerButton = new Button("Registrieren");
        cancelButton = new Button("Abbrechen");

        errorLabel = new Label();
        errorLabel.setStyle("-fx-text-fill: red");

        VBox fieldsBox = new VBox(10, usernameField, passwordField, repeatPasswordField, registerButton, cancelButton, errorLabel);
        fieldsBox.setAlignment(Pos.CENTER);

        VBox layout = new VBox(20, title, fieldsBox);
        layout.setAlignment(Pos.CENTER);
        layout.setPadding(new Insets(30));

        this.setCenter(layout);
    }

    public String getUsername() {
        return usernameField.getText().trim();
    }

    public String getPassword() {
        return passwordField.getText().trim();
    }

    public String getRepeatedPassword() {
        return repeatPasswordField.getText().trim();
    }

    public Button getRegisterButton() {
        return registerButton;
    }

    public Button getCancelButton() {
        return cancelButton;
    }

    public void setError(String msg) {
        errorLabel.setText(msg);
    }

    public void clearPasswords() {
        passwordField.clear();
        repeatPasswordField.clear();
    }
}
