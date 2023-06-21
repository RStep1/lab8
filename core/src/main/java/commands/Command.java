package commands;

import data.ClientRequest;
import utility.ServerAnswer;

/**
 * Interface for all commands.
 * Execute method passes the command arguments and the mode in which the command is executed.
 */
public interface Command {
    ServerAnswer execute(ClientRequest commandArguments);
}
