package it.polimi.ingsw.network.client;

import it.polimi.ingsw.controller.connectionType;
import it.polimi.ingsw.controller.gameController;
import it.polimi.ingsw.controller.serverController;
import it.polimi.ingsw.model.Lobby;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.network.messages.ReconnectMessage;
import it.polimi.ingsw.network.messages.SetNameMessage;
import org.json.simple.parser.ParseException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/** It manages multiple client connections via socket.
 * @author Caterina Motti, Andrea Grassi. */
public class ClientHandler extends Thread{
    private final Socket clientSocket;
    private PrintWriter out;
    private BufferedReader in;
    private final serverController SC;
    private static final String RESET = "\u001B[0m";
    private final static String RED = "\u001B[31m";

    /** Constructor that specify the client for the entire class.
     * @param socket the client's socket.
     * @param controller the server's controller. */
    public ClientHandler(Socket socket, serverController controller){
        this.clientSocket = socket;
        this.SC = controller;
    }

    /** It overrides the run() method to manage multiple thread, each one rearguing a single socket connection. */
    @Override
    public void run() {
        SetNameMessage nickname = null;
        try {
            //Creates the socket between client and sever via input/output
            out = new PrintWriter(clientSocket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

            //Firstly it ask for a nickname until the given one is available
            //In this way each client is represented by a unique nickname
            out.println("\u001b[34mWelcome to MyShelfie!\u001b[0m");
            String name = in.readLine().replaceAll(" ", "");
            nickname = SetNameMessage.decrypt(name);
            while(gameController.allPlayers.containsKey(nickname.getUsername()) && gameController.allPlayers.get(nickname.getUsername()).isConnected()){
                out.println(new SetNameMessage("",false));
                name = in.readLine().replaceAll(" ", "");
                nickname = SetNameMessage.decrypt(name);
            }
            if(gameController.allPlayers.containsKey(nickname.getUsername()) && !gameController.allPlayers.get(nickname.getUsername()).isConnected()){
                gameController.allPlayers.get(nickname.getUsername()).setConnected(true);
                serverController.connections.get(nickname.getUsername()).changeConnection(true, out,null);
                int lobbyId = -1;
                for (Lobby L: gameController.allLobbies) {
                    if(L.isPlayerInLobby(gameController.allPlayers.get(nickname.getUsername()))){
                        lobbyId = L.lobbyId;
                    }
                }

                int gameId = -1;
                for (int i: gameController.allGames.keySet()) {
                    if(gameController.allGames.get(i).getPlayers().contains(gameController.allPlayers.get(nickname.getUsername()))){
                        gameId = i;
                    }
                }
                out.println(new ReconnectMessage(lobbyId,gameId,nickname.getUsername()));
                if(gameId>0){
                    gameController.allGames.get(gameId).reconnectPlayer(nickname.getUsername());
                }

            } else {
                //If the nickname is not already taken, it is added to the static hashmap allPlayers and connections
                gameController.allPlayers.put(nickname.getUsername(), new Player(nickname.getUsername()));
                out.println(new SetNameMessage(nickname.getUsername(), true));
                serverController.connections.put(nickname.getUsername(), new connectionType(true, out, null));
            }
            ExecutorService executor = Executors.newSingleThreadExecutor();
            String finalNickname = nickname.getUsername();
            executor.submit(() -> {
                int x = -1;
                while (serverController.connections.get(finalNickname).getPing()) {
                    serverController.connections.get(finalNickname).setPing(false);
                    try {
                        Thread.sleep(Client.pingTime*2000);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
                System.out.println(RED + finalNickname + " disconnected" + RESET);
                gameController.allPlayers.get(finalNickname).setConnected(false);
                gameController.unlockQueue();
            });
            //Loop that enable the reception of messages from the client
            String input;
            while((input = in.readLine()) != null){
                SC.getMessage(input);
            }

        } catch (IOException e) {
            //If the client disconnects it is notified to the server
            if(nickname != null) {
                System.out.println(RED + nickname.getUsername() + " has disconnected!" + RESET);
                if(gameController.allPlayers.containsKey(nickname.getUsername())){
                    gameController.allPlayers.get(nickname.getUsername()).setConnected(false);
                    gameController.unlockQueue();
                }
            } else {
                System.out.println(RED + "Client has disconnected!" + RESET);
            }
        } catch (ParseException ignored ){
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