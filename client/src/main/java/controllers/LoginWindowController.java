package controllers;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import commands.LoginCommand;
import commands.RegisterCommand;
import data.CommandArguments;
import data.User;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import processing.TCPExchanger;
import run.MainLauncher;
import user.Listener;
import utility.ServerAnswer;

public class LoginWindowController {

    // private static final String DATABASE_WINDOW = "../view/DatabaseWindow.fxml";

    private static DatabaseWindowController databaseWindowController;
    private static final String HOST = "localhost";
    private static final int PORT = 18022;
    private Scene scene;
    private static LoginWindowController instance;
    private final Listener listener = new Listener(HOST, PORT);

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
        User user = new User(login, password);
        loginField.clear();
        passwordField.clear();
        ServerAnswer serverAnswer;
        try {
            listener.setConnection();
            Listener.sendRequest(new CommandArguments(LoginCommand.getName(), null, null, null, null, user));
            serverAnswer = Listener.readServerAnswer();
        } catch (IOException e) {
            showErrorDialog("Could not connect to server", "Please check for firewall issues and check if the server is running.");
            System.out.println("Could not connect to server");
            return;
        }
        if (!serverAnswer.commandExitStatus()) {
            showErrorDialog(serverAnswer.userErrors().get(0), "Please check your login and password");
            return;
        }
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(MainLauncher.getDatabaseWindowPath()));
        Parent window = null;
        try {
            window = (Pane) fxmlLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        databaseWindowController = fxmlLoader.<DatabaseWindowController>getController();
        listener.setUser(user);
        listener.setDatabaseController(databaseWindowController);
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
        User user = new User(login, password);
        loginField.clear();
        passwordField.clear();
        System.out.println("register event");
        ServerAnswer serverAnswer = null;
        try {
            listener.setConnection();
            Listener.sendRequest(new CommandArguments(RegisterCommand.getName(), null, null, null, null, user));
            serverAnswer = Listener.readServerAnswer();
        } catch (IOException e) {
            showErrorDialog("Could not connect to server", "Please check for firewall issues and check if the server is running.");
            System.out.println("Could not connect to server");
            return;
        }
        if (!serverAnswer.commandExitStatus()) {
            showErrorDialog(serverAnswer.userErrors().get(0), "Please, check your login");
        }
    }

    public void showScene() {
        Platform.runLater(() -> {
            Stage stage = (Stage) passwordField.getScene().getWindow();
            stage.setWidth(MainLauncher.getSceneWidth());
            stage.setHeight(MainLauncher.getSceneHeight());
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

    public static void showErrorDialog(String message, String contentText) {
        Platform.runLater(()-> {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning!");
            alert.setHeaderText(message);
            alert.setContentText(contentText);
            alert.showAndWait();
        });
    }
    
    @FXML
    void initialize() {
        
    }  
}

