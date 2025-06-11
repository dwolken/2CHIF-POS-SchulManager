package at.spengergasse.projekt;

import javafx.application.Application;

/**
 * Launcher-Klasse für die SchulManager-Anwendung.
 *
 * <p>Diese Klasse stellt den eigentlichen Einstiegspunkt für das Java-Programm dar
 * und ruft den JavaFX-Launcher auf, der wiederum {@link MainApplicationFX} startet.</p>
 *
 * <p>Wird typischerweise benötigt, um Probleme mit bestimmten IDEs oder Modulsystemen zu umgehen,
 * bei denen {@code javafx.application.Application} nicht direkt von der {@code main()} gestartet werden kann.</p>
 */
public class Launcher {

    /**
     * Hauptmethode zum Starten der JavaFX-Anwendung.
     *
     * @param args Kommandozeilenargumente, die an JavaFX weitergereicht werden
     */
    public static void main(String[] args) {
        Application.launch(MainApplicationFX.class, args);
    }
}
