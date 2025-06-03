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

    public static boolean userExists(String name) throws IOException {
        return Files.exists(Paths.get(BENUTZER_PFAD)) &&
                Files.lines(Paths.get(BENUTZER_PFAD)).anyMatch(line -> line.startsWith(name + ";"));
    }

    public static boolean isPasswordCorrect(String name, String plainPassword) throws IOException {
        return Files.lines(Paths.get(BENUTZER_PFAD))
                .filter(line -> line.startsWith(name + ";"))
                .map(line -> line.split(";"))
                .anyMatch(parts -> parts.length == 3 &&
                        encodeString(plainPassword).equals(parts[2]));
    }

    public static void saveUser(String name, String password, String rolle) throws IOException {
        String zeile = name + ";" + rolle + ";" + encodeString(password);
        Files.write(Paths.get(BENUTZER_PFAD), (zeile + System.lineSeparator()).getBytes(),
                StandardOpenOption.CREATE, StandardOpenOption.APPEND);
    }

    public static List<String[]> loadBenutzer() throws IOException {
        if (!Files.exists(Paths.get(BENUTZER_PFAD))) return new ArrayList<>();
        return Files.lines(Paths.get(BENUTZER_PFAD))
                .map(l -> l.split(";"))
                .filter(p -> p.length >= 2)
                .collect(Collectors.toList());
    }

    public static void deleteUser(String name) throws IOException {
        if (!Files.exists(Paths.get(BENUTZER_PFAD))) return;
        List<String> lines = Files.readAllLines(Paths.get(BENUTZER_PFAD));
        List<String> filtered = lines.stream()
                .filter(l -> !l.startsWith(name + ";"))
                .toList();
        Files.write(Paths.get(BENUTZER_PFAD), filtered);
    }

    public static List<Termin> loadTermine(String pfad) throws IOException {
        if (!Files.exists(Paths.get(pfad))) return new ArrayList<>();
        return Files.lines(Paths.get(pfad))
                .map(line -> line.split(";"))
                .filter(p -> p.length == 4)
                .map(p -> new Termin(p[0], LocalDate.parse(p[1]), p[2], p[3]))
                .collect(Collectors.toList());
    }

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
}
