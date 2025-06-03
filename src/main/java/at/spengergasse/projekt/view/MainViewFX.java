package at.spengergasse.projekt.view;

import at.spengergasse.projekt.controller.MainControllerFX;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

/**
 * Hauptansicht nach erfolgreichem Login.
 * Zeigt Navigation, Buttons, Footer und lädt zentrale Panels.
 */
public class MainViewFX {

    private final BorderPane root;
    private Label footerLabel;
    private final MainControllerFX controller;

    /**
     * Erstellt die Hauptansicht mit Navigation und Inhalt.
     * @param primaryStage Hauptfenster
     * @param username Angemeldeter Benutzername
     */
    public MainViewFX(Stage primaryStage, String username) {
        this.controller = new MainControllerFX(this, username);

        root = new BorderPane();
        root.setPadding(new Insets(10));

        createMenuBar(primaryStage);
        createButtonBar();
        createFooter(username);

        Scene scene = new Scene(root, 800, 600);
        scene.getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());
        primaryStage.setScene(scene);
        primaryStage.setTitle("SchulManager - Willkommen " + username);
        primaryStage.show();

        controller.loadTerminView();
    }

    private void createMenuBar(Stage stage) {
        MenuBar menuBar = new MenuBar();

        Menu dateiMenu = new Menu("Datei");
        MenuItem neueInstanz = new MenuItem("Neue Instanz öffnen");
        MenuItem fensterSchließen = new MenuItem("Fenster schließen");
        MenuItem pfadÄndern = new MenuItem("Standard-Datenpfad ändern");

        neueInstanz.setOnAction(controller::handleNeueInstanz);
        fensterSchließen.setOnAction(e -> stage.close());
        pfadÄndern.setOnAction(controller::handlePfadAendern);

        dateiMenu.getItems().addAll(neueInstanz, fensterSchließen, pfadÄndern);

        Menu einstellungenMenu = new Menu("Einstellungen");

        menuBar.getMenus().addAll(dateiMenu, einstellungenMenu);

        root.setTop(menuBar);
    }

    private void createButtonBar() {
        HBox buttonBox = new HBox(10);
        buttonBox.setPadding(new Insets(10));
        buttonBox.setAlignment(Pos.CENTER);

        Button termineButton = new Button("Termine");
        Button statistikButton = new Button("Statistik");
        Button zieleButton = new Button("Ziele");

        termineButton.setOnAction(controller::handleTermine);
        statistikButton.setOnAction(controller::handleStatistik);
        zieleButton.setOnAction(controller::handleZiele);

        buttonBox.getChildren().addAll(termineButton, statistikButton, zieleButton);
        root.setCenter(buttonBox);
    }

    private void createFooter(String username) {
        HBox footer = new HBox(10);
        footer.setPadding(new Insets(8));
        footer.setAlignment(Pos.CENTER_LEFT);
        footer.getStyleClass().add("footer");

        Label userLabel = new Label("Angemeldet als: " + username);
        footerLabel = new Label("Speicherpfad: [Standardpfad wird hier angezeigt]");

        Button logoutButton = new Button("Abmelden");
        logoutButton.setOnAction(controller::handleLogout);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        footer.getChildren().addAll(userLabel, spacer, footerLabel, logoutButton);
        root.setBottom(footer);
    }

    /**
     * Erlaubt dem Controller, den zentralen Panel-Inhalt zu ändern.
     * @param node Neuer Knoten für den Center-Bereich
     */
    public void setCenterContent(javafx.scene.Node node) {
        root.setCenter(node);
    }

    /**
     * Setzt den Speicherpfad im Footer.
     * @param pfad Text für den Pfad
     */
    public void setFooterPath(String pfad) {
        if (footerLabel != null) {
            footerLabel.setText("Speicherpfad: " + pfad);
        }
    }
}
