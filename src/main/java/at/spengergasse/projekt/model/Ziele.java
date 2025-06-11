package at.spengergasse.projekt.model;

import java.util.Objects;

/**
 * Repräsentiert ein Ziel eines Benutzers, z.B. als Eintrag in einer To-Do-Liste.
 * Ein Ziel besteht aus einem beschreibenden Text sowie einem Erledigt-Status.
 */
public class Ziele {

    private String zielText;
    private boolean erledigt;

    /**
     * Erstellt ein neues Ziel, das standardmäßig als nicht erledigt gilt.
     *
     * @param zielText Textbeschreibung des Ziels (z.B. „Mathe lernen“)
     */
    public Ziele(String zielText) {
        this.zielText = zielText;
        this.erledigt = false;
    }

    /**
     * Erstellt ein neues Ziel mit angegebenem Erledigt-Status.
     *
     * @param zielText Zieltext
     * @param erledigt true, wenn das Ziel bereits erledigt ist
     */
    public Ziele(String zielText, boolean erledigt) {
        this.zielText = zielText;
        this.erledigt = erledigt;
    }

    /** @return Der Zieltext */
    public String getZielText() {
        return zielText;
    }

    /** @param zielText Neuer Text für das Ziel */
    public void setZielText(String zielText) {
        this.zielText = zielText;
    }

    /** @return true, wenn das Ziel als erledigt markiert ist */
    public boolean isErledigt() {
        return erledigt;
    }

    /** @param erledigt Neuer Erledigt-Status */
    public void setErledigt(boolean erledigt) {
        this.erledigt = erledigt;
    }

    /**
     * Gibt den Zieltext zurück.
     * Wird z.B. von einer {@code ListView<Ziele>} zur Anzeige verwendet.
     *
     * @return Der Zieltext als Zeichenkette
     */
    @Override
    public String toString() {
        return zielText;
    }

    /**
     * Zwei Ziele gelten als gleich, wenn ihr Zieltext identisch ist.
     *
     * @param o Vergleichsobjekt
     * @return true, wenn Zieltexte gleich sind
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Ziele ziele = (Ziele) o;
        return Objects.equals(zielText, ziele.zielText);
    }

    /** @return Hashcode basierend auf dem Zieltext */
    @Override
    public int hashCode() {
        return Objects.hash(zielText);
    }
}
