package at.spengergasse.projekt.model;

import javafx.beans.property.*;

import java.time.LocalDate;

/**
 * Repräsentiert einen schulischen Termin mit Titel, Datum, Art und optionaler Notiz.
 * Verwendet JavaFX Properties zur einfachen Bindung an UI-Komponenten.
 */
public class Termin {

    private final StringProperty titel = new SimpleStringProperty();
    private final ObjectProperty<LocalDate> datum = new SimpleObjectProperty<>();
    private final StringProperty art = new SimpleStringProperty();
    private final StringProperty notiz = new SimpleStringProperty();

    /**
     * Konstruktor zum Erstellen eines Termins.
     *
     * @param titel Titel des Termins
     * @param datum Datum des Termins
     * @param art   Art des Termins (z.B. Prüfung, Event, ...)
     * @param notiz Zusätzliche Notiz (optional)
     */
    public Termin(String titel, LocalDate datum, String art, String notiz) {
        this.titel.set(titel);
        this.datum.set(datum);
        this.art.set(art);
        this.notiz.set(notiz);
    }

    /**
     * Gibt den Titel des Termins zurück.
     *
     * @return Titel
     */
    public String getTitel() {
        return titel.get();
    }

    /**
     * Setzt den Titel des Termins.
     *
     * @param t Neuer Titel
     */
    public void setTitel(String t) {
        titel.set(t);
    }

    /**
     * Property-Zugriff für Datenbindung des Titels.
     *
     * @return Titel-Property
     */
    public StringProperty titelProperty() {
        return titel;
    }

    /**
     * Gibt das Datum des Termins zurück.
     *
     * @return Datum
     */
    public LocalDate getDatum() {
        return datum.get();
    }

    /**
     * Setzt das Datum des Termins.
     *
     * @param d Neues Datum
     */
    public void setDatum(LocalDate d) {
        datum.set(d);
    }

    /**
     * Property-Zugriff für Datenbindung des Datums.
     *
     * @return Datum-Property
     */
    public ObjectProperty<LocalDate> datumProperty() {
        return datum;
    }

    /**
     * Gibt die Art des Termins zurück.
     *
     * @return Art (z.B. Prüfung, Hausaufgabe, ...)
     */
    public String getArt() {
        return art.get();
    }

    /**
     * Setzt die Art des Termins.
     *
     * @param a Neue Art
     */
    public void setArt(String a) {
        art.set(a);
    }

    /**
     * Property-Zugriff für Datenbindung der Art.
     *
     * @return Art-Property
     */
    public StringProperty artProperty() {
        return art;
    }

    /**
     * Gibt die Notiz zum Termin zurück.
     *
     * @return Notiztext
     */
    public String getNotiz() {
        return notiz.get();
    }

    /**
     * Setzt die Notiz zum Termin.
     *
     * @param n Neuer Notiztext
     */
    public void setNotiz(String n) {
        notiz.set(n);
    }

    /**
     * Property-Zugriff für Datenbindung der Notiz.
     *
     * @return Notiz-Property
     */
    public StringProperty notizProperty() {
        return notiz;
    }
}
