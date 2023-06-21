package commands;

import data.ClientRequest;
import processing.BufferedDataBase;

/**
 * Acts as a wrapper for the 'save' command.
 * Calls the method containing the implementation of this command.
 */
public class SaveCommand implements Command {
    private BufferedDataBase bufferedDataBase;
    private static final String NAME = "save";
    private static final String ARGUMENTS = "";
    private static final String DESCRIPTION = "save the collection to file";
    private static final int COUNT_OF_ARGUMENTS = 0;
    private static final int COUNT_OF_EXTRA_ARGUMENTS = 0;

    public SaveCommand(BufferedDataBase bufferedDataBase) {
        this.bufferedDataBase = bufferedDataBase;
    }

    @Override
    public boolean execute(ClientRequest commandArguments) {
        return bufferedDataBase.save(commandArguments);
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
