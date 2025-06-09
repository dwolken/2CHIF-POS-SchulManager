package at.spengergasse.projekt.controller;

import at.spengergasse.projekt.model.Ziele;
import at.spengergasse.projekt.model.PfadManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.HBox;
import javafx.geometry.Insets;
import javafx.geometry.Pos;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Controller für die Ziele-Ansicht.
 * Verwaltet das Laden, Speichern und Bearbeiten der Ziel-Daten.
 */
public class ZieleControllerFX {

    private final String username;
    private final ObservableList<Ziele> zieleListe;

    /**
     * Konstruktor für den Ziele-Controller.
     *
     * @param username Benutzername (nur zur Identifikation)
     */
    public ZieleControllerFX(String username) {
        this.username = username;
        this.zieleListe = FXCollections.observableArrayList();
        load();
    }

    /**
     * Gibt die ObservableList mit den aktuellen Zielen zurück.
     *
     * @return Liste der Ziele
     */
    public ObservableList<Ziele> getZiele() {
        return zieleListe;
    }

    /**
     * Fügt ein neues Ziel hinzu und speichert die Liste.
     *
     * @param ziel Das neue Ziel
     */
    public void addZiel(Ziele ziel) {
        zieleListe.add(ziel);
        save();
    }

    /**
     * Entfernt ein Ziel aus der Liste und speichert.
     *
     * @param ziel Das Ziel, das entfernt werden soll
     */
    public void removeZiel(Ziele ziel) {
        zieleListe.remove(ziel);
        save();
    }

    /**
     * Wird im Controller der View verwendet für die "Hinzufügen"-Schaltfläche.
     */
    public void handleAdd(TextField eingabeFeld) {
        String text = eingabeFeld.getText().trim();
        if (!text.isEmpty()) {
            addZiel(new Ziele(text));
            eingabeFeld.clear();
        }
    }

    /**
     * Wird im Controller der View verwendet für die "Löschen"-Schaltfläche.
     */
    public void handleRemove(ListView<Ziele> listView) {
        Ziele selected = listView.getSelectionModel().getSelectedItem();
        if (selected != null) {
            removeZiel(selected);
        }
    }

    /**
     * Konfiguriert die Zell- und Auswahl-Logik der ListView.
     * Hebt Auswahl bei Klick auf leere Zeile auf.
     */
    public void getAktionen(ListView<Ziele> listView) {
        listView.setCellFactory(lv -> {
            ListCell<Ziele> cell = new ListCell<>() {
                private final CheckBox checkBox = new CheckBox();
                private final HBox hBox = new HBox(10);

                {
                    hBox.setAlignment(Pos.CENTER_LEFT);
                    hBox.getChildren().add(checkBox);

                    this.setOnMouseClicked(event -> {
                        if (isEmpty()) {
                            listView.getSelectionModel().clearSelection();
                        } else {
                            listView.getSelectionModel().select(getIndex());
                        }
                    });
                }

                @Override
                protected void updateItem(Ziele item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty || item == null) {
                        setGraphic(null);
                    } else {
                        checkBox.setText(item.getZielText());
                        checkBox.setSelected(item.isErledigt());
                        checkBox.selectedProperty().addListener((obs, oldVal, newVal) -> {
                            item.setErledigt(newVal);
                            save();
                        });
                        setGraphic(hBox);
                    }
                }
            };
            return cell;
        });
    }


    /**
     * Lädt die Ziele aus der Datei.
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
     * Speichert die aktuelle Liste in die Datei.
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
