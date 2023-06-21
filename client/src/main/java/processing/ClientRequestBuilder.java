package processing;

import commands.ExecuteScriptCommand;
import commands.ExitCommand;
import commands.InsertCommand;
import commands.LoginCommand;
import commands.RegisterCommand;
import commands.UpdateCommand;
import data.ClientRequest;
import data.User;
import data.Vehicle;
import mods.EventType;
import mods.ExecuteMode;
import user.Client;
import utility.FileHandler;

import java.io.File;
import java.util.*;

public class ClientRequestBuilder {
    public final Scanner scanner;
    private User user;

    public ClientRequestBuilder(Scanner scanner) {
        this.scanner = scanner;
    }

    public ArrayList<ClientRequest> userEnter() {
        Console.print("Type command and press Enter: ");
        String nextLine = "";
        try {
            nextLine = scanner.nextLine();
        } catch (NoSuchElementException e) {
            Client.stop();
            scanner.close();
            System.exit(0);
        }
        return commandProcessing(nextLine, ExecuteMode.COMMAND_MODE, null);
    }

    private ArrayList<ClientRequest> commandProcessing(String nextLine, ExecuteMode executeMode, File currentScriptFile) {
        if (nextLine.trim().equals(""))
            return new ArrayList<>();
        UserLineSeparator userLineSeparator = new UserLineSeparator(nextLine);
        String nextCommand = userLineSeparator.getCommand();
        String[] arguments = userLineSeparator.getArguments();
        String[] extraArguments = null;
        if (nextCommand.equals(RegisterCommand.getName()) || nextCommand.equals(LoginCommand.getName())) {
            user = Console.enterUsernameAndPassword(scanner, nextLine);
        }
        ClientRequest newClientRequest = new ClientRequest(nextCommand, arguments, extraArguments, null, executeMode, user);
                        // new ClientRequest(nextCommand, arguments, extraArguments, executeMode, user);

        newClientRequest.setScriptFile(currentScriptFile);
        if (nextCommand.equals(ExecuteScriptCommand.getName())) // if it's execute_script command, start script processing
            return scriptProcessing(newClientRequest);
        ArrayList<ClientRequest> clientRequestArrayList = new ArrayList<>();
        CommandValidator commandValidator = new CommandValidator();
        if (commandValidator.validate(newClientRequest)) {// add command only if it's correct
            clientRequestArrayList.add(newClientRequest);
        }
        return clientRequestArrayList;
    }

    /**
     * Separates a command from its arguments.
     */
    private static class UserLineSeparator {
        private final String command;
        private final String[] arguments;
        public UserLineSeparator(String nextLine) {
            String[] nextSplitedLine = nextLine.trim().split("\\s+");
            this.arguments = new String[nextSplitedLine.length - 1];
            for (int i = 1; i < nextSplitedLine.length; i++) {
                this.arguments[i - 1] = nextSplitedLine[i];
            }
            this.command = nextSplitedLine[0];
        }
        public String getCommand() {
            return command;
        }
        public String[] getArguments() {
            return arguments;
        }
    }

    private ArrayList<ClientRequest> scriptProcessing(ClientRequest clientRequest) {
        CommandValidator commandValidator = new CommandValidator();
        if (!commandValidator.validate(clientRequest))
            return new ArrayList<>();
        File currentScriptFile = clientRequest.getScriptFile();
        ArrayList<String> scriptLines = FileHandler.readScriptFile(clientRequest.getScriptFile());
        ArrayList<ClientRequest> scriptCommands = new ArrayList<>();
        int countOfScriptLines = scriptLines.size();
        for (int line = 0; line < countOfScriptLines; line++) {
            String scriptLine = scriptLines.get(line);
            if (scriptLine.trim().equals(""))
                continue;
            scriptCommands.addAll(commandProcessing(scriptLine, ExecuteMode.SCRIPT_MODE, currentScriptFile));
            if (!scriptCommands.isEmpty()) {
                ClientRequest lastClientRequest = scriptCommands.get(scriptCommands.size() - 1);
                if (lastClientRequest.getCommandName().equals(ExitCommand.getName()) &&
                    lastClientRequest.getScriptFile().getName().equals(currentScriptFile.getName())) { // exit from script, stop adding commands (only if the last command from current scrip file)
                    break;
                }
                if ((lastClientRequest.getCommandName().equals(InsertCommand.getName()) || 
                    lastClientRequest.getCommandName().equals(UpdateCommand.getName())) &&
                    lastClientRequest.getScriptFile().getName().equals(currentScriptFile.getName()) &&
                    lastClientRequest.getExtraArguments() == null) {
                    String[] extraArguments = readExtraArguments(Vehicle.getCountOfChangeableFields(),
                                                                     line, countOfScriptLines, scriptLines);
                    lastClientRequest.setExtraArguments(extraArguments);
                    line += Vehicle.getCountOfChangeableFields();
                }
            }
        }
        return scriptCommands;
    }

     /**
     * Adds extra lines from script that are used as arguments to change the collection element.
     */
    private String[] readExtraArguments(int countOfExtraArguments, int currentLineIndex,
                                        int countOfScriptLines, ArrayList<String> scriptLines) {
        String[] extraArguments =
                new String[Math.min(countOfExtraArguments, countOfScriptLines - currentLineIndex - 1)];
        for (int i = 0, j = currentLineIndex + 1; 
        j < currentLineIndex + countOfExtraArguments + 1 && j < countOfScriptLines; j++, i++)
            extraArguments[i] = scriptLines.get(j).trim();
        return extraArguments;
    }
}
