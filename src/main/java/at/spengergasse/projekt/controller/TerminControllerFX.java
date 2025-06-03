package at.spengergasse.projekt.controller;

import at.spengergasse.projekt.model.CsvManager;
import at.spengergasse.projekt.model.Termin;
import at.spengergasse.projekt.model.User;
import at.spengergasse.projekt.view.TerminViewFX;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;

import java.time.LocalDate;
import java.util.List;

/**
 * Controller für die Terminansicht.
 * Verwaltet das Bearbeiten, Speichern und Löschen von Terminen.
 */
public class TerminControllerFX {

    private final TerminViewFX view;
    private final User user;

    /**
     * Konstruktor des TerminControllers.
     *
     * @param view die View-Komponente
     * @param user aktuell eingeloggter Benutzer
     */
    public TerminControllerFX(TerminViewFX view, User user) {
        this.view = view;
        this.user = user;

        List<Termin> termine = CsvManager.loadTermine(user.getUsername());
        view.getTerminTable().setItems(FXCollections.observableArrayList(termine));

        view.getSpeichernButton().setOnAction(this::handleSpeichern);
        view.getLöschenButton().setOnAction(this::handleLöschen);

        // Auswahl beobachten
        view.getTerminTable().getSelectionModel().selectedItemProperty().addListener((obs, old, selected) -> {
            if (selected != null) {
                view.getTitelField().setText(selected.getFach());
                view.getDatumPicker().setValue(selected.getDatum());
                view.getArtBox().setValue(selected.getTyp());
                view.getNotizField().setText(selected.getNotiz());
                view.getLöschenButton().setDisable(false);
            } else {
                view.clearFields();
            }
        });

        // Leere Zeile -> Auswahl löschen
        view.getTerminTable().setRowFactory(tv -> {
            TableRow<Termin> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (row.isEmpty()) {
                    view.getTerminTable().getSelectionModel().clearSelection();
                    view.clearFields();
                }
            });
            return row;
        });

        // Notiz editierbar machen + speichern
        @SuppressWarnings("unchecked")
        TableColumn<Termin, String> notizCol = (TableColumn<Termin, String>) view.getTerminTable().getColumns().get(3);
        notizCol.setOnEditCommit((TableColumn.CellEditEvent<Termin, String> e) -> {
            e.getRowValue().setNotiz(e.getNewValue());
            CsvManager.saveTermine(user.getUsername(), view.getTerminTable().getItems());
        });
    }

    /**
     * Speichert neuen oder bearbeiteten Termin.
     */
    private void handleSpeichern(ActionEvent e) {
        String titel = view.getTitelField().getText().trim();
        LocalDate datum = view.getDatumPicker().getValue();
        String art = view.getArtBox().getValue();
        String notiz = view.getNotizField().getText().trim();

        if (titel.isEmpty() || datum == null || art == null) {
            new Alert(Alert.AlertType.WARNING, "Bitte Titel, Datum und Art angeben!").showAndWait();
            return;
        }

        Termin selected = view.getTerminTable().getSelectionModel().getSelectedItem();
        if (selected == null) {
            Termin neu = new Termin(titel, datum, art, notiz);
            view.getTerminTable().getItems().add(neu);
        } else {
            selected.setFach(titel);
            selected.setDatum(datum);
            selected.setTyp(art);
            selected.setNotiz(notiz);
            view.getTerminTable().refresh();
        }

        CsvManager.saveTermine(user.getUsername(), view.getTerminTable().getItems());
        view.clearFields();
        view.getTerminTable().getSelectionModel().clearSelection();
    }

    /**
     * Löscht den ausgewählten Termin.
     */
    private void handleLöschen(ActionEvent e) {
        Termin selected = view.getTerminTable().getSelectionModel().getSelectedItem();
        if (selected != null) {
            view.getTerminTable().getItems().remove(selected);
            CsvManager.saveTermine(user.getUsername(), view.getTerminTable().getItems());
            view.clearFields();
        }
    }
}
