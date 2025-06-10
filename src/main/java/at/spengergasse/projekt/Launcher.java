package at.spengergasse.projekt;

import javafx.application.Application;

/**
 * Einstiegspunkt für den SchulManager.
 * Diese Klasse startet die JavaFX-Anwendung über {@link MainApplicationFX}.
 */
public class Launcher {

    /**
     * Main-Methode zum Starten der Anwendung.
     * @param args Kommandozeilenargumente
     */
    public static void main(String[] args) {
        Application.launch(MainApplicationFX.class, args);
    }
}
