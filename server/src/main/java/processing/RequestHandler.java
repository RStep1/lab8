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
        
        

        if (this.user == null && 
            !(clientRequest.getCommandName().equals(LoginCommand.getName()) || 
            clientRequest.getCommandName().equals(RegisterCommand.getName()) || 
            clientRequest.getCommandName().equals(HelpCommand.getName()) ||
            clientRequest.getCommandName().equals(SaveCommand.getName()))) {
                MessageHolder.putMessage("You need to be logged in to run commands", MessageType.USER_ERROR);
            return new ServerAnswer(MessageHolder.getOutputInfo(), MessageHolder.getUserErrors(),
                         false, null);
        }

        boolean exitStatus = invoker.execute(clientRequest);

        if (this.user != null && clientRequest.getCommandName().equals(QuitCommand.getName())) {
            this.user = null;
        }
        if (clientRequest.getCommandName().equals(LoginCommand.getName()) && exitStatus) {
            this.user = clientRequest.getUser();
        }

        ArrayList<String> outputInfo = MessageHolder.getOutputInfo();
        ArrayList<String> userErrors = MessageHolder.getUserErrors();

        return new ServerAnswer(outputInfo, userErrors, exitStatus); 
    }

    public static boolean isChangingCommand(ClientRequest clientRequest) {
        return clientRequest.getCommandName().equals(UpdateCommand.getName()) ||
        clientRequest.getCommandName().equals(InsertCommand.getName());
    }

    private boolean isCommandMode(ClientRequest clientRequest) {
        return clientRequest.getExecuteMode() == ExecuteMode.COMMAND_MODE;
    }

    private boolean isExtraArgsNull(ClientRequest clientRequest) {
        return clientRequest.getExtraArguments() == null;
    }
}
