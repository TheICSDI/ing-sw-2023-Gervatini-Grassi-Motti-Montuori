/** Tests for class serverController.java.
 * @author Caterina Motti.
 */
package it.polimi.ingsw.test.controller;

import it.polimi.ingsw.controller.connectionType;
import it.polimi.ingsw.controller.gameController;
import it.polimi.ingsw.controller.serverController;
import it.polimi.ingsw.exceptions.InvalidActionException;
import it.polimi.ingsw.exceptions.InvalidKeyException;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.Position;
import it.polimi.ingsw.network.messages.*;
import javafx.geometry.Pos;
import org.json.simple.parser.ParseException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.io.PrintWriter;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

class serverControllerTest {
    Player p1 = new Player("Mario");
    Player p2 = new Player("Luigi");
    Player p3 = new Player("Peach");
    Player p4 = new Player("Toad");
    Player p5 = new Player("Wario");
    Game g;
    gameController GC = new gameController();
    serverController SC = new serverController();
    PrintWriter out = new PrintWriter(System.out, true);

    void setPlayer(){
        gameController.allPlayers.clear();
        gameController.allLobbies.clear();
        gameController.allGames.clear();
        p1.setConnected(true);
        p2.setConnected(true);
        p3.setConnected(true);
        p4.setConnected(true);
        p5.setConnected(true);
        gameController.allPlayers.put(p1.getNickname(), p1);
        gameController.allPlayers.put(p2.getNickname(), p2);
        gameController.allPlayers.put(p3.getNickname(), p3);
        gameController.allPlayers.put(p4.getNickname(), p4);
        gameController.allPlayers.put(p5.getNickname(), p5);
        ArrayList<Player> players = new ArrayList<>();
        players.add(p1);
        players.add(p2);
        g = new Game(players, this.GC);
        gameController.allGames.put(g.id, g);

        connectionType type = new connectionType(true, out, null);
        serverController.connections.put(p1.getNickname(), type);
        serverController.connections.put(p2.getNickname(), type);
        serverController.connections.put(p3.getNickname(), type);
        serverController.connections.put(p4.getNickname(), type);
        serverController.connections.put(p5.getNickname(), type);
    }

    @Test
    void executeMessage() throws RemoteException {
        setPlayer();

        //Case: CREATELOBBY
        GeneralMessage m = new CreateLobbyMessage(0, p3.getNickname(), 2);
        assertTrue(SC.executeMessage(m));
        assertFalse(SC.executeMessage(m));  //player already in a lobby
        m = new CreateLobbyMessage(0, p1.getNickname(), 2);
        assertFalse(SC.executeMessage(m));  //player already in a game

        //Case: SHOWLOBBY
        m = new ShowLobbyMessage(1, p3.getNickname());
        assertTrue(SC.executeMessage(m));

        m = new ShowLobbyMessage(2, p1.getNickname());
        assertFalse(SC.executeMessage(m)); //player in a game

        //Case: STARTGAME
        m = new StartGameMessage(9, gameController.allLobbies.get(gameController.allLobbies.size() -1).lobbyId, p3.getNickname());
        assertFalse(SC.executeMessage(m)); //not enough players

        //Case: JOINLOBBY
        m = new JoinLobbyMessage(3, 1, p1.getNickname());
        assertFalse(SC.executeMessage(m));  //player already in a game

        m = new JoinLobbyMessage(4, 1000, p3.getNickname());
        assertFalse(SC.executeMessage(m)); ; //selected lobby does not exist
        m = new JoinLobbyMessage(5, gameController.allLobbies.get(gameController.allLobbies.size() -1).lobbyId, p4.getNickname());
        assertTrue(SC.executeMessage(m));
        assertFalse(SC.executeMessage(m));  //player already in a lobby
        m = new JoinLobbyMessage(6, gameController.allLobbies.get(gameController.allLobbies.size() -1).lobbyId, p5.getNickname());
        assertFalse(SC.executeMessage(m)); //lobby full
        m = new JoinLobbyMessage(7, -1, p5.getNickname());
        assertFalse(SC.executeMessage(m)); //selected lobby does not exist

        //Case: CA
        m = new BroadcastMessage(g.id, gameController.allLobbies.get(gameController.allLobbies.size() - 1).lobbyId, p1.getNickname(), "ciao");
        assertTrue(SC.executeMessage(m));

        //Case: STARTGAME
        m = new StartGameMessage(7, -1, p1.getNickname());
        assertFalse(SC.executeMessage(m)); //already in a game
        m = new StartGameMessage(8, -1, p5.getNickname());
        assertFalse(SC.executeMessage(m)); //not in lobby
        m = new StartGameMessage(9, gameController.allLobbies.get(gameController.allLobbies.size() -1).lobbyId, p3.getNickname());
        assertTrue(SC.executeMessage(m));

        //Case: PT
        List<Position> pos = new ArrayList<>();
        pos.add(new Position(3, 1));
        pos.add(new Position(4, 1));
        m = new PickTilesMessage(10, p1.getNickname(), pos, g.id);
        assertTrue(SC.executeMessage(m));

        //Case: SO
        List<Integer> order = new ArrayList<>();
        order.add(1);
        order.add(2);
        m = new SelectOrderMessage(11, p1.getNickname(), order, g.id);
        assertTrue(SC.executeMessage(m));

        //Case: SC
        m = new SelectColumnMessage(12, p1.getNickname(), 2, g.id);
        assertTrue(SC.executeMessage(m));

        //Case: C
        m = new ChatMessage(p1.getNickname(), "ciao", p2.getNickname());
        assertTrue(SC.executeMessage(m));

        m = new ChatMessage(p1.getNickname(), "ciao", "lala");
        assertFalse(SC.executeMessage(m)); //invalid nickname

        //Case: CA
        m = new BroadcastMessage(g.id, -1, p1.getNickname(), "ciao");
        assertTrue(SC.executeMessage(m));

        m = new BroadcastMessage(g.id, 13, p2.getNickname(), "ciao");
        assertFalse(SC.executeMessage(m)); //invalid lobby
    }

    @Test
    void isInAGame() {
        //Create a game with two players
        setPlayer();

        assertTrue(SC.isInAGame(p1));
        assertTrue(SC.isInAGame(p2));
        assertFalse(SC.isInAGame(p5));
    }

    @Test
    void getName() {
    }

    @Test
    void getMessage() throws InvalidActionException, ParseException, InvalidKeyException, RemoteException {
        setPlayer();
        GeneralMessage mex;

        mex = new CreateLobbyMessage(-1, p1.getNickname(), 2);
        assertEquals(Action.CREATELOBBY, SC.getMessage(mex.toString()));

        mex = new ShowLobbyMessage(-1, p1.getNickname());
        assertEquals(Action.SHOWLOBBY, SC.getMessage(mex.toString()));

        mex = new JoinLobbyMessage(-1, 1, p1.getNickname());
        assertEquals(Action.JOINLOBBY, SC.getMessage(mex.toString()));

        mex = new StartGameMessage(-1, 1, p1.getNickname());
        assertEquals(Action.STARTGAME, SC.getMessage(mex.toString()));

        List<Position> pos = new ArrayList<>();
        pos.add(new Position(2, 1));
        pos.add(new Position(3, 1));
        mex = new PickTilesMessage(-1, p1.getNickname(), pos, 1);
        assertEquals(Action.PT, SC.getMessage(mex.toString()));

        List<Integer> order = new ArrayList<>();
        order.add(1);
        order.add(2);
        mex = new SelectOrderMessage(-1, p1.getNickname(), order, 1);
        assertEquals(Action.SO, SC.getMessage(mex.toString()));

        mex = new SelectColumnMessage(-1, p1.getNickname(), 1, 1);
        assertEquals(Action.SC, SC.getMessage(mex.toString()));

        mex = new ChatMessage( p1.getNickname(), "ciao", p2.getNickname());
        assertEquals(Action.C, SC.getMessage(mex.toString()));

        mex = new BroadcastMessage(1, 1, p1.getNickname(), "ciao");
        assertEquals(Action.CA, SC.getMessage(mex.toString()));

        mex = new PingMessage(p1.getNickname());
        assertEquals(Action.PING, SC.getMessage(mex.toString()));
    }

    @Test
    void sendMessage() {
    }
}