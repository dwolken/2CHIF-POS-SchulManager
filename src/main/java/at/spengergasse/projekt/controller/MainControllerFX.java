package at.spengergasse.projekt.controller;

import at.spengergasse.projekt.view.MainViewFX;
import at.spengergasse.projekt.view.StatistikViewFX;
import at.spengergasse.projekt.view.TerminViewFX;
import at.spengergasse.projekt.view.ZieleViewFX;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

import java.io.File;

/**
 * Controller für das Hauptfenster. Steuert Navigation und Menüaktionen.
 */
public class MainControllerFX {

    private final MainViewFX view;
    private final String username;
    private String aktuellerPfad;

    /**
     * Erstellt den Controller für das Hauptfenster.
     * @param view Verknüpfte View
     * @param username Angemeldeter Benutzer
     */
    public MainControllerFX(MainViewFX view, String username) {
        this.view = view;
        this.username = username;
        this.aktuellerPfad = System.getProperty("user.home") + "/SchulManager/data/" + username + "_termine.csv";
        view.setFooterPath(aktuellerPfad);
    }

    /**
     * Zeigt das Termin-Panel im Center-Bereich an.
     * Wird beim Start automatisch geladen.
     */
    public void loadTerminView() {
        view.setCenterContent(new TerminViewFX(username, aktuellerPfad));
    }

    public void handleTermine(ActionEvent e) {
        loadTerminView();
    }

    public void handleStatistik(ActionEvent e) {
        view.setCenterContent(new StatistikViewFX(username));
    }

    public void handleZiele(ActionEvent e) {
        view.setCenterContent(new ZieleViewFX(username));
    }

    public void handleLogout(ActionEvent e) {
        System.exit(0);
    }

    public void handleNeueInstanz(ActionEvent e) {
        try {
            new at.spengergasse.projekt.view.LoginViewFX(new Stage());
        } catch (Exception ex) {
            showError("Fehler beim Öffnen einer neuen Instanz.");
        }
    }

    public void handlePfadAendern(ActionEvent e) {
        DirectoryChooser chooser = new DirectoryChooser();
        chooser.setTitle("Neuer Speicherordner für Termine");
        File selectedDir = chooser.showDialog(null);
        if (selectedDir != null) {
            aktuellerPfad = selectedDir.getAbsolutePath() + "/" + username + "_termine.csv";
            view.setFooterPath(aktuellerPfad);
            loadTerminView();
        }
    }

    private void showError(String msg) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle("Fehler");
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }
}
