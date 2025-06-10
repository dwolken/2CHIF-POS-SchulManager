package at.spengergasse.projekt.model;

import java.util.Objects;

/**
 * Modellklasse für ein einzelnes Ziel.
 * Enthält den Zieltext und den Erledigt-Status (z. B. für To-Do-Listen).
 */
public class Ziele {

    private String zielText;
    private boolean erledigt;

    /**
     * Erstellt ein neues Ziel mit unerledigtem Status.
     *
     * @param zielText Der Zielinhalt (z.B. "Mathe lernen")
     */
    public Ziele(String zielText) {
        this.zielText = zielText;
        this.erledigt = false;
    }

    /**
     * Erstellt ein neues Ziel mit definiertem Status.
     *
     * @param zielText Inhalt des Ziels
     * @param erledigt true, wenn das Ziel bereits erledigt ist
     */
    public Ziele(String zielText, boolean erledigt) {
        this.zielText = zielText;
        this.erledigt = erledigt;
    }

    /**
     * Gibt den Zieltext zurück.
     *
     * @return Zieltext
     */
    public String getZielText() {
        return zielText;
    }

    /**
     * Setzt den Zieltext.
     *
     * @param zielText Neuer Text des Ziels
     */
    public void setZielText(String zielText) {
        this.zielText = zielText;
    }

    /**
     * Prüft, ob das Ziel als erledigt markiert ist.
     *
     * @return true, wenn erledigt
     */
    public boolean isErledigt() {
        return erledigt;
    }

    /**
     * Setzt den Erledigt-Status.
     *
     * @param erledigt true, wenn erledigt
     */
    public void setErledigt(boolean erledigt) {
        this.erledigt = erledigt;
    }

    /**
     * Gibt den Zieltext zurück.
     * Wird z.B. von ListView verwendet.
     *
     * @return Zieltext als String
     */
    @Override
    public String toString() {
        return zielText;
    }

    /**
     * Vergleicht zwei Ziele basierend auf ihrem Text.
     *
     * @param o Vergleichsobjekt
     * @return true, wenn Texte gleich sind
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Ziele ziele = (Ziele) o;
        return Objects.equals(zielText, ziele.zielText);
    }

    /**
     * Erzeugt einen Hashcode basierend auf dem Zieltext.
     *
     * @return Hashcode
     */
    @Override
    public int hashCode() {
        return Objects.hash(zielText);
    }
}
