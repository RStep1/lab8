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
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import run.MainLauncher;
import user.Listener;

public class LoginWindowController {

    // private static final String DATABASE_WINDOW = "../view/DatabaseWindow.fxml";

    private static DatabaseWindowController databaseWindowController;
    private final String host = "localhost";
    private final int port = 18022;
    private Scene scene;
    private static LoginWindowController instance;

    public LoginWindowController() {
        instance = this;
    }

    public static LoginWindowController getInstance() {
        return instance;
    }

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private TextField passwordField;

    @FXML
    private TextField loginField;

    @FXML
    private Button loginSignInButton;

    @FXML
    private Button loginSignUpButton;

    @FXML
    void signIn(ActionEvent event) {
        String login = loginField.getText();
        String password = passwordField.getText();
        if (login.trim().equals("") || password.trim().equals(""))
            return;
        loginField.clear();
        passwordField.clear();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(MainLauncher.getDatabaseWindowPath()));
        Parent window = null;
        try {
            window = (Pane) fxmlLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        databaseWindowController = fxmlLoader.<DatabaseWindowController>getController();
        Listener listener = new Listener(host, port, login, password, databaseWindowController);
        Thread listenerThread = new Thread(listener);
        listenerThread.start();
        this.scene = new Scene(window);
    }

    @FXML
    void signUp(ActionEvent event) {
        String login = loginField.getText();
        String password = passwordField.getText();
        if (login.trim().equals("") || password.trim().equals(""))
            return;
        loginField.clear();
        passwordField.clear();
        System.out.println("register event");
    }

    public void showScene() {
        Platform.runLater(() -> {
            Stage stage = (Stage) passwordField.getScene().getWindow();
            stage.setWidth(800);
            stage.setHeight(600);
            stage.setOnCloseRequest((WindowEvent e) -> {
                Platform.exit();
                System.exit(0);
            });
            stage.setMinWidth(400);
            stage.setMinHeight(300);
            stage.setScene(this.scene);
            stage.centerOnScreen();
        });
    }

    public void closeSystem(){
        Platform.exit();
        System.exit(0);
    }
    
    @FXML
    void initialize() {
        
    }    
}

