package commands;
import data.ClientRequest;
import processing.BufferedDataBase;
import utility.ServerAnswer;

/**
 * Acts as a wrapper for the 'exit' command.
 * Calls the method containing the implementation of this command.
 */
public class CountCommand implements Command {
    private static final String NAME = "count";
    private static final String ARGUMENTS = "";
    private static final String DESCRIPTION =
            "count elements by some column";
    private static final int COUNT_OF_ARGUMENTS = 1;
    private static final int COUNT_OF_EXTRA_ARGUMENTS = 0;
    public CountCommand() {
    }

    @Override
    public ServerAnswer execute(ClientRequest commandArguments) {
        return null;
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