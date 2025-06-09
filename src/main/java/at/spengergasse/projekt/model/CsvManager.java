package at.spengergasse.projekt.model;

import java.io.*;
import java.nio.file.*;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Statische Hilfsklasse zur Datei-Verarbeitung für Benutzer- und Termindaten.
 */
public class CsvManager {

    private static final String BENUTZER_PFAD = "data/benutzer.csv";

    /**
     * Prüft, ob ein Benutzer mit dem angegebenen Namen existiert.
     */
    public static boolean userExists(String name) throws IOException {
        return Files.exists(Paths.get(BENUTZER_PFAD)) &&
                Files.lines(Paths.get(BENUTZER_PFAD))
                        .anyMatch(line -> line.startsWith(name + ";"));
    }

    /**
     * Überprüft, ob das Passwort korrekt ist.
     */
    public static boolean isPasswordCorrect(String name, String plainPassword) throws IOException {
        return Files.lines(Paths.get(BENUTZER_PFAD))
                .filter(line -> line.startsWith(name + ";"))
                .map(line -> line.split(";"))
                .anyMatch(parts -> parts.length == 3 &&
                        encodeString(plainPassword).equals(parts[2]));
    }

    /**
     * Speichert neuen Benutzer in der CSV-Datei.
     */
    public static void saveUser(String name, String password, String rolle) throws IOException {
        String zeile = name + ";" + rolle + ";" + encodeString(password);
        Files.write(Paths.get(BENUTZER_PFAD), (zeile + System.lineSeparator()).getBytes(),
                StandardOpenOption.CREATE, StandardOpenOption.APPEND);
    }

    /**
     * Lädt alle Benutzer aus der Datei.
     */
    public static List<String[]> loadBenutzer() throws IOException {
        if (!Files.exists(Paths.get(BENUTZER_PFAD))) return new ArrayList<>();
        return Files.lines(Paths.get(BENUTZER_PFAD))
                .map(l -> l.split(";"))
                .filter(p -> p.length >= 2)
                .collect(Collectors.toList());
    }

    /**
     * Entfernt Benutzer anhand des Namens aus der Datei.
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
     * Lädt eine Liste von Terminen aus einer CSV-Datei.
     * Unterstützt auch Zeilen ohne Notiz (leeres Feld).
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
     * Speichert alle Termine als CSV in die angegebene Datei.
     */
    public static void saveTermine(List<Termin> termine, String pfad) throws IOException {
        List<String> lines = termine.stream()
                .map(t -> t.getTitel() + ";" + t.getDatum() + ";" + t.getArt() + ";" + t.getNotiz())
                .toList();
        Files.write(Paths.get(pfad), lines);
    }

    /**
     * Kodiert einen String mit SHA-256 über die Encoding-Klasse.
     * @param input Der Eingabestring
     * @return Der Hash als hex-String
     */
    public static String encodeString(String input) {
        try {
            Encoding enc = new Encoding(input, Encoding.EncodingType.SHA256);
            return enc.bytesToHex();
        } catch (Exception e) {
            return "ERROR";
        }
    }
    public static String getRolle(String username) throws IOException {
        return Files.lines(Paths.get(BENUTZER_PFAD))
                .filter(line -> line.startsWith(username + ";"))
                .map(line -> line.split(";"))
                .filter(parts -> parts.length >= 2)
                .map(parts -> parts[1])
                .findFirst()
                .orElse("user");
    }

}
