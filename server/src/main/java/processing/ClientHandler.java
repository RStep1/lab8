package processing;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import commands.QuitCommand;
import data.ClientRequest;
import utility.ServerAnswer;

public class ClientHandler implements Runnable {
    private Socket client;
    private CommandInvoker invoker;
    private Lock lock;

    public ClientHandler(Socket client, CommandInvoker invoker, Lock lock) {
        this.client = client;
        this.invoker = invoker;
        this.lock = lock;
    }

    @Override
    public void run() {
        try (BufferedInputStream bufferedInputStream = new BufferedInputStream(new DataInputStream(client.getInputStream()));
            BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(new DataOutputStream(client.getOutputStream()));
            ) {
            ServerAnswer serverAnswer = null;
            ClientRequest clientRequest = null;
            RequestHandler requestHandler = new RequestHandler(invoker);
            while (true) { 
                clientRequest = (ClientRequest) TCPExchanger.read(bufferedInputStream);
                if (clientRequest.getCommandName().equals(QuitCommand.getName()))
                    throw new IOException();
                serverAnswer = requestHandler.processRequest(clientRequest);

                //Lock (same)
                lock.lock();
                TCPExchanger.write(bufferedOutputStream, serverAnswer);
                bufferedOutputStream.flush();
                lock.unlock();
                System.out.println("WRITE ANSWER: ");
                System.out.println(serverAnswer.eventType() + "");

            }
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Client disconnection");
        } finally {
            try {
                client.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.out.println("Client " + client + " exit");
        }
    }
}

