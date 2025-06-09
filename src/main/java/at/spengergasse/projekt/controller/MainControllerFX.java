package at.spengergasse.projekt.controller;

import at.spengergasse.projekt.model.CsvManager;
import at.spengergasse.projekt.model.Termin;
import at.spengergasse.projekt.view.*;
import javafx.event.ActionEvent;
import javafx.scene.Scene;
import javafx.scene.control.*;
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
import java.util.Optional;

/**
 * MainControllerFX steuert alle Funktionen der Anwendung wie Navigation, Dateioperationen und Dark Mode.
 * Er verwaltet die aktuelle Benutzer-Session und die Pfade zu Termin- und Ziele-Dateien.
 */
public class MainControllerFX {

    private final MainViewFX view;
    private final String username;
    private String terminePfad;
    private String zielePfad;
    private boolean darkModeAktiv = false;

    public MainControllerFX(MainViewFX view, String username) {
        this.view = view;
        this.username = username;
        this.terminePfad = System.getProperty("user.home") + "/SchulManager/data/" + username + "_termine.csv";
        this.zielePfad = System.getProperty("user.home") + "/SchulManager/data/" + username + "_ziele.csv";
    }

    public void updateFooter() {
        view.setFooterPath(terminePfad, zielePfad);
    }

    public String getUsername() {
        return username;
    }

    public void handleTermine(ActionEvent e) {
        view.setCenterContent(new TerminViewFX(username, terminePfad));
        updateFooter();
    }

    public void handleStatistik(ActionEvent e) {
        view.setCenterContent(new StatistikViewFX(username));
        updateFooter();
    }

    public void handleZiele(ActionEvent e) {
        view.setCenterContent(new ZieleViewFX(username, zielePfad));
        updateFooter();
    }

    public void handleNeueInstanz(ActionEvent e) {
        try {
            File jarFile = new File(MainControllerFX.class.getProtectionDomain().getCodeSource().getLocation().toURI());
            if (!jarFile.getName().endsWith(".jar")) {
                new Alert(Alert.AlertType.WARNING, "Neue Instanz geht nur aus dem Jar-File.").showAndWait();
                return;
            }
            String javaExe = System.getProperty("os.name").toLowerCase().contains("win") ? "javaw" : "java";
            new ProcessBuilder(javaExe, "-jar", jarFile.getPath()).start();
        } catch (URISyntaxException | IOException ex) {
            new Alert(Alert.AlertType.ERROR, "Fehler beim Öffnen einer neuen Instanz.").showAndWait();
        }
    }

    public void handlePfadAendernTermine(ActionEvent e) {
        DirectoryChooser chooser = new DirectoryChooser();
        chooser.setTitle("Neuen Speicherort wählen (Termine)");
        File selectedDir = chooser.showDialog(null);
        if (selectedDir != null) {
            File neueDatei = new File(selectedDir.getAbsolutePath() + "/" + username + "_termine.csv");
            try {
                Files.move(new File(terminePfad).toPath(), neueDatei.toPath(), StandardCopyOption.REPLACE_EXISTING);
                terminePfad = neueDatei.getAbsolutePath();
                updateFooter();
                view.setCenterContent(new TerminViewFX(username, terminePfad));
            } catch (IOException ex) {
                new Alert(Alert.AlertType.ERROR, "Fehler beim Verschieben der Datei.").showAndWait();
            }
        }
    }

    public void handlePfadAendernZiele(ActionEvent e) {
        DirectoryChooser chooser = new DirectoryChooser();
        chooser.setTitle("Neuen Speicherort wählen (Ziele)");
        File selectedDir = chooser.showDialog(null);
        if (selectedDir != null) {
            File neueDatei = new File(selectedDir.getAbsolutePath() + "/" + username + "_ziele.csv");
            try {
                Files.move(new File(zielePfad).toPath(), neueDatei.toPath(), StandardCopyOption.REPLACE_EXISTING);
                zielePfad = neueDatei.getAbsolutePath();
                updateFooter();
                view.setCenterContent(new ZieleViewFX(username, zielePfad));
            } catch (IOException ex) {
                new Alert(Alert.AlertType.ERROR, "Fehler beim Verschieben der Ziele-Datei.").showAndWait();
            }
        }
    }

    public void handlePfadZuruecksetzen(ActionEvent e) {
        String defaultTermine = System.getProperty("user.home") + "/SchulManager/data/" + username + "_termine.csv";
        String defaultZiele = System.getProperty("user.home") + "/SchulManager/data/" + username + "_ziele.csv";
        try {
            Files.move(new File(terminePfad).toPath(), new File(defaultTermine).toPath(), StandardCopyOption.REPLACE_EXISTING);
            Files.move(new File(zielePfad).toPath(), new File(defaultZiele).toPath(), StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException ex) {
            new Alert(Alert.AlertType.ERROR, "Fehler beim Zurücksetzen der Pfade.").showAndWait();
        }
        terminePfad = defaultTermine;
        zielePfad = defaultZiele;
        updateFooter();
    }

    public void handleZuruecksetzen(ActionEvent e) {
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION, "Willst du wirklich ALLES zurücksetzen?\nAlle Termine und Ziele werden gelöscht.");
        Optional<ButtonType> result = confirm.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            handlePfadZuruecksetzen(new ActionEvent());
            new File(terminePfad).delete();
            new File(zielePfad).delete();
            view.loadWelcomeCenter(username);
            updateFooter();
            darkModeAktiv = false;
            Scene scene = view.getScene();
            scene.getStylesheets().clear();
            scene.getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());
            saveDarkModeState();
        }
    }

    public void handleLogout(ActionEvent e) {
        ((Stage) ((Button) e.getSource()).getScene().getWindow()).close();
        new LoginViewFX(new Stage());
    }

    public void handleToggleDarkMode(MenuItem item, Scene scene) {
        darkModeAktiv = !darkModeAktiv;
        scene.getStylesheets().clear();
        scene.getStylesheets().add(getClass().getResource(
                darkModeAktiv ? "/dark.css" : "/styles.css").toExternalForm());
        item.setText(darkModeAktiv ? "Dark Mode deaktivieren" : "Dark Mode aktivieren");
        saveDarkModeState();
    }

    public void handleTermineLaden(ActionEvent e) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Termine aus Datei laden");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV-Dateien", "*.csv"));
        File file = fileChooser.showOpenDialog(null);

        if (file != null) {
            try {
                List<Termin> geladene = CsvManager.loadTermine(file.getAbsolutePath());
                List<Termin> eigene = CsvManager.loadTermine(terminePfad);
                eigene.addAll(geladene);
                CsvManager.saveTermine(eigene, terminePfad);
                view.setCenterContent(new TerminViewFX(username, terminePfad));
                updateFooter();
            } catch (IOException ex) {
                new Alert(Alert.AlertType.ERROR, "Fehler beim Importieren der Termine.").showAndWait();
            }
        }
    }


    public void handleZieleLaden(ActionEvent e) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Ziele aus Datei laden");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV-Dateien", "*.csv"));
        File file = fileChooser.showOpenDialog(null);

        if (file != null) {
            try {
                List<String> neueZeilen = Files.readAllLines(file.toPath());
                File zielDatei = new File(zielePfad);
                if (!zielDatei.exists()) {
                    zielDatei.getParentFile().mkdirs();
                    zielDatei.createNewFile();
                }

                List<String> vorhandeneZeilen = Files.readAllLines(zielDatei.toPath());

                for (String zeile : neueZeilen) {
                    String[] parts = zeile.split(";", 2);
                    if (parts.length == 2) {
                        boolean erledigt = Boolean.parseBoolean(parts[1]);
                        String text = parts[0];
                        vorhandeneZeilen.add(erledigt + ";" + text);
                    }
                }

                Files.write(zielDatei.toPath(), vorhandeneZeilen);
                view.setCenterContent(new ZieleViewFX(username, zielePfad));
                updateFooter();

            } catch (IOException ex) {
                new Alert(Alert.AlertType.ERROR, "Fehler beim Importieren der Ziele.").showAndWait();
            }
        }
    }



    private void saveDarkModeState() {
        try {
            File file = new File(System.getProperty("user.home") + "/SchulManager/data/" + username + "_config.properties");
            file.getParentFile().mkdirs();
            Files.writeString(file.toPath(), darkModeAktiv ? "dark=true" : "dark=false");
        } catch (IOException e) {
            System.err.println("Konnte DarkMode nicht speichern.");
        }
    }

    public void loadDarkModeState(Scene scene) {
        try {
            File file = new File(System.getProperty("user.home") + "/SchulManager/data/" + username + "_config.properties");
            if (file.exists() && Files.readString(file.toPath()).contains("dark=true")) {
                darkModeAktiv = true;
                scene.getStylesheets().clear();
                scene.getStylesheets().add(getClass().getResource("/dark.css").toExternalForm());
            }
        } catch (IOException e) {
            System.err.println("Konnte DarkMode nicht laden.");
        }
    }
}
