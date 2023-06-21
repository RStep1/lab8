package processing;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

import commands.QuitCommand;
import data.ClientRequest;
import utility.ServerAnswer;

public class ClientHandler implements Runnable {
    private Socket client;
    private CommandInvoker invoker;

    public ClientHandler(Socket client, CommandInvoker invoker) {
        this.client = client;
        this.invoker = invoker;
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
                TCPExchanger.write(bufferedOutputStream, serverAnswer);
                System.out.println("WRITE ANSWER: ");
                System.out.println(serverAnswer.eventType() + "");
                bufferedOutputStream.flush();
            }
        } catch (IOException e) {
            // e.printStackTrace();
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

