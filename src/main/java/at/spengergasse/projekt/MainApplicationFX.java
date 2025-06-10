package at.spengergasse.projekt;

import at.spengergasse.projekt.view.LoginViewFX;
import javafx.application.Application;
import javafx.stage.Stage;

/**
 * Einstiegspunkt der JavaFX-Anwendung für den SchulManager.
 * Startet das JavaFX-Framework und öffnet das Login-Fenster.
 */
public class MainApplicationFX extends Application {

    /**
     * Wird beim Start der Anwendung automatisch aufgerufen.
     * Initialisiert das Login-Fenster.
     *
     * @param primaryStage das Hauptfenster (Stage)
     */
    @Override
    public void start(Stage primaryStage) {
        new LoginViewFX(primaryStage); // Szene wird intern gesetzt
    }

    /**
     * Main-Methode zum Starten der JavaFX-Anwendung.
     *
     * @param args Kommandozeilenargumente
     */
    public static void main(String[] args) {
        launch(args);
    }
}
