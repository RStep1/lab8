package commands;

import data.ClientRequest;
import data.Vehicle;
import processing.BufferedDataBase;

/**
 * Acts as a wrapper for the 'insert' command.
 * Calls the method containing the implementation of this command.
 */
public class InsertCommand implements Command {
    private BufferedDataBase bufferedDataBase;
    private static final String NAME = "insert";
    private static final String ARGUMENTS = " <key> <element>";
    private static final String DESCRIPTION = "adds a new element with the given key";
    private static final int COUNT_OF_ARGUMENTS = 1;
    private static final int COUNT_OF_EXTRA_ARGUMENTS = Vehicle.getCountOfChangeableFields();
    public InsertCommand(BufferedDataBase bufferedDataBase) {
        this.bufferedDataBase = bufferedDataBase;
    }

    @Override
    public boolean execute(ClientRequest commandArguments) {
        return bufferedDataBase.insert(commandArguments);
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