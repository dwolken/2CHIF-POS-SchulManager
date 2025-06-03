package at.spengergasse.projekt.view;

import at.spengergasse.projekt.model.Termin;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

/**
 * GUI-Komponente für die Terminansicht.
 * Zeigt Tabelle + Formular zur Eingabe/Bearbeitung.
 */
public class TerminViewFX extends VBox {

    private final TableView<Termin> terminTable;
    private final TextField titelField;
    private final DatePicker datumPicker;
    private final ComboBox<String> artBox;
    private final TextField notizField;
    private final Button speichernButton;
    private final Button löschenButton;

    /**
     * Erstellt das GUI für die Terminverwaltung.
     */
    public TerminViewFX() {
        setPadding(new Insets(20));
        setSpacing(10);

        terminTable = new TableView<>();
        terminTable.setEditable(true);
        terminTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        TableColumn<Termin, String> titelCol = new TableColumn<>("Titel");
        titelCol.setCellValueFactory(data -> new ReadOnlyStringWrapper(data.getValue().getFach()));

        TableColumn<Termin, String> datumCol = new TableColumn<>("Datum");
        datumCol.setCellValueFactory(data -> new ReadOnlyStringWrapper(
                data.getValue().getDatum() != null ? data.getValue().getDatum().toString() : ""));

        TableColumn<Termin, String> artCol = new TableColumn<>("Art");
        artCol.setCellValueFactory(data -> new ReadOnlyStringWrapper(data.getValue().getTyp()));

        TableColumn<Termin, String> notizCol = new TableColumn<>("Notiz");
        notizCol.setCellValueFactory(data -> new ReadOnlyStringWrapper(data.getValue().getNotiz()));
        notizCol.setEditable(true);

        terminTable.getColumns().addAll(titelCol, datumCol, artCol, notizCol);

        titelField = new TextField();
        titelField.setPromptText("Titel");

        datumPicker = new DatePicker();
        datumPicker.setPrefWidth(120);

        artBox = new ComboBox<>();
        artBox.getItems().addAll("Prüfung", "Hausaufgabe", "Event", "Sonstiges");
        artBox.setPromptText("Art");
        artBox.setPrefWidth(100);

        notizField = new TextField();
        notizField.setPromptText("Notiz (optional)");

        speichernButton = new Button("Speichern");
        löschenButton = new Button("Löschen");
        löschenButton.setDisable(true);

        HBox inputBox = new HBox(10, titelField, datumPicker, artBox, notizField, speichernButton, löschenButton);
        inputBox.setAlignment(Pos.CENTER);

        getChildren().addAll(terminTable, inputBox);
    }

    public TableView<Termin> getTerminTable() {
        return terminTable;
    }

    public TextField getTitelField() {
        return titelField;
    }

    public DatePicker getDatumPicker() {
        return datumPicker;
    }

    public ComboBox<String> getArtBox() {
        return artBox;
    }

    public TextField getNotizField() {
        return notizField;
    }

    public Button getSpeichernButton() {
        return speichernButton;
    }

    public Button getLöschenButton() {
        return löschenButton;
    }

    /**
     * Leert alle Eingabefelder und deaktiviert Löschen.
     */
    public void clearFields() {
        titelField.clear();
        datumPicker.setValue(null);
        artBox.setValue(null);
        notizField.clear();
        löschenButton.setDisable(true);
    }
}
