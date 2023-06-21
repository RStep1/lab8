package controllers;

import java.io.IOException;
import java.lang.reflect.Array;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatterBuilder;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.TreeSet;

import javax.swing.text.html.parser.Element;

import commands.QuitCommand;
import commands.ShowCommand;
import data.ClientRequest;
import data.Coordinates;
import data.CountMode;
import data.FilterMode;
import data.FuelType;
import data.TableRowVehicle;
import data.User;
import data.Vehicle;
import data.VehicleType;
import javafx.application.Platform;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import mods.ExecuteMode;
import mods.RemoveMode;
import processing.TCPExchanger;
import run.MainLauncher;
import user.Listener;

public class DatabaseWindowController {

    private Hashtable<Long, Vehicle> vehicleHashtable;
    private TreeSet<TableRowVehicle> vehicleTreeSet;

    @FXML
    private TableColumn<TableRowVehicle, String> creationDateColumn;

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
        System.out.print("count");
        System.out.println(countByChoiceBox.getValue());
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
            Listener.sendRequest(new ClientRequest(QuitCommand.getName()));
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

        initializeLabels();
        initializeChoiceBoxes();
        initializeTable();
    }


    private void initializeTable() {
        vehicleHashtable = loadColletion();
        try {
            Listener.sendRequest(new ClientRequest(ShowCommand.getName(), LoginWindowController.getInstance().getUser()));
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        };
        ObservableList<TableRowVehicle> vehicleObservableList = FXCollections.observableArrayList();
        Set<Long> keySet = vehicleHashtable.keySet();
        for (Long key : keySet) {
            Vehicle vehicle = vehicleHashtable.get(key);
            TableRowVehicle tableRowVehicle = 
                new TableRowVehicle(vehicle.getId(),
                                    key,
                                    vehicle.getName(),
                                    vehicle.getCoordinates().getX(),
                                    vehicle.getCoordinates().getY(),
                                    vehicle.getEnginePower(),
                                    vehicle.getDistanceTravelled(),
                                    vehicle.getType().toString(),
                                    vehicle.getFuelType().toString(),
                                    vehicle.getCreationDate(),
                                    vehicle.getUsername()
                );
            vehicleObservableList.add(tableRowVehicle);
        }
        idColumn.setCellValueFactory(new PropertyValueFactory<TableRowVehicle, Long>("id"));
        keyColumn.setCellValueFactory(new PropertyValueFactory<TableRowVehicle, Long>("key"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<TableRowVehicle, String>("name"));
        xColumn.setCellValueFactory(new PropertyValueFactory<TableRowVehicle, Float>("xCoordinate"));
        yColumn.setCellValueFactory(new PropertyValueFactory<TableRowVehicle, Double>("yCoordinate"));
        enginePowerColumn.setCellValueFactory(new PropertyValueFactory<TableRowVehicle, Integer>("enginePower"));
        distanceTravelledColumn.setCellValueFactory(new PropertyValueFactory<TableRowVehicle, Long>("distanceTravelled"));
        vehicleTypeColumn.setCellValueFactory(new PropertyValueFactory<TableRowVehicle, String>("vehicleType"));
        fuelTypeColumn.setCellValueFactory(new PropertyValueFactory<TableRowVehicle, String>("fuelType"));
        creationDateColumn.setCellValueFactory(new PropertyValueFactory<TableRowVehicle, String>("creationDate"));
        ownerColumn.setCellValueFactory(new PropertyValueFactory<TableRowVehicle, String>("owner"));

        vehicleTable.setItems(vehicleObservableList);
    }

    private Hashtable<Long, Vehicle> loadColletion() {
        Hashtable<Long, Vehicle> hashtable = new Hashtable<>();
        Vehicle vehicle = new Vehicle(245, "aaf", new Coordinates(254, 5), "asdoifj", 4452, 2346, VehicleType.BOAT, FuelType.ALCOHOL);
        vehicle.setUsername("username");
        hashtable.put(1l, vehicle);
        return hashtable;
    }

    private void initializeLabels() {
        this.countLabel.setText("0");
        this.lastModifiedTimeLabel.setText("no changes yet");
        this.tableRecordsLabel.setText("0");
    }
    
    private void initializeChoiceBoxes() {
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