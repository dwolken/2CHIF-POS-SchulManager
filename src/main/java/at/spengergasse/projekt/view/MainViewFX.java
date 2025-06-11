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
 * Hauptansicht der Anwendung.
 *
 * <p>Diese View ist für die Anzeige des zentralen Layouts verantwortlich, bestehend aus
 * Menüleiste, Navigation, Content-Bereich und Footer.</p>
 *
 * <p>Buttons und Menüpunkte ermöglichen den Zugriff auf Termin-, Ziel- und Statistikfunktionen,
 * sowie auf Einstellungen und Dateioperationen. Sie nutzt den zugehörigen Controller für die
 * Ereignisbehandlung.</p>
 */
public class MainViewFX {

    private final BorderPane root;
    private final MainControllerFX controller;
    private Scene scene;
    private Label footerLabel;

    /**
     * Konstruktor: Initialisiert die Hauptansicht mit Navigation, Footer und Controller-Verbindung.
     *
     * @param primaryStage Die Stage, in der die Scene gesetzt wird
     * @param username     Aktuell angemeldeter Benutzer
     */
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
        primaryStage.setOnCloseRequest(event -> event.consume()); // verhindert versehentliches Schließen
        primaryStage.show();
    }

    /**
     * Erstellt die Navigationsleiste bestehend aus Menüleiste und Buttonzeile.
     *
     * @return VBox mit Menü und Navigationsbuttons
     */
    private VBox createNavigationBox() {
        VBox container = new VBox();

        // Menüleiste
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

        // Hauptbuttons
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

    /**
     * Zeigt eine Willkommensansicht im Hauptbereich.
     *
     * @param username Der aktuell angemeldete Benutzername
     */
    public void loadWelcomeCenter(String username) {
        VBox centerBox = new VBox(20);
        centerBox.setAlignment(Pos.CENTER);
        Label welcome = new Label("Willkommen, " + username + "!");
        welcome.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");
        centerBox.getChildren().add(welcome);
        root.setCenter(centerBox);
    }

    /**
     * Erstellt den unteren Footerbereich mit Benutzername, Speicherpfad-Info und Logout-Button.
     *
     * @param username Der angemeldete Benutzer
     * @return Eine HBox als Footer-Komponente
     */
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

    /**
     * Aktualisiert den Speicherpfad-Text im Footer.
     *
     * @param pfadTermine Pfad zur Termin-Datei
     * @param pfadZiele   Pfad zur Ziele-Datei
     */
    public void setFooterPath(String pfadTermine, String pfadZiele) {
        if (footerLabel != null) {
            String pfadT = new File(pfadTermine).getParent();
            String pfadZ = new File(pfadZiele).getParent();
            footerLabel.setText("Termine: " + pfadT + " | Ziele: " + pfadZ);
        }
    }

    /**
     * Setzt ein neues UI-Element in den Center-Bereich des Hauptlayouts.
     *
     * @param node Ein JavaFX-Node, z.B. ein Formular oder eine Tabelle
     */
    public void setCenterContent(javafx.scene.Node node) {
        root.setCenter(node);
    }

    /**
     * Gibt die aktuelle Scene zurück, z.B. für Theme-Umschaltung.
     *
     * @return Die Scene der Hauptansicht
     */
    public Scene getScene() {
        return root.getScene();
    }
}
