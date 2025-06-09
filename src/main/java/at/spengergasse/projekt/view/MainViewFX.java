package at.spengergasse.projekt.view;

import at.spengergasse.projekt.controller.MainControllerFX;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.io.File;
import java.util.List;

/**
 * View für die Hauptanwendung inklusive Navigation, Menüs, Footer und Buttons.
 */
public class MainViewFX {

    private final BorderPane root;
    private final MainControllerFX controller;
    private Scene scene;
    private Label footerLabel;

    public MainViewFX(Stage primaryStage, String username) {
        this.controller = new MainControllerFX(this, username);

        root = new BorderPane();
        root.setPadding(new Insets(10));

        root.setTop(createNavigationBox());
        root.setBottom(createFooter(username));
        controller.updateFooter();
        loadWelcomeCenter(username);

        scene = new Scene(root, 1120, 650);
        scene.getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());
        controller.loadDarkModeState(scene);

        primaryStage.setScene(scene);
        primaryStage.setTitle("SchulManager - Willkommen " + username);
        primaryStage.setOnCloseRequest(event -> event.consume());
        primaryStage.show();
    }

    private VBox createNavigationBox() {
        VBox container = new VBox();

        MenuBar menuBar = new MenuBar();

        Menu dateiMenu = new Menu("Datei");
        MenuItem termineLaden = new MenuItem("Termine aus Datei laden");
        MenuItem zieleLaden = new MenuItem("Ziele aus Datei laden");
        MenuItem pfadAendernTermine = new MenuItem("Speicherpfad ändern (Termine)");
        MenuItem pfadAendernZiele = new MenuItem("Speicherpfad ändern (Ziele)");
        termineLaden.setOnAction(controller::handleTermineLaden);
        zieleLaden.setOnAction(controller::handleZieleLaden);
        pfadAendernTermine.setOnAction(controller::handlePfadAendernTermine);
        pfadAendernZiele.setOnAction(controller::handlePfadAendernZiele);
        dateiMenu.getItems().addAll(termineLaden, zieleLaden, pfadAendernTermine, pfadAendernZiele);

        Menu fensterMenu = new Menu("Fenster");
        MenuItem neueInstanz = new MenuItem("Neue Instanz öffnen");
        MenuItem fensterSchliessen = new MenuItem("Fenster schließen");
        neueInstanz.setOnAction(controller::handleNeueInstanz);
        fensterSchliessen.setOnAction(e -> System.exit(0));
        fensterMenu.getItems().addAll(neueInstanz, fensterSchliessen);

        Menu einstellungenMenu = new Menu("Einstellungen");
        MenuItem darkMode = new MenuItem("Dark Mode aktivieren");
        MenuItem pfadZuruecksetzen = new MenuItem("Pfad zurücksetzen");
        MenuItem zuruecksetzen = new MenuItem("Zurücksetzen");
        darkMode.setOnAction(e -> controller.handleToggleDarkMode(darkMode, scene));
        pfadZuruecksetzen.setOnAction(controller::handlePfadZuruecksetzen);
        zuruecksetzen.setOnAction(controller::handleZuruecksetzen);
        einstellungenMenu.getItems().addAll(darkMode, pfadZuruecksetzen, zuruecksetzen);

        menuBar.getMenus().addAll(dateiMenu, fensterMenu, einstellungenMenu);

        HBox buttonBox = new HBox(15);
        buttonBox.setPadding(new Insets(10));
        buttonBox.setAlignment(Pos.CENTER);

        Button btnHome = new Button("Home");
        Button btnTermine = new Button("Termine");
        Button btnStatistik = new Button("Statistik");
        Button btnZiele = new Button("Ziele");

        for (Button btn : List.of(btnHome, btnTermine, btnStatistik, btnZiele)) {
            btn.setMinWidth(200);
            btn.setMinHeight(35);
            btn.setStyle("-fx-font-size: 15px; -fx-font-weight: bold;");
            btn.getStyleClass().add("button");
        }

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
        footerLabel = new Label("Speicherpfad: -");

        Button logoutButton = new Button("Abmelden");
        logoutButton.setOnAction(controller::handleLogout);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        footer.getChildren().addAll(userLabel, spacer, footerLabel, logoutButton);
        return footer;
    }

    public void setFooterPath(String pfadTermine, String pfadZiele) {
        if (footerLabel != null) {
            String pfadT = new File(pfadTermine).getParent();
            String pfadZ = new File(pfadZiele).getParent();
            footerLabel.setText("Termine: " + pfadT + " | Ziele: " + pfadZ);
        }
    }

    public void setCenterContent(javafx.scene.Node node) {
        root.setCenter(node);
    }

    public Scene getScene() {
        return root.getScene();
    }
}