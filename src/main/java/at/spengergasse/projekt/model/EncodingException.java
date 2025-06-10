package at.spengergasse.projekt.model;

/**
 * Eigene Exception-Klasse für Fehler bei der Encoding-Verarbeitung.
 * Wird z.B. ausgelöst bei ungültigen Algorithmen oder fehlerhaften Eingaben.
 */
public class EncodingException extends Exception {

	/**
	 * Standard-Konstruktor ohne Nachricht.
	 */
	public EncodingException() {
		super();
	}

	/**
	 * Konstruktor mit Fehlermeldung.
	 *
	 * @param message Beschreibende Fehlermeldung
	 */
	public EncodingException(String message) {
		super(message);
	}

	/**
	 * Konstruktor mit Ursache.
	 *
	 * @param cause Die zugrunde liegende Exception
	 */
	public EncodingException(Throwable cause) {
		super(cause);
	}

	/**
	 * Konstruktor mit Nachricht und Ursache.
	 *
	 * @param message Beschreibende Fehlermeldung
	 * @param cause   Die zugrunde liegende Exception
	 */
	public EncodingException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * Erweiterter Konstruktor mit Kontrolle über Suppression und StackTrace.
	 *
	 * @param message            Fehlermeldung
	 * @param cause              Ursache
	 * @param enableSuppression  Suppression unterdrücken
	 * @param writableStackTrace StackTrace beschreibbar?
	 */
	public EncodingException(String message, Throwable cause,
							 boolean enableSuppression,
							 boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}
}
