package at.spengergasse.projekt.controller;

import at.spengergasse.projekt.model.CsvManager;
import at.spengergasse.projekt.model.Termin;
import at.spengergasse.projekt.view.*;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableView;
import javafx.scene.layout.HBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.List;

/**
 * Controller für das Hauptfenster. Steuert Navigation, Menüaktionen und View-Wechsel.
 */
public class MainControllerFX {

    private final MainViewFX view;
    private final String username;
    private String aktuellerPfad;
    private boolean darkModeAktiv = false;

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
            File jarFile = new File(MainControllerFX.class.getProtectionDomain().getCodeSource().getLocation().toURI());
            if (!jarFile.getName().endsWith(".jar")) {
                Alert alert = new Alert(Alert.AlertType.WARNING, "Neue Instanz geht nur aus dem Jar-File.");
                alert.showAndWait();
                return;
            }

            String javaExe = System.getProperty("os.name").toLowerCase().contains("win") ? "javaw" : "java";

            new ProcessBuilder(javaExe, "-jar", jarFile.getPath())
                    .start();

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
            File alteDatei = new File(aktuellerPfad);
            File neueDatei = new File(selectedDir.getAbsolutePath() + "/" + username + "_termine.csv");

            try {
                if (!neueDatei.exists()) {
                    Files.move(alteDatei.toPath(), neueDatei.toPath(), StandardCopyOption.REPLACE_EXISTING);
                }
                aktuellerPfad = neueDatei.getAbsolutePath();
                view.setFooterPath(aktuellerPfad);
                view.setCenterContent(new TerminViewFX(username, aktuellerPfad));
            } catch (IOException ex) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Fehler beim Verschieben der Datei.");
                alert.showAndWait();
            }
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
                List<Termin> eigeneTermine = CsvManager.loadTermine(aktuellerPfad);
                eigeneTermine.addAll(geladeneTermine);
                CsvManager.saveTermine(eigeneTermine, aktuellerPfad);
                view.setCenterContent(new TerminViewFX(username, aktuellerPfad));
            } catch (IOException ex) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Fehler beim Importieren der Termine.");
                alert.showAndWait();
            }
        }
    }

    public void handleZuruecksetzen(ActionEvent e) {
        aktuellerPfad = System.getProperty("user.home") + "/SchulManager/data/" + username + "_termine.csv";
        view.setFooterPath(aktuellerPfad);

        darkModeAktiv = false;
        Scene scene = view.getScene();
        if (scene != null) {
            scene.getStylesheets().clear();
            scene.getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());
        }

        view.setCenterContent(new TerminViewFX(username, aktuellerPfad));
    }


    public void handleLogout(ActionEvent e) {
        Stage currentStage = (Stage) ((Button) e.getSource()).getScene().getWindow();
        currentStage.close();
        new LoginViewFX(new Stage());
    }

    public void handleToggleDarkMode(MenuItem item, Scene scene) {
        darkModeAktiv = !darkModeAktiv;
        scene.getStylesheets().clear();
        if (darkModeAktiv) {
            scene.getStylesheets().add(getClass().getResource("/dark.css").toExternalForm());
            item.setText("Dark Mode deaktivieren");
        } else {
            scene.getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());
            item.setText("Dark Mode aktivieren");
        }
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

    public void handlePfadZuruecksetzen(ActionEvent e) {
        aktuellerPfad = System.getProperty("user.home") + "/SchulManager/data/" + username + "_termine.csv";
        view.setFooterPath(aktuellerPfad);
        view.setCenterContent(new TerminViewFX(username, aktuellerPfad));
    }

}
