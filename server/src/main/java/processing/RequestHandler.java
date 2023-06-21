package processing;

import commands.HelpCommand;
import commands.InsertCommand;
import commands.LoginCommand;
import commands.QuitCommand;
import commands.RegisterCommand;
import commands.SaveCommand;
import commands.UpdateCommand;
import data.ClientRequest;
import data.User;
import mods.EventType;
import mods.ExecuteMode;
import mods.MessageType;
import utility.MessageHolder;
import utility.ServerAnswer;

import java.util.ArrayList;

public class RequestHandler {
    private CommandInvoker invoker;
    private User user;

    public RequestHandler(CommandInvoker invoker) {
        this.invoker = invoker;
    }

    public ServerAnswer processRequest(ClientRequest clientRequest) {
        MessageHolder.clearMessages(MessageType.OUTPUT_INFO);
        MessageHolder.clearMessages(MessageType.USER_ERROR);
        System.out.println(this.user);
    
        ServerAnswer serverAnswer = invoker.execute(clientRequest);

        if (this.user != null && clientRequest.getCommandName().equals(QuitCommand.getName())) {
            this.user = null;
        }
        if (clientRequest.getCommandName().equals(LoginCommand.getName()) && serverAnswer.commandExitStatus()) {
            this.user = clientRequest.getUser();
        }
        serverAnswer.setMessages(MessageHolder.getOutputInfo(), MessageHolder.getUserErrors());
        serverAnswer.setUser(clientRequest.getUser());
        return serverAnswer;
    }
}
