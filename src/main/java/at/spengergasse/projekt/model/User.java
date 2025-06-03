package at.spengergasse.projekt.model;

/**
 * Repräsentiert einen Benutzer im System.
 */
public class User {

    private final String benutzername;
    private final String rolle;
    private final String verschlüsseltesPasswort;

    public User(String benutzername, String rolle, String verschlüsseltesPasswort) {
        this.benutzername = benutzername;
        this.rolle = rolle;
        this.verschlüsseltesPasswort = verschlüsseltesPasswort;
    }

    public String getBenutzername() {
        return benutzername;
    }

    public String getRolle() {
        return rolle;
    }

    public String getVerschlüsseltesPasswort() {
        return verschlüsseltesPasswort;
    }
}
