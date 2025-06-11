package at.spengergasse.projekt.model;

/**
 * Repräsentiert einen Benutzer im SchulManager-System mit Benutzername, Rolle und verschlüsseltem Passwort.
 * Diese Klasse wird insbesondere für das Verwalten, Speichern und Vergleichen von Benutzerdaten verwendet.
 */
public class User {

    private final String benutzername;
    private final String rolle;
    private final String verschlüsseltesPasswort;

    /**
     * Erstellt ein neues {@code User}-Objekt mit den angegebenen Daten.
     *
     * @param benutzername            Eindeutiger Benutzername
     * @param rolle                   Rolle des Benutzers (z. B. "admin" oder "user")
     * @param verschlüsseltesPasswort Passwort als SHA256-Hash (bereits verschlüsselt)
     */
    public User(String benutzername, String rolle, String verschlüsseltesPasswort) {
        this.benutzername = benutzername;
        this.rolle = rolle;
        this.verschlüsseltesPasswort = verschlüsseltesPasswort;
    }

    /** @return Der Benutzername dieses Users */
    public String getBenutzername() {
        return benutzername;
    }

    /** @return Die Rolle dieses Users (z.B. "admin") */
    public String getRolle() {
        return rolle;
    }

    /** @return Das verschlüsselte Passwort (Hashwert als String) */
    public String getVerschlüsseltesPasswort() {
        return verschlüsseltesPasswort;
    }
}
