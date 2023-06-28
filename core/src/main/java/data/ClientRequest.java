package data;

import mods.ExecuteMode;
import mods.RemoveMode;

import java.io.File;
import java.io.Serializable;
import java.util.Arrays;

public class ClientRequest implements Serializable {
    private final String commandName;
    private String[] arguments;
    private String[] extraArguments;
    private RemoveMode removeMode;
    private CountMode countMode;
    private FilterMode filterMode;
    private final ExecuteMode executeMode;
    private File scriptFile;
    private User user;

    public ClientRequest(String commandName, String[] arguments, String[] extraArguments,
                        RemoveMode removeMode, ExecuteMode executeMode, User user) {
        this.commandName = commandName;
        this.arguments = arguments;
        this.extraArguments = extraArguments;
        this.removeMode = removeMode;
        this.executeMode = executeMode;
        this.user = user;
    }

    public ClientRequest(String commandName, String[] arguments, String[] extraArguments, User user) {
        this.commandName = commandName;
        this.arguments = arguments;
        this.extraArguments = extraArguments;
        this.user = user;
        this.executeMode = ExecuteMode.COMMAND_MODE;
    }

    public ClientRequest(String commandName, User user) {
        this.commandName = commandName;
        this.arguments = null;
        this.executeMode = ExecuteMode.COMMAND_MODE;
        this.user = user;
    }

    public ClientRequest(String commandName) {
        this.commandName = commandName;
        this.arguments = null;
        this.executeMode = ExecuteMode.COMMAND_MODE;
    }

    public ClientRequest(String commandName, String[] arguments) {
        this.commandName = commandName;
        this.arguments = arguments;
        this.executeMode = ExecuteMode.COMMAND_MODE;
    }

    public ClientRequest(String commandName, String[] arguments, CountMode countMode) {
        this(commandName, arguments);
        this.countMode = countMode;
    }

    public ClientRequest(String commandName, String[] arguments, FilterMode filterMode) {
        this(commandName, arguments);
        this.filterMode = filterMode;
    }

    public ClientRequest(String commandName, String[] argumensts, RemoveMode removeMode, User user) {
        this(commandName, argumensts);
        this.removeMode = removeMode;
        this.user = user;
    }

    public String getCommandName() {
        return commandName;
    }

    public String[] getArguments() {
        return arguments;
    }

    public String[] getExtraArguments() {
        return extraArguments;
    }

    public RemoveMode getRemoveMode() {
        return removeMode;
    }

    public ExecuteMode getExecuteMode() {
        return executeMode;
    }

    public File getScriptFile() {
        return scriptFile;
    }

    public User getUser() {
        return user;
    }

    public CountMode getCountMode() {
        return countMode;
    }

    public FilterMode getFilterMode() {
        return filterMode;
    }

    public void setScriptFile(File scriptFile) {
        this.scriptFile = scriptFile;
    }

    public void setExtraArguments(String[] extraArguments) {
        this.extraArguments = extraArguments;
    }

    public void setArguments(String[] arguments) {
        this.arguments = arguments;
    }

    @Override
    public String toString() {
        return String.format(
                """
                        Command name: %s
                        Argument: %s
                        Vehicle arguments: %s
                        Execute mode: %s
                        """,
                commandName, Arrays.toString(arguments),
                Arrays.toString(extraArguments), executeMode);
    }
}
