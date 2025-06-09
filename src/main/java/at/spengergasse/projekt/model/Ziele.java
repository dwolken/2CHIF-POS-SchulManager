package at.spengergasse.projekt.model;

import java.util.Objects;

/**
 * Modellklasse für ein einzelnes Ziel. Enthält Zieltext und Erledigt-Status.
 */
public class Ziele {

    private String zielText;
    private boolean erledigt;

    /**
     * Konstruktor mit Zieltext.
     * @param zielText Inhalt des Ziels
     */
    public Ziele(String zielText) {
        this.zielText = zielText;
        this.erledigt = false;
    }

    /**
     * Konstruktor mit Zieltext und Erledigt-Status.
     * @param zielText Inhalt des Ziels
     * @param erledigt true, wenn erledigt
     */
    public Ziele(String zielText, boolean erledigt) {
        this.zielText = zielText;
        this.erledigt = erledigt;
    }

    public String getZielText() {
        return zielText;
    }

    public void setZielText(String zielText) {
        this.zielText = zielText;
    }

    public boolean isErledigt() {
        return erledigt;
    }

    public void setErledigt(boolean erledigt) {
        this.erledigt = erledigt;
    }

    @Override
    public String toString() {
        return zielText;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Ziele ziele = (Ziele) o;
        return Objects.equals(zielText, ziele.zielText);
    }

    @Override
    public int hashCode() {
        return Objects.hash(zielText);
    }
}