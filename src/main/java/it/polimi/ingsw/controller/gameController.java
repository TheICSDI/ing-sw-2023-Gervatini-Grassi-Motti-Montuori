/** It is used by the server controller to modify the games/lobbies created.
 * It is used by the started game to get the useful information.
 * @author Marco Gervatini, Caterina Motti, Andrea Grassi. */
package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.Lobby;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.Position;
import it.polimi.ingsw.network.messages.Action;

import java.util.*;
import java.util.stream.Collectors;

public class gameController {
    /** Map that contains all connected players, the nickname is the key. */
    public static Map<String, Player> allPlayers = new HashMap<>();
    /** Map that contains all started games, the id is the key*/
    public static Map<Integer, Game> allGames = new HashMap<>();
    /** List that contains all the created lobbies.*/
    public static List<Lobby> allLobbies = new ArrayList<>();
    /** List that contains all pending commands.*/
    public static List<command> queue = new ArrayList<>();
    public final static Object queueLock = new Object();
    private final static String RED = "\u001B[31m";

    /** It returns the chosen number of column of the given player in the game.
     * @param player nickname of the player that is choosing the column.
     * @param gameId id of the game in which the player is playing.
     * @return chosen column (-2 if not found). */
    public int chooseColumn(String player, int gameId) {
        Optional<command> order = findTheRequest(player, gameId, Action.SC);
        return order.get().getNumCol();
    }

    /** It returns the chosen order of tiles of the given player in the game.
     * @param player nickname of the player that is choosing the order.
     * @param gameId id of the game in which the player is playing.
     * @return list of chosen tiles (null if not found). */
    public List<Integer> chooseOrder(String player, int gameId) {
        Optional<command> order = findTheRequest(player, gameId, Action.SO);
        return order.get().getOrder();
    }

    /** It returns the chosen position (regarding the tiles in the board) of the given player in the game.
     * @param player nickname of the player that is choosing the tiles.
     * @param gameId id of the game in which the player is playing.
     * @return list of positions (null if not found).*/
    public List<Position> chooseTiles(String player , int gameId) {
        Optional<command> order = findTheRequest(player,gameId,Action.PT);
        return order.get().getPos();
    }

    /** It finds the request of the given player in the queue of commands.
     * @param player nickname of the player.
     * @param gameId id of the game in which the player is playing.
     * @param action action to be found.
     * @return the requested command. */
    private Optional<command> findTheRequest(String player, int gameId, Action action) {
        Player p = allPlayers.get(player);
        List<command> toFind;
        Optional<command> found = Optional.empty();
        synchronized (queueLock) {
            while(found.isEmpty() && p.isConnected()){
                System.out.println(RED + "Locked queue, finding request " + action);
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

    /** It adds the command "pick tiles" to the queue of pending order.
     * @param player nickname of the player.
     * @param pos list of positions of the picked tiles.
     * @param gameId id of the game in which the player is playing.
     * @param numMess number of the message. */
    public void pickTiles(String player, List<Position> pos, int gameId, int numMess){
        Player p = allPlayers.get(player);
        Game g = allGames.get(gameId);
        command pending = new command(g, p, Action.PT, numMess);
        pending.setPos(pos);
        synchronized (queueLock) {
            queue.add(pending);
            queueLock.notifyAll();
        }
    }

    /** It adds the command "select order of tiles" to the queue of pending order.
     * @param player nickname of the player.
     * @param order chosen order of the tiles.
     * @param gameId id of the game in which the player is playing.
     * @param numMess number of the message. */
    public void selectOrder(String player, List<Integer> order, int gameId, int numMess){
        Player p = allPlayers.get(player);
        Game g = allGames.get(gameId);
        command pending = new command(g, p, Action.SO, numMess);
        pending.setOrder(order);
        synchronized (queueLock) {
            queue.add(pending);
            queueLock.notifyAll();
        }
    }

    /** It adds the command "select column" to the queue of pending order.
     * @param player nickname of the player.
     * @param numCol selected column.
     * @param gameId id of the game in which the player is playing.
     * @param numMess number of the message. */
    public  void selectColumn(String player, int numCol, int gameId, int numMess){
        Player p = allPlayers.get(player);
        Game g = allGames.get(gameId);
        command pending = new command(g, p, Action.SC,numMess);
        pending.setNumCol(numCol);
        synchronized (queueLock) {
            queue.add(pending);
            queueLock.notifyAll();
        }
    }

    /** It releases the lock on queue. It is used when the player disconnects on his turn.*/
    public static void unlockQueue(){
        synchronized (queueLock) {
            queueLock.notifyAll();
        }
    }
}
