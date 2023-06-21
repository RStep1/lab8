package commands;

import data.ClientRequest;
import processing.BufferedDataBase;


/**
 * Acts as a wrapper for the 'count by fuel' type command.
 * Calls the method containing the implementation of this command.
 */
public class CountByFuelTypeCommand implements Command {
    private BufferedDataBase bufferedDataBase;
    private static final String NAME = "count_by_fuel_type";
    private static final String ARGUMENTS = " <fuelType>";
    private static final String DESCRIPTION =
            "displays the number of elements whose fuelType field value is equal to the given one";
    private static final int COUNT_OF_ARGUMENTS = 1;
    private static final int COUNT_OF_EXTRA_ARGUMENTS = 0;
    public CountByFuelTypeCommand(BufferedDataBase bufferedDataBase) {
        this.bufferedDataBase = bufferedDataBase;
    }

    @Override
    public boolean execute(ClientRequest commandArguments) {
        return bufferedDataBase.countByFuelType(commandArguments);
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
    }}
