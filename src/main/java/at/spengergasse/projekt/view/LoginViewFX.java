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
     * Erstellt die Login-Ansicht mit Formularen für Anmeldung und Registrierung.
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

        // Info: Wo werden die Dateien gespeichert?
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

    // Getter für Controller-Zugriff
    public TextField getUsernameField() { return usernameField; }
    public PasswordField getPasswordField() { return passwordField; }
    public Label getErrorLabel() { return errorLabel; }
    public Stage getStage() { return stage; }
    public Button getLoginButton() { return loginButton; }
    public Button getRegisterButton() { return registerButton; }
}