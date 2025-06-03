package at.spengergasse.projekt.view;

import at.spengergasse.projekt.controller.TerminControllerFX;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;

/**
 * View für die Anzeige und Verwaltung von Terminen in Tabellenform.
 * Beinhaltet Formular zum Hinzufügen, Tabelle mit Bearbeitungsmöglichkeit und Löschfunktion.
 */
public class TerminViewFX extends VBox {

    private final TerminControllerFX controller;

    /**
     * Konstruktor für die Terminansicht.
     * @param username Aktuell eingeloggter Benutzer
     * @param pfad Speicherpfad für die CSV-Datei
     */
    public TerminViewFX(String username, String pfad) {
        this.controller = new TerminControllerFX(username, pfad);

        this.setSpacing(15);
        this.setPadding(new Insets(20));
        this.getStyleClass().add("termin-view");

        TableView table = controller.getTable();
        HBox formular = controller.getFormular();
        HBox buttons = controller.getAktionen();

        this.getChildren().addAll(table, formular, buttons);
    }
}