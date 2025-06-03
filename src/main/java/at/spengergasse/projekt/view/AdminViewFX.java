package at.spengergasse.projekt.view;

import at.spengergasse.projekt.model.User;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

/**
 * View für die Admin-Oberfläche.
 * Zeigt alle Benutzer in einer Tabelle und bietet Verwaltungsfunktionen.
 */
public class AdminViewFX extends BorderPane {

    private final TableView<User> userTable;
    private final Button deleteButton;
    private final Button renameButton;
    private final Button changePasswordButton;
    private final Button addButton;
    private final Label errorLabel;

    /**
     * Konstruktor. Baut die Admin-Oberfläche auf.
     */
    public AdminViewFX() {
        this.setPadding(new Insets(20));

        Label title = new Label("Admin-Benutzerverwaltung");
        title.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        userTable = new TableView<>();
        TableColumn<User, String> usernameCol = new TableColumn<>("Benutzername");
        usernameCol.setCellValueFactory(cellData -> cellData.getValue().usernameProperty());

        TableColumn<User, String> roleCol = new TableColumn<>("Rolle");
        roleCol.setCellValueFactory(cellData -> cellData.getValue().roleProperty());

        userTable.getColumns().addAll(usernameCol, roleCol);
        userTable.setPrefHeight(300);

        deleteButton = new Button("Löschen");
        renameButton = new Button("Umbenennen");
        changePasswordButton = new Button("Passwort ändern");
        addButton = new Button("Neu");

        HBox buttonBox = new HBox(10, deleteButton, renameButton, changePasswordButton, addButton);
        buttonBox.setPadding(new Insets(10));

        errorLabel = new Label();
        errorLabel.setStyle("-fx-text-fill: red;");

        VBox centerBox = new VBox(10, title, userTable, buttonBox, errorLabel);
        this.setCenter(centerBox);
    }

    /**
     * Übergibt die User-Liste an die Tabelle.
     *
     * @param users ObservableList mit Usern
     */
    public void setUserList(ObservableList<User> users) {
        userTable.setItems(users);
    }

    /**
     * Gibt den aktuell ausgewählten User zurück.
     *
     * @return ausgewählter User oder null
     */
    public User getSelectedUser() {
        return userTable.getSelectionModel().getSelectedItem();
    }

    public Button getDeleteButton() {
        return deleteButton;
    }

    public Button getRenameButton() {
        return renameButton;
    }

    public Button getChangePasswordButton() {
        return changePasswordButton;
    }

    public Button getAddButton() {
        return addButton;
    }

    /**
     * Zeigt eine Fehlermeldung im roten Label an.
     *
     * @param msg Fehlermeldung
     */
    public void setError(String msg) {
        errorLabel.setText(msg);
    }
}
