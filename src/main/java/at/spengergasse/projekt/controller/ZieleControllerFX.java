package at.spengergasse.projekt.controller;

import at.spengergasse.projekt.model.Ziele;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Controller für die Ziele-Ansicht.
 * Verwaltet das Laden, Speichern und Bearbeiten der Ziel-Daten.
 */
public class ZieleControllerFX {

    private final String username;
    private final String pfad;
    private final ObservableList<Ziele> zieleListe;

    /**
     * Konstruktor für den Ziele-Controller.
     *
     * @param username Benutzername (nur zur Identifikation)
     * @param pfad     Speicherpfad zur Ziel-Datei
     */
    public ZieleControllerFX(String username, String pfad) {
        this.username = username;
        this.pfad = pfad;
        this.zieleListe = FXCollections.observableArrayList();
        load();
    }

    /**
     * Gibt die ObservableList mit den aktuellen Zielen zurück.
     *
     * @return Liste der Ziele
     */
    public ObservableList<Ziele> getZiele() {
        return zieleListe;
    }

    /**
     * Fügt ein neues Ziel hinzu und speichert die Liste.
     *
     * @param ziel Das neue Ziel
     */
    public void addZiel(Ziele ziel) {
        zieleListe.add(ziel);
        save();
    }

    /**
     * Entfernt ein Ziel aus der Liste und speichert.
     *
     * @param ziel Das Ziel, das entfernt werden soll
     */
    public void removeZiel(Ziele ziel) {
        zieleListe.remove(ziel);
        save();
    }

    /**
     * Lädt die Ziele aus der Datei.
     */
    private void load() {
        Path path = Path.of(pfad);
        if (!Files.exists(path)) return;

        try (BufferedReader reader = new BufferedReader(new FileReader(pfad))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(";", 2);
                if (parts.length == 2) {
                    boolean erledigt = Boolean.parseBoolean(parts[0]);
                    String text = parts[1];
                    zieleListe.add(new Ziele(text, erledigt));
                }
            }
        } catch (IOException e) {
            System.err.println("Fehler beim Laden der Ziele-Datei.");
        }
    }

    /**
     * Speichert die aktuelle Liste in die Datei.
     */
    public void save() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(pfad))) {
            for (Ziele ziel : zieleListe) {
                writer.write(ziel.isErledigt() + ";" + ziel.getZielText());
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("Fehler beim Speichern der Ziele-Datei.");
        }
    }
}
