package commands;

import data.ClientRequest;
import data.Vehicle;
import processing.BufferedDataBase;

/**
 * Acts as a wrapper for the 'quit' command.
 * Calls the method containing the implementation of this command.
 */
public class QuitCommand implements Command {
    private BufferedDataBase bufferedDataBase;
    private static final String NAME = "quit";
    private static final String ARGUMENTS = "";
    private static final String DESCRIPTION = "quit from user account";
    private static final int COUNT_OF_ARGUMENTS = 0;
    private static final int COUNT_OF_EXTRA_ARGUMENTS = Vehicle.getCountOfChangeableFields();
    public QuitCommand(BufferedDataBase bufferedDataBase) {
        this.bufferedDataBase = bufferedDataBase;
    }

    @Override
    public boolean execute(ClientRequest commandArguments) {
        return bufferedDataBase.quit(commandArguments);
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