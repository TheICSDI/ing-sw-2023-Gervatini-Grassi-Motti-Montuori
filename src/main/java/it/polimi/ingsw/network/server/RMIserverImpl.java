package it.polimi.ingsw.network.server;

import it.polimi.ingsw.controller.connectionType;
import it.polimi.ingsw.controller.gameController;
import it.polimi.ingsw.controller.serverController;
import it.polimi.ingsw.model.Lobby;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.network.client.Client;
import it.polimi.ingsw.network.messages.GeneralMessage;
import it.polimi.ingsw.network.messages.ReconnectMessage;
import it.polimi.ingsw.network.messages.SetNameMessage;
import org.json.simple.parser.ParseException;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/** It implements the RMIconnection interface, in order to be able to establish RMI connection server side.
 * @author Caterina Motti, Andrea Grassi. */
public class RMIserverImpl extends UnicastRemoteObject implements RMIconnection {
    private static final String RESET = "\u001B[0m";
    private final static String RED = "\u001B[31m";
    serverController SC;

    /** Creates a RMIserverImpl given a server controller by parameter. */
    public RMIserverImpl(serverController SC) throws RemoteException {
        super();
        this.SC = SC;
    }

    /** It gets a nickname from the client via RMI connection.
     * If the nickname is not already taken it is putted in the list of players (in gameController) and in the map of
     * connections (in this class). Otherwise, it replies with false.
     * @param m message from the client (JSON format).
     * @param reply way to talk with the client. */
    @Override
    public void RMIsendName(String m, RMIconnection reply) throws RemoteException {
        GeneralMessage mex;
        mex = SetNameMessage.decrypt(m);
        if (gameController.allPlayers.containsKey(mex.getUsername())) {
            if (gameController.allPlayers.get(mex.getUsername()).isConnected()) {
                //Nickname already taken
                reply.RMIsendName(new SetNameMessage("Nickname already taken!", false).toString(), null);
            } else {
                //Reconnection
                gameController.allPlayers.get(mex.getUsername()).setConnected(true);
                serverController.connections.get(mex.getUsername()).changeConnection(false, null, reply);
                ExecutorService executor = Executors.newSingleThreadExecutor();
                executor.submit(() -> {
                    int x = -1;
                    while (serverController.connections.get(mex.getUsername()).getPing()) {
                        serverController.connections.get(mex.getUsername()).setPing(false);
                        try {
                            Thread.sleep(Client.pingTime*2000);
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    }
                    System.out.println(RED + mex.getUsername() + " disconnected" + RESET);
                    gameController.allPlayers.get(mex.getUsername()).setConnected(false);
                    gameController.unlockQueue();
                });
                int lobbyId = -1;
                for (Lobby L :
                        gameController.allLobbies) {
                    if (L.isPlayerInLobby(gameController.allPlayers.get(mex.getUsername()))) {
                        lobbyId = L.lobbyId;
                    }
                }

                int gameId = -1;
                for (int i :
                        gameController.allGames.keySet()) {
                    if (gameController.allGames.get(i).getPlayers().contains(gameController.allPlayers.get(mex.getUsername()))) {
                        gameId = i;
                    }
                }
                reply.RMIsendName(new ReconnectMessage(lobbyId, gameId, mex.getUsername()).toString(), null);
                if (gameId > 0) {
                    gameController.allGames.get(gameId).reconnectPlayer(mex.getUsername());
                }
            }
        } else {
            //Nickname available
            reply.RMIsendName(new SetNameMessage(mex.getUsername(), true).toString(), null);
            gameController.allPlayers.put(mex.getUsername(), new Player(mex.getUsername()));
            serverController.connections.put(mex.getUsername(), new connectionType(false, null, reply));
            ExecutorService executor = Executors.newSingleThreadExecutor();
            executor.submit(() -> {
                int x = -1;
                while (serverController.connections.get(mex.getUsername()).getPing()) {
                    serverController.connections.get(mex.getUsername()).setPing(false);
                    try {
                        Thread.sleep(Client.pingTime*2000);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
                System.out.println(RED + mex.getUsername() + " disconnected" + RESET);
                gameController.allPlayers.get(mex.getUsername()).setConnected(false);
                gameController.unlockQueue();
            });
        }
    }

    /**
     * It gets a message from the client and sends it to the server, that execute the command.
     * @param m message (JSON format).
     */
    @Override
    public void RMIsend(String m) throws RemoteException {
        try {
            SC.getMessage(m);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }
}

