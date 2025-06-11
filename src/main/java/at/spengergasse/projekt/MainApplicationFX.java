package at.spengergasse.projekt;

import at.spengergasse.projekt.view.LoginViewFX;
import javafx.application.Application;
import javafx.stage.Stage;

/**
 * Hauptklasse der SchulManager-Anwendung.
 *
 * <p>Diese Klasse dient als Einstiegspunkt der JavaFX-Applikation.
 * Beim Start wird das {@link javafx.application.Application} Framework initialisiert
 * und die Login-Oberfläche angezeigt.</p>
 */
public class MainApplicationFX extends Application {

    /**
     * Startet die JavaFX-Oberfläche und zeigt das Login-Fenster an.
     * Diese Methode wird automatisch vom JavaFX-Launcher aufgerufen.
     *
     * @param primaryStage Das Hauptfenster der Anwendung
     */
    @Override
    public void start(Stage primaryStage) {
        new LoginViewFX(primaryStage);
    }

    /**
     * Einstiegsmethode der Anwendung.
     * Übergibt Kommandozeilenargumente an JavaFX.
     *
     * @param args Programmargumente (werden derzeit ignoriert)
     */
    public static void main(String[] args) {
        launch(args);
    }
}
