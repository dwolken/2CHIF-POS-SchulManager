package at.spengergasse.projekt.controller;

import at.spengergasse.projekt.model.CsvManager;
import at.spengergasse.projekt.model.PfadManager;
import at.spengergasse.projekt.model.Termin;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Controller zur Auswertung von Termin-Statistiken.
 * Stellt Funktionen zur Analyse der gespeicherten Termine bereit,
 * z.B. Gesamtanzahl, Verteilung nach Art etc.
 */
public class StatistikControllerFX {

    private final List<Termin> termine;

    /**
     * Konstruktor: Lädt alle Termine des angegebenen Benutzers.
     *
     * @param username Benutzername, dessen Termin-Datei geladen werden soll.
     */
    public StatistikControllerFX(String username) {
        String pfad = PfadManager.getTerminPfad(username);
        List<Termin> geladen;
        try {
            geladen = CsvManager.loadTermine(pfad);
        } catch (IOException e) {
            geladen = List.of();
        }
        this.termine = geladen;
    }

    /**
     * Gibt die Gesamtanzahl aller geladenen Termine zurück.
     *
     * @return Anzahl aller Termine.
     */
    public int getGesamtAnzahl() {
        return termine.size();
    }

    /**
     * Zählt, wie viele Termine eine bestimmte Art haben.
     * Die Vergleich erfolgt ohne Beachtung der Groß-/Kleinschreibung.
     *
     * @param art Die Art von Termin (z.B. "Prüfung", "Meeting").
     * @return Anzahl der Termine mit dieser Art.
     */
    public int getAnzahlNachArt(String art) {
        return (int) termine.stream()
                .filter(t -> t.getArt().equalsIgnoreCase(art))
                .count();
    }

    /**
     * Erstellt eine Verteilung aller Terminarten.
     *
     * @return Map mit Terminart als Schlüssel und Anzahl als Wert.
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
