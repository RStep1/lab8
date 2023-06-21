package user;

import commands.ExitCommand;
import data.ClientRequest;
import mods.*;
import processing.ClientRequestBuilder;
import processing.Console;
import utility.MessageHolder;
import utility.ServerAnswer;
import utility.FileHandler;

import java.io.IOException;
import java.util.*;

public class ClientManager {
    private Client client;
    private final Scanner scanner;
    private ClientRequest clientRequest;
    private final Queue<ClientRequest> clientRequestQueue = new LinkedList<>();

    public ClientManager(Scanner scanner) {
        this.scanner = scanner;
    }

    public boolean setConnection(String host, int port) {
        try {
            this.client = new Client(host, port);
        } catch (IOException e) {
            return false;
        }
        return true;
    }

    public boolean processRequestToServer() {
        Console.println("Available commands:");
        Console.println(FileHandler.readFile(FileType.REFERENCE));
        ClientRequestBuilder  clientRequestBuilder = new ClientRequestBuilder(scanner);
        do {
            try {
                if (clientRequestQueue.isEmpty()) { //if all commands have been processed, then we enter new ones
                    clientRequestQueue.addAll(clientRequestBuilder.userEnter());
                    Console.printUserErrors();
                    MessageHolder.clearMessages(MessageType.USER_ERROR);
                }
                if (clientRequestQueue.isEmpty()) {// if input is empty or it has mistakes
                    clientRequest = null;
                    continue;
                }
                clientRequest = clientRequestQueue.peek();
                if (clientRequest.getCommandName().equals(ExitCommand.getName())) {
                    if (clientRequest.getExecuteMode() == ExecuteMode.SCRIPT_MODE) {
                        System.out.println("Command exit:");
                        System.out.println(String.format("Script '%s' successfully completed",
                                clientRequest.getScriptFile().getName()));
                        clientRequest = null;
                        clientRequestQueue.remove();
                        continue;
                    }
                    System.out.println("client exit");
                    break;
                }
                // serverAnswer = client.dataExchange(clientRequest);
                // if (serverAnswer == null) {
                //     Client.stop();
                //     Console.println("Connection lost");
                //     return false;
                // }
                // if (serverAnswer.answerType() == AnswerType.DATA_REQUEST && serverAnswer.commandExitStatus()) {
                //     if (clientRequest.getExecuteMode() == ExecuteMode.COMMAND_MODE) {
                //         String[] extraArguments = Console.insertMode();
                //         clientRequest.setExtraArguments(extraArguments);
                //     }
                //     serverAnswer = client.dataExchange(clientRequest);
                //     if (serverAnswer == null) {
                //         Client.stop();
                //         Console.println("Connection lost");
                //         return false;
                //     }
                // }
                // clientRequestQueue.remove();
                // Console.printOutputInfo(serverAnswer.outputInfo());
                // Console.printUserErrors(serverAnswer.userErrors());
            } catch (NoSuchElementException e) {
                Client.stop();
                return false;
            }
        } while (clientRequest == null || !clientRequest.getCommandName().equals(ExitCommand.getName()));
        
        Client.stop();
        return true;
    }
}
