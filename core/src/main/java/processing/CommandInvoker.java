package processing;

import commands.*;
import data.ClientRequest;
import utility.FileHandler;
import utility.ServerAnswer;

import java.util.HashMap;
import java.util.Map;

/**
 * Calls each wrapper command.
 */
public class CommandInvoker {
    private static final Map<String, Command> commandMap = new HashMap<>();

    /**
     * Initializes each command and set reference file.
     */
    public CommandInvoker(Command helpCommand, Command infoCommand, Command showCommand,
                          Command insertCommand, Command updateCommand, Command removeKeyCommand,
                          Command clearCommand, Command saveCommand, Command executeScriptCommand,
                          Command exitCommand, Command removeGreaterCommand, Command removeLowerCommand,
                          Command removeGreaterKeyCommand, Command removeAllByEnginePowerCommand,
                          Command countByFuelTypeCommand, Command filterLessThanFuelTypeCommand,
                          Command registerCommand, Command loginCommand, Command quitCommand,
                          Command countCommand) {
        commandMap.put(HelpCommand.getName(), helpCommand);
        commandMap.put(InfoCommand.getName(), infoCommand);
        commandMap.put(ShowCommand.getName(), showCommand);
        commandMap.put(InsertCommand.getName(), insertCommand);
        commandMap.put(UpdateCommand.getName(), updateCommand);
        commandMap.put(RemoveKeyCommand.getName(), removeKeyCommand);
        commandMap.put(ClearCommand.getName(), clearCommand);
        commandMap.put(SaveCommand.getName(), saveCommand);
        commandMap.put(ExecuteScriptCommand.getName(), executeScriptCommand);
        commandMap.put(ExitCommand.getName(), exitCommand);
        commandMap.put(RemoveGreaterCommand.getName(), removeGreaterCommand);
        commandMap.put(RemoveLowerCommand.getName(), removeLowerCommand);
        commandMap.put(RemoveGreaterKeyCommand.getName(), removeGreaterKeyCommand);
        commandMap.put(RemoveAllByEnginePowerCommand.getName(), removeAllByEnginePowerCommand);
        commandMap.put(CountByFuelTypeCommand.getName(), countByFuelTypeCommand);
        commandMap.put(FilterLessThanFuelTypeCommand.getName(), filterLessThanFuelTypeCommand);
        commandMap.put(RegisterCommand.getName(), registerCommand);
        commandMap.put(LoginCommand.getName(), loginCommand);
        commandMap.put(QuitCommand.getName(), quitCommand);
        commandMap.put(CountCommand.getName(), countCommand);

        setReferenceFile();
    }

    public static Map<String, Command> getCommandMap() {
        return commandMap;
    }
    /**
     * Fills the reference file.
     */
    private void setReferenceFile() {
        StringBuilder reference = new StringBuilder();
        commandMap.forEach((commandName, command) -> reference.append(command).append("\n"));
        FileHandler.writeReferenceFile(reference.toString());
    }

    /**
    * Invokes a command from its wrapper class.
    * @param clientRequest contains the name of the command, its arguments on a single line,
    *                        arguments that are characteristics of the collection class and execution mode.
    * @return Command exit status.
    */
    public ServerAnswer execute(ClientRequest clientRequest) {
        return commandMap.get(clientRequest.getCommandName()).execute(clientRequest);
    }
}
