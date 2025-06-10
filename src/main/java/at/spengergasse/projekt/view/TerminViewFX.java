package at.spengergasse.projekt.view;

import at.spengergasse.projekt.controller.TerminControllerFX;
import at.spengergasse.projekt.model.Termin;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.TableView;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

/**
 * View zur Anzeige und Verwaltung von Terminen.
 * Besteht aus Tabelle, Eingabeformular und Aktions-Buttons.
 */
public class TerminViewFX extends VBox {

    private final TerminControllerFX controller;

    /**
     * Konstruktor: Initialisiert die Terminansicht für den angegebenen Benutzer.
     *
     * @param username Benutzername für die geladenen Termin-Daten
     */
    public TerminViewFX(String username) {
        this.controller = new TerminControllerFX(username);

        setSpacing(20);
        setPadding(new Insets(30));
        setAlignment(Pos.TOP_CENTER);
        getStyleClass().add("termin-view");

        // Tabelle mit Termin-Daten
        TableView<Termin> table = controller.getTable();
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        table.setMaxWidth(Double.MAX_VALUE);
        VBox.setVgrow(table, Priority.ALWAYS);

        // Formularbereich
        VBox formularBox = new VBox(controller.getFormular());
        formularBox.setAlignment(Pos.CENTER);

        // Buttonbereich
        VBox buttonBox = new VBox(controller.getAktionen());
        buttonBox.setAlignment(Pos.CENTER);

        getChildren().addAll(table, formularBox, buttonBox);
    }

    /**
     * Gibt die Termin-Tabelle zurück (z.B. für Tests oder manuelle Aktualisierung).
     *
     * @return TableView mit Terminen
     */
    public TableView<Termin> getTable() {
        return controller.getTable();
    }
}
