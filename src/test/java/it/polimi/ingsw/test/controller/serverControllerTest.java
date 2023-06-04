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
import it.polimi.ingsw.model.Lobby;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.network.messages.*;
import org.json.simple.parser.ParseException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.io.PrintWriter;
import java.rmi.RemoteException;
import java.util.ArrayList;

class serverControllerTest {
    Player p1 = new Player("Mario");
    Player p2 = new Player("Luigi");
    Player p3 = new Player("Peach");
    Game g;
    gameController GC = new gameController();
    serverController SC = new serverController();
    PrintWriter out = new PrintWriter(System.out, true);

    void setPlayer(){
        p1.setConnected(true);
        p2.setConnected(true);
        p3.setConnected(true);
        gameController.allPlayers.put(p1.getNickname(), p1);
        gameController.allPlayers.put(p2.getNickname(), p2);
        gameController.allPlayers.put(p3.getNickname(), p3);
        ArrayList<Player> players = new ArrayList<>();
        players.add(p1);
        players.add(p2);
        Game g = new Game(players, this.GC);
        gameController.allGames.put(g.id, g);

        connectionType type = new connectionType(true, out, null);
        serverController.connections.put(p1.getNickname(), type);
        serverController.connections.put(p2.getNickname(), type);
        serverController.connections.put(p3.getNickname(), type);
    }

    @Test
    void executeMessage() throws RemoteException, InvalidActionException, ParseException, InvalidKeyException {
        setPlayer();
        String mex;

        //Case: CREATELOBBY
        GeneralMessage m = new CreateLobbyMessage(0, p3.getNickname(), 2);
        SC.executeMessage(m);
        //player already in a lobby
        SC.executeMessage(m);

        //player already in a game
        m = new CreateLobbyMessage(0, p1.getNickname(), 2);
        SC.executeMessage(m);

        //Case: SHOWLOBBY
        m = new ShowLobbyMessage(1, p3.getNickname());
        //player not in a game
        SC.executeMessage(m);

        m = new ShowLobbyMessage(2, p1.getNickname());
        SC.executeMessage(m);

        //Case: JOINLOBBY
        m = new JoinLobbyMessage(3, 1, p1.getNickname());
        //player already in a game
        SC.executeMessage(m);

        m = new JoinLobbyMessage(4, 1000, p3.getNickname());
        //selected lobby does not exist
        SC.executeMessage(m);
        m = new JoinLobbyMessage(5, 1, p3.getNickname());
        SC.executeMessage(m);
        //player already in a lobby
        SC.executeMessage(m);

        //Case: STARTGAME
        m = new StartGameMessage(6, 1, p1.getNickname());
        SC.executeMessage(m);

    }

    @Test
    void isInAGame() {
        //Create a game with two players
        p1.setConnected(true);
        p2.setConnected(true);
        p3.setConnected(true);
        gameController.allPlayers.put(p1.getNickname(), p1);
        gameController.allPlayers.put(p2.getNickname(), p2);
        ArrayList<Player> players = new ArrayList<>();
        players.add(p1);
        players.add(p2);
        Game g = new Game(players, this.GC);
        gameController.allGames.put(g.id, g);

        assertTrue(SC.isInAGame(p1));
        assertTrue(SC.isInAGame(p2));
        assertFalse(SC.isInAGame(p3));
    }

    @Test
    void getName() {
    }

    @Test
    void getMessage() {
    }

    @Test
    void sendMessage() {
    }
}