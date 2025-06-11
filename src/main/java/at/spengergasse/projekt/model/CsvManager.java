package at.spengergasse.projekt.model;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Die Klasse {@code CsvManager} verwaltet alle Dateioperationen im Zusammenhang mit:
 * <ul>
 *     <li>Benutzerdaten (Benutzername, Passwort, Rolle)</li>
 *     <li>Terminen (inkl. automatischer Klassifizierung)</li>
 *     <li>Zielen (inkl. Erledigt-Status)</li>
 * </ul>
 * Alle Methoden sind statisch. Es wird direkt mit UTF-8 codierten CSV-Dateien gearbeitet,
 * um Kompatibilitätsprobleme mit Umlauten und Sonderzeichen zu vermeiden.
 */
public class CsvManager {

    private static final String BENUTZER_PFAD = "data/benutzer.csv";

    /**
     * Prüft, ob ein Benutzer in der Datei vorhanden ist.
     *
     * @param name Benutzername
     * @return true, wenn der Benutzer existiert
     * @throws IOException Bei Dateifehlern
     */
    public static boolean userExists(String name) throws IOException {
        if (!Files.exists(Paths.get(BENUTZER_PFAD))) return false;
        try (BufferedReader reader = Files.newBufferedReader(Paths.get(BENUTZER_PFAD), StandardCharsets.UTF_8)) {
            return reader.lines().anyMatch(line -> line.startsWith(name + ";"));
        }
    }

    /**
     * Gibt die Rolle eines Benutzers zurück.
     *
     * @param username Der Benutzername
     * @return Rolle oder "user" als Fallback
     * @throws IOException Bei Datei-Leseproblemen
     */
    public static String getUserRole(String username) throws IOException {
        List<String[]> lines = loadBenutzer();
        for (String[] line : lines) {
            if (line[0].equalsIgnoreCase(username)) {
                return line[1];
            }
        }
        return "user";
    }

    /**
     * Überprüft das Passwort eines Benutzers.
     *
     * @param name          Benutzername
     * @param plainPassword Klartext-Passwort
     * @return true, wenn korrekt
     * @throws IOException Bei Fehlern beim Lesen
     */
    public static boolean isPasswordCorrect(String name, String plainPassword) throws IOException {
        try (BufferedReader reader = Files.newBufferedReader(Paths.get(BENUTZER_PFAD), StandardCharsets.UTF_8)) {
            return reader.lines()
                    .filter(line -> line.startsWith(name + ";"))
                    .map(line -> line.split(";"))
                    .anyMatch(parts -> parts.length == 3 && encodeString(plainPassword).equals(parts[2]));
        }
    }

    /**
     * Speichert einen neuen Benutzer mit verschlüsseltem Passwort.
     *
     * @param name     Benutzername
     * @param password Klartext-Passwort
     * @param rolle    Benutzerrolle (z.B. "admin")
     * @throws IOException Bei Schreibfehlern
     */
    public static void saveUser(String name, String password, String rolle) throws IOException {
        String zeile = name + ";" + rolle + ";" + encodeString(password);
        try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(BENUTZER_PFAD), StandardCharsets.UTF_8,
                StandardOpenOption.CREATE, StandardOpenOption.APPEND)) {
            writer.write(zeile);
            writer.newLine();
        }
    }

    /**
     * Lädt alle Benutzer als Liste von String-Arrays.
     *
     * @return Benutzerliste
     * @throws IOException Wenn Datei fehlt oder nicht lesbar ist
     */
    public static List<String[]> loadBenutzer() throws IOException {
        if (!Files.exists(Paths.get(BENUTZER_PFAD))) return new ArrayList<>();
        try (BufferedReader reader = Files.newBufferedReader(Paths.get(BENUTZER_PFAD), StandardCharsets.UTF_8)) {
            return reader.lines()
                    .map(l -> l.split(";"))
                    .filter(p -> p.length >= 2)
                    .collect(Collectors.toList());
        }
    }

    /**
     * Löscht einen Benutzer aus der Datei.
     *
     * @param name Benutzername
     * @throws IOException Bei Bearbeitungsfehlern
     */
    public static void deleteUser(String name) throws IOException {
        if (!Files.exists(Paths.get(BENUTZER_PFAD))) return;
        List<String> lines = Files.readAllLines(Paths.get(BENUTZER_PFAD), StandardCharsets.UTF_8);
        List<String> filtered = lines.stream()
                .filter(l -> !l.startsWith(name + ";"))
                .toList();
        Files.write(Paths.get(BENUTZER_PFAD), filtered, StandardCharsets.UTF_8);
    }

    /**
     * Lädt alle Termine aus einer Datei und klassifiziert sie automatisch.
     *
     * @param pfad Pfad zur CSV-Datei
     * @return Liste von {@code Termin}-Objekten
     * @throws IOException Bei Datei- oder Formatfehlern
     */
    public static List<Termin> loadTermine(String pfad) throws IOException {
        if (!Files.exists(Paths.get(pfad))) return new ArrayList<>();

        try (BufferedReader reader = Files.newBufferedReader(Paths.get(pfad), StandardCharsets.UTF_8)) {
            return reader.lines()
                    .map(line -> line.split(";"))
                    .filter(p -> p.length >= 3)
                    .map(p -> {
                        String titel = p[0];
                        LocalDate datum = LocalDate.parse(p[1]);

                        String rawArt = p[2].toLowerCase();
                        String art = switch (rawArt) {
                            case "prüfung", "test", "sa", "schularbeit" -> "Prüfung";
                            case "hausaufgabe", "hausübung", "übung", "hw" -> "Hausaufgabe";
                            case "event", "veranstaltung", "termin" -> "Event";
                            case "präsentation", "sonstiges", "diverses" -> "Sonstiges";
                            default -> "Sonstiges";
                        };

                        String notiz = p.length >= 4 ? p[3] : "";
                        return new Termin(titel, datum, art, notiz);
                    })
                    .collect(Collectors.toList());
        }
    }

    /**
     * Speichert eine Liste von Terminen als CSV-Datei im UTF-8 Format.
     *
     * @param termine Liste mit Terminen
     * @param pfad    Zielpfad
     * @throws IOException Bei Fehlern beim Schreiben
     */
    public static void saveTermine(List<Termin> termine, String pfad) throws IOException {
        try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(pfad), StandardCharsets.UTF_8)) {
            for (Termin t : termine) {
                writer.write(t.getTitel() + ";" + t.getDatum() + ";" + t.getArt() + ";" + t.getNotiz());
                writer.newLine();
            }
        }
    }

    /**
     * Verschlüsselt einen String mit SHA-256.
     *
     * @param input Eingabetext
     * @return Hashwert als Hex-String
     */
    public static String encodeString(String input) {
        try {
            Encoding enc = new Encoding(input, Encoding.EncodingType.SHA256);
            return enc.bytesToHex();
        } catch (Exception e) {
            return "ERROR";
        }
    }

    /**
     * Ändert den Benutzernamen in der Benutzerdatei.
     * Rolle und Passwort bleiben erhalten.
     *
     * @param alterName Bisheriger Name
     * @param neuerName Neuer Name
     * @throws IOException Bei Datei- oder Schreibfehlern
     */
    public static void updateBenutzername(String alterName, String neuerName) throws IOException {
        Path path = Paths.get(BENUTZER_PFAD);
        List<String> lines = Files.readAllLines(path, StandardCharsets.UTF_8);
        List<String> updatedLines = new ArrayList<>();

        for (String line : lines) {
            String[] parts = line.split(";");
            if (parts.length == 3 && parts[0].equals(alterName)) {
                updatedLines.add(neuerName + ";" + parts[1] + ";" + parts[2]);
            } else {
                updatedLines.add(line);
            }
        }

        Files.write(path, updatedLines, StandardCharsets.UTF_8);
    }

    /**
     * Lädt eine Liste von Zielen aus einer UTF-8-codierten CSV-Datei.
     * Jede Zeile muss das Format {@code erledigt;zieltext} haben.
     * Leere oder fehlerhafte Zeilen werden übersprungen.
     *
     * @param pfad Pfad zur CSV-Datei
     * @return Liste von {@code Ziele}-Objekten
     * @throws IOException Wenn beim Lesen ein Fehler auftritt
     */
    public static List<Ziele> loadZiele(String pfad) throws IOException {
        List<Ziele> liste = new ArrayList<>();
        Path path = Path.of(pfad);
        if (!Files.exists(path)) return liste;

        try (BufferedReader reader = Files.newBufferedReader(path, StandardCharsets.UTF_8)) {
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) continue; // leere Zeile überspringen

                String[] parts = line.split(";", 2);
                if (parts.length != 2) continue; // unvollständige Zeile überspringen

                boolean erledigt = Boolean.parseBoolean(parts[0].trim());
                String text = parts[1].trim();
                liste.add(new Ziele(text, erledigt));
            }
        }

        return liste;
    }

    /**
     * Speichert eine Liste von Zielen im UTF-8-Format ohne BOM.
     * Jede Zeile im CSV-Format: {@code erledigt;zieltext}
     *
     * @param ziele Liste mit Zielobjekten
     * @param pfad  Pfad zur Zieldatei
     * @throws IOException Wenn beim Schreiben ein Fehler auftritt
     */
    public static void saveZiele(List<Ziele> ziele, String pfad) throws IOException {
        try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(pfad), StandardCharsets.UTF_8)) {
            for (Ziele z : ziele) {
                writer.write(z.isErledigt() + ";" + z.getZielText());
                writer.newLine();
            }
        }
    }


}
