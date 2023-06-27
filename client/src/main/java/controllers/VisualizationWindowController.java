package controllers;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import data.TableRowVehicle;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.Window;
import run.MainLauncher;
import visualization.snake.Direction;

public class VisualizationWindowController {

    private ObservableList<TableRowVehicle> vehicles;
    private DatabaseWindowController databaseWindowController;

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private GridPane gridPane;

    @FXML
    private Button closeGameButton;

    @FXML
    private Label scoreLabel;

    @FXML
    public void onCloseGameButtonClick(ActionEvent event) {
        Stage stage = (Stage) closeGameButton.getScene().getWindow();
        databaseWindowController.setIsVisualizing(false);
        stage.close();
    }

    @FXML
    public void initialize() {
        
        System.out.println("VIS START");
        scoreLabel.setText("0");
        Platform.runLater(() -> {
            System.out.println(vehicles.size());
            scoreLabel.getScene().setOnKeyPressed(event -> {
            switch (event.getCode()) {
                case UP, W -> System.out.println("UP");
                case DOWN, S -> System.out.println("DOWN");
                case RIGHT, D -> System.out.println("RIGHT");
                case LEFT, A -> System.out.println("LEFT");
                default -> System.out.println("another key " + event.getCode() + " " + event.getCharacter());
            }
        });
        });
    }

    public void setCollection(ObservableList<TableRowVehicle> vehicles) {
        this.vehicles = vehicles;
    }

    public void setDatabaseWindowController(DatabaseWindowController databaseWindowController) {
        this.databaseWindowController = databaseWindowController;
    }
}
