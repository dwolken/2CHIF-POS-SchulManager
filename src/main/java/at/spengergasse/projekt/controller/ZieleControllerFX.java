package at.spengergasse.projekt.controller;

import at.spengergasse.projekt.model.Ziele;
import at.spengergasse.projekt.model.PfadManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Der {@code ZieleControllerFX} verwaltet alle Ziele eines Benutzers innerhalb der Anwendung.
 * <p>
 * Verantwortlich für:
 * <ul>
 *     <li>Hinzufügen und Entfernen von Zielen</li>
 *     <li>Speichern und Laden im CSV-Format</li>
 *     <li>GUI-Interaktion via {@code ListView} und {@code CheckBox}</li>
 * </ul>
 */
public class ZieleControllerFX {

    private final String username;
    private final ObservableList<Ziele> zieleListe;

    /**
     * Konstruktor initialisiert Benutzerkontext und lädt vorhandene Ziele.
     *
     * @param username Der aktuelle Benutzername
     */
    public ZieleControllerFX(String username) {
        this.username = username;
        this.zieleListe = FXCollections.observableArrayList();
        load();
    }

    /**
     * Gibt die aktuell gehaltene Liste der Ziele zurück.
     *
     * @return ObservableList mit {@code Ziele}-Objekten
     */
    public ObservableList<Ziele> getZiele() {
        return zieleListe;
    }

    /**
     * Fügt ein Ziel zur Liste hinzu, wenn es noch nicht existiert, und speichert.
     *
     * @param ziel Das hinzuzufügende Ziel
     */
    public void addZiel(Ziele ziel) {
        if (!zieleListe.contains(ziel)) {
            zieleListe.add(ziel);
            save();
        }
    }

    /**
     * Entfernt ein Ziel aus der Liste und speichert den neuen Zustand.
     *
     * @param ziel Das zu entfernende Ziel
     */
    public void removeZiel(Ziele ziel) {
        zieleListe.remove(ziel);
        save();
    }

    /**
     * Verarbeitet Eingaben aus einem {@code TextField} und fügt neues Ziel hinzu.
     *
     * @param eingabeFeld Eingabefeld für Zieltext
     */
    public void handleAdd(TextField eingabeFeld) {
        String text = eingabeFeld.getText().trim();
        if (!text.isEmpty()) {
            addZiel(new Ziele(text));
            eingabeFeld.clear();
        }
    }

    /**
     * Entfernt das aktuell selektierte Ziel aus der {@code ListView}.
     *
     * @param listView Die GUI-ListView mit Zielen
     */
    public void handleRemove(ListView<Ziele> listView) {
        Ziele selected = listView.getSelectionModel().getSelectedItem();
        if (selected != null) {
            removeZiel(selected);
        }
    }

    /**
     * Richtet die grafische Darstellung und Interaktion für Ziele in der ListView ein.
     * Dazu gehören Checkboxen zum Abhaken und Klickverhalten.
     *
     * @param listView Die Ziel-ListView aus der GUI
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
     * Lädt Ziele aus der Datei des jeweiligen Benutzers.
     * Erwartet UTF-8-Codierung, Format: {@code erledigt;zieltext}.
     * Leere oder ungültige Zeilen werden ignoriert.
     */
    private void load() {
        String pfad = PfadManager.getZielePfad(username);
        Path path = Path.of(pfad);
        if (!Files.exists(path)) return;

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(
                new FileInputStream(pfad), StandardCharsets.UTF_8))) {

            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) continue;

                String[] parts = line.split(";", 2);
                if (parts.length == 2) {
                    boolean erledigt = Boolean.parseBoolean(parts[0].trim());
                    String text = parts[1].trim();
                    zieleListe.add(new Ziele(text, erledigt));
                }
            }

        } catch (IOException e) {
            System.err.println("Fehler beim Laden der Ziele-Datei.");
        }
    }

    /**
     * Speichert die aktuelle Zielliste in die benutzerbezogene Datei.
     * UTF-8-Codierung, Format pro Zeile: {@code erledigt;zieltext}
     */
    public void save() {
        String pfad = PfadManager.getZielePfad(username);
        try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(
                new FileOutputStream(pfad), StandardCharsets.UTF_8))) {

            for (Ziele ziel : zieleListe) {
                writer.write(ziel.isErledigt() + ";" + ziel.getZielText());
                writer.newLine();
            }

        } catch (IOException e) {
            System.err.println("Fehler beim Speichern der Ziele-Datei.");
        }
    }
}
