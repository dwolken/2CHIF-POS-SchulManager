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
 * Der {@code MainControllerFX} koordiniert die Benutzerinteraktionen in der Hauptanwendung.
 * <p>
 * Er verbindet die grafische Oberfläche mit der zugrunde liegenden Geschäftslogik
 * und verwaltet das Öffnen von Ansichten, Dateioperationen, Speicherpfade und
 * Darstellungsmodi wie z.B. den Dark Mode.
 */
public class MainControllerFX {

    private final MainViewFX view;
    private final String username;
    private boolean darkModeAktiv = false;

    /**
     * Konstruktor für den {@code MainControllerFX}.
     *
     * @param view     Die View der Hauptanwendung
     * @param username Der aktuell eingeloggte Benutzername
     */
    public MainControllerFX(MainViewFX view, String username) {
        this.view = view;
        this.username = username;
    }

    /**
     * Aktualisiert die Fußzeile der Benutzeroberfläche mit aktuellen Dateipfaden.
     */
    public void updateFooter() {
        view.setFooterPath(PfadManager.getTerminPfad(username), PfadManager.getZielePfad(username));
    }

    /**
     * Gibt den aktuellen Benutzernamen zurück.
     *
     * @return Benutzername als {@code String}
     */
    public String getUsername() {
        return username;
    }

    /**
     * Öffnet die Termin-Ansicht und aktualisiert den Footer.
     *
     * @param e Auslösendes ActionEvent
     */
    public void handleTermine(ActionEvent e) {
        view.setCenterContent(new TerminViewFX(username));
        updateFooter();
    }

    /**
     * Öffnet die Statistik-Ansicht und aktualisiert den Footer.
     *
     * @param e Auslösendes ActionEvent
     */
    public void handleStatistik(ActionEvent e) {
        view.setCenterContent(new StatistikViewFX(username));
        updateFooter();
    }

    /**
     * Öffnet die Ziele-Ansicht und aktualisiert den Footer.
     *
     * @param e Auslösendes ActionEvent
     */
    public void handleZiele(ActionEvent e) {
        view.setCenterContent(new ZieleViewFX(username));
        updateFooter();
    }

    /**
     * Öffnet eine neue Instanz der Anwendung (funktioniert nur bei Start aus JAR-Datei).
     *
     * @param e Auslösendes ActionEvent
     */
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

    /**
     * Ändert den Speicherort für Termin-Daten.
     *
     * @param e Auslösendes ActionEvent
     */
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
                new Alert(Alert.AlertType.ERROR, "Datei noch nicht vorhanden").showAndWait();
            }
        }
    }

    /**
     * Ändert den Speicherort für Ziel-Daten.
     *
     * @param e Auslösendes ActionEvent
     */
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
                new Alert(Alert.AlertType.ERROR, "Datei noch nicht vorhanden").showAndWait();
            }
        }
    }

    /**
     * Setzt Speicherpfade für Ziele und Termine auf Standardwerte zurück.
     * Verschiebt aktuelle Dateien, wenn sie existieren.
     *
     * @param e Auslösendes ActionEvent
     */
    public void handlePfadZuruecksetzen(ActionEvent e) {
        String defaultTermine = PfadManager.getDefaultTerminPfad(username);
        String defaultZiele = PfadManager.getDefaultZielePfad(username);

        File aktuelleTermine = new File(PfadManager.getTerminPfad(username));
        File aktuelleZiele = new File(PfadManager.getZielePfad(username));

        boolean termineExistieren = aktuelleTermine.exists();
        boolean zieleExistieren = aktuelleZiele.exists();

        if (!termineExistieren && !zieleExistieren) {
            new Alert(Alert.AlertType.INFORMATION,
                    "Es wurden keine vorhandenen Dateien gefunden, die zurückgesetzt werden können.").showAndWait();
            return;
        }

        try {
            if (termineExistieren) {
                Files.move(aktuelleTermine.toPath(), new File(defaultTermine).toPath(), StandardCopyOption.REPLACE_EXISTING);
            }
            if (zieleExistieren) {
                Files.move(aktuelleZiele.toPath(), new File(defaultZiele).toPath(), StandardCopyOption.REPLACE_EXISTING);
            }
        } catch (IOException ex) {
            new Alert(Alert.AlertType.ERROR,
                    "Fehler beim Zurücksetzen der Pfade. Möglicherweise sind die Dateien gerade geöffnet oder gesperrt.").showAndWait();
        }

        PfadManager.setTerminPfad(username, defaultTermine);
        PfadManager.setZielePfad(username, defaultZiele);
        updateFooter();
    }

    /**
     * Setzt Benutzeroberfläche und Dateien komplett zurück.
     * Termine und Ziele werden gelöscht, Dark Mode deaktiviert.
     *
     * @param e Auslösendes ActionEvent
     */
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

    /**
     * Meldet den Benutzer ab und öffnet das Login-Fenster neu.
     *
     * @param e Auslösendes ActionEvent
     */
    public void handleLogout(ActionEvent e) {
        ((Stage) ((Button) e.getSource()).getScene().getWindow()).close();
        new LoginViewFX(new Stage());
    }

    /**
     * Aktiviert oder deaktiviert den Dark Mode und speichert die Auswahl.
     *
     * @param item  Das Menüelement, dessen Beschriftung angepasst wird.
     * @param scene Die Szene, deren Stylesheet aktualisiert wird.
     */
    public void handleToggleDarkMode(MenuItem item, Scene scene) {
        darkModeAktiv = !darkModeAktiv;
        scene.getStylesheets().clear();
        scene.getStylesheets().add(getClass().getResource(
                darkModeAktiv ? "/dark.css" : "/styles.css").toExternalForm());
        item.setText(darkModeAktiv ? "Dark Mode deaktivieren" : "Dark Mode aktivieren");
        saveDarkModeState();
    }

    /**
     * Speichert den aktuellen Dark Mode-Zustand in eine Konfigurationsdatei.
     */
    public void saveDarkModeState() {
        try {
            File file = new File(System.getProperty("user.home") + "/SchulManager/data/" + username + "_config.properties");
            file.getParentFile().mkdirs();
            Files.writeString(file.toPath(), darkModeAktiv ? "dark=true" : "dark=false");
        } catch (IOException e) {
            System.err.println("Konnte DarkMode nicht speichern.");
        }
    }

    /**
     * Lädt die gespeicherte Dark Mode-Einstellung aus der Konfigurationsdatei.
     *
     * @param scene Die aktuelle JavaFX-Szene
     */
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

    /**
     * Importiert eine CSV-Datei mit Terminen und fügt neue Einträge der eigenen Liste hinzu.
     * Duplikate werden vermieden.
     *
     * @param e Auslösendes ActionEvent
     */
    public void handleTermineLaden(ActionEvent e) {
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Termine importieren");
        chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV-Dateien", "*.csv"));
        File file = chooser.showOpenDialog(null);

        if (file != null) {
            try {
                List<Termin> importiert = CsvManager.loadTermine(file.getAbsolutePath());
                String eigenerPfad = PfadManager.getTerminPfad(username);
                List<Termin> eigene = CsvManager.loadTermine(eigenerPfad);

                for (Termin termin : importiert) {
                    if (!eigene.contains(termin)) {
                        eigene.add(termin);
                    }
                }

                CsvManager.saveTermine(eigene, eigenerPfad);
                view.setCenterContent(new TerminViewFX(username));
                updateFooter();
            } catch (IOException ex) {
                new Alert(Alert.AlertType.ERROR, "Fehler beim Importieren der Termine: " + ex.getMessage()).showAndWait();
            }
        } else {
            System.err.println("Dateiauswahl für Terminimport wurde abgebrochen oder war ungültig.");
        }
    }

    /**
     * Importiert eine CSV-Datei mit Zielen und fügt neue Einträge der eigenen Liste hinzu.
     * Duplikate werden vermieden.
     *
     * @param e Auslösendes ActionEvent
     */
    public void handleZieleLaden(ActionEvent e) {
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Ziele importieren");
        chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV-Dateien", "*.csv"));
        File file = chooser.showOpenDialog(null);

        if (file != null) {
            try {
                List<Ziele> importiert = CsvManager.loadZiele(file.getAbsolutePath());
                String eigenerPfad = PfadManager.getZielePfad(username);
                List<Ziele> eigene = CsvManager.loadZiele(eigenerPfad);

                for (Ziele ziel : importiert) {
                    if (!eigene.contains(ziel)) {
                        eigene.add(ziel);
                    }
                }

                CsvManager.saveZiele(eigene, eigenerPfad);
                view.setCenterContent(new ZieleViewFX(username));
                updateFooter();
            } catch (IOException ex) {
                new Alert(Alert.AlertType.ERROR, "Fehler beim Importieren der Ziele: " + ex.getMessage()).showAndWait();
            }
        } else {
            System.err.println("Dateiauswahl abgebrochen oder ungültig.");
        }
    }
}
