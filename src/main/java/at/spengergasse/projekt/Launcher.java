package at.spengergasse.projekt;

import javafx.application.Application;

/**
 * Startklasse für das SchulManager-Projekt. Leitet den JavaFX-Start an
 * {@link MainApplicationFX} weiter.
 */
public class Launcher {

    /**
     * Einstiegspunkt der Anwendung.
     *
     * @param args Kommandozeilenargumente
     */
    public static void main(String[] args) {
        Application.launch(MainApplicationFX.class, args);
    }
}
