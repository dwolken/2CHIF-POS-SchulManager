package at.spengergasse.projekt.view;

import at.spengergasse.projekt.controller.TerminControllerFX;
import at.spengergasse.projekt.model.Termin;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.TableView;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

/**
 * JavaFX-Komponente zur Anzeige und Bearbeitung von Terminen.
 *
 * <p>Diese View beinhaltet:</p>
 * <ul>
 *     <li>Eine Tabelle zur Anzeige aller gespeicherten Termine</li>
 *     <li>Ein Eingabeformular zur Erstellung und Bearbeitung</li>
 *     <li>Aktionselemente wie Löschen, Speichern etc.</li>
 * </ul>
 *
 * <p>Die eigentliche Logik wird durch {@link TerminControllerFX} gesteuert.</p>
 */
public class TerminViewFX extends VBox {

    private final TerminControllerFX controller;

    /**
     * Konstruktor: Erzeugt die gesamte Terminansicht für den gegebenen Benutzer.
     *
     * @param username Benutzername, für den die Termin-Daten geladen und verwaltet werden
     */
    public TerminViewFX(String username) {
        this.controller = new TerminControllerFX(username);

        setSpacing(20);
        setPadding(new Insets(30));
        setAlignment(Pos.TOP_CENTER);
        getStyleClass().add("termin-view");

        // Termin-Tabelle
        TableView<Termin> table = controller.getTable();
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        table.setMaxWidth(Double.MAX_VALUE);
        VBox.setVgrow(table, Priority.ALWAYS);

        // Eingabeformular
        VBox formularBox = new VBox(controller.getFormular());
        formularBox.setAlignment(Pos.CENTER);

        // Button-Bereich
        VBox buttonBox = new VBox(controller.getAktionen());
        buttonBox.setAlignment(Pos.CENTER);

        getChildren().addAll(table, formularBox, buttonBox);
    }

    /**
     * Gibt die JavaFX-Tabelle mit allen Terminen zurück.
     *
     * @return TableView der gespeicherten Termine
     */
    public TableView<Termin> getTable() {
        return controller.getTable();
    }
}
