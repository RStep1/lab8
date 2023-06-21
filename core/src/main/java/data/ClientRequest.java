package data;

import mods.ExecuteMode;
import mods.RemoveMode;

import java.io.File;
import java.io.Serializable;
import java.util.Arrays;

public class ClientRequest implements Serializable {
    private final String commandName;
    private final String[] arguments;
    private String[] extraArguments;
    private RemoveMode removeMode;
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

    public String getCommandName() {
        return commandName;
    }

    public String[] getArguments() {
        return arguments;
    }

    public String[] getExtraArguments() {
        return extraArguments;
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

    public void setScriptFile(File scriptFile) {
        this.scriptFile = scriptFile;
    }

    public void setExtraArguments(String[] extraArguments) {
        this.extraArguments = extraArguments;
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
