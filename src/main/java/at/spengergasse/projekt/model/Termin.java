package at.spengergasse.projekt.model;

import javafx.beans.property.*;

import java.time.LocalDate;
import java.util.Objects;

/**
 * Die Klasse {@code Termin} repräsentiert einen schulischen Termin wie z.B. eine Prüfung,
 * Hausaufgabe oder Veranstaltung. Sie speichert Informationen zu Titel, Datum, Art und einer optionalen Notiz.
 * <p>
 * Zur besseren Integration mit JavaFX nutzt sie Observable Properties, wodurch UI-Komponenten direkt gebunden werden können.
 */
public class Termin {

    private final StringProperty titel = new SimpleStringProperty();
    private final ObjectProperty<LocalDate> datum = new SimpleObjectProperty<>();
    private final StringProperty art = new SimpleStringProperty();
    private final StringProperty notiz = new SimpleStringProperty();

    /**
     * Erstellt ein neues {@code Termin}-Objekt mit allen Angaben.
     *
     * @param titel Titel des Termins (z.B. "Mathematik-Test")
     * @param datum Datum des Termins
     * @param art   Art des Termins (z.B. "Prüfung", "Event", ...)
     * @param notiz Zusätzliche Notiz oder Beschreibung
     */
    public Termin(String titel, LocalDate datum, String art, String notiz) {
        this.titel.set(titel);
        this.datum.set(datum);
        this.art.set(art);
        this.notiz.set(notiz);
    }

    /** @return Der Titel des Termins */
    public String getTitel() {
        return titel.get();
    }

    /** @param t Neuer Titel */
    public void setTitel(String t) {
        titel.set(t);
    }

    /** @return Titel-Property für JavaFX-Datenbindung */
    public StringProperty titelProperty() {
        return titel;
    }

    /** @return Das Datum des Termins */
    public LocalDate getDatum() {
        return datum.get();
    }

    /** @param d Neues Datum */
    public void setDatum(LocalDate d) {
        datum.set(d);
    }

    /** @return Datum-Property für JavaFX-Datenbindung */
    public ObjectProperty<LocalDate> datumProperty() {
        return datum;
    }

    /** @return Die Art des Termins (z.B. "Prüfung") */
    public String getArt() {
        return art.get();
    }

    /** @param a Neue Art */
    public void setArt(String a) {
        art.set(a);
    }

    /** @return Art-Property für JavaFX-Datenbindung */
    public StringProperty artProperty() {
        return art;
    }

    /** @return Notiztext zum Termin (kann leer sein) */
    public String getNotiz() {
        return notiz.get();
    }

    /** @param n Neue Notiz */
    public void setNotiz(String n) {
        notiz.set(n);
    }

    /** @return Notiz-Property für JavaFX-Datenbindung */
    public StringProperty notizProperty() {
        return notiz;
    }

    /**
     * Vergleicht dieses Termin-Objekt mit einem anderen.
     * Zwei Termine sind gleich, wenn Titel, Datum, Art und Notiz übereinstimmen.
     *
     * @param o Das Vergleichsobjekt
     * @return true, wenn beide Termine identisch sind
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Termin termin = (Termin) o;
        return Objects.equals(getTitel(), termin.getTitel()) &&
                Objects.equals(getDatum(), termin.getDatum()) &&
                Objects.equals(getArt(), termin.getArt()) &&
                Objects.equals(getNotiz(), termin.getNotiz());
    }

    /**
     * Berechnet einen eindeutigen Hashcode aus allen Eigenschaften des Termins.
     *
     * @return Hashcode dieses Objekts
     */
    @Override
    public int hashCode() {
        return Objects.hash(getTitel(), getDatum(), getArt(), getNotiz());
    }
}
