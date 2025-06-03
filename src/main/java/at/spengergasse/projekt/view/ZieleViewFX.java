package at.spengergasse.projekt.view;

import at.spengergasse.projekt.controller.ZieleControllerFX;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.*;

/**
 * View für Ziele-Verwaltung – erlaubt das Hinzufügen und Abhaken von Zielen.
 */
public class ZieleViewFX extends VBox {

    /**
     * Konstruktor für die Zieleansicht.
     * @param username Aktuell eingeloggter Benutzer
     */
    public ZieleViewFX(String username) {
        setSpacing(10);
        setPadding(new Insets(20));

        ZieleControllerFX controller = new ZieleControllerFX(username);

        ListView<CheckBox> zielListe = controller.getZielListe();
        TextField eingabeFeld = new TextField();
        eingabeFeld.setPromptText("Neues Ziel...");

        Button hinzufügenButton = new Button("Hinzufügen");
        hinzufügenButton.setOnAction(e -> {
            String text = eingabeFeld.getText().trim();
            if (!text.isEmpty()) {
                controller.zielHinzufügen(text);
                eingabeFeld.clear();
            }
        });

        getChildren().addAll(zielListe, eingabeFeld, hinzufügenButton);
    }
}
