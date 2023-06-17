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
import java.net.Socket;

import controllers.DatabaseWindowController;
import controllers.LoginWindowController;
import processing.TCPExchanger;

public class Listener implements Runnable {
    private Socket socket;
    private final String host;
    private final int port;
    private final String login;
    private final String password;
    private static BufferedInputStream bufferedInputStream;
    private static BufferedOutputStream bufferedOutputStream;
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
        try {
            this.socket = new Socket(host, port);
            LoginWindowController.getInstance().showScene();
            bufferedInputStream = new BufferedInputStream(new DataInputStream(this.socket.getInputStream()));
            bufferedOutputStream = new BufferedOutputStream(new DataOutputStream(this.socket.getOutputStream()));
        } catch (IOException e) {
            System.out.println("Could not connect to server");

        }
        System.out.println("Connection accepted");
        try {
            while (this.socket.isConnected()) {
                
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
            databaseWindowController.logoutScene();
        }
    }
}
