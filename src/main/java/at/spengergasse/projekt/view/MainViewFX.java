package at.spengergasse.projekt.view;

import at.spengergasse.projekt.model.User;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;

/**
 * Hauptansicht nach Login. Zeigt Navigation, Begrüßung, Menü und Inhalt.
 * Wird vom MainControllerFX gesteuert.
 */
public class MainViewFX extends BorderPane {

    // Navigations-Buttons
    private final Button terminButton = new Button("Termine");
    private final Button abmeldenButton = new Button("Abmelden");
    private final Button zurueckButton = new Button("Zurück zur Startseite");

    // Menüeinträge
    private final MenuItem neueInstanzMenuItem = new MenuItem("Neue Instanz öffnen");
    private final MenuItem beendenMenuItem = new MenuItem("Fenster schließen");
    private final MenuItem pfadAendernMenuItem = new MenuItem("Standard-Datenpfad ändern");
    private final MenuItem themeWechselnMenuItem = new MenuItem("Theme wechseln");

    // Footer & Begrüßung
    private final Label begruessungLabel = new Label();
    private final Label footerLabel = new Label("Angemeldet als: ");
    private final Label pfadLabel = new Label("Pfad: ");

    private final VBox menuBox = new VBox(10);

    /**
     * Standardkonstruktor.
     */
    public MainViewFX() {
        initLayout();
    }

    /**
     * Konstruktor mit Begrüßung.
     * @param username Benutzername
     */
    public MainViewFX(String username) {
        initLayout();
        setBegruessung(username);
    }

    /**
     * Neuer Konstruktor für vollständige Benutzer-Objekte.
     * @param user User-Objekt mit Name + mehr.
     */
    public MainViewFX(User user) {
        initLayout();
        setBegruessung(user.getUsername());
        setFooterInfo(user.getUsername(), "Pfad: wird später gesetzt"); // oder dynamisch
    }

    private void initLayout() {
        setPadding(new Insets(10));

        // Menüleiste oben
        MenuBar menuBar = new MenuBar();

        Menu dateiMenu = new Menu("Datei");
        dateiMenu.getItems().addAll(neueInstanzMenuItem, beendenMenuItem, pfadAendernMenuItem);

        Menu einstellungenMenu = new Menu("Einstellungen");
        einstellungenMenu.getItems().add(themeWechselnMenuItem);

        menuBar.getMenus().addAll(dateiMenu, einstellungenMenu);

        // Begrüßung
        begruessungLabel.setFont(new Font("Arial", 22));
        VBox topBox = new VBox(10, menuBar, begruessungLabel);
        topBox.setAlignment(Pos.CENTER_LEFT);
        setTop(topBox);

        // Navigation links
        menuBox.getChildren().addAll(terminButton, zurueckButton, abmeldenButton);
        menuBox.setAlignment(Pos.CENTER_LEFT);
        menuBox.setPadding(new Insets(10));
        setLeft(menuBox);

        // Footer
        HBox footerBox = new HBox(20, footerLabel, pfadLabel);
        footerBox.setPadding(new Insets(10));
        footerBox.setAlignment(Pos.CENTER);
        setBottom(footerBox);

        setStyle("-fx-background-color: white;");
    }

    public Node createGreeting(String text) {
        Label label = new Label(text);
        label.setFont(new Font("Arial", 24));
        VBox box = new VBox(label);
        box.setAlignment(Pos.CENTER);
        box.setPadding(new Insets(30));
        return box;
    }

    public void setMainContent(Node node) {
        setCenter(node);
    }

    public void setBegruessung(String username) {
        begruessungLabel.setText("Willkommen, " + username);
    }

    public void setFooterInfo(String username, String pfad) {
        footerLabel.setText("Angemeldet als: " + username);
        pfadLabel.setText("Datenpfad: " + pfad);
    }

    public Button getTerminButton() {
        return terminButton;
    }

    public Button getAbmeldenButton() {
        return abmeldenButton;
    }

    public Button getZurueckButton() {
        return zurueckButton;
    }

    public MenuItem getPfadAendernMenuItem() {
        return pfadAendernMenuItem;
    }

    public MenuItem getNeueInstanzMenuItem() {
        return neueInstanzMenuItem;
    }

    public MenuItem getBeendenMenuItem() {
        return beendenMenuItem;
    }

    public MenuItem getThemeWechselnMenuItem() {
        return themeWechselnMenuItem;
    }
}
