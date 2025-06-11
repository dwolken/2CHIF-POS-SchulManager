package at.spengergasse.projekt.view;

import at.spengergasse.projekt.controller.StatistikControllerFX;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

/**
 * GUI-Komponente zur Darstellung von Terminstatistiken für einen Benutzer.
 *
 * <p>Zeigt die prozentuale Verteilung der Terminarten als Kreisdiagramm sowie
 * eine Gesamtanzahl aller vorhandenen Termine. Daten werden über den zugehörigen
 * Controller aus CSV-Dateien geladen.</p>
 *
 * <p>Diese View ist rein visuell und übernimmt keine Logikverarbeitung.</p>
 */
public class StatistikViewFX extends VBox {

    /**
     * Konstruktor: Baut die Statistik-Oberfläche für einen bestimmten Benutzer auf.
     *
     * @param username Der eingeloggte Benutzer, dessen Termine ausgewertet werden
     */
    public StatistikViewFX(String username) {
        setPadding(new Insets(30));
        setSpacing(30);
        setAlignment(Pos.TOP_CENTER);
        getStyleClass().add("statistik-view");

        StatistikControllerFX controller = new StatistikControllerFX(username);

        // Titel der Statistik
        Label headline = new Label("Statistik für " + username);
        headline.setFont(new Font(20));
        headline.getStyleClass().add("statistik-title");

        // Kreisdiagramm zur Darstellung der Terminarten
        PieChart chart = new PieChart();
        controller.getVerteilungNachArt().forEach((art, anzahl) -> {
            chart.getData().add(new PieChart.Data(art, anzahl));
        });

        chart.setLegendVisible(true);
        chart.setLabelsVisible(true);
        chart.setTitle("Verteilung nach Termin-Art");
        chart.getStyleClass().add("statistik-chart");

        // Gesamtanzahl der Termine als Text
        Label gesamtLabel = new Label("Gesamte Termine: " + controller.getGesamtAnzahl());
        gesamtLabel.getStyleClass().add("statistik-gesamt");

        // Zusammenbauen der GUI
        getChildren().addAll(headline, chart, gesamtLabel);
    }
}
