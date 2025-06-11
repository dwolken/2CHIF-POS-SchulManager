package at.spengergasse.projekt.model;

/**
 * Die {@code EncodingException} ist eine benutzerdefinierte Ausnahme,
 * die bei Fehlern im Zusammenhang mit der {@link Encoding}-Klasse ausgelöst wird.
 * <p>
 * Typische Ursachen:
 * <ul>
 *     <li>Ungültiger oder nicht unterstützter Algorithmus</li>
 *     <li>Null-Werte bei Parametern</li>
 *     <li>Ungültige Dateiobjekte</li>
 * </ul>
 */
public class EncodingException extends Exception {

    /**
     * Erstellt eine neue {@code EncodingException} ohne Nachricht.
     */
    public EncodingException() {
        super();
    }

    /**
     * Erstellt eine {@code EncodingException} mit einer Beschreibung.
     *
     * @param message Eine detailreiche Fehlermeldung
     */
    public EncodingException(String message) {
        super(message);
    }

    /**
     * Erstellt eine {@code EncodingException} mit der ursprünglichen Ursache.
     *
     * @param cause Die zugrunde liegende {@code Throwable}-Instanz
     */
    public EncodingException(Throwable cause) {
        super(cause);
    }

    /**
     * Erstellt eine {@code EncodingException} mit Nachricht und Ursache.
     *
     * @param message Fehlermeldung
     * @param cause   Die ursprüngliche Exception
     */
    public EncodingException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Erweiteter Konstruktor mit zusätzlichen Kontrolloptionen.
     *
     * @param message            Beschreibung der Ausnahme
     * @param cause              Ursächliche Exception
     * @param enableSuppression  Gibt an, ob Suppression aktiviert ist
     * @param writableStackTrace Gibt an, ob der StackTrace beschreibbar ist
     */
    public EncodingException(String message, Throwable cause,
                             boolean enableSuppression,
                             boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
