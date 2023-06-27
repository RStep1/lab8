package controllers;

import java.io.IOException;
import java.lang.reflect.Array;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Hashtable;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import javax.swing.text.html.parser.Element;

import org.controlsfx.control.table.FilterValue;
import org.controlsfx.control.tableview2.filter.filtereditor.SouthFilter;

import commands.*;
import data.ClientRequest;
import data.Coordinates;
import data.CountMode;
import data.FilterMode;
import data.FuelType;
import data.TableRowVehicle;
import data.User;
import data.Vehicle;
import data.VehicleType;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventType;
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
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.stage.WindowEvent;
import javafx.util.Duration;
import mods.ExecuteMode;
import mods.MessageType;
import mods.RemoveMode;
import processing.CommandValidator;
import processing.Console;
import processing.ModeConverter;
import processing.TCPExchanger;
import run.MainLauncher;
import user.Listener;
import utility.AlertCaller;
import utility.MessageHolder;
import utility.ServerAnswer;
import utility.ValueHandler;
import utility.ValueTransformer;

public class DatabaseWindowController {
    private ObservableList<TableRowVehicle> vehicleObservableList;
    private ObservableList<TableRowVehicle> bufferedVehicles;
    private static final String datePattern = "dd/MM/yyy - HH:mm:ss";
    private static final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(datePattern);
    private volatile FilterMode currentFilterMode;
    private volatile String currentFilterValue;
    private volatile boolean isVisualizing = false;
    private Stage gameStage;

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
    private Label outputCountLabel;

    @FXML
    private TextField countValue;

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
    private Button resetFilterButton;

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
        try {
            Listener.sendRequest(new ClientRequest(ClearCommand.getName(), LoginWindowController.getInstance().getUser()));
        } catch (IOException e) {
            AlertCaller.showErrorDialog("Connection lost", "Please check for firewall issues and check if the server is running.");
            Console.println("Connection lost");
        }
    }

    public void clearEvent(ServerAnswer serverAnswer) {
        Console.println("clear event");
        // List<TableRowVehicle> toRemove = vehicleObservableList
        //     .stream()
        //     .filter(x -> x.getOwner().equals(serverAnswer.user().getLogin()))
        //     .toList();
        // vehicleObservableList.removeAll(toRemove);
        // Platform.runLater(() -> tableRecordsLabel.setText(Integer.toString(vehicleObservableList.size())));
        // vehicleTable.refresh();
    }

    @FXML
    public void onInfoButtonClick(ActionEvent event) {
        
    }

    private String[] fillExtraArgumenst() {
        String[] extraArguments = new String[7];
        extraArguments[0] = nameField.getText();
        extraArguments[1] = xField.getText();
        extraArguments[2] = yField.getText();
        extraArguments[3] = enginePowerField.getText();
        extraArguments[4] = distanceTravelledField.getText();
        extraArguments[5] = vehicleTypeChoice.getSelectionModel().getSelectedItem();
        extraArguments[6] = fuelTypeChoice.getSelectionModel().getSelectedItem();
        return extraArguments;
    }

    @FXML
    public void onInsertButtonClick(ActionEvent event) {
        String[] arguments = new String[1];
        String[] extraArguments = fillExtraArgumenst();
        arguments[0] = keyField.getText();
        ClientRequest clientRequest = new ClientRequest(InsertCommand.getName(), arguments, extraArguments,
                                                        LoginWindowController.getInstance().getUser());
        for (int i = 0; i < Vehicle.getCountOfChangeableFields(); i++) {
            if (extraArguments[i] == null || extraArguments[i].trim().equals("")) {
                AlertCaller.errorAlert("All fields must be filled to insert a new element");
                return;
            }
        }
        CommandValidator commandValidator = new CommandValidator();
        if (!commandValidator.validate(clientRequest) || !ValueHandler.checkValues(extraArguments, InsertCommand.getName())) {
            AlertCaller.errorAlert(MessageHolder.getMessages(MessageType.USER_ERROR));
            MessageHolder.clearMessages(MessageType.USER_ERROR);
            return;
        }
        try {
            Listener.sendRequest(clientRequest);
        } catch (IOException e) {
            AlertCaller.showErrorDialog("Connection lost", "Please check for firewall issues and check if the server is running.");
            Console.println("Connection lost");
        }
    }

    public void insertEvnet(ServerAnswer serverAnswer) {
        if (!serverAnswer.commandExitStatus()) {
            AlertCaller.errorAlert(MessageHolder.getMessages(MessageType.USER_ERROR));
            MessageHolder.clearMessages(MessageType.USER_ERROR);
            return;
        }
        Platform.runLater(() -> {
            keyField.clear();
            clearFields();
        });
    }

    private void clearFields() {
        nameField.clear();
        xField.clear();
        yField.clear();
        enginePowerField.clear();
        distanceTravelledField.clear();
        vehicleTypeChoice.setValue(null);
        fuelTypeChoice.setValue(null);
    }

    @FXML
    public void onUpdateButtonClick(ActionEvent event) {
        String[] arguments = new String[1];
        arguments[0] = idField.getText();
        String[] extraArguments = fillExtraArgumenst();
        ClientRequest clientRequest = new ClientRequest(UpdateCommand.getName(), arguments, extraArguments,
                                                        LoginWindowController.getInstance().getUser());
        for (int i = 0; i < Vehicle.getCountOfChangeableFields(); i++) {
            if (extraArguments[i] == null || extraArguments[i].trim().equals("")) {
                AlertCaller.errorAlert("All fields must be filled to update a new element");
                return;
            }
        }
        CommandValidator commandValidator = new CommandValidator();
        if (!commandValidator.validate(clientRequest) || !ValueHandler.checkValues(extraArguments, UpdateCommand.getName())) {
            AlertCaller.errorAlert(MessageHolder.getMessages(MessageType.USER_ERROR));
            MessageHolder.clearMessages(MessageType.USER_ERROR);
            return;
        }
        try {
            Listener.sendRequest(clientRequest);
        } catch (IOException e) {
            AlertCaller.showErrorDialog("Connection lost", "Please check for firewall issues and check if the server is running.");
            Console.println("Connection lost");
        }
    }

    public void updateEvent(ServerAnswer serverAnswer) {
        if (!serverAnswer.commandExitStatus()) {
            AlertCaller.errorAlert(MessageHolder.getMessages(MessageType.USER_ERROR));
            MessageHolder.clearMessages(MessageType.USER_ERROR);
            return;
        }
        Platform.runLater(() -> {
            idField.clear();
            clearFields();
        });
    }

    @FXML
    public void onVisualizeButtonClick(ActionEvent event) {
        if (isVisualizing == true) {
            return;
        }
        try {
            gameStage = new Stage();
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(MainLauncher.getVisualizationWindowPath()));
            Parent gameWindow = fxmlLoader.load();
            gameStage.setTitle("Snake game");
            // stage.getIcons().add(new Image());
            VisualizationWindowController visualizationWindowController = fxmlLoader.<VisualizationWindowController>getController();
            visualizationWindowController.setCollection(vehicleObservableList);
            visualizationWindowController.setDatabaseWindowController(this);
            Scene snakeScene = new Scene(gameWindow, MainLauncher.getSceneHeight(), MainLauncher.getSceneHeight());
            gameStage.centerOnScreen();
            gameStage.setScene(snakeScene);
            isVisualizing = true;
            gameStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void onCountButtonClick(ActionEvent event) {
        CommandValidator commandValidator = new CommandValidator();
        String[] arguments = new String[1];
        arguments[0] = countValue.getText();
        CountMode countMode = ModeConverter.GET_COUNT_MODE.apply(countByChoiceBox.getValue());
        if (!commandValidator.validate(new ClientRequest(CountCommand.getName(), arguments, countMode))) {
            AlertCaller.errorAlert(MessageHolder.getMessages(MessageType.USER_ERROR));
            MessageHolder.clearMessages(MessageType.USER_ERROR);
            return;
        }
        long count = 0;
        switch (countMode) {
            case BY_DISTANCE_TRAVELLED -> count = 
                vehicleObservableList.stream().filter(x -> x.getDistanceTravelled() == Long.parseLong(arguments[0])).count();
            case BY_ENGINE_POWER -> count = 
                vehicleObservableList.stream().filter(x -> x.getEnginePower() == Integer.parseInt(arguments[0])).count();
        }
        this.outputCountLabel.setText(Long.toString(count));
    }

    @FXML
    public void onFilterButtonClick(ActionEvent event) {
        currentFilterMode = ModeConverter.GET_FILTER_MODE.apply(filterByChoiceBox.getValue());
        currentFilterValue = filteringValue.getText();
        CommandValidator commandValidator = new CommandValidator();
        String[] arguments = new String[1];
        arguments[0] = currentFilterValue;
        if (!commandValidator.validate(new ClientRequest("filter", arguments, currentFilterMode))) {
            AlertCaller.errorAlert(MessageHolder.getMessages(MessageType.USER_ERROR));
            MessageHolder.clearMessages(MessageType.USER_ERROR);
            return;
        }
        vehicleObservableList.clear();
        vehicleObservableList.addAll(getFilteredRowVehicles());
        Platform.runLater(() -> this.tableRecordsLabel.setText(Integer.toString(vehicleObservableList.size())));
        vehicleTable.refresh();
        Console.println("filter");
    }

    @FXML
    public void onResetFilterButtonClick(ActionEvent event) {
        filterByChoiceBox.setValue(null);
        filteringValue.clear();
        currentFilterMode = null;
        currentFilterValue = "";
        vehicleObservableList.clear();
        vehicleObservableList.addAll(bufferedVehicles);
        Platform.runLater(() -> this.tableRecordsLabel.setText(Integer.toString(vehicleObservableList.size())));
        vehicleTable.refresh();
        System.out.println("reset filter");
    }

    @FXML
    public void onRemoveButtonClick(ActionEvent event) {
        Console.println("remove");
    }

    @FXML
    public void quit(ActionEvent event) {
        Console.println("logout");
        try {
            Listener.sendRequest(new ClientRequest(QuitCommand.getName()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        logoutScene();
    }

    public void logoutScene() {
        Platform.runLater(() -> {
            if (gameStage != null)
                gameStage.close();
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
    }

    @FXML
    public void initialize() {
        try {
            Listener.sendRequest(new ClientRequest(ShowCommand.getName(), LoginWindowController.getInstance().getUser()));
        } catch (IOException e) {
            AlertCaller.showErrorDialog("Connection lost", "Please check for firewall issues and check if the server is running.");
            Console.println("Connection lost");
            logoutScene();
        };
        initializeLabels();
        initializeChoiceBoxes();
    }


    public void initializeTableEvent(ServerAnswer serverAnswer) {
        Hashtable<Long, Vehicle> vehicleHashtable = serverAnswer.database();
        Console.println("database size = " + vehicleHashtable.size());
        vehicleObservableList = FXCollections.observableArrayList();
        bufferedVehicles = FXCollections.observableArrayList();
        Set<Long> keySet = vehicleHashtable.keySet();
        for (Long key : keySet) {
            Vehicle vehicle = vehicleHashtable.get(key);
            TableRowVehicle tableRowVehicle = buildTableRowVehicle(vehicle, key);
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

        bufferedVehicles.addAll(vehicleObservableList);
        vehicleTable.setItems(vehicleObservableList.sorted());
        vehicleTable.refresh();
        this.tableRecordsLabel.setText(Integer.toString(vehicleHashtable.size()));
        this.initializationTimeLabel.setText(ZonedDateTime.now().format(dateFormatter));
        System.out.println("INIT TABLE");
    }

    public synchronized void updateTableViewEvent(ServerAnswer serverAnswer) {
        Hashtable<Long, Vehicle> hashtable = serverAnswer.database();
        vehicleObservableList.clear();
        bufferedVehicles.clear();
        bufferedVehicles.addAll(
            hashtable.keySet()
            .stream()
            .map((key -> buildTableRowVehicle(hashtable.get(key), key)))
            .toList()
        );
        if (currentFilterMode != null) {
            vehicleObservableList.addAll(getFilteredRowVehicles());
        } else {
            vehicleObservableList.addAll(bufferedVehicles);
        }
        vehicleObservableList.sort((tuple1, tuple2) -> (int) (tuple1.getId() - tuple2.getId()));
        Platform.runLater(() -> {
            this.lastModifiedTimeLabel.setText(ZonedDateTime.now().format(dateFormatter));
            this.tableRecordsLabel.setText(Integer.toString(vehicleObservableList.size()));
        });
        vehicleTable.refresh();

        System.out.println("UPDATE TABLE");
    }

    private ObservableList<TableRowVehicle> getFilteredRowVehicles() {
        ObservableList<TableRowVehicle> filteredVehicles = FXCollections.observableArrayList();
        switch (currentFilterMode) {
                case LESS_THEN_ENGINE_POWER -> filteredVehicles = 
                        bufferedVehicles.stream()
                                .filter(x -> x.getEnginePower() < ValueTransformer.SET_ENGINE_POWER.apply(currentFilterValue))
                                .collect(Collectors.toCollection(FXCollections::observableArrayList));
                case LESS_THEN_FUEL_TYPE -> filteredVehicles = 
                        bufferedVehicles.stream()
                                .filter(x -> 
                                ValueTransformer.SET_FUEL_TYPE
                                 .apply(x.getFuelType()).getSerialNumber() < 
                                        ValueTransformer.SET_FUEL_TYPE.apply(currentFilterValue).getSerialNumber())
                                .collect(Collectors.toCollection(FXCollections::observableArrayList));
        }
        return filteredVehicles;
    }

    private TableRowVehicle buildTableRowVehicle(Vehicle vehicle, Long key) {
        return new TableRowVehicle(
            vehicle.getId(),
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
    }

    private void initializeLabels() {
        this.outputCountLabel.setText("0");
        this.lastModifiedTimeLabel.setText("no changes yet");
        this.tableRecordsLabel.setText("0");
    }
    
    private void initializeChoiceBoxes() {
        for (CountMode countMode : CountMode.values()) 
            countByChoiceBox.getItems().add(countMode.getDisplayName());
        for (FilterMode filterMode : FilterMode.values())
            filterByChoiceBox.getItems().add(filterMode.getName());
        for (RemoveMode removeMode : RemoveMode.values())
            removeByChoiceBox.getItems().add(removeMode.getName());
        for (VehicleType vehicleType : VehicleType.values())
            vehicleTypeChoice.getItems().add(vehicleType.toString());
        for (FuelType fuelType : FuelType.values())
            fuelTypeChoice.getItems().add(fuelType.toString());
    }

    public void setIsVisualizing(boolean value) {
        this.isVisualizing = value;
    }
}