package at.spengergasse.projekt.model;

import javafx.beans.property.*;

import java.time.LocalDate;

/**
 * Repräsentiert einen schulischen Termin.
 */
public class Termin {

    private final StringProperty titel = new SimpleStringProperty();
    private final ObjectProperty<LocalDate> datum = new SimpleObjectProperty<>();
    private final StringProperty art = new SimpleStringProperty();
    private final StringProperty notiz = new SimpleStringProperty();

    public Termin(String titel, LocalDate datum, String art, String notiz) {
        this.titel.set(titel);
        this.datum.set(datum);
        this.art.set(art);
        this.notiz.set(notiz);
    }

    public String getTitel() { return titel.get(); }
    public void setTitel(String t) { titel.set(t); }
    public StringProperty titelProperty() { return titel; }

    public LocalDate getDatum() { return datum.get(); }
    public void setDatum(LocalDate d) { datum.set(d); }
    public ObjectProperty<LocalDate> datumProperty() { return datum; }

    public String getArt() { return art.get(); }
    public void setArt(String a) { art.set(a); }
    public StringProperty artProperty() { return art; }

    public String getNotiz() { return notiz.get(); }
    public void setNotiz(String n) { notiz.set(n); }
    public StringProperty notizProperty() { return notiz; }
}
