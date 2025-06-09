package at.spengergasse.projekt.controller;

import at.spengergasse.projekt.model.*;
import at.spengergasse.projekt.view.*;
import javafx.event.ActionEvent;
import javafx.scene.Scene;
import javafx.scene.control.*;
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
 * Controller für die Hauptanwendung. Verbindet UI-Events mit Logik.
 */
public class MainControllerFX {

    private final MainViewFX view;
    private final String username;
    private boolean darkModeAktiv = false;

    public MainControllerFX(MainViewFX view, String username) {
        this.view = view;
        this.username = username;
    }

    public void updateFooter() {
        view.setFooterPath(PfadManager.getTerminPfad(username), PfadManager.getZielePfad(username));
    }

    public String getUsername() {
        return username;
    }

    public void handleTermine(ActionEvent e) {
        view.setCenterContent(new TerminViewFX(username));
        updateFooter();
    }

    public void handleStatistik(ActionEvent e) {
        view.setCenterContent(new StatistikViewFX(username));
        updateFooter();
    }

    public void handleZiele(ActionEvent e) {
        view.setCenterContent(new ZieleViewFX(username));
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
                Files.move(new File(PfadManager.getTerminPfad(username)).toPath(), neueDatei.toPath(), StandardCopyOption.REPLACE_EXISTING);
                PfadManager.setTerminPfad(username, neueDatei.getAbsolutePath());
                updateFooter();
                view.setCenterContent(new TerminViewFX(username));
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
                Files.move(new File(PfadManager.getZielePfad(username)).toPath(), neueDatei.toPath(), StandardCopyOption.REPLACE_EXISTING);
                PfadManager.setZielePfad(username, neueDatei.getAbsolutePath());
                updateFooter();
                view.setCenterContent(new ZieleViewFX(username));
            } catch (IOException ex) {
                new Alert(Alert.AlertType.ERROR, "Fehler beim Verschieben der Ziele-Datei.").showAndWait();
            }
        }
    }

    public void handlePfadZuruecksetzen(ActionEvent e) {
        String defaultTermine = PfadManager.getDefaultTerminPfad(username);
        String defaultZiele = PfadManager.getDefaultZielePfad(username);
        try {
            Files.move(new File(PfadManager.getTerminPfad(username)).toPath(), new File(defaultTermine).toPath(), StandardCopyOption.REPLACE_EXISTING);
            Files.move(new File(PfadManager.getZielePfad(username)).toPath(), new File(defaultZiele).toPath(), StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException ex) {
            new Alert(Alert.AlertType.ERROR, "Fehler beim Zurücksetzen der Pfade.").showAndWait();
        }
        PfadManager.setTerminPfad(username, defaultTermine);
        PfadManager.setZielePfad(username, defaultZiele);
        updateFooter();
    }

    public void handleZuruecksetzen(ActionEvent e) {
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION, "Willst du wirklich ALLES zurücksetzen? Alle Termine und Ziele werden gelöscht.");
        Optional<ButtonType> result = confirm.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            handlePfadZuruecksetzen(new ActionEvent());
            new File(PfadManager.getTerminPfad(username)).delete();
            new File(PfadManager.getZielePfad(username)).delete();
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

    public void saveDarkModeState() {
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

    public void handleTermineLaden(ActionEvent e) {
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Termine importieren");
        chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV-Dateien", "*.csv"));
        File file = chooser.showOpenDialog(null);

        if (file != null) {
            try {
                List<Termin> importiert = CsvManager.loadTermine(file.getAbsolutePath());
                List<Termin> eigene = CsvManager.loadTermine(PfadManager.getTerminPfad(username));
                eigene.addAll(importiert);
                CsvManager.saveTermine(eigene, PfadManager.getTerminPfad(username));
                view.setCenterContent(new TerminViewFX(username));
                updateFooter();
            } catch (IOException ex) {
                new Alert(Alert.AlertType.ERROR, "Fehler beim Importieren der Termine.").showAndWait();
            }
        }
    }

    public void handleZieleLaden(ActionEvent e) {
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Ziele importieren");
        chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV-Dateien", "*.csv"));
        File file = chooser.showOpenDialog(null);

        if (file != null) {
            try {
                List<String> neueZeilen = Files.readAllLines(file.toPath());
                File zielDatei = new File(PfadManager.getZielePfad(username));
                if (!zielDatei.exists()) {
                    zielDatei.getParentFile().mkdirs();
                    zielDatei.createNewFile();
                }

                List<String> vorhandeneZeilen = Files.readAllLines(zielDatei.toPath());

                for (String zeile : neueZeilen) {
                    String[] parts = zeile.split(";", 2);
                    if (parts.length == 2) {
                        boolean erledigt = Boolean.parseBoolean(parts[0]);
                        String text = parts[1];
                        vorhandeneZeilen.add(erledigt + ";" + text);
                    }
                }

                Files.write(zielDatei.toPath(), vorhandeneZeilen);
                view.setCenterContent(new ZieleViewFX(username));
                updateFooter();

            } catch (IOException ex) {
                new Alert(Alert.AlertType.ERROR, "Fehler beim Importieren der Ziele.").showAndWait();
            }
        }
    }
}