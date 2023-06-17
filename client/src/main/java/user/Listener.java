package user;

import controllers.DatabaseWindowController;

public class Listener implements Runnable {

    private final String host;
    private final int port;
    private final String login;
    private final String password;
    private final DatabaseWindowController databaseWindowController;

    public Listener(String host, int port, String login, String password, DatabaseWindowController databaseWindowController) {
        this.host = host;
        this.port = port;
        this.login = login;
        this.password = password;
        this.databaseWindowController = databaseWindowController;
    }

    @Override
    public void run() {
        
    }
    
}
