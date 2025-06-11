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
 * Repräsentiert das Login- und Registrierungsfenster der Anwendung.
 *
 * <p>Diese View ermöglicht es Benutzer:innen, sich mit bestehenden Zugangsdaten anzumelden
 * oder einen neuen Account zu registrieren.</p>
 *
 * <p>Die grafische Oberfläche basiert auf JavaFX-Komponenten und nutzt einen Controller
 * zur Verarbeitung der Benutzerinteraktionen (Login/Registrierung).</p>
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
     * Erstellt das Login-Fenster und initialisiert die benötigten UI-Komponenten.
     *
     * @param primaryStage Das Hauptfenster der Anwendung
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
     * Baut das Layout der Login-Oberfläche auf.
     *
     * <p>Enthält die Eingabefelder, Buttons sowie ein Label für den Standardspeicherpfad.
     * Das Layout wird in einer Scene gerendert und dem Stage zugewiesen.</p>
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
     * @return Das Eingabefeld für den Benutzernamen
     */
    public TextField getUsernameField() {
        return usernameField;
    }

    /**
     * @return Das Eingabefeld für das Passwort
     */
    public PasswordField getPasswordField() {
        return passwordField;
    }

    /**
     * @return Label zur Anzeige von Fehlermeldungen
     */
    public Label getErrorLabel() {
        return errorLabel;
    }

    /**
     * @return Die JavaFX-Stage (Fenster) der Login-View
     */
    public Stage getStage() {
        return stage;
    }

    /**
     * @return Der Button zum Einloggen
     */
    public Button getLoginButton() {
        return loginButton;
    }

    /**
     * @return Der Button zur Benutzerregistrierung
     */
    public Button getRegisterButton() {
        return registerButton;
    }
}
