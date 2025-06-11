package at.spengergasse.projekt.controller;

import at.spengergasse.projekt.model.CsvManager;
import at.spengergasse.projekt.model.PfadManager;
import at.spengergasse.projekt.model.Termin;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Der {@code StatistikControllerFX} analysiert und wertet gespeicherte Termine aus.
 * <p>
 * Er bietet Methoden zur:
 * <ul>
 *     <li>Ermittlung der Gesamtanzahl aller Termine</li>
 *     <li>Bestimmung der Häufigkeit eines bestimmten Termintyps</li>
 *     <li>Erstellung einer Verteilung nach Terminarten</li>
 * </ul>
 */
public class StatistikControllerFX {

    private final List<Termin> termine;

    /**
     * Konstruktor. Lädt alle Termine für den angegebenen Benutzer.
     * Bei Fehlern wird eine leere Liste verwendet.
     *
     * @param username Der Benutzername, dessen Termine analysiert werden sollen.
     */
    public StatistikControllerFX(String username) {
        String pfad = PfadManager.getTerminPfad(username);
        List<Termin> geladen;
        try {
            geladen = CsvManager.loadTermine(pfad);
        } catch (IOException e) {
            geladen = List.of(); // falls Datei fehlt oder defekt
        }
        this.termine = geladen;
    }

    /**
     * Gibt die Gesamtanzahl aller geladenen Termine zurück.
     *
     * @return Gesamtanzahl als {@code int}
     */
    public int getGesamtAnzahl() {
        return termine.size();
    }

    /**
     * Zählt die Anzahl der Termine mit einer bestimmten Art.
     * Die Art wird dabei case-insensitive verglichen.
     *
     * @param art Die Terminart (z.B. "Prüfung", "Meeting", etc.)
     * @return Anzahl der entsprechenden Termine
     */
    public int getAnzahlNachArt(String art) {
        return (int) termine.stream()
                .filter(t -> t.getArt().equalsIgnoreCase(art))
                .count();
    }

    /**
     * Erstellt eine Statistik über alle verschiedenen Terminarten.
     * Jede Art wird einmalig als Schlüssel in der Map aufgeführt.
     *
     * @return {@code Map<String, Integer>} mit Terminart als Schlüssel und Anzahl als Wert
     */
    public Map<String, Integer> getVerteilungNachArt() {
        Map<String, Integer> verteilung = new HashMap<>();
        for (Termin t : termine) {
            String art = t.getArt();
            verteilung.put(art, verteilung.getOrDefault(art, 0) + 1);
        }
        return verteilung;
    }
}
