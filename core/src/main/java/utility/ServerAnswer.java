package utility;

import mods.EventType;
import mods.RemoveMode;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.concurrent.ConcurrentHashMap;

import data.TableRowVehicle;
import data.User;
import data.Vehicle;

public class ServerAnswer implements Serializable {
    private EventType eventType;
    private String[] arguments;
    private TableRowVehicle tableRowVehicle;
    private RemoveMode removeMode;
    private ArrayList<String> outputInfo;
    private ArrayList<String> userErrors;
    private boolean commandExitStatus;
    private User user;
    private Hashtable<Long, Vehicle> database;
    private long databaseVersion;

    public ServerAnswer(EventType eventType, String[] arguments, TableRowVehicle tableRowVehicle, RemoveMode removeMode,
                        ArrayList<String> outputInfo, ArrayList<String> userErrors, boolean commandExitStatus, User user,
                        Hashtable<Long, Vehicle> database) {
        this.eventType = eventType;
        this.arguments = arguments;
        this.tableRowVehicle = tableRowVehicle;
        this.outputInfo = outputInfo;
        this.userErrors = userErrors;
        this.removeMode = removeMode;
        this.commandExitStatus = commandExitStatus;
        this.user = user;
        this.database = database;
    }

    public ServerAnswer(EventType eventType, String[] arguments, boolean commandExitStatus) {
        this(eventType, commandExitStatus);
        this.arguments = arguments;
    }

    public ServerAnswer(EventType eventType, boolean commandExitStatus) {
        this.eventType = eventType;
        this.commandExitStatus = commandExitStatus;
    }

    public ServerAnswer(EventType eventType, boolean commandExitStatus, Hashtable<Long, Vehicle> database) {
        this(eventType, commandExitStatus);
        this.database = database;
    }

    public ServerAnswer(EventType eventType, String[] arguments, TableRowVehicle tableRowVehicle, boolean commandExitStatus) {
        this(eventType, commandExitStatus);
        this.arguments = arguments;
        this.tableRowVehicle = tableRowVehicle;
    }

    public ServerAnswer(EventType eventType, String[] arguments, RemoveMode removeMode, boolean commandExitStatus) {
        this(eventType, commandExitStatus);
        this.arguments = arguments;
        this.removeMode = removeMode;
    }

    public EventType eventType() {
        return eventType;
    }

    public String[] arguments() {
        return arguments;
    }

    public TableRowVehicle tableRowVehicle() {
        return tableRowVehicle;
    }

    public RemoveMode removeMode() {
        return removeMode;
    }

    public ArrayList<String> outputInfo() {
        return outputInfo;
    }

    public ArrayList<String> userErrors() {
        return userErrors;
    }

    public boolean commandExitStatus() {
        return commandExitStatus;
    }

    public User user() {
        return user;
    }

    public long databaseVersion() {
        return databaseVersion;
    }

    public Hashtable<Long, Vehicle> database() {
        return database;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setMessages(ArrayList<String> outputInfo, ArrayList<String> userErrors) {
        this.outputInfo = outputInfo;
        this.userErrors = userErrors;
    }

    public void setDatabaseVersion(long databaseVersion) {
        this.databaseVersion = databaseVersion;
    }

    public String toString() {
        return String.format(
                """
                        __________________________________
                        Answer type:            %s
                        Args:                   %s
                        Extra args:             %s
                        RemoveMode:             %s
                        Output info:            %s
                        User errors:            %s
                        Command exit status:    %s
                        ___________________________________
                        """,
                eventType, arguments, tableRowVehicle, removeMode,
                outputInfo, userErrors, commandExitStatus);
    }
}
