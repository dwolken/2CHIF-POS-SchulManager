package at.spengergasse.projekt.controller;

import at.spengergasse.projekt.model.CsvManager;
import at.spengergasse.projekt.model.User;
import at.spengergasse.projekt.view.MainViewFX;
import at.spengergasse.projekt.view.LoginViewFX;
import at.spengergasse.projekt.view.TerminViewFX;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

import java.io.File;

/**
 * Controller für das Hauptfenster nach Login.
 * Steuert Navigation, Logout und Pfadänderung.
 */
public class MainControllerFX implements EventHandler<ActionEvent> {

    private final MainViewFX view;
    private final User user;
    private final Stage stage;

    /**
     * Konstruktor des MainControllers.
     *
     * @param view  Hauptansicht
     * @param user  eingeloggter Benutzer
     */
    public MainControllerFX(MainViewFX view, User user) {
        this.view = view;
        this.user = user;
        this.stage = new Stage();

        // Begrüßung setzen
        view.setBegruessung(user.getUsername());
        view.setFooterInfo(user.getUsername(), CsvManager.getBaseDir().toString());

        // Scene setzen
        stage.setScene(new Scene(view, 900, 600));
        stage.setTitle("SchulManager – " + user.getUsername());
        stage.setOnCloseRequest(e -> stage.close());
        stage.show();

        // Event-Handler zuweisen
        view.getTerminButton().setOnAction(this);
        view.getAbmeldenButton().setOnAction(this);
        view.getZurueckButton().setOnAction(this);
        view.getPfadAendernMenuItem().setOnAction(this);
    }

    @Override
    public void handle(ActionEvent event) {
        Object src = event.getSource();

        if (src == view.getAbmeldenButton()) {
            LoginViewFX loginView = new LoginViewFX();
            Stage loginStage = new Stage();
            new LoginControllerFX(loginView, loginStage);
            loginStage.setScene(new Scene(loginView, 400, 300));
            loginStage.setTitle("SchulManager – Login");
            loginStage.show();
            stage.close(); // Hauptfenster schließen
        }

        else if (src == view.getTerminButton()) {
            TerminViewFX terminView = new TerminViewFX();
            new TerminControllerFX(terminView, user);
            view.setMainContent(terminView);
        }

        else if (src == view.getZurueckButton()) {
            view.setMainContent(view.createGreeting("Willkommen, " + user.getUsername()));
        }

        else if (src == view.getPfadAendernMenuItem()) {
            DirectoryChooser chooser = new DirectoryChooser();
            chooser.setTitle("Neuen Speicherort wählen");
            File dir = chooser.showDialog(stage);
            if (dir != null) {
                CsvManager.setBaseDir(dir.toPath());
                view.setFooterInfo(user.getUsername(), CsvManager.getBaseDir().toString());
            }
        }
    }
}
