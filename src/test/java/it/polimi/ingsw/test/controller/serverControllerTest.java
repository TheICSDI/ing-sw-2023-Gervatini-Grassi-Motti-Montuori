/** Tests for class serverController.java.
 * @author Caterina Motti.
 */
package it.polimi.ingsw.test.controller;

import it.polimi.ingsw.controller.gameController;
import it.polimi.ingsw.controller.serverController;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.Player;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;

class serverControllerTest {
    Player p1 = new Player("Mario");
    Player p2 = new Player("Luigi");
    Player p3 = new Player("Peach");
    gameController GC = new gameController();
    serverController SC = new serverController();

    @Test
    void executeMessage() {
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