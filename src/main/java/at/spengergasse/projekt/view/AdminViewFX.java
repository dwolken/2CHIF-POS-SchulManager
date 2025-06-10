package at.spengergasse.projekt.view;

import at.spengergasse.projekt.controller.AdminControllerFX;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.*;

/**
 * View für den Admin-Modus.
 * Zeigt die Benutzerverwaltung mit Tabelle, Eingabeformular,
 * Bearbeitungsoptionen und Logout-Schaltfläche.
 */
public class AdminViewFX extends VBox {

    /**
     * Konstruktor: Initialisiert die Admin-Oberfläche.
     * Lädt alle Bestandteile der Benutzerverwaltung (Tabelle, Buttons, Formular).
     */
    public AdminViewFX() {
        setSpacing(15);
        setPadding(new Insets(30));

        AdminControllerFX controller = new AdminControllerFX();

        Label title = new Label("Benutzerverwaltung (Admin-Modus)");
        title.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");
        title.setAlignment(Pos.CENTER);

        VBox tableBox = new VBox(controller.getTable());
        VBox aktionenBox = new VBox(controller.getAktionen());
        VBox formularBox = new VBox(controller.getFormular());
        VBox logoutBox = new VBox(controller.getLogoutButton());

        tableBox.setSpacing(10);
        formularBox.setSpacing(10);
        aktionenBox.setSpacing(10);
        logoutBox.setSpacing(10);
        logoutBox.setAlignment(Pos.CENTER);
        logoutBox.setPadding(new Insets(10));

        getChildren().addAll(title, tableBox, aktionenBox, formularBox, logoutBox);
    }
}
