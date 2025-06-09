package at.spengergasse.projekt.view;

import at.spengergasse.projekt.controller.AdminControllerFX;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.*;

/**
 * View für den Admin-Modus. Zeigt Benutzerliste mit Bearbeitungsmöglichkeiten.
 */
public class AdminViewFX extends VBox {

    /**
     * Initialisiert die Admin-Oberfläche.
     */
    public AdminViewFX() {
        setSpacing(10);
        setPadding(new Insets(20));

        AdminControllerFX controller = new AdminControllerFX();

        Label title = new Label("Benutzerverwaltung (Admin-Modus)");
        title.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        VBox tableBox = new VBox(controller.getTable());
        VBox aktionenBox = new VBox(controller.getAktionen());
        VBox formularBox = new VBox(controller.getFormular());
        VBox logoutBox = new VBox(controller.getLogoutButton());
        logoutBox.setPadding(new Insets(10));
        logoutBox.setSpacing(10);
        logoutBox.setAlignment(Pos.CENTER);

        getChildren().addAll(title, tableBox, aktionenBox, formularBox, logoutBox);
    }
}