
package at.spengergasse.projekt.model;

import java.util.HashMap;
import java.util.Map;

/**
 * Haupt-Launcher-Klasse für den SchulManager.
 * Startet die JavaFX-Anwendung.
 */
public class PfadManager {

    private static final Map<String, String> terminPfadMap = new HashMap<>();
    private static final Map<String, String> zielePfadMap = new HashMap<>();

    public static void setTerminPfad(String username, String pfad) {
        terminPfadMap.put(username, pfad);
    }

    public static void setZielePfad(String username, String pfad) {
        zielePfadMap.put(username, pfad);
    }

    public static String getTerminPfad(String username) {
        return terminPfadMap.getOrDefault(username, getDefaultTerminPfad(username));
    }

    public static String getZielePfad(String username) {
        return zielePfadMap.getOrDefault(username, getDefaultZielePfad(username));
    }

    public static String getDefaultTerminPfad(String username) {
        return System.getProperty("user.home") + "/SchulManager/data/" + username + "_termine.csv";
    }

    public static String getDefaultZielePfad(String username) {
        return System.getProperty("user.home") + "/SchulManager/data/" + username + "_ziele.csv";
    }

    public static void resetPfade(String username) {
        terminPfadMap.remove(username);
        zielePfadMap.remove(username);
    }
}
