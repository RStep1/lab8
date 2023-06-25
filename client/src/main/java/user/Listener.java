package user;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.net.Socket;

import commands.ShowCommand;
import controllers.DatabaseWindowController;
import controllers.LoginWindowController;
import data.ClientRequest;
import data.User;
import mods.RemoveMode;
import processing.TCPExchanger;
import utility.ServerAnswer;

public class Listener implements Runnable {
    private Socket socket;
    private final String host;
    private final int port;
    private User user;
    private static BufferedInputStream bufferedInputStream;
    private static BufferedOutputStream bufferedOutputStream;
    private DatabaseWindowController databaseWindowController; 

    public Listener(String host, int port) {
        this.host = host;
        this.port = port;
    }
    
    public void setConnection() throws IOException {
        if (isConnected()) {
            System.out.println("Socket already connected");
            return;
        }
        socket = new Socket(this.host, this.port);
        bufferedInputStream = new BufferedInputStream(new DataInputStream(this.socket.getInputStream()));
        bufferedOutputStream = new BufferedOutputStream(new DataOutputStream(this.socket.getOutputStream()));
        System.out.println("Connection accepted");
    }

    public boolean isConnected() {
        return socket != null;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setDatabaseController(DatabaseWindowController databaseWindowController) {
        this.databaseWindowController = databaseWindowController;
        this.databaseWindowController.setUsernameLabel(this.user.getLogin());
    }

    @Override
    public void run() {
        try {
            while (this.socket.isConnected()) {
                ServerAnswer serverAnswer = null;
                serverAnswer = (ServerAnswer) TCPExchanger.read(bufferedInputStream);
                switch (serverAnswer.eventType()) {
                    case DATABASE_INIT -> databaseWindowController.initializeTableEvent(serverAnswer);
                    case DATABASE_UPDATE -> databaseWindowController.updateTableViewEvent(serverAnswer);
                    case INSERT -> databaseWindowController.insertEvnet(serverAnswer);
                    case UPDATE -> databaseWindowController.updateEvent(serverAnswer);
                    case CLEAR -> databaseWindowController.clearEvent(serverAnswer);
                    case REMOVE -> System.out.println("remove");
                    case QUIT -> System.out.println("quit");
                    case LOGIN -> LoginWindowController.getInstance().loginEvent(serverAnswer);
                    case REGISTER -> LoginWindowController.getInstance().registerEvent(serverAnswer);
                    case INFO -> {
                        System.out.println("some info");}
                }
            }
        } catch (IOException | NullPointerException e) {
            e.printStackTrace();
            if (databaseWindowController != null)
                databaseWindowController.logoutScene();
        }
    }

    public static void sendRequest(ClientRequest commandArguments) throws IOException {
        TCPExchanger.write(bufferedOutputStream, commandArguments);
    }

    public static ServerAnswer readServerAnswer() throws IOException {
        return (ServerAnswer) TCPExchanger.read(bufferedInputStream);
    }

    public void stop() {
        try {
            socket.close();
            bufferedInputStream.close();
            bufferedOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
