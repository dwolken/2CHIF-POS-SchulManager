package at.spengergasse.projekt.view;

import at.spengergasse.projekt.controller.AdminControllerFX;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.TableView;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

/**
 * View für den Admin-Modus. Zeigt Benutzerliste mit Bearbeitungsmöglichkeiten.
 */
public class AdminViewFX extends VBox {

    private final AdminControllerFX controller;

    public AdminViewFX() {
        this.controller = new AdminControllerFX();

        this.setSpacing(20);
        this.setPadding(new Insets(30));
        this.setAlignment(Pos.TOP_CENTER);
        this.getStyleClass().add("admin-view");

        TableView<String[]> table = controller.getTable();
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        table.setMaxWidth(Double.MAX_VALUE);
        VBox.setVgrow(table, Priority.ALWAYS);

        VBox formularBox = new VBox(controller.getFormular());
        formularBox.setAlignment(Pos.CENTER);

        VBox buttonBox = new VBox(controller.getAktionen());
        buttonBox.setAlignment(Pos.CENTER);

        this.getChildren().addAll(table, formularBox, buttonBox);
    }

    public TableView<String[]> getTable() {
        return controller.getTable();
    }
}