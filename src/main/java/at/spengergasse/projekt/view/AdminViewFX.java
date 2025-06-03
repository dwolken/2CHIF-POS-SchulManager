package at.spengergasse.projekt.view;

import at.spengergasse.projekt.controller.AdminControllerFX;
import javafx.geometry.Insets;
import javafx.scene.control.*;
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

        TableView<String[]> userTable = controller.getUserTable();
        HBox aktionen = controller.getAktionen();
        VBox neuBenutzerFormular = controller.getNeuesBenutzerFormular();

        getChildren().addAll(userTable, aktionen, neuBenutzerFormular);
    }
}
