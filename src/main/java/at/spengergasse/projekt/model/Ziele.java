package at.spengergasse.projekt.model;

/**
 * Repräsentiert ein Ziel mit Titel und Beschreibung.
 */
public class Ziele {
    private String titel;
    private String beschreibung;

    /**
     * Konstruktor für Ziel.
     * @param titel Titel des Ziels
     * @param beschreibung Beschreibung des Ziels
     */
    public Ziele(String titel, String beschreibung) {
        this.titel = titel;
        this.beschreibung = beschreibung;
    }

    public String getTitel() {
        return titel;
    }

    public void setTitel(String titel) {
        this.titel = titel;
    }

    public String getBeschreibung() {
        return beschreibung;
    }

    public void setBeschreibung(String beschreibung) {
        this.beschreibung = beschreibung;
    }
}
