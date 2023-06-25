package processing;

import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.Lock;

import data.Vehicle;
import mods.EventType;
import utility.ServerAnswer;

public class DatabaseVersionHandler {
    private volatile ArrayList<Socket> socketList = new ArrayList<>();
    private Lock lock;
    private ConcurrentHashMap<Long, Vehicle> database;

    public DatabaseVersionHandler(Lock lock) {
        this.lock = lock;
    }

    public void updateVersion() {
        lock.lock();
        for (Socket socket : socketList) {
            if (socket.isClosed())
                continue;
            try {
                Hashtable<Long, Vehicle> hashtable = new Hashtable<>();
                hashtable.putAll(database);
                TCPExchanger.write(socket.getOutputStream(), new ServerAnswer(EventType.DATABASE_UPDATE, true, hashtable));
            } catch (IOException e) {
                // e.printStackTrace();
                Console.println("socket is closed " + socket);
            }
        }
        Iterator<Socket> socketIterator = socketList.iterator();
        while(socketIterator.hasNext()) {
            Socket socket = socketIterator.next();
            if (socket.isClosed()) {
                socketList.remove(socket);
                // System.out.println("removing closed socket");
            }
        }
        lock.unlock();
    }

    public void loadDatabase(ConcurrentHashMap<Long, Vehicle> database) {
        this.database = database;
    }

    public synchronized void addToSocketList(Socket socket) {
        socketList.add(socket);
    }
}
