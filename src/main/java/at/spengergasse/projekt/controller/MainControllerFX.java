package at.spengergasse.projekt.controller;

import at.spengergasse.projekt.model.CsvManager;
import at.spengergasse.projekt.model.Termin;
import at.spengergasse.projekt.view.MainViewFX;
import at.spengergasse.projekt.view.StatistikViewFX;
import at.spengergasse.projekt.view.TerminViewFX;
import at.spengergasse.projekt.view.ZieleViewFX;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import javafx.scene.layout.HBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

/**
 * Controller für das Hauptfenster. Steuert Navigation, Menüaktionen und View-Wechsel.
 */
public class MainControllerFX {

    private final MainViewFX view;
    private final String username;
    private String aktuellerPfad;

    public MainControllerFX(MainViewFX view, String username) {
        this.view = view;
        this.username = username;
        this.aktuellerPfad = System.getProperty("user.home") + "/SchulManager/data/" + username + "_termine.csv";
        view.setFooterPath(aktuellerPfad);
    }

    public String getUsername() {
        return username;
    }

    public void handleTermine(ActionEvent e) {
        view.setCenterContent(new TerminViewFX(username, aktuellerPfad));
    }

    public void handleStatistik(ActionEvent e) {
        view.setCenterContent(new StatistikViewFX(username));
    }

    public void handleZiele(ActionEvent e) {
        view.setCenterContent(new ZieleViewFX(username));
    }

    public void handleNeueInstanz(ActionEvent e) {
        try {
            String path = new File(MainControllerFX.class.getProtectionDomain().getCodeSource().getLocation().toURI()).getPath();
            new ProcessBuilder("java", "-jar", path).start();
        } catch (URISyntaxException | IOException ex) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Fehler beim Öffnen einer neuen Instanz.");
            alert.showAndWait();
        }
    }

    public void handlePfadAendern(ActionEvent e) {
        DirectoryChooser chooser = new DirectoryChooser();
        chooser.setTitle("Neuen Speicherort wählen");
        File selectedDir = chooser.showDialog(null);
        if (selectedDir != null) {
            aktuellerPfad = selectedDir.getAbsolutePath() + "/" + username + "_termine.csv";
            view.setFooterPath(aktuellerPfad);
            view.setCenterContent(new TerminViewFX(username, aktuellerPfad));
        }
    }

    public void handleTermineLaden(ActionEvent e) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Termine laden");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV-Dateien", "*.csv"));
        File file = fileChooser.showOpenDialog(null);

        if (file != null) {
            try {
                List<Termin> geladeneTermine = CsvManager.loadTermine(file.getAbsolutePath());
                TerminViewFX neueView = new TerminViewFX(username, aktuellerPfad); // Speicherpfad bleibt gleich!
                TableView<Termin> table = (TableView<Termin>) neueView.getChildren().get(0);
                table.setItems(FXCollections.observableArrayList(geladeneTermine));
                view.setCenterContent(neueView);
            } catch (IOException ex) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Fehler beim Laden der Datei.");
                alert.showAndWait();
            }
        }
    }

    public void handleZuruecksetzen(ActionEvent e) {
        view.setCenterContent(new TerminViewFX(username, aktuellerPfad));
    }

    public void handleLogout(ActionEvent e) {
        System.exit(0); // Optional: LoginView anzeigen
    }

    public HBox getNavBar() {
        Button homeButton = new Button("Home");
        Button termineButton = new Button("Termine");
        Button statistikButton = new Button("Statistik");
        Button zieleButton = new Button("Ziele");

        homeButton.setOnAction(e -> view.loadWelcomeCenter(username));
        termineButton.setOnAction(this::handleTermine);
        statistikButton.setOnAction(this::handleStatistik);
        zieleButton.setOnAction(this::handleZiele);

        homeButton.getStyleClass().add("nav-button");
        termineButton.getStyleClass().add("nav-button");
        statistikButton.getStyleClass().add("nav-button");
        zieleButton.getStyleClass().add("nav-button");

        HBox navBar = new HBox(20, homeButton, termineButton, statistikButton, zieleButton);
        navBar.setAlignment(javafx.geometry.Pos.CENTER);
        return navBar;
    }
}
