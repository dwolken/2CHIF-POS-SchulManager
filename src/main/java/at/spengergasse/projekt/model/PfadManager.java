package at.spengergasse.projekt.model;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

/**
 * Verwaltet benutzerabhängige Speicherpfade für Termine und Ziele.
 * Ermöglicht das dauerhafte Setzen, Zurücksetzen und Speichern der Pfade in einer Datei.
 * Wird ausschließlich im Model verwendet (MVC-Konformität).
 */
public class PfadManager {

    private static final Map<String, String> terminPfadMap = new HashMap<>();
    private static final Map<String, String> zielePfadMap = new HashMap<>();
    private static final String PFAD_DATEI = "data/pfade.csv";

    /**
     * Setzt und speichert den benutzerdefinierten Pfad für Termine.
     *
     * @param username Benutzername
     * @param pfad     Neuer Pfad zur Termin-Datei
     */
    public static void setTerminPfad(String username, String pfad) {
        terminPfadMap.put(username, pfad);
        savePfade();
    }

    /**
     * Setzt und speichert den benutzerdefinierten Pfad für Ziele.
     *
     * @param username Benutzername
     * @param pfad     Neuer Pfad zur Ziele-Datei
     */
    public static void setZielePfad(String username, String pfad) {
        zielePfadMap.put(username, pfad);
        savePfade();
    }

    /**
     * Gibt den aktuellen Pfad für Termine zurück. Falls keiner gesetzt wurde, wird der Standardpfad zurückgegeben.
     *
     * @param username Benutzername
     * @return Pfad zur Termin-Datei
     */
    public static String getTerminPfad(String username) {
        return terminPfadMap.getOrDefault(username, getDefaultTerminPfad(username));
    }

    /**
     * Gibt den aktuellen Pfad für Ziele zurück. Falls keiner gesetzt wurde, wird der Standardpfad zurückgegeben.
     *
     * @param username Benutzername
     * @return Pfad zur Ziele-Datei
     */
    public static String getZielePfad(String username) {
        return zielePfadMap.getOrDefault(username, getDefaultZielePfad(username));
    }

    /**
     * Gibt den Standardpfad für Termine zurück.
     *
     * @param username Benutzername
     * @return Standardpfad zur Termin-Datei
     */
    public static String getDefaultTerminPfad(String username) {
        return System.getProperty("user.home") + "/SchulManager/data/" + username + "_termine.csv";
    }

    /**
     * Gibt den Standardpfad für Ziele zurück.
     *
     * @param username Benutzername
     * @return Standardpfad zur Ziele-Datei
     */
    public static String getDefaultZielePfad(String username) {
        return System.getProperty("user.home") + "/SchulManager/data/" + username + "_ziele.csv";
    }

    /**
     * Entfernt alle benutzerdefinierten Pfade eines Users aus dem Speicher und Datei.
     *
     * @param username Benutzername
     */
    public static void resetPfade(String username) {
        terminPfadMap.remove(username);
        zielePfadMap.remove(username);
        savePfade();
    }

    /**
     * Lädt alle gespeicherten Pfade aus der Datei (einmal beim App-Start aufrufen).
     */
    public static void loadPfade() {
        terminPfadMap.clear();
        zielePfadMap.clear();

        try {
            if (!Files.exists(Paths.get(PFAD_DATEI))) return;

            List<String> lines = Files.readAllLines(Paths.get(PFAD_DATEI));
            for (String line : lines) {
                String[] parts = line.split(";", 3);
                if (parts.length == 3) {
                    String user = parts[0];
                    terminPfadMap.put(user, parts[1]);
                    zielePfadMap.put(user, parts[2]);
                }
            }
        } catch (IOException e) {
            System.err.println("Fehler beim Laden der Pfade: " + e.getMessage());
        }
    }

    /**
     * Speichert alle aktuell gesetzten Pfade in die Datei.
     * Wird nach jeder Änderung automatisch aufgerufen.
     */
    public static void savePfade() {
        try {
            File dir = new File("data");
            if (!dir.exists()) dir.mkdirs();

            try (BufferedWriter writer = new BufferedWriter(new FileWriter(PFAD_DATEI))) {
                for (String user : terminPfadMap.keySet()) {
                    String termin = terminPfadMap.get(user);
                    String ziele = zielePfadMap.getOrDefault(user, getDefaultZielePfad(user));
                    writer.write(user + ";" + termin + ";" + ziele);
                    writer.newLine();
                }
            }

        } catch (IOException e) {
            System.err.println("Fehler beim Speichern der Pfade: " + e.getMessage());
        }
    }
}
