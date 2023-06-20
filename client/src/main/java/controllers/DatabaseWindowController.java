package controllers;

import java.io.IOException;
import java.lang.reflect.Array;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatterBuilder;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.swing.text.html.parser.Element;

import commands.QuitCommand;
import data.CommandArguments;
import data.CountMode;
import data.FilterMode;
import data.FuelType;
import data.TableRowVehicle;
import data.VehicleType;
import javafx.application.Platform;
import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
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
import mods.RemoveMode;
import run.MainLauncher;
import user.Listener;

public class DatabaseWindowController {

    @FXML
    private Label usernameLabel;

    @FXML
    private ResourceBundle currentBundle;

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
    private Button clearButton;

    @FXML
    private Button countButton;

    @FXML
    private Button filterButton;

    @FXML
    private Button removeButton;

    @FXML
    private TableColumn<TableRowVehicle, String> ownerColumn;

    @FXML
    private ChoiceBox<String> removeByChoiceBox;

    @FXML
    private Label tableRecordsLabel;

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
    public void onClearButtonClick(ActionEvent event) {
        System.out.println("clear");
    }

    @FXML
    public void onInfoButtonClick(ActionEvent event) {
        System.out.println("info");
    }

    @FXML
    public void onInsertButtonClick(ActionEvent event) {
        System.out.println("insert");
    }

    @FXML
    public void onUpdateButtonClick(ActionEvent event) {
        System.out.println("udpate");
    }

    @FXML
    public void onVisualizeButtonClick(ActionEvent event) {
        System.out.println("visualize");
    }

    @FXML
    public void onCountButtonClick(ActionEvent event) {
        System.out.println("count");
    }

    @FXML
    public void onFilterButtonClick(ActionEvent event) {
        System.out.println("filter");
    }

    @FXML
    public void onRemoveButtonClick(ActionEvent event) {
        System.out.println("remove");
    }

    @FXML
    public void quit(ActionEvent event) {
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

    public void setUsernameLabel(String username) {
        this.usernameLabel.setText(username);
        System.out.println(usernameLabel.getText());
    }

    @FXML
    public void initialize() {
        this.countLabel.setText("0");
        this.lastModifiedTimeLabel.setText("no changes yet");
        this.tableRecordsLabel.setText("0");


        initChoiceBoxes();
        // Arrays.asList(CountMods.values()).stream().forEach(x -> countByChoiceBox.getItems().add(x.getName()));
        
        // this.countLabel.setText(currentBundle.getString("984359"));
    }


    private void initChoiceBoxes() {
        for (CountMode countMode : CountMode.values()) 
            countByChoiceBox.getItems().add(countMode.getName());
        for (FilterMode filterMode : FilterMode.values())
            filterByChoiceBox.getItems().add(filterMode.getName());
        for (RemoveMode removeMode : RemoveMode.values())
            removeByChoiceBox.getItems().add(removeMode.getName());
        for (VehicleType vehicleType : VehicleType.values())
            vehicleTypeChoice.getItems().add(vehicleType.toString());
        for (FuelType fuelType : FuelType.values())
            fuelTypeChoice.getItems().add(fuelType.toString());
    }
}