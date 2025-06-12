package at.spengergasse.projekt.view;

import at.spengergasse.projekt.controller.AdminControllerFX;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * JavaFX-Komponente zur Verwaltung von Benutzern im Admin-Modus.
 *
 * <p>Diese View beinhaltet:</p>
 * <ul>
 *     <li>Eine Tabelle zur Anzeige aller Benutzer</li>
 *     <li>Aktionen zum Hinzufügen, Bearbeiten und Löschen von Benutzern</li>
 *     <li>Ein Formular zur Eingabe von Benutzerdaten</li>
 *     <li>Logout-Funktionalität</li>
 * </ul>
 *
 * <p>Die Interaktion wird durch {@link AdminControllerFX} gesteuert.</p>
 */
public class AdminViewFX {

    public AdminViewFX(Stage stage) {
        AdminControllerFX controller = new AdminControllerFX();

        Label title = new Label("Benutzerverwaltung (Admin-Modus)");
        title.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");
        title.setAlignment(Pos.CENTER);

        VBox root = new VBox(15);
        root.setPadding(new Insets(30));

        VBox tableBox = new VBox(controller.getTable());
        VBox aktionenBox = new VBox(controller.getAktionen());
        VBox formularBox = new VBox(controller.getFormular());

        Button logoutButton = new Button("Abmelden");
        logoutButton.setOnAction(controller::handleLogout);
        stage.setOnCloseRequest(controller::handleLogout);


        VBox logoutBox = new VBox(logoutButton);
        logoutBox.setAlignment(Pos.CENTER);
        logoutBox.setPadding(new Insets(10));

        root.getChildren().addAll(title, tableBox, aktionenBox, formularBox, logoutBox);

        Scene scene = new Scene(root, 800, 600); // Größe wie du willst
        stage.setTitle("Admin-Bereich");
        stage.setScene(scene);
        scene.getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());
        stage.show();
    }
}
