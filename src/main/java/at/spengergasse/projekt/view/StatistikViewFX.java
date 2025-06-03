package at.spengergasse.projekt.view;

import at.spengergasse.projekt.controller.StatistikControllerFX;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

/**
 * View zur Anzeige von einfachen Terminstatistiken.
 */
public class StatistikViewFX extends VBox {

    /**
     * Konstruktor für die Statistikansicht.
     * @param username Der aktuell eingeloggte Benutzer
     */
    public StatistikViewFX(String username) {
        setPadding(new Insets(20));
        setSpacing(15);

        StatistikControllerFX controller = new StatistikControllerFX(username);

        Label gesamtLabel = new Label("Gesamte Termine: " + controller.getGesamtAnzahl());
        Label prüfungLabel = new Label("Prüfungen: " + controller.getAnzahlNachArt("Prüfung"));
        Label hausaufgabeLabel = new Label("Hausaufgaben: " + controller.getAnzahlNachArt("Hausaufgabe"));
        Label eventLabel = new Label("Events: " + controller.getAnzahlNachArt("Event"));
        Label sonstigesLabel = new Label("Sonstiges: " + controller.getAnzahlNachArt("Sonstiges"));

        getChildren().addAll(gesamtLabel, prüfungLabel, hausaufgabeLabel, eventLabel, sonstigesLabel);
    }
}
