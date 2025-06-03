package at.spengergasse.projekt.model;

import java.time.LocalDate;

/**
 * Datenmodell für einen Termin eines Benutzers. Ein Termin besteht aus
 * Titel/Fach, Datum, Typ und einer optionalen Notiz.
 */
public class Termin {
    /** Titel bzw. Fach des Termins. */
    private String fach;
    /** Datum des Termins. */
    private LocalDate datum;
    /** Art des Termins (z.B. Prüfung). */
    private String typ;
    /** Optionale Notiz. */
    private String notiz;

    /**
     * Erstellt einen neuen Termin.
     *
     * @param fach  Titel oder Fach
     * @param datum Datum des Termins
     * @param typ   Art des Termins
     * @param notiz optionale Notiz
     */
    public Termin(String fach, LocalDate datum, String typ, String notiz) {
        this.fach = fach;
        this.datum = datum;
        this.typ = typ;
        this.notiz = notiz;
    }

    /** @return Titel/Fach des Termins */
    public String getFach() { return fach; }

    /** @param fach neuer Titel/Fach */
    public void setFach(String fach) { this.fach = fach; }

    /** @return Datum des Termins */
    public LocalDate getDatum() { return datum; }

    /** @param datum neues Datum */
    public void setDatum(LocalDate datum) { this.datum = datum; }

    /** @return Typ des Termins */
    public String getTyp() { return typ; }

    /** @param typ neuer Typ */
    public void setTyp(String typ) { this.typ = typ; }

    /** @return Notiz zum Termin */
    public String getNotiz() { return notiz; }

    /** @param notiz neue Notiz */
    public void setNotiz(String notiz) { this.notiz = notiz; }
}
