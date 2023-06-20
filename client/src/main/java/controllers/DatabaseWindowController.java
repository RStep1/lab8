package controllers;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import commands.QuitCommand;
import data.CommandArguments;
import data.TableRowVehicle;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
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
    private ChoiceBox<String> countByChoiceBox;

    @FXML
    private Label countLabel;

    @FXML
    private Button databaseQuitButton;

    @FXML
    private TableColumn<TableRowVehicle, Long> distanceTravelledColumn;

    @FXML
    private TextField distanceTravelledField;

    @FXML
    private TableColumn<TableRowVehicle, Integer> enginePowerColumn;

    @FXML
    private TextField enginePowerField;

    @FXML
    private ChoiceBox<String> filterByChoiceBox;

    @FXML
    private TextField filteringValue;

    @FXML
    private ComboBox<String> fuelTypeChoice;

    @FXML
    private TableColumn<TableRowVehicle, String> fuelTypeColumn;

    @FXML
    private TableColumn<TableRowVehicle, Long> idColumn;

    @FXML
    private TextField idField;

    @FXML
    private Button infoButton;

    @FXML
    private Label initializationTimeLabel;

    @FXML
    private TableColumn<TableRowVehicle, Long> keyColumn;

    @FXML
    private TextField keyField;

    @FXML
    private Label lastModifiedTimeLabel;

    @FXML
    private TableColumn<TableRowVehicle, String> nameColumn;

    @FXML
    private TextField nameField;

    @FXML
    private Button onClearButtonClick;

    @FXML
    private Button onCountButtonClick;

    @FXML
    private Button onFilterButtonClick;

    @FXML
    private Button onRemoveButtonClick;

    @FXML
    private TableColumn<TableRowVehicle, String> ownerColumn;

    @FXML
    private ChoiceBox<String> removeByChoiceBox;

    @FXML
    private Label tableRecords;

    @FXML
    private TextField valueToRemove;

    @FXML
    private TableView<TableRowVehicle> vehicleTable;

    @FXML
    private ComboBox<String> vehicleTypeChoice;

    @FXML
    private TableColumn<TableRowVehicle, String> vehicleTypeColumn;

    @FXML
    private Button visualizeButton;

    @FXML
    private TableColumn<TableRowVehicle, Float> xColumn;

    @FXML
    private TextField xField;

    @FXML
    private TableColumn<TableRowVehicle, Double> yColumn;

    @FXML
    private TextField yField;

    @FXML
    void onClearButtonClick(ActionEvent event) {

    }

    @FXML
    void onInfoButtonClick(ActionEvent event) {

    }

    @FXML
    void onInsertButtonClick(ActionEvent event) {

    }

    @FXML
    void onUpdateButtonClick(ActionEvent event) {

    }

    @FXML
    void onVisualizeButtonClick(ActionEvent event) {

    }

    @FXML
    void quit(ActionEvent event) {
        System.out.println("logout");
        try {
            Listener.sendRequest(new CommandArguments(QuitCommand.getName(), null, null, null, null, null));
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        logoutScene();
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