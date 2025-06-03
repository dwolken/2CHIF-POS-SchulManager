package at.spengergasse.projekt.controller;

import at.spengergasse.projekt.model.CsvManager;
import at.spengergasse.projekt.model.Termin;

import java.io.IOException;
import java.util.List;

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
        String pfad = System.getProperty("user.home") + "/SchulManager/data/" + username + "_termine.csv";
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
}
