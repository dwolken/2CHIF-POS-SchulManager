package at.spengergasse.projekt;

import at.spengergasse.projekt.controller.LoginControllerFX;
import at.spengergasse.projekt.view.LoginViewFX;
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;

/**
 * Einstiegspunkt der SchulManager-Anwendung.
 * Startet die JavaFX-Oberfläche und zeigt die Login-View.
 */
public class MainApplicationFX extends Application {

    /**
     * Wird beim Start der Anwendung automatisch aufgerufen.
     * Initialisiert das Login-Fenster und zeigt es an.
     *
     * @param primaryStage das Hauptfenster (Stage)
     */
    @Override
    public void start(Stage primaryStage) {
        LoginViewFX loginView = new LoginViewFX();
        new LoginControllerFX(loginView, primaryStage);
        primaryStage.setScene(new Scene(loginView, 400, 300));
        primaryStage.setTitle("SchulManager - Login");
        primaryStage.show();
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
