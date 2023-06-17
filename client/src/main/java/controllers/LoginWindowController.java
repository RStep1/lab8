package controllers;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import user.Listener;

public class LoginWindowController {

    private static final String DATABASE_WINDOW = "../view/DatabaseWindow.fxml";

    private static DatabaseWindowController databaseWindowController;
    private final String host = "localhost";
    private final int port = 18022;

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
        
        loginSignInButton.getScene().getWindow().hide();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(DATABASE_WINDOW));
        try {
            Parent window = (Pane) fxmlLoader.load();
            databaseWindowController = fxmlLoader.<DatabaseWindowController>getController();
            Listener listener = new Listener(host, port, login, password, databaseWindowController);
            Thread listenerThread = new Thread(listener);
            listenerThread.start();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Parent root = fxmlLoader.getRoot();
        Stage stage = new Stage();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.showAndWait();
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

    @FXML
    void initialize() {
        
    }    
}

