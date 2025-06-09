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
 */
public class StatistikControllerFX {

    private final List<Termin> termine;

    /**
     * Lädt alle Termine des Benutzers.
     * @param username Benutzername
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
     * Gesamtanzahl aller Termine
     */
    public int getGesamtAnzahl() {
        return termine.size();
    }

    /**
     * Anzahl der Termine nach Art (z. B. "Prüfung")
     */
    public int getAnzahlNachArt(String art) {
        return (int) termine.stream()
                .filter(t -> t.getArt().equalsIgnoreCase(art))
                .count();
    }

    /**
     * Zählt alle Arten von Terminen dynamisch.
     * @return Map mit Art als Schlüssel und Häufigkeit als Wert
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
