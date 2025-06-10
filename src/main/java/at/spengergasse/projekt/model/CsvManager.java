package at.spengergasse.projekt.model;

import java.io.*;
import java.nio.file.*;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Statische Hilfsklasse zur Datei-Verarbeitung für Benutzer- und Termindaten.
 * Unterstützt CSV-basierte Speicherung und einfache Datenmanipulation.
 */
public class CsvManager {

    private static final String BENUTZER_PFAD = "data/benutzer.csv";

    /**
     * Prüft, ob ein Benutzer mit dem angegebenen Namen existiert.
     *
     * @param name Benutzername
     * @return true, wenn ein Benutzer mit diesem Namen existiert
     * @throws IOException bei Datei- oder Lesefehlern
     */
    public static boolean userExists(String name) throws IOException {
        return Files.exists(Paths.get(BENUTZER_PFAD)) &&
                Files.lines(Paths.get(BENUTZER_PFAD))
                        .anyMatch(line -> line.startsWith(name + ";"));
    }

    /**
     * Gibt die Rolle eines Benutzers zurück.
     * Falls keine explizite Rolle gespeichert ist, wird "user" angenommen.
     *
     * @param username Benutzername
     * @return Rolle des Benutzers ("admin", "user", etc.)
     * @throws IOException bei Datei- oder Lesefehlern
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
     * Prüft, ob das eingegebene Passwort mit dem gespeicherten Hash übereinstimmt.
     *
     * @param name Benutzername
     * @param plainPassword Das eingegebene Klartextpasswort
     * @return true, wenn das Passwort korrekt ist
     * @throws IOException bei Datei- oder Lesefehlern
     */
    public static boolean isPasswordCorrect(String name, String plainPassword) throws IOException {
        return Files.lines(Paths.get(BENUTZER_PFAD))
                .filter(line -> line.startsWith(name + ";"))
                .map(line -> line.split(";"))
                .anyMatch(parts -> parts.length == 3 &&
                        encodeString(plainPassword).equals(parts[2]));
    }

    /**
     * Speichert einen neuen Benutzer mit verschlüsseltem Passwort.
     *
     * @param name Benutzername
     * @param password Passwort im Klartext
     * @param rolle Benutzerrolle (z.B. "user", "admin")
     * @throws IOException bei Schreibfehlern
     */
    public static void saveUser(String name, String password, String rolle) throws IOException {
        String zeile = name + ";" + rolle + ";" + encodeString(password);
        Files.write(Paths.get(BENUTZER_PFAD), (zeile + System.lineSeparator()).getBytes(),
                StandardOpenOption.CREATE, StandardOpenOption.APPEND);
    }

    /**
     * Lädt alle Benutzerzeilen als String-Arrays.
     *
     * @return Liste von Benutzerzeilen (Array: [Name, Rolle, (Passwort)])
     * @throws IOException bei Datei- oder Lesefehlern
     */
    public static List<String[]> loadBenutzer() throws IOException {
        if (!Files.exists(Paths.get(BENUTZER_PFAD))) return new ArrayList<>();
        return Files.lines(Paths.get(BENUTZER_PFAD))
                .map(l -> l.split(";"))
                .filter(p -> p.length >= 2)
                .collect(Collectors.toList());
    }

    /**
     * Löscht einen Benutzer aus der Datei.
     *
     * @param name Benutzername, der gelöscht werden soll
     * @throws IOException bei Datei- oder Schreibfehlern
     */
    public static void deleteUser(String name) throws IOException {
        if (!Files.exists(Paths.get(BENUTZER_PFAD))) return;
        List<String> lines = Files.readAllLines(Paths.get(BENUTZER_PFAD));
        List<String> filtered = lines.stream()
                .filter(l -> !l.startsWith(name + ";"))
                .toList();
        Files.write(Paths.get(BENUTZER_PFAD), filtered);
    }

    /**
     * Lädt Terminobjekte aus einer CSV-Datei.
     * Jede Zeile wird als Termin interpretiert.
     *
     * @param pfad Pfad zur CSV-Datei
     * @return Liste der geladenen Termine
     * @throws IOException bei Lese- oder Formatfehlern
     */
    public static List<Termin> loadTermine(String pfad) throws IOException {
        if (!Files.exists(Paths.get(pfad))) return new ArrayList<>();
        return Files.lines(Paths.get(pfad))
                .map(line -> line.split(";"))
                .filter(p -> p.length >= 3) // mindestens: titel, datum, art
                .map(p -> new Termin(
                        p[0],
                        LocalDate.parse(p[1]),
                        p[2],
                        p.length >= 4 ? p[3] : "" // Notiz optional
                ))
                .collect(Collectors.toList());
    }

    /**
     * Speichert eine Liste von Terminen in eine CSV-Datei.
     *
     * @param termine Liste von Termin-Objekten
     * @param pfad Speicherpfad für die CSV-Datei
     * @throws IOException bei Schreibfehlern
     */
    public static void saveTermine(List<Termin> termine, String pfad) throws IOException {
        List<String> lines = termine.stream()
                .map(t -> t.getTitel() + ";" + t.getDatum() + ";" + t.getArt() + ";" + t.getNotiz())
                .toList();
        Files.write(Paths.get(pfad), lines);
    }

    /**
     * Kodiert einen String mit SHA-256.
     *
     * @param input Eingabestring
     * @return SHA-256-Hash als Hex-String
     */
    public static String encodeString(String input) {
        try {
            Encoding enc = new Encoding(input, Encoding.EncodingType.SHA256);
            return enc.bytesToHex();
        } catch (Exception e) {
            return "ERROR";
        }
    }
}
