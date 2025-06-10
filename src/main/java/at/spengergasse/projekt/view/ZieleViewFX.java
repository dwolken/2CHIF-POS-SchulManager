package at.spengergasse.projekt.view;

import at.spengergasse.projekt.controller.ZieleControllerFX;
import at.spengergasse.projekt.model.Ziele;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.*;

/**
 * View zur Darstellung und Verwaltung von Zielen.
 * Zeigt eine ListView mit Checkboxen, ein Eingabefeld und Buttons zum Hinzufügen und Entfernen.
 * Auswahl kann durch Klick auf eine leere Fläche aufgehoben werden.
 */
public class ZieleViewFX extends VBox {

    private final ZieleControllerFX controller;
    private final ListView<Ziele> listView;
    private final TextField eingabeFeld;
    private final Button addBtn;
    private final Button removeBtn;

    /**
     * Konstruktor für die Zieleansicht eines Benutzers.
     * @param username Aktuell eingeloggter Benutzer
     */
    public ZieleViewFX(String username) {
        this.controller = new ZieleControllerFX(username);

        setSpacing(20);
        setPadding(new Insets(30));
        setAlignment(Pos.TOP_CENTER);

        // ListView mit Zielen
        listView = new ListView<>(controller.getZiele());
        controller.setupClickSelection(listView);

        // Klick auf leere Fläche hebt Auswahl auf
        listView.setOnMouseClicked(event -> {
            if (event.getButton() == MouseButton.PRIMARY) {
                int clickedIndex = listView.getSelectionModel().getSelectedIndex();
                if (clickedIndex < 0 || clickedIndex >= listView.getItems().size()) {
                    listView.getSelectionModel().clearSelection();
                }
            }
        });

        // Eingabe neuer Ziele
        eingabeFeld = new TextField();
        eingabeFeld.setPromptText("Neues Ziel eingeben...");

        // Hinzufügen-Button
        addBtn = new Button("Hinzufügen");
        addBtn.setOnAction(e -> controller.handleAdd(eingabeFeld));

        // Löschen-Button
        removeBtn = new Button("Löschen");
        removeBtn.setDisable(true);
        removeBtn.setOnAction(e -> controller.handleRemove(listView));

        // Button-Aktivierung nur bei Auswahl
        listView.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            removeBtn.setDisable(newVal == null);
        });

        // Formularbereich
        VBox form = new VBox(10, eingabeFeld, addBtn, removeBtn);
        form.setAlignment(Pos.CENTER);

        getChildren().addAll(listView, form);
    }
}
