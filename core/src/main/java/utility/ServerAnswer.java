package utility;

import mods.EventType;
import mods.RemoveMode;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.concurrent.ConcurrentHashMap;

import data.User;
import data.Vehicle;

public record ServerAnswer(EventType eventType, String arguments, String[] extraArguments, RemoveMode removeMode,
                        ArrayList<String> outputInfo, ArrayList<String> userErrors, boolean commandExitStatus, User user,
                        Hashtable<Long, Vehicle> database) implements Serializable {
    public ServerAnswer(EventType eventType, String arguments, String[] extraArguments, RemoveMode removeMode,
                        ArrayList<String> outputInfo, ArrayList<String> userErrors, boolean commandExitStatus, User user,
                        Hashtable<Long, Vehicle> database) {
        this.eventType = eventType;
        this.arguments = arguments;
        this.extraArguments = extraArguments;
        this.outputInfo = outputInfo;
        this.userErrors = userErrors;
        this.removeMode = removeMode;
        this.commandExitStatus = commandExitStatus;
        this.user = user;
        this.database = database;
    }


    public ServerAnswer(EventType eventType, boolean commandExitStatus) {
        this(eventType, null, null, null, null, null, commandExitStatus, null);
    }

    public ServerAnswer(EventType eventType, ArrayList<String> outputInfo, ArrayList<String> userErrors,
                         boolean commandExitStatus, User user) {
        this.eventType = eventType;
        this.arguments = arguments;
        this.extraArguments = extraArguments;
        this.outputInfo = outputInfo;
        this.userErrors = userErrors;
        this.removeMode = removeMode;
        this.commandExitStatus = commandExitStatus;
        this.user = user;
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
                eventType, arguments, Arrays.asList(extraArguments), removeMode,
                outputInfo, userErrors, commandExitStatus);
    }
}
