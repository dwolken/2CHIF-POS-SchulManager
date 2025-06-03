package at.spengergasse.projekt.model;

/**
 * (Geplant für spätere Speicherung einzelner Ziele)
 * Wird aktuell nicht verwendet, kann für Datei-Speicherung vorbereitet werden.
 */
public class Ziele {

    private String text;
    private boolean erledigt;

    public Ziele(String text, boolean erledigt) {
        this.text = text;
        this.erledigt = erledigt;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public boolean isErledigt() {
        return erledigt;
    }

    public void setErledigt(boolean erledigt) {
        this.erledigt = erledigt;
    }
}
