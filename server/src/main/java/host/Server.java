package host;

import commands.SaveCommand;
import data.ClientRequest;
import processing.ClientHandler;
import processing.CommandInvoker;
import processing.DatabaseVersionHandler;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.locks.Lock;

public class Server {
    private final int port;
    private ServerSocket serverSocket;
    private final CommandInvoker invoker;
    private static final ClientRequest SAVE_COMMAND = 
                new ClientRequest(SaveCommand.getName());
    private final Lock lock;
    private final DatabaseVersionHandler databaseVersionHandler;

    public Server(CommandInvoker invoker, int port, Lock lock, DatabaseVersionHandler databaseVersionHandler) {
        this.invoker = invoker;
        this.port = port;
        this.lock = lock;
        this.databaseVersionHandler = databaseVersionHandler;
    }
    
    private void setup() {
        try {
            serverSocket = new ServerSocket(port);
            serverSocket.setReuseAddress(true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void run() {
        setup();
        System.out.println("setup completed");
        try {
            while (true) {
                System.out.println("accept()");
                Socket client = serverSocket.accept();
                System.out.println("New client connected: " + client.getInetAddress());
                databaseVersionHandler.addToSocketList(client);
                
                ClientHandler clientHandler = new ClientHandler(client, invoker, lock);
                Thread clientHandlerThread = new Thread(clientHandler);
                clientHandlerThread.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (serverSocket != null) {
                try {
                    serverSocket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static ClientRequest getSaveCommand() {
        return SAVE_COMMAND;
    }
}