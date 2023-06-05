/** Tests for class serverController.java.
 * @author Caterina Motti.
 */
package it.polimi.ingsw.test.controller;

import it.polimi.ingsw.controller.command;
import it.polimi.ingsw.controller.connectionType;
import it.polimi.ingsw.controller.gameController;
import it.polimi.ingsw.controller.serverController;
import it.polimi.ingsw.exceptions.InvalidActionException;
import it.polimi.ingsw.exceptions.InvalidKeyException;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.Lobby;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.Position;
import it.polimi.ingsw.network.messages.*;
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
        g = new Game(players, this.GC);
        gameController.allGames.put(g.id, g);

        connectionType type = new connectionType(true, out, null);
        serverController.connections.put(p1.getNickname(), type);
        serverController.connections.put(p2.getNickname(), type);
        serverController.connections.put(p3.getNickname(), type);
    }

    @Test
    void executeMessage() throws RemoteException {
        setPlayer();

        //Case: CREATELOBBY
        GeneralMessage m = new CreateLobbyMessage(0, p3.getNickname(), 2);
        SC.executeMessage(m);
        SC.executeMessage(m); //player already in a lobby

        //player already in a game
        m = new CreateLobbyMessage(0, p1.getNickname(), 2);
        SC.executeMessage(m);

        //Case: SHOWLOBBY
        m = new ShowLobbyMessage(1, p3.getNickname());
        SC.executeMessage(m); //player not in a game

        m = new ShowLobbyMessage(2, p1.getNickname());
        SC.executeMessage(m);

        //Case: JOINLOBBY
        m = new JoinLobbyMessage(3, 1, p1.getNickname());
        SC.executeMessage(m); //player already in a game

        m = new JoinLobbyMessage(4, 1000, p3.getNickname());
        SC.executeMessage(m); //selected lobby does not exist
        m = new JoinLobbyMessage(5, 1, p3.getNickname());
        SC.executeMessage(m);
        SC.executeMessage(m); //player already in a lobby

        //Case: STARTGAME
        m = new StartGameMessage(6, 1, p1.getNickname());
        SC.executeMessage(m);

        //Case: PT
        List<Position> pos = new ArrayList<>();
        pos.add(new Position(3, 1));
        pos.add(new Position(4, 1));
        m = new PickTilesMessage(8, p1.getNickname(), pos, g.id);
        SC.executeMessage(m);

        //Case: SO
        List<Integer> order = new ArrayList<>();
        order.add(1);
        order.add(2);
        m = new SelectOrderMessage(9, p1.getNickname(), order, g.id);
        SC.executeMessage(m);

        //Case: SC
        m = new SelectColumnMessage(10, p1.getNickname(), 2, g.id);
        SC.executeMessage(m);

        //Case: C
        m = new ChatMessage(p1.getNickname(), "ciao", p2.getNickname());
        SC.executeMessage(m);

        m = new ChatMessage(p1.getNickname(), "ciao", "lala");
        SC.executeMessage(m); //invalid nickname

        //Case: CA
        m = new BroadcastMessage(g.id, 1, p3.getNickname(), "ciao");
        SC.executeMessage(m);

        m = new BroadcastMessage(g.id, 10, p2.getNickname(), "ciao");
        SC.executeMessage(m); //invalid lobby

        m = new BroadcastMessage(g.id, -1, p1.getNickname(), "ciao");
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