package processing;

import commands.*;
import data.ClientRequest;
import data.CountMode;
import data.FilterMode;
import exceptions.WrongAmountOfArgumentsException;
import mods.EventType;
import mods.ExecuteMode;
import mods.MessageType;
import mods.RemoveMode;
import utility.*;

import java.io.File;
import java.util.HashSet;
import java.util.concurrent.ConcurrentHashMap;
import java.util.Set;

public class CommandValidator {
    private static final Set<String> scriptCounter = new HashSet<>();

    public CommandValidator() {
    }

    private boolean checkNumberOfArguments(ClientRequest clientRequest, int expectedNumberOfArguments) {
        try {
            if (clientRequest.getArguments().length != expectedNumberOfArguments) {
                // MessageHolder.putCurrentCommand(clientRequest.getCommandName(), MessageType.USER_ERROR);
                throw new WrongAmountOfArgumentsException("Wrong amount of arguments: ",
                        clientRequest.getArguments().length, expectedNumberOfArguments);
            }
            return true;
        } catch (WrongAmountOfArgumentsException e) {
            MessageHolder.putMessage(e.getMessage(), MessageType.USER_ERROR);
        }
        return false;
    }

    private boolean validateDistanceTravelled(ClientRequest clientRequest) {
        String commandName = clientRequest.getCommandName();
        String[] arguments = clientRequest.getArguments();
        if (!checkNumberOfArguments(clientRequest, 1))
            return false;
        CheckingResult checkingResult = ValueHandler.DISTANCE_TRAVELLED_CHECKER.check(arguments[0]);
        if (!checkingResult.getStatus()) {
            // MessageHolder.putCurrentCommand(commandName + " " + arguments[0], MessageType.USER_ERROR);
            MessageHolder.putMessage(checkingResult.getMessage(), MessageType.USER_ERROR);
            return false;
        }
        return true;
    }

    private boolean validateEnginePower(ClientRequest clientRequest) {
        String[] arguments = clientRequest.getArguments();
        if (!checkNumberOfArguments(clientRequest, 1))
            return false;
        CheckingResult checkingResult = ValueHandler.ENGINE_POWER_CHECKER.check(arguments[0]);
        if (!checkingResult.getStatus()) {
            // MessageHolder.putCurrentCommand(RemoveAllByEnginePowerCommand.getName() + " " +
                    // arguments[0], MessageType.USER_ERROR);
            MessageHolder.putMessage(checkingResult.getMessage(), MessageType.USER_ERROR);
            return false;
        }
        return true;
    }
    /**
     * Checks the arguments of commands that use the key as an argument.
     * @param clientRequest
     * @return Check status.
     */
    private boolean validateKey(ClientRequest clientRequest) {
        String[] arguments = clientRequest.getArguments();
        String commandName = clientRequest.getCommandName();
        if (arguments.length == 0) {
            // MessageHolder.putCurrentCommand(commandName, MessageType.USER_ERROR);
            MessageHolder.putMessage("Key value cannot be null", MessageType.USER_ERROR);
            return false;
        }
        if (!checkNumberOfArguments(clientRequest, 1))
            return false;
        IdentifierHandler identifierHandler = new IdentifierHandler(new ConcurrentHashMap<>());
        if (!identifierHandler.checkKey(arguments[0], commandName + " " + arguments[0]))
            return false;
        return true;
    }

    private boolean validateFuelType(ClientRequest clientRequest) {
        String[] arguments = clientRequest.getArguments();
        if (!checkNumberOfArguments(clientRequest, 1))
            return false;
        CheckingResult checkingResult = ValueHandler.FUEL_TYPE_CHECKER.check(arguments[0]);
        if (!checkingResult.getStatus()) {
            // MessageHolder.putCurrentCommand(CountByFuelTypeCommand.getName() + " " +
                    // arguments[0], MessageType.USER_ERROR);
            MessageHolder.putMessage(checkingResult.getMessage(), MessageType.USER_ERROR);
            return false;
        }
        return true;
    }

    private boolean validateId(ClientRequest clientRequest) {
        String[] arguments = clientRequest.getArguments();
        if (arguments.length == 0) {
            // MessageHolder.putCurrentCommand(clientRequest.getCommandName(), MessageType.USER_ERROR);
            MessageHolder.putMessage("id value cannot be null", MessageType.USER_ERROR);
            return false;
        }
        if (!checkNumberOfArguments(clientRequest, 1))
            return false;
        IdentifierHandler identifierHandler = new IdentifierHandler(new ConcurrentHashMap<>());//////////////
        if (!identifierHandler.checkId(arguments[0], UpdateCommand.getName() + " " + arguments[0]))
            return false;
        return true;
    }

    private boolean validateExecuteScriptCommand(ClientRequest clientRequest) {
        String[] arguments = clientRequest.getArguments();
        if (clientRequest.getExecuteMode() == ExecuteMode.COMMAND_MODE)
            scriptCounter.clear();
        if (!checkNumberOfArguments(clientRequest, 1))
            return false;
        File scriptFile = FileHandler.findFile(new File("../scripts"), arguments[0]);
        if (scriptFile == null) {
            MessageHolder.putMessage(String.format(
                    "Script '%s' not found in 'scripts' directory", arguments[0]), MessageType.USER_ERROR);
            return false;
        }
        clientRequest.setScriptFile(scriptFile);
        if (scriptCounter.contains(scriptFile.getAbsolutePath())) {
            MessageHolder.putMessage(String.format("Command '%s %s':",
                    ExecuteScriptCommand.getName(), scriptFile.getName()), MessageType.USER_ERROR);
            MessageHolder.putMessage(String.format(
                    "Recursion on '%s' script noticed", scriptFile.getName()), MessageType.USER_ERROR);
            return false;
        }
        scriptCounter.add(scriptFile.getAbsolutePath());
        return true;
    }

    private boolean validateUser(ClientRequest clientRequest) {
        String[] arguments = clientRequest.getArguments();
        if (!checkNumberOfArguments(clientRequest, 0)) {
            return false;
        }
        return true;
    }

    public boolean validate(ClientRequest clientRequest) {
        if (clientRequest == null)
            return false;
        return validateArguments(clientRequest);
    }

    private boolean validateCountCommand(ClientRequest clientRequest) {
        CountMode countMode = clientRequest.getCountMode();
        if (countMode == null) {
            MessageHolder.putMessage("You have to choose the value you want to count", MessageType.USER_ERROR);
            return false;
        }
        boolean isCorrect = false;
        switch (countMode) {
            case BY_ENGINE_POWER -> isCorrect = validateEnginePower(clientRequest);
            case BY_DISTANCE_TRAVELLED -> isCorrect = validateDistanceTravelled(clientRequest);
        }
        return isCorrect;
    }

    private boolean validateFilterCommand(ClientRequest clientRequest) {
        FilterMode filterMode = clientRequest.getFilterMode();
        if (filterMode == null) {
            MessageHolder.putMessage("You have to choose the filter mode", MessageType.USER_ERROR);
            return false;
        }
        boolean isCorrect = false;
        switch (filterMode) {
            case LESS_THEN_ENGINE_POWER -> isCorrect = validateEnginePower(clientRequest);
            case LESS_THEN_FUEL_TYPE -> isCorrect = validateFuelType(clientRequest);
        }
        return isCorrect;
    }

    private boolean validateRemoveCommand(ClientRequest clientRequest) {
        RemoveMode removeMode = clientRequest.getRemoveMode();
        if (removeMode == null) {
            MessageHolder.putMessage("You have to choose the filter ", MessageType.USER_ERROR);
            return false;
        }
        boolean isCorrect = false;
        switch (removeMode) {
            case BY_ENGINE_POWER -> isCorrect = validateEnginePower(clientRequest);
            case BY_KEY, GREATER_KEY -> isCorrect = validateKey(clientRequest);
            case GREATER_THEN_DISTANCE_TRAVELLED, LOWER_THEN_DISTANCE_TRAVELLED -> validateDistanceTravelled(clientRequest);
        }
        return isCorrect;
    }

    private boolean validateArguments(ClientRequest clientRequest) {
        boolean isCorrect;
        switch (clientRequest.getCommandName()) {
            case "help", "info", "show", "clear", "exit" -> isCorrect = checkNumberOfArguments(clientRequest, 0);
            case "insert", "remove_key", "remove_greater_key" -> isCorrect = validateKey(clientRequest);
            case "update" -> isCorrect = validateId(clientRequest);
            case "execute_script" -> isCorrect = validateExecuteScriptCommand(clientRequest);
            case "remove_greater", "remove_lower" -> isCorrect = validateDistanceTravelled(clientRequest);
            case "remove_all_by_engine_power" -> isCorrect = validateEnginePower(clientRequest);
            case "count_by_fuel_type", "filter_less_than_fuel_type" -> isCorrect = validateFuelType(clientRequest);
            case "register", "login" -> isCorrect = validateUser(clientRequest);
            case "quit" -> isCorrect = checkNumberOfArguments(clientRequest, 0);
            case "count" -> isCorrect = validateCountCommand(clientRequest);
            case "filter" -> isCorrect = validateFilterCommand(clientRequest);
            case "remove" -> isCorrect = validateRemoveCommand(clientRequest);
            default -> {
                MessageHolder.putMessage(String.format(
                        "'%s': No such command", clientRequest.getCommandName()), MessageType.USER_ERROR);
                isCorrect = false;
            }
        }
        return isCorrect;
    }
}
