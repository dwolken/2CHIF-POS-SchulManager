package at.spengergasse.projekt.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * Repräsentiert einen Benutzer des SchulManagers.
 * Wird z. B. in der AdminView für die Benutzerverwaltung verwendet.
 */
public class User {

    private final StringProperty username;
    private final StringProperty role;
    private final StringProperty password;

    /**
     * Erstellt einen neuen Benutzer mit Name, Rolle und Passwort.
     *
     * @param username Benutzername
     * @param role     Rolle (z. B. "admin" oder "user")
     * @param password verschlüsseltes Passwort
     */
    public User(String username, String role, String password) {
        this.username = new SimpleStringProperty(username);
        this.role = new SimpleStringProperty(role);
        this.password = new SimpleStringProperty(password);
    }

    /** @return Benutzername */
    public String getUsername() {
        return username.get();
    }

    /** @param username neuer Benutzername */
    public void setUsername(String username) {
        this.username.set(username);
    }

    /** @return JavaFX-Property für Binding */
    public StringProperty usernameProperty() {
        return username;
    }

    /** @return Benutzerrolle */
    public String getRole() {
        return role.get();
    }

    /** @param role neue Rolle */
    public void setRole(String role) {
        this.role.set(role);
    }

    /** @return JavaFX-Property für Binding */
    public StringProperty roleProperty() {
        return role;
    }

    /** @return verschlüsseltes Passwort */
    public String getPassword() {
        return password.get();
    }

    /** @param password neues Passwort */
    public void setPassword(String password) {
        this.password.set(password);
    }

    /** @return JavaFX-Property für Binding */
    public StringProperty passwordProperty() {
        return password;
    }
}
