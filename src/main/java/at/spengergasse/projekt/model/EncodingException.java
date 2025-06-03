package at.spengergasse.projekt.model;

/**
 * Eigene Exception, die bei Fehlern während der Kodierung (Hashing) ausgelöst
 * werden kann.
 */
public class EncodingException extends Exception {

    /** Erstellt eine neue EncodingException ohne Nachricht. */
    public EncodingException() {
        super();
    }

    /**
     * Erstellt eine neue EncodingException mit Nachricht.
     *
     * @param message Fehlerbeschreibung
     */
    public EncodingException(String message) {
        super(message);
    }

    /**
     * Erstellt eine neue EncodingException mit Ursache.
     *
     * @param cause ursprüngliche Exception
     */
    public EncodingException(Throwable cause) {
        super(cause);
    }

    /**
     * Erstellt eine neue EncodingException mit Nachricht und Ursache.
     *
     * @param message Fehlermeldung
     * @param cause   Auslösende Exception
     */
    public EncodingException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Vollständiger Konstruktor.
     */
    public EncodingException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
