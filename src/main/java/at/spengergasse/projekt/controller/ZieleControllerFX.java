package at.spengergasse.projekt.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ListView;

/**
 * Controller zur Verwaltung von Benutzerzielen.
 */
public class ZieleControllerFX {

    private final ObservableList<CheckBox> ziele;
    private final String username;

    /**
     * Initialisiert den Controller mit leerer Zielliste.
     * @param username Aktueller Benutzer
     */
    public ZieleControllerFX(String username) {
        this.username = username;
        this.ziele = FXCollections.observableArrayList();
        // TODO: Später: Ziele aus Datei laden
    }

    /**
     * Gibt die ListView mit Checkboxen zurück.
     */
    public ListView<CheckBox> getZielListe() {
        ListView<CheckBox> view = new ListView<>(ziele);
        view.setPrefHeight(300);
        return view;
    }

    /**
     * Fügt ein neues Ziel hinzu.
     */
    public void zielHinzufügen(String text) {
        CheckBox box = new CheckBox(text);
        ziele.add(box);
        // TODO: Später: Ziele speichern
    }
}
