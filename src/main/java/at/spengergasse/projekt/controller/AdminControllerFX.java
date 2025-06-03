package at.spengergasse.projekt.controller;

import at.spengergasse.projekt.model.CsvManager;
import at.spengergasse.projekt.model.Encoding;
import at.spengergasse.projekt.model.Encoding.EncodingType;
import at.spengergasse.projekt.model.EncodingException;
import at.spengergasse.projekt.model.User;
import at.spengergasse.projekt.view.AdminViewFX;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.TextInputDialog;
import javafx.stage.Stage;

import java.security.NoSuchAlgorithmException;
import java.util.Optional;

/**
 * Controller für die Admin-Oberfläche.
 * Ermöglicht Benutzerverwaltung (anzeigen, löschen, ändern, neu anlegen).
 */
public class AdminControllerFX {

    private final AdminViewFX view;
    private final ObservableList<User> users;

    /**
     * Konstruktor des AdminControllers.
     *
     * @param view  die AdminView
     * @param stage das Hauptfenster
     */
    public AdminControllerFX(AdminViewFX view, Stage stage) {
        this.view = view;
        this.users = FXCollections.observableArrayList(CsvManager.loadUsers());

        view.setUserList(users);

        view.getDeleteButton().setOnAction(this::handleDelete);
        view.getRenameButton().setOnAction(this::handleRename);
        view.getChangePasswordButton().setOnAction(this::handleChangePassword);
        view.getAddButton().setOnAction(this::handleAddUser);
    }

    private void handleDelete(ActionEvent event) {
        User selected = view.getSelectedUser();
        if (selected != null && !"admin".equalsIgnoreCase(selected.getUsername())) {
            CsvManager.deleteUser(selected.getUsername());
            users.setAll(CsvManager.loadUsers());
        } else {
            view.setError("Admin-Konto kann nicht gelöscht werden.");
        }
    }

    private void handleRename(ActionEvent event) {
        User selected = view.getSelectedUser();
        if (selected == null) return;

        TextInputDialog dialog = new TextInputDialog(selected.getUsername());
        dialog.setHeaderText("Neuen Benutzernamen eingeben:");
        Optional<String> result = dialog.showAndWait();

        result.ifPresent(newName -> {
            if (!newName.trim().isEmpty()) {
                CsvManager.changeUsername(selected.getUsername(), newName.trim());
                users.setAll(CsvManager.loadUsers());
            }
        });
    }

    private void handleChangePassword(ActionEvent event) {
        User selected = view.getSelectedUser();
        if (selected == null) return;

        TextInputDialog dialog = new TextInputDialog();
        dialog.setHeaderText("Neues Passwort eingeben:");
        Optional<String> result = dialog.showAndWait();

        result.ifPresent(pass -> {
            if (!pass.trim().isEmpty()) {
                try {
                    String hashed = new Encoding(pass.trim(), EncodingType.SHA256).bytesToHex();
                    CsvManager.changePassword(selected.getUsername(), hashed);
                    users.setAll(CsvManager.loadUsers());
                } catch (NoSuchAlgorithmException | EncodingException e) {
                    view.setError("Fehler beim Verschlüsseln.");
                }
            }
        });
    }


    private void handleAddUser(ActionEvent event) {
        TextInputDialog userDialog = new TextInputDialog();
        userDialog.setHeaderText("Benutzernamen für neuen User eingeben:");
        Optional<String> username = userDialog.showAndWait();

        if (username.isEmpty() || username.get().trim().isEmpty()) return;

        TextInputDialog passDialog = new TextInputDialog();
        passDialog.setHeaderText("Passwort für neuen User eingeben:");
        Optional<String> password = passDialog.showAndWait();

        if (password.isEmpty() || password.get().trim().isEmpty()) return;

        try {
            String hashed = new Encoding(password.get(), EncodingType.SHA256).bytesToHex();
            CsvManager.addUser(username.get().trim(), "user", hashed);
            users.setAll(CsvManager.loadUsers());
        } catch (Exception e) {
            view.setError("Fehler beim Anlegen des Benutzers.");
        }
    }
}
