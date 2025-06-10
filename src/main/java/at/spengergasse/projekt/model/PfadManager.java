package at.spengergasse.projekt.model;

import java.util.HashMap;
import java.util.Map;

/**
 * Verwaltet benutzerabhängige Speicherpfade für Termine und Ziele.
 * Ermöglicht das Setzen und Zurücksetzen von benutzerdefinierten Pfaden
 * sowie Zugriff auf Standardpfade im Benutzerverzeichnis.
 */
public class PfadManager {

    private static final Map<String, String> terminPfadMap = new HashMap<>();
    private static final Map<String, String> zielePfadMap = new HashMap<>();

    /**
     * Setzt einen benutzerdefinierten Speicherpfad für Termine.
     *
     * @param username Benutzername
     * @param pfad     Neuer Speicherpfad für Termine
     */
    public static void setTerminPfad(String username, String pfad) {
        terminPfadMap.put(username, pfad);
    }

    /**
     * Setzt einen benutzerdefinierten Speicherpfad für Ziele.
     *
     * @param username Benutzername
     * @param pfad     Neuer Speicherpfad für Ziele
     */
    public static void setZielePfad(String username, String pfad) {
        zielePfadMap.put(username, pfad);
    }

    /**
     * Gibt den aktuellen Pfad für Termine zurück.
     * Falls kein eigener Pfad gesetzt wurde, wird der Standardpfad verwendet.
     *
     * @param username Benutzername
     * @return Pfad zur Termin-Datei
     */
    public static String getTerminPfad(String username) {
        return terminPfadMap.getOrDefault(username, getDefaultTerminPfad(username));
    }

    /**
     * Gibt den aktuellen Pfad für Ziele zurück.
     * Falls kein eigener Pfad gesetzt wurde, wird der Standardpfad verwendet.
     *
     * @param username Benutzername
     * @return Pfad zur Ziele-Datei
     */
    public static String getZielePfad(String username) {
        return zielePfadMap.getOrDefault(username, getDefaultZielePfad(username));
    }

    /**
     * Gibt den Standardpfad für die Termin-Datei eines Benutzers zurück.
     * Der Pfad liegt im Home-Verzeichnis unter „SchulManager/data“.
     *
     * @param username Benutzername
     * @return Standardpfad für Termine
     */
    public static String getDefaultTerminPfad(String username) {
        return System.getProperty("user.home") + "/SchulManager/data/" + username + "_termine.csv";
    }

    /**
     * Gibt den Standardpfad für die Ziele-Datei eines Benutzers zurück.
     * Der Pfad liegt im Home-Verzeichnis unter „SchulManager/data“.
     *
     * @param username Benutzername
     * @return Standardpfad für Ziele
     */
    public static String getDefaultZielePfad(String username) {
        return System.getProperty("user.home") + "/SchulManager/data/" + username + "_ziele.csv";
    }

    /**
     * Entfernt benutzerdefinierte Pfade aus dem Zwischenspeicher,
     * sodass beim nächsten Zugriff wieder der Standardpfad verwendet wird.
     *
     * @param username Benutzername
     */
    public static void resetPfade(String username) {
        terminPfadMap.remove(username);
        zielePfadMap.remove(username);
    }
}
