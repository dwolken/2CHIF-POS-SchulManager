package at.spengergasse.projekt.controller;

import at.spengergasse.projekt.model.Ziele;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.*;
import java.nio.file.Files;
import java.util.List;

/**
 * Controller für Zielverwaltung – lädt, speichert und verwaltet die Ziel-Daten.
 */
public class ZieleControllerFX {

    private final String pfad;
    private final ObservableList<Ziele> ziele;

    /**
     * Erstellt einen ZieleController für den übergebenen Benutzer.
     * @param username aktueller Benutzername
     */
    public ZieleControllerFX(String username) {
        this.pfad = System.getProperty("user.home") + "/SchulManager/data/" + username + "_ziele.csv";
        this.ziele = FXCollections.observableArrayList();
        loadZiele();
    }

    public ObservableList<Ziele> getZiele() {
        return ziele;
    }

    /**
     * Fügt ein neues Ziel hinzu und speichert es.
     * @param ziel das neue Ziel
     */
    public void addZiel(Ziele ziel) {
        ziele.add(ziel);
        saveZiele();
    }

    /**
     * Entfernt ein Ziel aus der Liste und speichert die Änderung.
     * @param ziel Ziel, das entfernt werden soll
     */
    public void removeZiel(Ziele ziel) {
        ziele.remove(ziel);
        saveZiele();
    }

    /**
     * Lädt Ziele aus der CSV-Datei. Wenn keine Datei vorhanden ist, wird sie ignoriert.
     */
    private void loadZiele() {
        File file = new File(pfad);
        if (!file.exists()) return;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String zeile;
            while ((zeile = reader.readLine()) != null) {
                String[] parts = zeile.split(";");
                if (parts.length >= 2) {
                    ziele.add(new Ziele(parts[0], parts[1]));
                }
            }
        } catch (IOException e) {
            e.printStackTrace(); // optional: Logger verwenden
        }
    }

    /**
     * Speichert alle Ziele in die CSV-Datei.
     */
    public void saveZiele() {
        try {
            File file = new File(pfad);
            file.getParentFile().mkdirs();
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
                for (Ziele ziel : ziele) {
                    writer.write(ziel.getTitel() + ";" + ziel.getBeschreibung());
                    writer.newLine();
                }
            }
        } catch (IOException e) {
            e.printStackTrace(); // optional: Fehleranzeige im UI
        }
    }
}
