/** Tests for class Game.java.
 * @author Caterina Motti.
 */
package it.polimi.ingsw.test.model;

import it.polimi.ingsw.controller.clientController;
import it.polimi.ingsw.controller.connectionType;
import it.polimi.ingsw.controller.gameController;
import it.polimi.ingsw.controller.serverController;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.Position;
import it.polimi.ingsw.model.Tile.Tile;
import it.polimi.ingsw.network.client.RMIclientImpl;
import it.polimi.ingsw.network.server.RMIconnection;
import org.junit.jupiter.api.Test;

import java.io.PrintWriter;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GameTest {
    Player p1 = new Player("CLR");
    Player p2 = new Player("Jhonny");
    Player p3 = new Player("Mayhem");
    Player p4 = new Player("Fozy");
    List<Player> playerList = new ArrayList<>();
    gameController GC = new gameController();
    PrintWriter out = new PrintWriter(System.out, true);

    @Test
    void Game(){
        playerList.add(p1);
        playerList.add(p2);
        playerList.add(p3);
        playerList.add(p4);
        Game g = new Game(playerList,null);

        //Only the first player of the list of Game g has the first token
        assertTrue(g.getPlayers().get(0).getFirstToken());
        assertFalse(g.getPlayers().get(1).getFirstToken());
        assertFalse(g.getPlayers().get(2).getFirstToken());
        assertFalse(g.getPlayers().get(3).getFirstToken());

        //Each player has a personal goal card and there are no duplicate
        for (int i = 0; i < playerList.size(); i++) {
            for (int j = 0; j < playerList.size(); j++) {
                assertNotEquals(null, playerList.get(i).getPersonalCard());
                if(i != j){
                    assertNotEquals(playerList.get(i).getPersonalCard(), playerList.get(j).getPersonalCard());
                }
            }
        }

        //No duplicate in list of all common goal cards
        for (int i = 0; i < g.getAllCC().size(); i++) {
            for (int j = 0; j < g.getAllCC().size(); j++) {
                if(i != j){
                    assertNotEquals(g.getAllCC().get(i), g.getAllCC().get(j));
                }
            }
        }

        //No duplicate in list of common goal cards for the game
        for (int i = 0; i < g.getCommonCards().size(); i++) {
            for (int j = 0; j < g.getCommonCards().size(); j++) {
                if(i != j){
                    assertNotEquals(g.getCommonCards().get(i), g.getCommonCards().get(j));
                }
            }
        }

        //No duplicate in list of all personal goal cards
        for (int i = 0; i < g.getAllPC().size(); i++) {
            for (int j = 0; j < g.getAllPC().size(); j++) {
                if(i != j){
                    assertNotEquals(g.getAllPC().get(i), g.getAllPC().get(j));
                }
            }
        }
    }

    @Test
    void startGame() throws RemoteException {
        playerList.add(p1);
        playerList.add(p2);
        playerList.add(p3);
        p1.setConnected(true);
        p2.setConnected(true);
        p3.setConnected(false);
        connectionType type = new connectionType(true, out, null);
        serverController.connections.put(p1.getNickname(), type);
        serverController.connections.put(p2.getNickname(), type);
        serverController.connections.put(p3.getNickname(), type);

        Game g = new Game(playerList, GC);
        gameController.allGames.put(g.id, g);
        gameController.allPlayers.put(p1.getNickname(), p1);
        gameController.allPlayers.put(p2.getNickname(), p2);
        gameController.allPlayers.put(p3.getNickname(), p3);

        //Select some position from the board
        List<Position> pos = new ArrayList<>();
        Position pos1 = new Position(3, 0);
        Position pos2 = new Position(3, 1);
        pos.add(pos1);
        pos.add(pos2);
        GC.pickTiles(p1.getNickname(), pos, g.id, 1);
        GC.pickTiles(p2.getNickname(), pos, g.id, 4);

        //Select an order for the picked tiles
        List<Integer> order = new ArrayList<>();
        order.add(1);
        order.add(2);
        GC.selectOrder(p1.getNickname(), order, g.id, 2);
        GC.selectOrder(p2.getNickname(), order, g.id, 2);

        //Select a column
        int col = 2;
        GC.selectColumn(p1.getNickname(), col, g.id, 3);
        GC.selectColumn(p2.getNickname(), col, g.id, 5);

        //Make the shelf of p1 full (except for two places) to make the game end
        for (int i = 0; i < p1.getNumRows(); i++) {
            for (int j = 0; j < p1.getNumCols(); j++) {
                p1.getShelf()[i][j] = new Tile("games",1);
            }
        }
        assertTrue(p1.isShelfFull());
        p1.getShelf()[0][col-1] = new Tile("empty",1);
        p1.getShelf()[1][col-1] = new Tile("empty",1);

        //Make the board empty (except for two positions) to test the refill
        for (int i = 0; i < g.getBoard().getNumRows(); i++) {
            for (int j = 0; j < g.getBoard().getNumCols(); j++) {
                if(!g.getBoard().board[i][j].getCategory().equals(it.polimi.ingsw.model.Tile.type.NOT_ACCESSIBLE)){
                    g.getBoard().board[i][j] = new Tile("empty",1);
                }
            }
        }
        assertTrue(g.getBoard().isBoardEmpty());
        g.getBoard().board[3][0] = new Tile("games",1);
        g.getBoard().board[3][1] = new Tile("games",1);

        try {
            g.startGame();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void checkTiles(){
        Tile t1 = new Tile("games",1);
        Tile t2 = new Tile("games",1);
        Tile t3 = new Tile("plants",1);
        playerList.add(p1);
        Game g = new Game(playerList, GC);

        List<Tile> chosen = new ArrayList<>();
        //Two tiles of the same type
        chosen.add(t1);
        chosen.add(t2);
        assertTrue(g.checkTiles(chosen));

        //Three tiles of the different types
        chosen.add(t3);
        assertFalse(g.checkTiles(chosen));
    }

    @Test
    void calculateCC() throws RemoteException {
        playerList.add(p1);
        Game g = new Game(playerList, GC);

        //the player has zero points
        g.calculateCC(p1);
        assertEquals(0, p1.getTotalPoints());
        assertEquals(0, p1.getScoreToken1());
        assertEquals(0, p1.getScoreToken2());
    }

    @Test
    void calculateWinner() {
        playerList.add(p1);
        playerList.add(p2);
        playerList.add(p3);
        playerList.add(p4);
        Game g = new Game(playerList,GC);

        //Every player has totalPoints = 0, then the last player to play is the winner
        assertEquals(g.getPlayers().get(g.getPlayers().size() - 1), g.calculateWinner());

        //Adding some points to the players, the winner is the player that has more points in total
        p1.addPoints(30);
        p2.addPoints(30);
        p3.addPoints(40);
        p4.addPoints(35);
        assertEquals(p3, g.calculateWinner());

        //In case of tie, the winner is the one sitting further from the first player
        //In this case p2 and p3 have 40 points
        p2.addPoints(10);
        if (g.getPlayers().indexOf(p2) > g.getPlayers().indexOf(p3)) {
            assertEquals(p2, g.calculateWinner());
        } else {
            assertEquals(p3, g.calculateWinner());
        }
    }

    @Test
    void reconnectPlayer() throws RemoteException {
        RMIconnection reply = new RMIclientImpl(new clientController(p1.getNickname()));
        connectionType typeRMI = new connectionType(false, null, reply);
        connectionType type = new connectionType(true, out, null);
        serverController.connections.put(p1.getNickname(), typeRMI);
        serverController.connections.put(p2.getNickname(), type);
        serverController.connections.put(p3.getNickname(), type);
        p1.setConnected(true);
        p2.setConnected(true);
        p3.setConnected(false);
        playerList.add(p1);
        playerList.add(p2);
        playerList.add(p3);
        Game g = new Game(playerList, GC);
        gameController.allGames.put(g.id, g);
        gameController.allPlayers.put(p1.getNickname(), p1);
        gameController.allPlayers.put(p2.getNickname(), p2);
        gameController.allPlayers.put(p3.getNickname(), p3);

        typeRMI.changeConnection(true, out, null);
        g.reconnectPlayer(p3.getNickname());
    }
}