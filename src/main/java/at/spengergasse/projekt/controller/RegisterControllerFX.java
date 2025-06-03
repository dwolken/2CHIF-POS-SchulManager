package at.spengergasse.projekt.controller;

import at.spengergasse.projekt.model.CsvManager;
import at.spengergasse.projekt.model.Encoding;
import at.spengergasse.projekt.model.Encoding.EncodingType;
import at.spengergasse.projekt.model.EncodingException;
import at.spengergasse.projekt.view.RegisterViewFX;
import javafx.event.ActionEvent;
import javafx.stage.Stage;

/**
 * Controller für den Registrierungsvorgang.
 * Verarbeitet Eingaben und speichert Benutzer.
 */
public class RegisterControllerFX {

    private final RegisterViewFX view;
    private final Stage stage;

    /**
     * Konstruktor des RegisterControllers.
     *
     * @param view  das Registrierungsformular
     * @param stage das Fenster, in dem es angezeigt wird
     */
    public RegisterControllerFX(RegisterViewFX view, Stage stage) {
        this.view = view;
        this.stage = stage;

        view.getRegisterButton().setOnAction(this::handleRegister);
        view.getCancelButton().setOnAction(e -> stage.close());
    }

    /**
     * Verarbeitet den Registrierungs-Button.
     */
    private void handleRegister(ActionEvent event) {
        String username = view.getUsername();
        String password = view.getPassword();
        String repeated = view.getRepeatedPassword();

        if (username.isEmpty() || password.isEmpty() || repeated.isEmpty()) {
            view.setError("Bitte fülle alle Felder aus.");
            return;
        }

        if (!password.equals(repeated)) {
            view.setError("Passwörter stimmen nicht überein.");
            view.clearPasswords();
            return;
        }

        String hashed;
        try {
            hashed = new Encoding(password, EncodingType.SHA256).bytesToHex();
        } catch (Exception e) {
            view.setError("Fehler bei der Verschlüsselung.");
            return;
        }

        boolean success = CsvManager.registerUser(username, "user", hashed);
        if (!success) {
            view.setError("Benutzername bereits vergeben.");
            return;
        }

        stage.close(); // Registrierung erfolgreich → Fenster schließen
    }
}
