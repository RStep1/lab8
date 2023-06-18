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



import controllers.DatabaseWindowController;
import controllers.LoginWindowController;
import data.CommandArguments;
import data.User;
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
    }

    @Override
    public void run() {
        LoginWindowController.getInstance().showScene();
        try {
            while (this.socket.isConnected()) {
                ServerAnswer serverAnswer = null;
                serverAnswer = (ServerAnswer) TCPExchanger.read(bufferedInputStream);
                System.out.println(serverAnswer);
            }
        } catch (IOException | NullPointerException e) {
            // e.printStackTrace();
            databaseWindowController.logoutScene();
        }
    }

    public static void sendRequest(CommandArguments commandArguments) throws IOException {
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
