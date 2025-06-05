package at.spengergasse.projekt.view;

import at.spengergasse.projekt.controller.MainControllerFX;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import java.io.File;

/**
 * Hauptansicht nach erfolgreichem Login.
 * Zeigt Navigation, Buttons, Footer und lädt zentrale Panels.
 */
public class MainViewFX {

    private final BorderPane root;
    private Label footerLabel;
    private final MainControllerFX controller;

    public MainViewFX(Stage primaryStage, String username) {
        this.controller = new MainControllerFX(this, username);

        root = new BorderPane();
        root.setPadding(new Insets(10));

        root.setTop(createNavigationBox());
        root.setBottom(createFooter(username));
        loadWelcomeCenter(username);

        Scene scene = new Scene(root, 1000, 600);
        scene.getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());
        primaryStage.setScene(scene);
        primaryStage.setTitle("SchulManager - Willkommen " + username);
        primaryStage.show();
    }

    private VBox createNavigationBox() {
        VBox container = new VBox();

        // === Menüleiste ===
        MenuBar menuBar = new MenuBar();

        Menu dateiMenu = new Menu("Datei");
        MenuItem termineLaden = new MenuItem("Termine aus Datei laden");
        MenuItem pfadAendern = new MenuItem("Standard-Datenpfad ändern");
        termineLaden.setOnAction(controller::handleTermineLaden);
        pfadAendern.setOnAction(controller::handlePfadAendern);
        dateiMenu.getItems().addAll(termineLaden, pfadAendern);

        Menu fensterMenu = new Menu("Fenster");
        MenuItem neueInstanz = new MenuItem("Neue Instanz öffnen");
        MenuItem fensterSchliessen = new MenuItem("Fenster schließen");
        neueInstanz.setOnAction(controller::handleNeueInstanz);
        fensterSchliessen.setOnAction(e -> System.exit(0));
        fensterMenu.getItems().addAll(neueInstanz, fensterSchliessen);

        Menu einstellungenMenu = new Menu("Einstellungen");
        MenuItem darkMode = new MenuItem("Dark Mode aktivieren");
        MenuItem zuruecksetzen = new MenuItem("Zurücksetzen");
        zuruecksetzen.setOnAction(controller::handleZuruecksetzen);
        einstellungenMenu.getItems().addAll(darkMode, zuruecksetzen);

        menuBar.getMenus().addAll(dateiMenu, fensterMenu, einstellungenMenu);

        // === Buttonleiste ===
        HBox buttonBox = new HBox(15);
        buttonBox.setPadding(new Insets(10));
        buttonBox.setAlignment(Pos.CENTER);

        Button btnHome = new Button("Home");
        Button btnTermine = new Button("Termine");
        Button btnStatistik = new Button("Statistik");
        Button btnZiele = new Button("Ziele");

        btnHome.setMinWidth(150);
        btnTermine.setMinWidth(150);
        btnStatistik.setMinWidth(150);
        btnZiele.setMinWidth(150);

        btnHome.getStyleClass().add("button");
        btnTermine.getStyleClass().add("button");
        btnStatistik.getStyleClass().add("button");
        btnZiele.getStyleClass().add("button");

        btnHome.setOnAction(e -> loadWelcomeCenter(controller.getUsername()));
        btnTermine.setOnAction(controller::handleTermine);
        btnStatistik.setOnAction(controller::handleStatistik);
        btnZiele.setOnAction(controller::handleZiele);

        buttonBox.getChildren().addAll(btnHome, btnTermine, btnStatistik, btnZiele);

        container.getChildren().addAll(menuBar, buttonBox);
        return container;
    }

    public void loadWelcomeCenter(String username) {
        VBox centerBox = new VBox(20);
        centerBox.setAlignment(Pos.CENTER);

        Label welcome = new Label("Willkommen, " + username + "!");
        welcome.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");

        centerBox.getChildren().add(welcome);
        root.setCenter(centerBox);
    }

    private HBox createFooter(String username) {
        HBox footer = new HBox(10);
        footer.setPadding(new Insets(8));
        footer.setAlignment(Pos.CENTER_LEFT);
        footer.getStyleClass().add("footer");

        Label userLabel = new Label("Angemeldet als: " + username);
        String pfad = System.getProperty("user.home") + File.separator + "SchulManager" + File.separator + "data" + File.separator + username + "_termine.csv";
        footerLabel = new Label("Speicherpfad: " + pfad);

        Button logoutButton = new Button("Abmelden");
        logoutButton.setOnAction(controller::handleLogout);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        footer.getChildren().addAll(userLabel, spacer, footerLabel, logoutButton);
        return footer;
    }

    public void setCenterContent(javafx.scene.Node node) {
        root.setCenter(node);
    }

    public void setFooterPath(String pfad) {
        if (footerLabel != null) {
            footerLabel.setText("Speicherpfad: " + pfad);
        }
    }
}
