package controllers;

import java.net.URL;
import java.util.ResourceBundle;

import data.TableRowVehicle;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class VisualizationWindowController {

    private ObservableList<TableRowVehicle> vehicles;

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
        stage.close();
    }

    @FXML
    public void initialize() {
        System.out.println("VIS START");
        // System.out.println(vehicles.size());
    }

    public void setCollection(ObservableList<TableRowVehicle> vehicles) {
        this.vehicles = vehicles;
    }

}
