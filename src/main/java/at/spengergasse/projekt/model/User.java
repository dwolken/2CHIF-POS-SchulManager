package at.spengergasse.projekt.model;

/**
 * Repräsentiert einen Benutzer im System mit Benutzername, Rolle und verschlüsseltem Passwort.
 * Wird z. B. beim Laden von Benutzerdaten aus der CSV-Datei verwendet.
 */
public class User {

    private final String benutzername;
    private final String rolle;
    private final String verschlüsseltesPasswort;

    /**
     * Erstellt ein neues User-Objekt mit den gegebenen Attributen.
     *
     * @param benutzername            Benutzername
     * @param rolle                   Rolle des Benutzers (z.B. "admin", "user")
     * @param verschlüsseltesPasswort Das bereits verschlüsselte Passwort (SHA256-Hash)
     */
    public User(String benutzername, String rolle, String verschlüsseltesPasswort) {
        this.benutzername = benutzername;
        this.rolle = rolle;
        this.verschlüsseltesPasswort = verschlüsseltesPasswort;
    }

    /**
     * Gibt den Benutzernamen zurück.
     *
     * @return Benutzername
     */
    public String getBenutzername() {
        return benutzername;
    }

    /**
     * Gibt die Rolle des Benutzers zurück.
     *
     * @return Benutzerrolle
     */
    public String getRolle() {
        return rolle;
    }

    /**
     * Gibt das verschlüsselte Passwort (z.B. SHA256-Hash) zurück.
     *
     * @return Passwort als Hash-String
     */
    public String getVerschlüsseltesPasswort() {
        return verschlüsseltesPasswort;
    }
}
