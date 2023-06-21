package commands;

import data.ClientRequest;
import processing.BufferedDataBase;
import utility.ServerAnswer;

/**
 * Acts as a wrapper for the 'clear' command.
 * Calls the method containing the implementation of this command.
 */
public class ClearCommand implements Command {
    private BufferedDataBase bufferedDataBase;
    private static final String NAME = "clear";
    private static final String ARGUMENTS = "";
    private static final String DESCRIPTION = "clears the collection";
    private static final int COUNT_OF_ARGUMENTS = 0;
    private static final int COUNT_OF_EXTRA_ARGUMENTS = 0;

    public ClearCommand(BufferedDataBase bufferedDataBase) {
        this.bufferedDataBase = bufferedDataBase;
    }

    @Override
    public ServerAnswer execute(ClientRequest commandArguments) {
        return bufferedDataBase.clear(commandArguments);
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
