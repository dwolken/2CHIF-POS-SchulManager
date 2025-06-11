package at.spengergasse.projekt.model;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

/**
 * Der {@code PfadManager} verwaltet benutzerabhängige Speicherpfade
 * für Termin- und Ziel-Dateien.
 * <p>
 * Die Pfade werden in einer CSV-Datei gespeichert und beim Programmstart geladen.
 * Benutzer können individuelle Speicherorte für ihre Daten definieren.
 * Die Klasse ist Teil des Model-Layers im MVC-Konzept.
 */
public class PfadManager {

    private static final Map<String, String> terminPfadMap = new HashMap<>();
    private static final Map<String, String> zielePfadMap = new HashMap<>();
    private static final String PFAD_DATEI = "data/pfade.csv";

    /**
     * Speichert den benutzerdefinierten Pfad für die Termin-Datei.
     * Die Änderung wird sofort in der Datei persistiert.
     *
     * @param username Benutzername
     * @param pfad     Neuer Pfad zur Termin-Datei
     */
    public static void setTerminPfad(String username, String pfad) {
        terminPfadMap.put(username, pfad);
        savePfade();
    }

    /**
     * Speichert den benutzerdefinierten Pfad für die Ziele-Datei.
     * Die Änderung wird sofort gespeichert.
     *
     * @param username Benutzername
     * @param pfad     Neuer Pfad zur Ziele-Datei
     */
    public static void setZielePfad(String username, String pfad) {
        zielePfadMap.put(username, pfad);
        savePfade();
    }

    /**
     * Gibt den aktuellen Termin-Dateipfad für den Benutzer zurück.
     * Falls kein individueller Pfad gesetzt wurde, wird ein Standardpfad verwendet.
     *
     * @param username Benutzername
     * @return Absoluter Pfad zur Termin-Datei
     */
    public static String getTerminPfad(String username) {
        return terminPfadMap.getOrDefault(username, getDefaultTerminPfad(username));
    }

    /**
     * Gibt den aktuellen Ziele-Dateipfad für den Benutzer zurück.
     * Bei fehlender Zuordnung wird der Standardpfad zurückgegeben.
     *
     * @param username Benutzername
     * @return Absoluter Pfad zur Ziele-Datei
     */
    public static String getZielePfad(String username) {
        return zielePfadMap.getOrDefault(username, getDefaultZielePfad(username));
    }

    /**
     * Liefert den Standardpfad für die Termin-Datei eines Benutzers.
     *
     * @param username Benutzername
     * @return Standardpfad (z.B. unter user.home)
     */
    public static String getDefaultTerminPfad(String username) {
        return System.getProperty("user.home") + "/SchulManager/data/" + username + "_termine.csv";
    }

    /**
     * Liefert den Standardpfad für die Ziele-Datei eines Benutzers.
     *
     * @param username Benutzername
     * @return Standardpfad zur Ziele-Datei
     */
    public static String getDefaultZielePfad(String username) {
        return System.getProperty("user.home") + "/SchulManager/data/" + username + "_ziele.csv";
    }

    /**
     * Entfernt alle benutzerdefinierten Pfade eines Benutzers und speichert die Änderung.
     *
     * @param username Benutzername
     */
    public static void resetPfade(String username) {
        terminPfadMap.remove(username);
        zielePfadMap.remove(username);
        savePfade();
    }

    /**
     * Lädt gespeicherte Pfad-Zuordnungen aus der CSV-Datei.
     * Sollte beim Start der Anwendung einmalig aufgerufen werden.
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
     * Speichert alle aktuellen Pfad-Zuordnungen in die CSV-Datei.
     * Wird bei jeder Änderung automatisch aufgerufen.
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

    /**
     * Aktualisiert den Benutzernamen in allen Pfad-Zuordnungen.
     * Die verknüpften Pfade bleiben erhalten.
     *
     * @param alterName Der bisherige Benutzername
     * @param neuerName Der neue Benutzername
     */
    public static void updateBenutzername(String alterName, String neuerName) {
        String terminPfad = terminPfadMap.remove(alterName);
        String zielePfad = zielePfadMap.remove(alterName);

        if (terminPfad != null) {
            terminPfadMap.put(neuerName, terminPfad);
        }
        if (zielePfad != null) {
            zielePfadMap.put(neuerName, zielePfad);
        }

        savePfade();
    }
}
