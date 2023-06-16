/** It represents a server able to establish connections with multiple client, both via socket and RMI.
 * @author Caterina Motti, Andrea Grassi, Marco Gervatini. */
package it.polimi.ingsw.network.server;

import it.polimi.ingsw.controller.serverController;
import it.polimi.ingsw.network.client.ClientHandler;

import java.io.IOException;
import java.net.ServerSocket;
import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {
    private ServerSocket serverSocket;
    private static final serverController SC = new serverController();

    /** It starts the socket server on the given port, that listen for clients to connect.
     * @param port the server's port. */
    public void start(int port) throws IOException {
        serverSocket = new ServerSocket(port);
        while(true){
            new ClientHandler(serverSocket.accept(), SC).start();
        }
    }

    /** It closes the socket server. */
    public void end() throws IOException {
        serverSocket.close();
    }

    /** It starts the server both via socket and RMI. */
    public static void main(String[] args) throws IOException {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.submit(() ->{
            try{
                startRMI();
            } catch (InterruptedException ignored){}
        });
        startSocket();
    }

    /** It starts the server via RMI on port 23451. */
    public static void startRMI() throws InterruptedException{
            try {
                Registry registry = LocateRegistry.createRegistry(23451);
                RMIserverImpl s = new RMIserverImpl(SC);
                Naming.rebind("rmi://192.168.80.190:" + 23451 + "/RMIServer", s);
            } catch (Exception e) {
                System.err.println("Server exception");
                e.printStackTrace();
            }
    }

    /** It starts a server socket on the port 23450. */
    public static void startSocket() throws IOException {
        Server server = new Server();
        server.start(23450);
    }
}



