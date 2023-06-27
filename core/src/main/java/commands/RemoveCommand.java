package commands;

import data.ClientRequest;
import processing.BufferedDataBase;
import utility.ServerAnswer;

/**
 * Acts as a wrapper for the 'remove greater' command.
 * Calls the method containing the implementation of this command.
 */
public class RemoveCommand implements Command {
    private BufferedDataBase bufferedDataBase;
    private static final String NAME = "remove";
    private static final String ARGUMENTS = " <comparison value>";
    private static final String DESCRIPTION = "remove elements by choosen mode";
    private static final int COUNT_OF_ARGUMENTS = 1;
    private static final int COUNT_OF_EXTRA_ARGUMENTS = 0;

    public RemoveCommand(BufferedDataBase bufferedDataBase) {
        this.bufferedDataBase = bufferedDataBase;
    }

    @Override
    public ServerAnswer execute(ClientRequest commandArguments) {
        return bufferedDataBase.remove(commandArguments);
    }

    public static String getName() {
        return NAME;
    }

    public static String getDescription() {
        return DESCRIPTION;
    }

    public static int getCountOfArguments() {
        return COUNT_OF_ARGUMENTS;
    }

    public static int getCountOfExtraArguments() {
        return COUNT_OF_EXTRA_ARGUMENTS;
    }

    @Override
    public String toString() {
        return NAME + ARGUMENTS + ": " + DESCRIPTION;
    }
}