package at.spengergasse.projekt.view;

import at.spengergasse.projekt.controller.ZieleControllerFX;
import at.spengergasse.projekt.model.Ziele;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;

/**
 * ZieleViewFX zeigt eine Liste von Zielen mit Checkboxen und Buttons zum Hinzufügen und Entfernen.
 * Auswahl wird bei Klick auf leere Zeile aufgehoben. MVC-konform.
 */
public class ZieleViewFX extends VBox {

    private final ZieleControllerFX controller;
    private final ListView<Ziele> listView;
    private final TextField eingabeFeld;
    private final Button addBtn;
    private final Button removeBtn;

    public ZieleViewFX(String username) {
        this.controller = new ZieleControllerFX(username);

        this.setSpacing(20);
        this.setPadding(new Insets(30));
        this.setAlignment(Pos.TOP_CENTER);

        listView = new ListView<>(controller.getZiele());
        controller.getAktionen(listView);

        eingabeFeld = new TextField();
        eingabeFeld.setPromptText("Neues Ziel eingeben...");

        addBtn = new Button("Hinzufügen");
        addBtn.setOnAction(e -> controller.handleAdd(eingabeFeld));

        removeBtn = new Button("Löschen");
        removeBtn.setDisable(true);
        removeBtn.setOnAction(e -> controller.handleRemove(listView));

        listView.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            removeBtn.setDisable(newVal == null);
        });

        VBox form = new VBox(10, eingabeFeld, addBtn, removeBtn);
        form.setAlignment(Pos.CENTER);

        this.getChildren().addAll(listView, form);
    }
}
