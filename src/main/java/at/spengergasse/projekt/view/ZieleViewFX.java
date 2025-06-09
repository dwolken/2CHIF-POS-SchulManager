package at.spengergasse.projekt.view;

import at.spengergasse.projekt.controller.ZieleControllerFX;
import at.spengergasse.projekt.model.Ziele;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;

/**
 * View für die Anzeige und Bearbeitung von Zielen.
 * Zeigt eine Liste mit Checkboxen, Eingabezeile und Buttons.
 */
public class ZieleViewFX extends VBox {

    private final ZieleControllerFX controller;
    private final ListView<Ziele> listView;
    private final TextField eingabeFeld;
    private final Button addBtn;
    private final Button removeBtn;

    /**
     * Konstruktor für die Ziel-Ansicht.
     *
     * @param username Benutzername
     * @param pfad     Speicherpfad zur Ziel-Datei
     */
    public ZieleViewFX(String username, String pfad) {
        this.controller = new ZieleControllerFX(username, pfad);

        this.setSpacing(20);
        this.setPadding(new Insets(30));
        this.setAlignment(Pos.TOP_CENTER);

        listView = new ListView<>(controller.getZiele());
        listView.setCellFactory(lv -> new ListCell<>() {
            private final CheckBox checkBox = new CheckBox();
            private final HBox hBox = new HBox(10);

            {
                hBox.setAlignment(Pos.CENTER_LEFT);
                hBox.getChildren().add(checkBox);
            }

            @Override
            protected void updateItem(Ziele item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setGraphic(null);
                } else {
                    checkBox.setText(item.getZielText());
                    checkBox.setSelected(item.isErledigt());
                    checkBox.selectedProperty().addListener((obs, wasSelected, isNowSelected) -> {
                        item.setErledigt(isNowSelected);
                        controller.save();
                    });
                    setGraphic(hBox);
                }
            }
        });

        listView.setOnMouseClicked((MouseEvent event) -> {
            if (event.getButton() == MouseButton.PRIMARY) {
                ListCell<?> clickedCell = null;
                for (var node : listView.lookupAll(".list-cell")) {
                    if (node instanceof ListCell<?> cell && cell.getBoundsInParent().contains(event.getX(), event.getY())) {
                        clickedCell = cell;
                        break;
                    }
                }
                if (clickedCell == null || clickedCell.getItem() == null) {
                    listView.getSelectionModel().clearSelection();
                } else {
                    listView.getSelectionModel().select(clickedCell.getIndex());
                }
            }
        });

        eingabeFeld = new TextField();
        eingabeFeld.setPromptText("Neues Ziel eingeben...");

        addBtn = new Button("Hinzufügen");
        addBtn.setOnAction(e -> {
            String text = eingabeFeld.getText().trim();
            if (!text.isEmpty()) {
                controller.addZiel(new Ziele(text));
                eingabeFeld.clear();
            }
        });

        removeBtn = new Button("Löschen");
        removeBtn.setDisable(true);
        removeBtn.setOnAction(e -> {
            Ziele selected = listView.getSelectionModel().getSelectedItem();
            if (selected != null) {
                controller.removeZiel(selected);
            }
        });

        listView.getSelectionModel().selectedItemProperty().addListener((obs, oldSel, newSel) -> {
            removeBtn.setDisable(newSel == null);
        });

        VBox form = new VBox(10, eingabeFeld, addBtn, removeBtn);
        form.setAlignment(Pos.CENTER);

        this.getChildren().addAll(listView, form);
    }
}
