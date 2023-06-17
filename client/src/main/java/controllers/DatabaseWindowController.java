package controllers;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import run.MainLauncher;
import user.Listener;

public class DatabaseWindowController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button databaseQuitButton;

    @FXML
    void quit(ActionEvent event) {
        System.out.println("logout");
        // loginSignInButton.getScene().getWindow().hide();
        // FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(MainLauncher.getDatabaseWindowPath()));
        // try {
        //     Parent window = (Pane) fxmlLoader.load();
        //     databaseWindowController = fxmlLoader.<DatabaseWindowController>getController();
        //     Listener listener = new Listener(host, port, login, password, databaseWindowController);
        //     Thread listenerThread = new Thread(listener);
        //     listenerThread.start();
        // } catch (IOException e) {
        //     e.printStackTrace();
        // }

        // Parent root = fxmlLoader.getRoot();
        // Stage stage = new Stage();
        // Scene scene = new Scene(root);
        // stage.setScene(scene);
        // stage.showAndWait();
    }

    public void logoutScene() {
        Platform.runLater(() -> {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(MainLauncher.getLoginWindowPahth()));
            Parent window = null;
            try {
                window = (Pane) fxmlLoader.load();
            } catch (IOException e) {
                e.printStackTrace();
            }
            Stage stage = MainLauncher.getPrimaryStage();
            Scene scene = new Scene(window);
            stage.setScene(scene);
            stage.centerOnScreen();
        });
    }

    @FXML
    void initialize() {

    }

}
