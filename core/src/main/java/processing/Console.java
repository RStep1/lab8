package processing;

import mods.MessageType;
import utility.Process;
import utility.*;


import java.io.*;
import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.Scanner;

import commands.LoginCommand;
import data.User;

/**
 * Designed for input-output information.
 */
public class Console {
    private final CommandInvoker invoker;
    private static final String helpMessage = "Type 'help' and press Enter to see a list of commands";
    private static final String ANSI_RED = "\u001B[31m";
    private static final String ANSI_GREEN = "\u001B[32m";
    private static final String ANSI_RESET = "\u001B[0m";

    public Console(CommandInvoker invoker) {
        this.invoker = invoker;
    }

    /**
     * Reads the parameters of the collection element.
     * @param id Automatically generated parameter.
     * @param creationDate Automatically generated parameter.
     * @return New formed collection element.
     */
    public static String[] insertMode(/*long id, java.time.ZonedDateTime creationDate*/) {
        Scanner in = new Scanner(System.in);
        PrintStream printStream = new PrintStream(System.out);
        String[] newValues = new String[7];
        int index = 0;
        String newValue = "";
        ArrayList<Process> processes = ValueHandler.getValueProcesses();
        for (Process process : processes) {
            do {
                Console.printUserErrors();
                MessageHolder.clearMessages(MessageType.USER_ERROR);
                printStream.print(process.getMessage());
                try {
                    newValue = in.nextLine().trim();
                } catch (NoSuchElementException e) {
                    System.exit(0);
                }
                newValue = process.getCorrection().correct(newValue);
                CheckingResult checkingResult = process.getChecker().check(newValue);
                if (!checkingResult.getStatus())
                    MessageHolder.putMessage(checkingResult.getMessage(), MessageType.USER_ERROR);
            } while (!process.getChecker().check(newValue).getStatus());
            newValues[index++] = newValue;
        }
        return newValues;
    }

    public static User enterUsernameAndPassword(Scanner scanner, String commandName) {
        String username = "", passwordFirst = "", passwordSecond = "";
        try {
            username = enterWhile(scanner, "Enter username: ", "");
            if (commandName.equals(LoginCommand.getName())) {
                passwordFirst = enterWhile(scanner, "Enter password: ", "");
            } else {
                while (true) {
                    passwordFirst = enterWhile(scanner, "Enter new password: ", "");
                    passwordSecond = enterWhile(scanner, "Enter it again to confirm: ", "");
                    if (passwordFirst.equals(passwordSecond))
                        break;
                    Console.println("Passwrods don't match, please try again.");
                }
            }
        } catch (NoSuchElementException e) {
            System.exit(0);
        }
        return new User(username, passwordFirst);
    }

    private static String enterWhile(Scanner scanner, String displayMessage, String condition) {
        String value = "";
        do {
            Console.print(displayMessage);
            value = scanner.nextLine().trim();
        } while (value.equals(condition));
        return value;
    }

    public static String getHelpMessage() {
        return helpMessage;
    }

    public static void print(String message) {
        PrintStream printStream = new PrintStream(System.out);
        printStream.print(message);
    }

    public static void println(String massage) {
        PrintStream printStream = new PrintStream(System.out);
        printStream.println(massage);
    }

    public static void printOutputInfo() {
        print(ANSI_GREEN + MessageHolder.getMessages(MessageType.OUTPUT_INFO) + ANSI_RESET);
    }

    public static void printUserErrors() {
        print(ANSI_RED + MessageHolder.getMessages(MessageType.USER_ERROR) + ANSI_RESET);
    }

    public static void printOutputInfo(ArrayList<String> outputInfo) {
        StringBuilder messages = new StringBuilder();
        outputInfo.forEach(line -> messages.append(line).append("\n"));
        print(ANSI_GREEN + messages + ANSI_RESET);
    }

    public static void printUserErrors(ArrayList<String> userErrors) {
        StringBuilder messages = new StringBuilder();
        userErrors.forEach(line -> messages.append(line).append("\n"));
        print(ANSI_RED + messages + ANSI_RESET);
    }
}
