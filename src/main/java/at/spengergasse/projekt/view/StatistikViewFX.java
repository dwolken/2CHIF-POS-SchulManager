package at.spengergasse.projekt.view;

import at.spengergasse.projekt.controller.StatistikControllerFX;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

/**
 * View zur Anzeige von Terminstatistiken.
 * Zeigt eine Kreisdiagramm-Auswertung der Terminarten sowie Gesamtanzahl.
 */
public class StatistikViewFX extends VBox {

    /**
     * Konstruktor: Initialisiert die Statistikansicht für einen Benutzer.
     *
     * @param username Der aktuell eingeloggte Benutzer
     */
    public StatistikViewFX(String username) {
        setPadding(new Insets(30));
        setSpacing(30);
        setAlignment(Pos.TOP_CENTER);
        getStyleClass().add("statistik-view");

        StatistikControllerFX controller = new StatistikControllerFX(username);

        // Überschrift
        Label headline = new Label("Statistik für " + username);
        headline.setFont(new Font(20));
        headline.getStyleClass().add("statistik-title");

        // Diagramm mit Verteilung nach Termin-Art
        PieChart chart = new PieChart();
        controller.getVerteilungNachArt().forEach((art, anzahl) -> {
            chart.getData().add(new PieChart.Data(art, anzahl));
        });

        chart.setLegendVisible(true);
        chart.setLabelsVisible(true);
        chart.setTitle("Verteilung nach Termin-Art");
        chart.getStyleClass().add("statistik-chart");

        // Anzeige der Gesamtanzahl
        Label gesamtLabel = new Label("Gesamte Termine: " + controller.getGesamtAnzahl());
        gesamtLabel.getStyleClass().add("statistik-gesamt");

        // Aufbau der View
        getChildren().addAll(headline, chart, gesamtLabel);
    }
}
