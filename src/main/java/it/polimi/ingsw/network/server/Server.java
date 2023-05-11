/** It represents a server able to establish connections with multiple client, both via socket and RMI. */
package it.polimi.ingsw.network.server;

import it.polimi.ingsw.controller.gameController;
import it.polimi.ingsw.controller.serverController;
import it.polimi.ingsw.exceptions.InvalidActionException;
import it.polimi.ingsw.exceptions.InvalidKeyException;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.network.messages.SetNameMessage;
import org.json.simple.parser.ParseException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
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
            new clientHandler(serverSocket.accept()).start();
        }
    }

    /** It closes the socket server. */
    public void end() throws IOException {
        serverSocket.close();
    }

    /** It manages multiple client connections via socket. */
    private static class clientHandler extends Thread{
        private final Socket clientSocket;
        private PrintWriter out;
        private BufferedReader in;

        /** Constructor that specify the client for the entire class. */
        public clientHandler(Socket socket){
            this.clientSocket = socket;
        }

        /** It overrides the run() method to manage multiple thread, each one rearguing a single socket connection. */
        @Override
        public void run() {
            SetNameMessage nickname=null;
            try {
                //Creates the socket between client and sever via input/output
                out = new PrintWriter(clientSocket.getOutputStream(), true);
                in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                //clientSocket.setKeepAlive(true);
                String input;
                //Firstly it ask for a nickname until the given one is available
                //In this way each client is represented by a unique nickname
                out.println("\u001b[34mWelcome to MyShelfie!\u001b[0m");
                nickname = SetNameMessage.decrypt(in.readLine());
                while(gameController.allPlayers.containsKey(nickname.getUsername())){
                    out.println(new SetNameMessage("",false ));
                    nickname = SetNameMessage.decrypt(in.readLine());
                }
                //If the nickname is not already taken, it is added to the static hashmap allPlayers, and the
                //associated player object is created
                gameController.allPlayers.put(nickname.getUsername(),new Player(nickname.getUsername()));
                out.println(new SetNameMessage(nickname.getUsername(),true ));
                serverController.connections.put(nickname.getUsername(), new connectionType(true, out, null));
                //Infine loop that enable the reception of messages from the client
                //GeneralMessage mex = null;
                while((input = in.readLine()) != null){
                    SC.getMessage(input);
                }
            } catch (IOException e) {
                if(nickname != null) {
                    System.out.println(nickname.getUsername() + " has disconnected");
                    if(gameController.allPlayers.containsKey(nickname.getUsername())){
                        gameController.allPlayers.get(nickname.getUsername()).setConnected(false);
                    }
                }else{
                    System.out.println("Client has disconnected!");
                }
            } catch (ParseException | InvalidKeyException | InvalidActionException ignored ){
            } finally {
                try {
                    //If the client disconnect the socket connection is closed
                    in.close();
                    out.close();
                    clientSocket.close();
                } catch (IOException ignored) {}
            }
        }
    }

    /** It starts the server both via socket and RMI. */
    public static void main(String[] args) throws IOException {
        //TODO CRASH SERVER, SALVATAGGIO E RIPRESA PARTITA
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

                while(true) {
                    Naming.rebind("rmi://localhost:" + 23451 + "/RMIServer", s);
                    RMIconnection skeleton = (RMIconnection) Naming.lookup("rmi://localhost:" + 23451 + "/RMIServer");
                }
            } catch (Exception e) {
                System.err.println("Server exception");
                e.printStackTrace();
            }

    }

    /** It starts a server socket on the port 23450. */
    public static void startSocket() throws IOException {
        Server server = new Server();
        server.start(23450);
        System.out.println("Socket server is ready!"); //Sarebbe carino che funzionasse :D
    }


}



