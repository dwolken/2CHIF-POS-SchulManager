package at.spengergasse.projekt.view;

import at.spengergasse.projekt.controller.TerminControllerFX;
import at.spengergasse.projekt.model.Termin;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.TableView;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

/**
 * TerminViewFX zeigt alle Termine in einer Tabelle mit Formular und Buttons.
 * Passt sich automatisch an die Breite an und verhindert horizontale Scrollbars.
 */
public class TerminViewFX extends VBox {

    private final TerminControllerFX controller;

    public TerminViewFX(String username, String pfad) {
        this.controller = new TerminControllerFX(username, pfad);

        this.setSpacing(20);
        this.setPadding(new Insets(30));
        this.setAlignment(Pos.TOP_CENTER);
        this.getStyleClass().add("termin-view");

        TableView<Termin> table = controller.getTable();
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY); // <<< Verhindert horizontale Scrollbar
        table.setMaxWidth(Double.MAX_VALUE);
        VBox.setVgrow(table, Priority.ALWAYS);

        VBox formularBox = new VBox(controller.getFormular());
        formularBox.setAlignment(Pos.CENTER);

        VBox buttonBox = new VBox(controller.getAktionen());
        buttonBox.setAlignment(Pos.CENTER);

        this.getChildren().addAll(table, formularBox, buttonBox);
    }

    public TableView<Termin> getTable() {
        return controller.getTable();
    }
}
