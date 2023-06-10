/** It implements the RMIconnection interface, in order to be able to establish RMI connection server side.
 * @author Caterina Motti, Andrea Grassi. */
package it.polimi.ingsw.network.server;

import it.polimi.ingsw.controller.connectionType;
import it.polimi.ingsw.controller.gameController;
import it.polimi.ingsw.controller.serverController;
import it.polimi.ingsw.model.Lobby;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.network.messages.GeneralMessage;
import it.polimi.ingsw.network.messages.ReconnectMessage;
import it.polimi.ingsw.network.messages.SetNameMessage;
import org.json.simple.parser.ParseException;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class RMIserverImpl extends UnicastRemoteObject implements RMIconnection {
    serverController SC;
    public RMIserverImpl(serverController SC) throws RemoteException {
        super();
        this.SC = SC;
    }

    /** It gets a nickname from the server via RMI connection.
     * If the nickname is not already taken it is putted in the list of players (in gameController) and in the map of
     * connections (in this class). Otherwise, it replies with false. */
    @Override
    public void RMIsendName(String m, RMIconnection reply) throws RemoteException {
        GeneralMessage mex;
        mex = SetNameMessage.decrypt(m);
        System.out.println(mex);
        if (gameController.allPlayers.containsKey(mex.getUsername())) {
            if (gameController.allPlayers.get(mex.getUsername()).isConnected()) {
                //Nickname already taken
                reply.RMIsendName(new SetNameMessage("Nickname already taken!", false).toString(), null);
            } else {
                //Riconnessione
                gameController.allPlayers.get(mex.getUsername()).setConnected(true);
                serverController.connections.get(mex.getUsername()).changeConnection(false, null, reply);
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
                while (x < serverController.connections.get(mex.getUsername()).getPing()) {
                    x = serverController.connections.get(mex.getUsername()).getPing();
                    try {
                        TimeUnit.SECONDS.sleep(15);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
                System.out.println(mex.getUsername() + " disconnected");
                gameController.allPlayers.get(mex.getUsername()).setConnected(false);
                gameController.unlockQueue();
            });
        }
    }

    @Override
    public void RMIsend(String m) throws RemoteException {
        try {
            SC.getMessage(m);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }
}

