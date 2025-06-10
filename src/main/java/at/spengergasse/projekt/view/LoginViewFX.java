package at.spengergasse.projekt.view;

import at.spengergasse.projekt.controller.LoginControllerFX;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import java.io.File;
import java.net.URL;

/**
 * Diese Klasse stellt das Login- und Registrierungsfenster dar.
 * Sie erlaubt das Anmelden eines bestehenden Benutzers oder das Erstellen eines neuen Accounts.
 * Das Layout ist JavaFX-basiert und nutzt einen zugehörigen Controller zur Event-Verarbeitung.
 */
public class LoginViewFX {

    private final LoginControllerFX controller;
    private Label errorLabel;
    private TextField usernameField;
    private PasswordField passwordField;
    private final Stage stage;
    private Button loginButton;
    private Button registerButton;

    /**
     * Konstruktor, der das Login-Fenster mit zugehörigem Controller initialisiert.
     *
     * @param primaryStage Hauptfenster der Anwendung
     */
    public LoginViewFX(Stage primaryStage) {
        this.stage = primaryStage;
        this.usernameField = new TextField();
        this.passwordField = new PasswordField();
        this.errorLabel = new Label();
        this.loginButton = new Button("Login");
        this.registerButton = new Button("Registrieren");

        this.controller = new LoginControllerFX(this, primaryStage);
        createLoginScene();
    }

    /**
     * Erstellt die grafische Benutzeroberfläche des Login-Fensters.
     * Enthält Felder für Benutzername und Passwort sowie Login-/Registrieren-Buttons.
     * Außerdem wird ein Hinweis auf den Standardspeicherpfad angezeigt.
     */
    private void createLoginScene() {
        VBox root = new VBox(20);
        root.setPadding(new Insets(30));
        root.setAlignment(Pos.CENTER);
        root.getStyleClass().add("login-root");

        Label title = new Label("SchulManager Login");
        title.getStyleClass().add("login-title");

        usernameField.setPromptText("Benutzername");
        passwordField.setPromptText("Passwort");
        errorLabel.getStyleClass().add("error-label");
        errorLabel.setVisible(false);

        String standardPfad = System.getProperty("user.home") + File.separator + "SchulManager" + File.separator + "data";
        Label pfadLabel = new Label("Standard-Speicherpfad: " + standardPfad);
        pfadLabel.setWrapText(true);
        pfadLabel.setStyle("-fx-font-size: 11px; -fx-text-fill: gray;");

        HBox buttons = new HBox(10, loginButton, registerButton);
        buttons.setAlignment(Pos.CENTER);

        root.getChildren().addAll(title, usernameField, passwordField, errorLabel, buttons, pfadLabel);

        Scene scene = new Scene(root, 420, 380);
        URL css = getClass().getResource("/styles.css");
        if (css != null) {
            scene.getStylesheets().add(css.toExternalForm());
        }

        stage.setTitle("SchulManager - Login");
        stage.setScene(scene);
        stage.show();

        controller.setupEventHandling(scene);
    }

    /**
     * Gibt das Textfeld für den Benutzernamen zurück.
     *
     * @return TextField für Benutzername
     */
    public TextField getUsernameField() {
        return usernameField;
    }

    /**
     * Gibt das Passwortfeld zurück.
     *
     * @return PasswordField für Passwort
     */
    public PasswordField getPasswordField() {
        return passwordField;
    }

    /**
     * Gibt das Fehler-Label zurück.
     *
     * @return Label für Fehlermeldungen
     */
    public Label getErrorLabel() {
        return errorLabel;
    }

    /**
     * Gibt das zugehörige Fenster (Stage) zurück.
     *
     * @return JavaFX Stage
     */
    public Stage getStage() {
        return stage;
    }

    /**
     * Gibt den Login-Button zurück.
     *
     * @return Button für Login
     */
    public Button getLoginButton() {
        return loginButton;
    }

    /**
     * Gibt den Registrieren-Button zurück.
     *
     * @return Button für Registrierung
     */
    public Button getRegisterButton() {
        return registerButton;
    }
}
