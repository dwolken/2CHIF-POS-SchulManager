package at.spengergasse.projekt.view;

import at.spengergasse.projekt.controller.ZieleControllerFX;
import at.spengergasse.projekt.model.Ziele;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.*;

/**
 * JavaFX-Komponente zur Verwaltung von Zielen eines Benutzers.
 *
 * <p>Diese View beinhaltet:</p>
 * <ul>
 *     <li>Eine ListView zur Anzeige aller Ziele mit Checkboxen</li>
 *     <li>Ein Eingabefeld für neue Ziele</li>
 *     <li>Buttons zum Hinzufügen und Entfernen von Zielen</li>
 *     <li>Logik zur Aufhebung der Auswahl bei Klick auf leere Fläche</li>
 * </ul>
 *
 * <p>Die Interaktion wird durch {@link ZieleControllerFX} gesteuert.</p>
 */
public class ZieleViewFX extends VBox {

    private final ZieleControllerFX controller;
    private final ListView<Ziele> listView;
    private final TextField eingabeFeld;
    private final Button addBtn;
    private final Button removeBtn;

    /**
     * Konstruktor: Erzeugt die Zieleansicht für den gegebenen Benutzer.
     *
     * @param username Aktueller Benutzername zur Ziel-Verwaltung
     */
    public ZieleViewFX(String username) {
        this.controller = new ZieleControllerFX(username);

        setSpacing(20);
        setPadding(new Insets(30));
        setAlignment(Pos.TOP_CENTER);

        // ListView zeigt Ziele an
        listView = new ListView<>(controller.getZiele());
        controller.setupClickSelection(listView);

        // Auswahl wird bei Klick auf leere Fläche aufgehoben
        listView.setOnMouseClicked(event -> {
            if (event.getButton() == MouseButton.PRIMARY) {
                int clickedIndex = listView.getSelectionModel().getSelectedIndex();
                if (clickedIndex < 0 || clickedIndex >= listView.getItems().size()) {
                    listView.getSelectionModel().clearSelection();
                }
            }
        });

        // Eingabefeld für neue Ziele
        eingabeFeld = new TextField();
        eingabeFeld.setPromptText("Neues Ziel eingeben...");

        // Ziel hinzufügen
        addBtn = new Button("Hinzufügen");
        addBtn.setOnAction(e -> controller.handleAdd(eingabeFeld));

        // Ziel entfernen
        removeBtn = new Button("Löschen");
        removeBtn.setDisable(true);
        removeBtn.setOnAction(e -> controller.handleRemove(listView));

        // Entfernen-Button nur aktiv, wenn etwas ausgewählt ist
        listView.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            removeBtn.setDisable(newVal == null);
        });

        // Layout für Eingabemaske
        VBox form = new VBox(10, eingabeFeld, addBtn, removeBtn);
        form.setAlignment(Pos.CENTER);

        getChildren().addAll(listView, form);
    }
}
