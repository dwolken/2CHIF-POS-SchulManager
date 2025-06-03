package at.spengergasse.projekt.model;

import java.time.LocalDate;

public class Termin {
    private String fach;
    private LocalDate datum;
    private String typ;
    private String notiz;

    public Termin(String fach, LocalDate datum, String typ, String notiz) {
        this.fach = fach;
        this.datum = datum;
        this.typ = typ;
        this.notiz = notiz;
    }

    public String getFach() { return fach; }
    public void setFach(String fach) { this.fach = fach; }

    public LocalDate getDatum() { return datum; }
    public void setDatum(LocalDate datum) { this.datum = datum; }

    public String getTyp() { return typ; }
    public void setTyp(String typ) { this.typ = typ; }

    public String getNotiz() { return notiz; }
    public void setNotiz(String notiz) { this.notiz = notiz; }
}
