package processing;

import data.ClientRequest;
import data.FuelType;
import data.TableRowVehicle;
import data.User;
import data.Vehicle;
import database.DatabaseCollectionManager;
import database.DatabaseHandler;
import database.DatabaseUserManager;
import exceptions.NoSuchIdException;

import java.net.Socket;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import commands.*;
import mods.*;
import utility.*;

/**
 * Stores a database that can be manipulated in real time using a commands.
 * All commands implemented here.
 */
public class BufferedDataBase {
    private final DatabaseHandler databaseHandler;
    private final DatabaseUserManager databaseUserManager;
    private final DatabaseCollectionManager databaseCollectionManager;
    private final ConcurrentHashMap<Long, Vehicle> database;
    private final Set<String> scriptCounter = new HashSet<>();
    private CommandInvoker commandInvoker;
    private DatabaseVersionHandler databaseVersionHandler;
    private LocalDateTime lastInitTime;
    private LocalDateTime lastSaveTime;
    private final IdentifierHandler identifierHandler;
    private static final String datePattern = "dd/MM/yyy - HH:mm:ss";
    private static final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(datePattern);
    private Socket socket;

    public BufferedDataBase(DatabaseHandler databaseHandler, DatabaseUserManager databaseUserManager,
                            DatabaseCollectionManager databaseCollectionManager,
                            DatabaseVersionHandler databaseVersionHandler) {
        this.databaseHandler = databaseHandler;
        this.databaseUserManager = databaseUserManager;
        this.databaseCollectionManager = databaseCollectionManager;
        this.databaseVersionHandler = databaseVersionHandler;
        // dataBase = FileHandler.loadDataBase();
        database = databaseCollectionManager.loadDataBase();
        databaseVersionHandler.loadDatabase(database);
        identifierHandler = new IdentifierHandler(database);
        lastInitTime = database.isEmpty() && lastInitTime == null ? null : LocalDateTime.now();
    }

    public void setCommandInvoker(CommandInvoker commandInvoker) {
        this.commandInvoker = commandInvoker;
    }

    /**
     * Displays information about all commands.
     * @param clientRequest contains the name of the command, its arguments on a single line,
     *                        arguments that are characteristics of the collection class and execution mode.
     * @return Command exit status.
     */
    public ServerAnswer help(ClientRequest clientRequest) {
        MessageHolder.putCurrentCommand(HelpCommand.getName(), MessageType.OUTPUT_INFO);
        MessageHolder.putMessage(FileHandler.readFile(FileType.REFERENCE), MessageType.OUTPUT_INFO);
        return new ServerAnswer(EventType.INFO, true);
    }

    /**
     * Displays information about collection.
     * @param clientRequest contains the name of the command, its arguments on a single line,
     *                        arguments that are characteristics of the collection class and execution mode.
     * @return Command exit status.
     */
    public ServerAnswer info(ClientRequest clientRequest) {
        String stringLastInitTime = (lastInitTime == null ?
                "there have been no initializations in this session yet" : lastInitTime.format(dateFormatter));
        String stringLastSaveTime = (lastSaveTime == null ?
                "there hasn't been a save here yet" : lastSaveTime.format(dateFormatter));
        MessageHolder.putCurrentCommand(InfoCommand.getName(), MessageType.OUTPUT_INFO);
        MessageHolder.putMessage(String.format("""
                Information about collection:
                Type of collection:  %s
                Initialization date: %s
                Last save time:      %s
                Number of elements:  %s""", getCollectionType(), stringLastInitTime,
                stringLastSaveTime, getCollectionSize()), MessageType.OUTPUT_INFO);
        return new ServerAnswer(EventType.INFO, true);
    }

    /**
     * Displays all elements of the collection.
     * @param clientRequest contains the name of the command, its arguments on a single line,
     *                        arguments that are characteristics of the collection class and execution mode.
     * @return Command exit status.
     */
    public ServerAnswer show(ClientRequest clientRequest) {
        MessageHolder.putCurrentCommand(ShowCommand.getName(), MessageType.OUTPUT_INFO);
        if (database.isEmpty()) {
            MessageHolder.putMessage("Collection is empty", MessageType.OUTPUT_INFO);
            Hashtable<Long, Vehicle> vehicHashtable = new Hashtable<>();
            vehicHashtable.putAll(database);
            return new ServerAnswer(EventType.DATABASE_INIT, true, vehicHashtable);
        }
        TreeMap<Long, Vehicle> treeMapData = new TreeMap<>(database);
        treeMapData.keySet().forEach(key ->
                MessageHolder.putMessage("key:                " + key +
                        "\n" + treeMapData.get(key) + "", MessageType.OUTPUT_INFO));
        
        Hashtable<Long, Vehicle> vehicHashtable = new Hashtable<>();
            vehicHashtable.putAll(database);
            return new ServerAnswer(EventType.DATABASE_INIT, true, vehicHashtable);
    }

    /**
     * Adds a new element to the collection by key.
     * @param clientRequest contains the name of the command, its arguments on a single line,
     *                        arguments that are characteristics of the collection class and execution mode.
     * @return Command exit status.
     */
    public ServerAnswer insert(ClientRequest clientRequest) {
        if (identifierHandler.hasElementWithKey(clientRequest.getArguments()[0], true,
                    InsertCommand.getName() + " " + clientRequest.getArguments()[0])) {
            return new ServerAnswer(EventType.INSERT, false);
        }
        if (clientRequest.getExtraArguments() == null)
            return new ServerAnswer(EventType.INSERT, true);
        return addElementBy(clientRequest, AddMode.INSERT_MODE);
    }

    /**
     * Updates the collection element by id.
     * @param clientRequest contains the name of the command, its arguments on a single line,
     *                        arguments that are characteristics of the collection class and execution mode.
     * @return Command exit status.
     */
    public ServerAnswer update(ClientRequest clientRequest) {
        long id = Long.parseLong(clientRequest.getArguments()[0]);
        User user = clientRequest.getUser();
        if (!identifierHandler.hasElementWithId(id)) {
            MessageHolder.putMessage("No such element with this id", MessageType.USER_ERROR);
            new ServerAnswer(EventType.UPDATE, clientRequest.getArguments(), false);
        }
        if (!databaseCollectionManager.hasVehicleWithIdAndLogin(id, user.getLogin())) {
            MessageHolder.putMessage("The element you want to update does not belong to you", MessageType.USER_ERROR);
            new ServerAnswer(EventType.UPDATE, clientRequest.getArguments(), false);
        }
        if (clientRequest.getExtraArguments() == null)
            return new ServerAnswer(EventType.UPDATE, true);
        return addElementBy(clientRequest, AddMode.UPDATE_MODE);
    }

    /**
     * Executes 'insert' or 'update' command.
     * @param clientRequest
     * @param addMode Defines command.
     * @return Command exit status.
     */
    private ServerAnswer addElementBy(ClientRequest clientRequest, AddMode addMode) {
        String[] arguments = clientRequest.getArguments();
        String[] vehicleValues = clientRequest.getExtraArguments();
        String commandName = clientRequest.getCommandName();
        ExecuteMode executeMode = clientRequest.getExecuteMode();
        java.time.ZonedDateTime creationDate = ZonedDateTime.now();
        long key = 0, id = -1;
        EventType eventType = (addMode == AddMode.INSERT_MODE ? EventType.INSERT : EventType.UPDATE); 
        if (addMode == AddMode.INSERT_MODE) {
            key = Long.parseLong(arguments[0]);
        } else {
            id = Long.parseLong(arguments[0]);
            try {
                key = identifierHandler.getKeyById(id);
            } catch (NoSuchIdException e) {
                MessageHolder.putMessage(e.getMessage(), MessageType.USER_ERROR);
                return new ServerAnswer(eventType, arguments, false);
            }
        }
        if (executeMode == ExecuteMode.SCRIPT_MODE && vehicleValues.length != Vehicle.getCountOfChangeableFields()) {
            MessageHolder.putCurrentCommand(commandName + " " + arguments[0], MessageType.USER_ERROR);
            MessageHolder.putMessage(String.format(
                    "There are not enough lines in script '%s' for the '%s %s' command",
                    clientRequest.getScriptFile().getName(), commandName, arguments[0]), MessageType.USER_ERROR);
            return new ServerAnswer(eventType, arguments, false);
        }
        if (executeMode == ExecuteMode.SCRIPT_MODE && 
            !ValueHandler.checkValues(vehicleValues, commandName + " " + arguments[0])) {
            return new ServerAnswer(eventType, arguments, false);
        }
        Vehicle vehicle = ValueHandler.getVehicle(id, creationDate, vehicleValues);
        if (addMode == AddMode.INSERT_MODE) {
            try {
                id = databaseCollectionManager.insertVehicle(key, vehicle, clientRequest.getUser());
            } catch (SQLException e) {
                MessageHolder.putMessage(e.getMessage(), MessageType.USER_ERROR);
                return new ServerAnswer(eventType, arguments, false);
            }
            vehicle.setId(id);
            vehicle.setUsername(clientRequest.getUser().getLogin());
        } else {
            try {
                databaseCollectionManager.updateVehicleByIdAndLogin(vehicle, clientRequest.getUser());
            } catch (SQLException e) {
                MessageHolder.putMessage(e.getMessage(), MessageType.USER_ERROR);
                return new ServerAnswer(eventType, arguments, false);
            }
            String dateTime = database.get(key).getCreationDate();
            vehicle.setCreationDate(dateTime);
        }
        database.put(key, vehicle);
        MessageHolder.putCurrentCommand(commandName + " " + arguments[0], MessageType.OUTPUT_INFO);
        MessageHolder.putMessage("Element was successfully " + addMode.getResultMessage(), MessageType.OUTPUT_INFO);
        databaseVersionHandler.updateVersion();
        TableRowVehicle tableRowVehicle = new TableRowVehicle(id, key, vehicle.getName(), vehicle.getCoordinates().getX(),
                                                            vehicle.getCoordinates().getY(), vehicle.getEnginePower(),
                                                            vehicle.getDistanceTravelled(), vehicle.getType().toString(), vehicle.getFuelType().toString(),
                                                            vehicle.getCreationDate(), clientRequest.getUser().getLogin());
        return new ServerAnswer(eventType, arguments, tableRowVehicle, true);
    }

    public ServerAnswer remove(ClientRequest clientRequest) {
        ServerAnswer serverAnswer = new ServerAnswer(EventType.REMOVE, false);
        switch (clientRequest.getRemoveMode()) {
            case BY_ENGINE_POWER -> serverAnswer = removeAllByEnginePower(clientRequest);
            case BY_KEY -> serverAnswer = removeKey(clientRequest);
            case GREATER_KEY -> serverAnswer = removeGreaterKey(clientRequest);
            case GREATER_THEN_DISTANCE_TRAVELLED -> serverAnswer = removeGreater(clientRequest);
            case LOWER_THEN_DISTANCE_TRAVELLED -> serverAnswer = removeLower(clientRequest);
        }
        System.out.println(serverAnswer.outputInfo());
        return serverAnswer;
    }

    /**
     * Removes element by key.
     * @param clientRequest contains the name of the command, its arguments on a single line,
     *                        arguments that are characteristics of the collection class and execution mode.
     * @return Command exit status.
     */
    public ServerAnswer removeKey(ClientRequest clientRequest) {
        EventType eventType = EventType.REMOVE;
        String[] arguments = clientRequest.getArguments();
        if (!identifierHandler.hasElementWithKey(arguments[0], false,
                RemoveKeyCommand.getName() + " " + arguments[0]))
            return new ServerAnswer(eventType, arguments, clientRequest.getRemoveMode(), false);
        long key = Long.parseLong(arguments[0]);
        Vehicle vehicle = database.get(key);
        long id = vehicle.getId();
        if (!databaseCollectionManager.hasVehicleWithIdAndLogin(id, clientRequest.getUser().getLogin())) {
            MessageHolder.putMessage("The element you want to remove does not belong to you", MessageType.USER_ERROR);
            return new ServerAnswer(eventType, arguments, clientRequest.getRemoveMode(), false);
        }
        try {
            databaseCollectionManager.deleteByKey(key);
        } catch (SQLException e) {
            MessageHolder.putMessage(e.getMessage(), MessageType.USER_ERROR);
            return new ServerAnswer(eventType, arguments, clientRequest.getRemoveMode(), false);
        }
        database.remove(key);
        MessageHolder.putCurrentCommand(RemoveKeyCommand.getName() + " " + arguments[0], MessageType.OUTPUT_INFO);
        MessageHolder.putMessage(String.format(
                "Element with key = %s was successfully removed", key), MessageType.OUTPUT_INFO);
        databaseVersionHandler.updateVersion();
        return new ServerAnswer(eventType, arguments, clientRequest.getRemoveMode(), true);
    }

    /**
     * Clears the collection.
     * @param clientRequest contains the name of the command, its arguments on a single line,
     *                        arguments that are characteristics of the collection class and execution mode.
     * @return Command exit status.
     */
    public ServerAnswer clear(ClientRequest clientRequest) {
        MessageHolder.putCurrentCommand(ClearCommand.getName(), MessageType.OUTPUT_INFO);
        if (database.isEmpty()) {
            MessageHolder.putMessage("Collection is already empty", MessageType.OUTPUT_INFO);
        } else {
            String currentLogin = clientRequest.getUser().getLogin();
            try {
                databaseCollectionManager.deleteByLogin(currentLogin);
            } catch (SQLException e) {
                MessageHolder.putMessage(e.getMessage(), MessageType.USER_ERROR);
                return new ServerAnswer(EventType.CLEAR, false);
            }
            Set<Long> keySet = database.keySet()
                                        .stream()
                                        .filter(key -> database.get(key).getUsername().equals(currentLogin))
                                        .collect(Collectors.toSet());
            for (Long key : keySet) {
                database.remove(key);
            }
            MessageHolder.putMessage("Collection successfully cleared", MessageType.OUTPUT_INFO);
        }
        databaseVersionHandler.updateVersion();
        return new ServerAnswer(EventType.CLEAR, true);
    }

    /**
     * Saves the collection to a Json file.
     * @param clientRequest contains the name of the command, its arguments on a single line,
     *                        arguments that are characteristics of the collection class and execution mode.
     * @return Command exit status.
     */
    public ServerAnswer save(ClientRequest clientRequest) {
        FileHandler.saveDataBase(database);
        // MessageHolder.putCurrentCommand(SaveCommand.getName(), MessageType.OUTPUT_INFO);
        // MessageHolder.putMessage("Collection successfully saved", MessageType.OUTPUT_INFO);
        System.out.println("Command " + SaveCommand.getName());
        System.out.println("Collection successfully saved");
        lastSaveTime = LocalDateTime.now();
        return new ServerAnswer(null, true);
    }

    /**
     * Terminates a program or exits an executing script.
     * @param clientRequest contains the name of the command, its arguments on a single line,
     *                        arguments that are characteristics of the collection class and execution mode.
     * @return Command exit status.
     */
    public ServerAnswer exit(ClientRequest clientRequest) {
        MessageHolder.putCurrentCommand(ExitCommand.getName(), MessageType.OUTPUT_INFO);
        if (clientRequest.getExecuteMode() == ExecuteMode.COMMAND_MODE)
            MessageHolder.putMessage("Program successfully completed", MessageType.OUTPUT_INFO);
        return new ServerAnswer(null, true);
    }

    /**
     * Removes all elements of the collection whose distance travelled value exceeds the given value.
     * @param clientRequest contains the name of the command, its arguments on a single line,
     *                        arguments that are characteristics of the collection class and execution mode.
     * @return Command exit status.
     */
    public ServerAnswer removeGreater(ClientRequest clientRequest) {
        return removeAllByDistanceTravelled(clientRequest, RemoveGreaterCommand.getName(), RemoveMode.GREATER_THEN_DISTANCE_TRAVELLED);
    }

    /**
     * Removes all elements of the collection whose distance travelled value is less than the given value.
     * @param clientRequest contains the name of the command, its arguments on a single line,
     *                        arguments that are characteristics of the collection class and execution mode.
     * @return Command exit status.
     */
    public ServerAnswer removeLower(ClientRequest clientRequest) {
        return removeAllByDistanceTravelled(clientRequest, RemoveLowerCommand.getName(), RemoveMode.LOWER_THEN_DISTANCE_TRAVELLED);
    }

    /**
     * Executes 'remove greater' or 'remove_lower' command.
     * @param clientRequest contains the name of the command, its arguments on a single line,
     *                        arguments that are characteristics of the collection class and execution mode.
     * @param removeMode Defines command.
     * @return Command exit status.
     */
    private ServerAnswer removeAllByDistanceTravelled(ClientRequest clientRequest,
                                                 String commandName, RemoveMode removeMode) {
        String[] arguments = clientRequest.getArguments();
        long userDistanceTravelled = Long.parseLong(arguments[0]);
        try {
            databaseCollectionManager.deleteByDistanceTravelled(userDistanceTravelled, clientRequest.getUser().getLogin(), removeMode);
        } catch (SQLException e) {
            MessageHolder.putMessage(e.getMessage(), MessageType.USER_ERROR);
            return new ServerAnswer(EventType.REMOVE, arguments, clientRequest.getRemoveMode(), false);
        }
        Set<Long> filteredKeys = database.keySet().stream()
                .filter(key -> database.get(key).getUsername().equals(clientRequest.getUser().getLogin()))
                .filter(key -> (removeMode == RemoveMode.GREATER_THEN_DISTANCE_TRAVELLED ?
                        database.get(key).getDistanceTravelled() > userDistanceTravelled :
                        database.get(key).getDistanceTravelled() < userDistanceTravelled))
                        .collect(Collectors.toSet());
        int countOfRemoved = 0;
        for (Long key : filteredKeys) {
            database.remove(key);
            countOfRemoved++;
        }
        MessageHolder.putCurrentCommand(commandName, MessageType.OUTPUT_INFO);
        if (countOfRemoved == 0) {
            MessageHolder.putMessage(String.format(
                    "No elements found to remove with distance travelled %s %s",
                    removeMode.getSymbol(), userDistanceTravelled), MessageType.OUTPUT_INFO);
        } else {
            MessageHolder.putMessage(String.format(
                    "%s elements were successfully removed with distance travelled %s %s",
                    countOfRemoved, removeMode.getSymbol(), userDistanceTravelled), MessageType.OUTPUT_INFO);
        }
        databaseVersionHandler.updateVersion();
        return new ServerAnswer(EventType.REMOVE, arguments, clientRequest.getRemoveMode(), true);
    }

    /**
     * Removes all elements of the collection whose key is greater than the given value.
     * @param clientRequest contains the name of the command, its arguments on a single line,
     *                        arguments that are characteristics of the collection class and execution mode.
     * @return Command exit status.
     */
    public ServerAnswer removeGreaterKey(ClientRequest clientRequest) {
        String[] arguments = clientRequest.getArguments();
        long userKey = Long.parseLong(arguments[0]);
        try {
            databaseCollectionManager.deleteByGreaterKey(userKey, clientRequest.getUser().getLogin());
        } catch (SQLException e) {
            MessageHolder.putMessage(e.getMessage(), MessageType.USER_ERROR);
            return new ServerAnswer(EventType.REMOVE, arguments, clientRequest.getRemoveMode(), false);
        }
        int countOfRemovedKeys = 0;
        Set<Long> filteredKeys = database.keySet().stream()
                                .filter(key -> database.get(key).getUsername().equals(clientRequest.getUser().getLogin()))
                                .filter(key -> key > userKey).collect(Collectors.toSet());
        for (Long key : filteredKeys) {
            database.remove(key);
            countOfRemovedKeys++;
        }
        MessageHolder.putCurrentCommand(RemoveGreaterKeyCommand.getName(), MessageType.OUTPUT_INFO);
        if (countOfRemovedKeys == 0)
            MessageHolder.putMessage("No matching keys to remove element", MessageType.OUTPUT_INFO);
        else
            MessageHolder.putMessage(
                    String.format("%s elements was successfully removed", countOfRemovedKeys), MessageType.OUTPUT_INFO);
        databaseVersionHandler.updateVersion();
        return new ServerAnswer(EventType.REMOVE, arguments, clientRequest.getRemoveMode(), true);
    }

    /**
     * Removes all elements in the collection whose engine power is equal to the given value.
     * @param clientRequest contains the name of the command, its arguments on a single line,
     *                        arguments that are characteristics of the collection class and execution mode.
     * @return Command exit status.
     */
    public ServerAnswer removeAllByEnginePower(ClientRequest clientRequest) {
        String[] arguments = clientRequest.getArguments();
        int userEnginePower = Integer.parseInt(arguments[0]);
        try {
            databaseCollectionManager.deleteByEnginePower(userEnginePower, clientRequest.getUser().getLogin());
        } catch (SQLException e) {
            MessageHolder.putMessage(e.getMessage(), MessageType.USER_ERROR);
            return new ServerAnswer(EventType.REMOVE, arguments, clientRequest.getRemoveMode(), false);
        }
        int countOfRemoved = 0;
        Set<Long> filteredKeys = database.keySet().stream()
                .filter(key -> database.get(key).getUsername().equals(clientRequest.getUser().getLogin()))
                .filter(key -> database.get(key).getEnginePower() == userEnginePower)
                .collect(Collectors.toSet());
        for (Long key : filteredKeys) {
            database.remove(key);
            countOfRemoved++;
        }
        MessageHolder.putCurrentCommand(RemoveAllByEnginePowerCommand.getName(), MessageType.OUTPUT_INFO);
        if (countOfRemoved == 0)
            MessageHolder.putMessage(String.format(
                    "No elements found to remove with engine power = %s", userEnginePower), MessageType.OUTPUT_INFO);
        else
            MessageHolder.putMessage(String.format(
                    "%s elements were successfully removed with engine power = %s",
                    countOfRemoved, userEnginePower), MessageType.OUTPUT_INFO);
        databaseVersionHandler.updateVersion();
        return new ServerAnswer(EventType.REMOVE, arguments, clientRequest.getRemoveMode(), true);
    }

    /**
     * Prints the number of elements in the collection whose fuel type is equal to the given value.
     * @param clientRequest contains the name of the command, its arguments on a single line,
     *                        arguments that are characteristics of the collection class and execution mode.
     * @return Command exit status.
     */
    public ServerAnswer countByFuelType(ClientRequest clientRequest) {
        String[] arguments = clientRequest.getArguments();
        FuelType fuelType = ValueTransformer.SET_FUEL_TYPE.apply(
                ValueHandler.TYPE_CORRECTION.correct(arguments[0]));
        long count = database.keySet().stream()
                .filter(key -> fuelType.equals(database.get(key).getFuelType()))
                .count();
        MessageHolder.putCurrentCommand(CountByFuelTypeCommand.getName(), MessageType.OUTPUT_INFO);
        MessageHolder.putMessage(String.format("%s elements with fuel type = %s (%s)",
                count, fuelType.getSerialNumber(), fuelType), MessageType.OUTPUT_INFO);
        return new ServerAnswer(null, true);
    }

    /**
     * Prints all elements of the collection whose fuel type is less than or equal to the given value.
     * @param clientRequest contains the name of the command, its arguments on a single line,
     *                        arguments that are characteristics of the collection class and execution mode.
     * @return Command exit status.
     */
    public ServerAnswer filterLessThanFuelType(ClientRequest clientRequest) {
        String[] arguments = clientRequest.getArguments();
        FuelType fuelType = ValueTransformer.SET_FUEL_TYPE.apply(
                ValueHandler.TYPE_CORRECTION.correct(arguments[0]));
        AtomicBoolean hasSuchElements = new AtomicBoolean(false);
        MessageHolder.putCurrentCommand(FilterLessThanFuelTypeCommand.getName(), MessageType.OUTPUT_INFO);
        TreeMap<Long, Vehicle> treeMapData = new TreeMap<>(database);
        treeMapData.keySet().stream()
                .filter(key -> treeMapData.get(key).getFuelType().getSerialNumber() <= fuelType.getSerialNumber())
                .forEach(key -> {
                    MessageHolder.putMessage("key:                " + key +
                        "\n" + treeMapData.get(key) + "", MessageType.OUTPUT_INFO);
                    hasSuchElements.set(true);
                });
        if (!hasSuchElements.get()) {
            MessageHolder.putMessage(String.format(
                    "No elements found with fuel type value less than %s (%s)",
                    fuelType.getSerialNumber(), fuelType), MessageType.OUTPUT_INFO);
        }
        return new ServerAnswer(null, true);
    }

    public ServerAnswer register(ClientRequest clientRequest) {
        User user = clientRequest.getUser();
        if (databaseUserManager.getUserByLogin(user.getLogin()) != null) {
            MessageHolder.putMessage("User with such login already exists", MessageType.USER_ERROR);
            return new ServerAnswer(EventType.REGISTER, false);
        }
        boolean exitStatus = databaseUserManager.insertUser(user);
        if (!exitStatus) {
            MessageHolder.putMessage("", MessageType.USER_ERROR);
            return new ServerAnswer(EventType.REGISTER, false);
        }
        MessageHolder.putMessage(String.format("User '%s' successfully registered", user.getLogin()), MessageType.OUTPUT_INFO);
        return new ServerAnswer(EventType.REGISTER, true);
    }

    public ServerAnswer login(ClientRequest clientRequest) {
        String login = clientRequest.getUser().getLogin();
        User selectedUser = databaseUserManager.getUserByLogin(login);
        if (selectedUser == null) {
            MessageHolder.putMessage(String.format("User with login '%s' was not found", login), MessageType.USER_ERROR);
            return new ServerAnswer(EventType.LOGIN, false);
        }
        String selectedPassword = selectedUser.getPasword();
        String password = clientRequest.getUser().getPasword();
        if (!SHA256Hashing.hash(password).equals(selectedPassword)) {
            MessageHolder.putMessage("Incorrect password", MessageType.USER_ERROR);
            return new ServerAnswer(EventType.LOGIN, false);
        }
        MessageHolder.putMessage(String.format("User '%s' successfully logged in", login), MessageType.OUTPUT_INFO);
        return new ServerAnswer(EventType.LOGIN, true);
    }

    public ServerAnswer quit(ClientRequest clientRequest) {
        MessageHolder.putMessage("You are logout", MessageType.OUTPUT_INFO);
        return new ServerAnswer(EventType.QUIT, false);
    }

    public String getCollectionType() {
        return database.getClass().getName();
    }

    public int getCollectionSize() {
        return database.size();
    }
}
