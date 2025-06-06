package at.spengergasse.projekt.view;

import at.spengergasse.projekt.controller.ZieleControllerFX;
import at.spengergasse.projekt.model.Ziele;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

/**
 * View für die Zielverwaltung.
 */
public class ZieleViewFX extends VBox {

    private final ZieleControllerFX controller;

    public ZieleViewFX(String username) {
        this.controller = new ZieleControllerFX(username);

        this.setSpacing(20);
        this.setPadding(new Insets(30));
        this.setAlignment(Pos.TOP_CENTER);

        TableView<Ziele> table = new TableView<>(controller.getZiele());
        TableColumn<Ziele, String> titelCol = new TableColumn<>("Titel");
        titelCol.setCellValueFactory(new PropertyValueFactory<>("titel"));

        TableColumn<Ziele, String> beschrCol = new TableColumn<>("Beschreibung");
        beschrCol.setCellValueFactory(new PropertyValueFactory<>("beschreibung"));

        table.getColumns().addAll(titelCol, beschrCol);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        VBox.setVgrow(table, Priority.ALWAYS);

        // Auswahl aufheben bei Klick auf leere Stelle
        table.setRowFactory(tv -> {
            TableRow<Ziele> row = new TableRow<>();
            row.setOnMouseClicked(e -> {
                if (row.isEmpty()) {
                    table.getSelectionModel().clearSelection();
                }
            });
            return row;
        });

        TextField titelField = new TextField();
        titelField.setPromptText("Titel");

        TextField beschrField = new TextField();
        beschrField.setPromptText("Beschreibung");

        Button addBtn = new Button("Hinzufügen");
        Button removeBtn = new Button("Löschen");
        removeBtn.setDisable(true);

        // Button aktivieren nur bei Auswahl
        table.getSelectionModel().selectedItemProperty().addListener((obs, alt, neu) -> {
            removeBtn.setDisable(neu == null);
        });

        addBtn.setOnAction(e -> {
            if (!titelField.getText().isEmpty() && !beschrField.getText().isEmpty()) {
                Ziele ziel = new Ziele(titelField.getText(), beschrField.getText());
                controller.addZiel(ziel);
                titelField.clear();
                beschrField.clear();
            }
        });

        removeBtn.setOnAction(e -> {
            Ziele ziel = table.getSelectionModel().getSelectedItem();
            if (ziel != null) {
                controller.removeZiel(ziel);
            }
        });

        HBox eingabeBox = new HBox(10, titelField, beschrField, addBtn, removeBtn);
        eingabeBox.setAlignment(Pos.CENTER);

        this.getChildren().addAll(table, eingabeBox);
    }
}
