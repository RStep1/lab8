package controllers;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import commands.LoginCommand;
import commands.RegisterCommand;
import data.ClientRequest;
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
import utility.AlertCaller;
import utility.ServerAnswer;

public class LoginWindowController {
    private User user;
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

    private void connectToServer() {
        try {
            listener.setConnection();
        } catch (IOException e) {
            AlertCaller.showErrorDialog("Could not connect to server", "Please check for firewall issues and check if the server is running.");
            System.out.println("Could not connect to server");
            return;
        }
        Thread listenerThread = new Thread(listener);
        listenerThread.start();
    }

    private User processUser() {
        String login = loginField.getText();
        String password = passwordField.getText();
        if (login.trim().equals("") || password.trim().equals(""))
            return null;
        User newUser = new User(login, password);
        loginField.clear();
        passwordField.clear();
        return newUser;
    }

    @FXML
    public void signIn(ActionEvent event) {
        User newUser = processUser();
        if (newUser == null) return;
        this.user = newUser;

        if (!listener.isConnected()) {
            connectToServer();
            return;
        }
        try {
            Listener.sendRequest(new ClientRequest(LoginCommand.getName(), user));
        } catch (NullPointerException | IOException e) {
            AlertCaller.showErrorDialog("Could not connect to server", "Please check for firewall issues and check if the server is running.");
            System.out.println("Could not connect to server");
        }
    }

    public void loginEvent(ServerAnswer serverAnswer) {
        if (!serverAnswer.commandExitStatus()) {
            AlertCaller.showErrorDialog(serverAnswer.userErrors().get(0), "Please check your login and password");
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
        this.scene = new Scene(window);
        showScene();
    }

    @FXML
    public void signUp(ActionEvent event) {
        User newUser = processUser();
        if (newUser == null) return;
        this.user = newUser;

        if (!listener.isConnected())
            connectToServer();
        try {
            Listener.sendRequest(new ClientRequest(RegisterCommand.getName(), newUser));
        } catch (IOException e) {
            AlertCaller.showErrorDialog("Could not connect to server", "Please check for firewall issues and check if the server is running.");
            System.out.println("Could not connect to server");
        }
    }

    public void registerEvent(ServerAnswer serverAnswer) {
        if (!serverAnswer.commandExitStatus()) {
            AlertCaller.showErrorDialog(serverAnswer.userErrors().get(0), "Please, check your login");
            return;
        }
        AlertCaller.infoAlert(String.format("user '%s' successfully register!", serverAnswer.user().getLogin()));
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

    public User getUser() {
        return user;
    }
    
    @FXML
    public void initialize() {
        
    }  
}

