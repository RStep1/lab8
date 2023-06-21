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
        if (socket != null) {
            System.out.println("Socket already connected");
            return;
        }
        socket = new Socket(this.host, this.port);
        bufferedInputStream = new BufferedInputStream(new DataInputStream(this.socket.getInputStream()));
        bufferedOutputStream = new BufferedOutputStream(new DataOutputStream(this.socket.getOutputStream()));
        System.out.println("Connection accepted");
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
        LoginWindowController.getInstance().showScene();
        try {
            while (this.socket.isConnected()) {
                ServerAnswer serverAnswer = null;
                serverAnswer = (ServerAnswer) TCPExchanger.read(bufferedInputStream);
                System.out.println(serverAnswer);
                switch (serverAnswer.eventType()) {
                    case DATABASE_INIT -> {
                        // databaseWindowController.initCollection()
                        System.out.println("init");}
                    case INSERT -> System.out.println("insert");
                    case UPDATE -> System.out.println("update");
                    case CLEAR -> System.out.println("clear");
                    case REMOVE -> System.out.println("remove");
                    case QUIT -> System.out.println("quit");
                    case LOGIN -> System.out.println("login");
                    case REGISTER -> System.out.println("register");
                }
            }
        } catch (IOException | NullPointerException e) {
            // e.printStackTrace();
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
            // e.printStackTrace();
        }
    }
}
