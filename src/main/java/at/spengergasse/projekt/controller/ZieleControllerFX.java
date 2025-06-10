package at.spengergasse.projekt.controller;

import at.spengergasse.projekt.model.Ziele;
import at.spengergasse.projekt.model.PfadManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Controller für die Ziele-Ansicht.
 * Verwaltet das Laden, Speichern und Bearbeiten der Ziel-Daten eines Benutzers.
 */
public class ZieleControllerFX {

    private final String username;
    private final ObservableList<Ziele> zieleListe;

    /**
     * Konstruktor für den Ziele-Controller.
     *
     * @param username Benutzername (zur Pfadzuordnung)
     */
    public ZieleControllerFX(String username) {
        this.username = username;
        this.zieleListe = FXCollections.observableArrayList();
        load();
    }

    /**
     * Gibt die aktuelle Liste aller Ziele zurück.
     *
     * @return ObservableList mit Zielobjekten.
     */
    public ObservableList<Ziele> getZiele() {
        return zieleListe;
    }

    /**
     * Fügt ein neues Ziel zur Liste hinzu und speichert die Datei.
     *
     * @param ziel Das hinzuzufügende Ziel.
     */
    public void addZiel(Ziele ziel) {
        zieleListe.add(ziel);
        save();
    }

    /**
     * Entfernt ein Ziel aus der Liste und speichert die Datei.
     *
     * @param ziel Zielobjekt, das entfernt werden soll.
     */
    public void removeZiel(Ziele ziel) {
        zieleListe.remove(ziel);
        save();
    }

    /**
     * Handler für den Hinzufügen-Button.
     * Liest den Text aus dem Eingabefeld und erstellt ein neues Ziel.
     *
     * @param eingabeFeld Textfeld mit dem Zieltext.
     */
    public void handleAdd(TextField eingabeFeld) {
        String text = eingabeFeld.getText().trim();
        if (!text.isEmpty()) {
            addZiel(new Ziele(text));
            eingabeFeld.clear();
        }
    }

    /**
     * Handler für den Löschen-Button.
     * Entfernt das aktuell ausgewählte Ziel.
     *
     * @param listView Die ListView mit den Zielen.
     */
    public void handleRemove(ListView<Ziele> listView) {
        Ziele selected = listView.getSelectionModel().getSelectedItem();
        if (selected != null) {
            removeZiel(selected);
        }
    }

    /**
     * Richtet die Click-Logik ein, um eine Zielauswahl durch Klick zu ermöglichen
     * und erlaubt das direkte Abhaken per Checkbox.
     *
     * @param listView Die ListView mit Ziel-Elementen.
     */
    public void setupClickSelection(ListView<Ziele> listView) {
        listView.setCellFactory(lv -> {
            ListCell<Ziele> cell = new ListCell<>() {
                @Override
                protected void updateItem(Ziele item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty || item == null) {
                        setText(null);
                        setGraphic(null);
                    } else {
                        CheckBox checkBox = new CheckBox(item.getZielText());
                        checkBox.setSelected(item.isErledigt());
                        checkBox.selectedProperty().addListener((obs, oldVal, newVal) -> {
                            item.setErledigt(newVal);
                            save();
                        });
                        setGraphic(checkBox);
                    }
                }
            };

            cell.setOnMouseClicked(event -> {
                if (!cell.isEmpty() && event.getButton() == MouseButton.PRIMARY) {
                    listView.getSelectionModel().select(cell.getIndex());
                } else if (cell.isEmpty()) {
                    listView.getSelectionModel().clearSelection();
                }
            });

            return cell;
        });
    }

    /**
     * Lädt alle Ziele aus der zugehörigen Datei.
     * Falls Datei nicht existiert, passiert nichts.
     */
    private void load() {
        String pfad = PfadManager.getZielePfad(username);
        Path path = Path.of(pfad);
        if (!Files.exists(path)) return;

        try (BufferedReader reader = new BufferedReader(new FileReader(pfad))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(";", 2);
                if (parts.length == 2) {
                    boolean erledigt = Boolean.parseBoolean(parts[0]);
                    String text = parts[1];
                    zieleListe.add(new Ziele(text, erledigt));
                }
            }
        } catch (IOException e) {
            System.err.println("Fehler beim Laden der Ziele-Datei.");
        }
    }

    /**
     * Speichert alle aktuellen Ziele in die zugehörige Datei.
     */
    public void save() {
        String pfad = PfadManager.getZielePfad(username);
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(pfad))) {
            for (Ziele ziel : zieleListe) {
                writer.write(ziel.isErledigt() + ";" + ziel.getZielText());
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("Fehler beim Speichern der Ziele-Datei.");
        }
    }
}
