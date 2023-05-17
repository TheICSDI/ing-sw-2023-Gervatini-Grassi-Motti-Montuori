/** It is used by the server controller to modify the games/lobbies created.
 * It is used by the started game to get the useful information.
 * @author Marco Gervatini. */
package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.Lobby;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.Position;
import it.polimi.ingsw.model.Tile.Tile;
import it.polimi.ingsw.network.messages.Action;
import it.polimi.ingsw.network.messages.UpdateBoardMessage;

import java.rmi.RemoteException;
import java.util.*;
import java.util.stream.Collectors;

public class gameController {
    //It contains all connected players
    public static Map<String, Player> allPlayers = new HashMap<>();
    //It contains all started games
    public static Map<Integer, Game> allGames = new HashMap<>();
    //It contains all the lobbies
    public static List<Lobby> allLobbies=new ArrayList<>();
    //It contains all pending command
    public static List<command> queue = new ArrayList<>();
    private final Object queueLock = new Object();

    /** It returns the chosen number of column of the given player in the game. */
    public int chooseColumn(String player, int gameId) throws InterruptedException {
        Optional<command> order;
        order = findTheRequest(player,gameId,Action.SC);
        return order.get().getNumCol();
    }

    /** It returns the chosen order of tiles of the given player in the game. */
    public List<Integer> chooseOrder(String player, int gameId) throws InterruptedException {
        Optional<command> order;
        order = findTheRequest(player,gameId,Action.SO);
        return order.get().getOrder();
    }

    /** It returns the chosen position (regarding the tiles in the board) of the given player in the game. */
    public  Set<Position> chooseTiles(String player , int gameId) throws InterruptedException {
        Optional<command> order;
        order = findTheRequest(player,gameId,Action.PT);
        return new HashSet<>(order.get().getPos());
    }

    /** It finds the request of the given player in the queue of commands. */
    private Optional<command> findTheRequest(String player, int gameId, Action action) throws InterruptedException {
        Player p = allPlayers.get(player);
        List<command> toFind;
        Optional<command> found = Optional.empty();

        synchronized (queueLock) {
            while(found.isEmpty() && p.isConnected()){
                System.out.println("locked queue, finding request " + action);
                toFind = queue.stream()
                        .filter(ob -> ob.g.id == gameId)
                        .filter(ob -> ob.p.getNickname().equals(player))
                        .filter(ob -> ob.a.equals(action))
                        .collect(Collectors.toList());
                if (!toFind.isEmpty()) {
                    //If there are more than one pending command for the same action, the more recent is taken
                    //and the others are cancelled
                    queue.removeAll(toFind);
                    found = toFind.stream()
                            .reduce((ob1, ob2) -> ob1.numMess > ob2.numMess ? ob1 : ob2);
                } else {
                    try {
                        queueLock.wait();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return found;
    }

    /** It adds the command "pick tiles" to the queue of pending order. */
    public void pickTiles(String player, Action action, List<Position> pos, int gameId, int numMess){
        Player p = allPlayers.get(player);
        Game g = allGames.get(gameId);
        command pending = new command(g,p,action,numMess);
        pending.setPos(pos);
        synchronized (queueLock) {
            System.out.println("Adding pick to the request");
            queue.add(pending);
            queueLock.notifyAll();
        }
    }

    /** It adds the command "select order of tiles" to the queue of pending order. */
    public  void selectOrder(String player, Action action, List<Integer> order, int gameId, int numMess){
        Player p = allPlayers.get(player);
        Game g = allGames.get(gameId);
        command pending = new command(g,p,action,numMess);
        pending.setOrder(order);
        synchronized (queueLock) {
            System.out.println("Adding order selection");
            queue.add(pending);
            queueLock.notifyAll();
        }
    }

    /** It adds the command "select column" to the queue of pending order. */
    public  void selectColumn(String player, Action action, int numCol, int gameId, int numMess){
        Player p = allPlayers.get(player);
        Game g = allGames.get(gameId);
        command pending = new command(g,p,action,numMess);
        pending.setNumCol(numCol);
        synchronized (queueLock) {
            System.out.println("Adding column selection");
            queue.add(pending);
            queueLock.notifyAll();
        }
    }

    /*permette di uscire da una partita a patto che non sia cominciata
    TODO: integrare uscita da una lobby , non so se questa funzione va qui non sembra in linea con le altre
    public void leaveLobby(String player, int gameId) throws GameStartedException {
        Game g = allGames.get(gameId);
        Player p = allPlayers.get(player);
        if(g.isStarted()){
            g.getPlayers().remove(p);
        }
        else{
            throw new GameStartedException("The game is started you can't leave now!");
        }
    } */

    public void sendElement(Tile[][] element, List<Player> playersToSendTo, Action action) throws RemoteException {
        Tile[][] information = new Tile[element.length][element[0].length];
        for(int i = 0; i< element.length;i++ ){
            for(int j = 0; j<element[0].length;j++){
                information[i][j] = new Tile(element[i][j].getCategory().toString());
            }
        }
        for (Player p: playersToSendTo) {
            serverController.sendMessage(new UpdateBoardMessage(action, information), p.getNickname());
        }
    }

}
