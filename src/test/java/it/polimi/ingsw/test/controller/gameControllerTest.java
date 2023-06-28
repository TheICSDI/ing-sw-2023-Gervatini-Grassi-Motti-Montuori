package it.polimi.ingsw.test.controller;

import it.polimi.ingsw.controller.gameController;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.Position;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

/** Tests for class gameController.java.
 * @author Caterina Motti.
 */
class gameControllerTest {
    gameController controller = new gameController();

    @Test
    void gameLogicTest(){
        //Create a game between two players and adding in the static attributes of gameController
        Player p1 = new Player("Mario");
        Player p2 = new Player("Luigi");
        p1.setConnected(true);
        p2.setConnected(true);
        gameController.allPlayers.put(p1.getNickname(), p1);
        gameController.allPlayers.put(p2.getNickname(), p2);
        ArrayList<Player> players = new ArrayList<>();
        players.add(p1);
        players.add(p2);
        Game g = new Game(players, this.controller);
        gameController.allGames.put(g.id, g);

        //null cases
        controller.pickTiles(p1.getNickname(), null, g.id, 1);
        assertNull(controller.chooseTiles(p1.getNickname(), g.id));
        controller.selectOrder(p1.getNickname(), null, g.id, 2);
        assertNull(controller.chooseOrder(p1.getNickname(), g.id));

        //Player p1 send a pickTiles request
        ArrayList<Position> pos = new ArrayList<>();
        pos.add(new Position(3, 1));
        pos.add(new Position(3, 2));
        controller.pickTiles(p1.getNickname(), pos, g.id, 1);
        assertFalse(gameController.queue.isEmpty());
        assertEquals(pos, controller.chooseTiles(p1.getNickname(), g.id));

        //Player p1 send a selectOrder request
        ArrayList<Integer> order = new ArrayList<>();
        order.add(2);
        order.add(1);
        controller.selectOrder(p1.getNickname(), order, g.id, 2);
        assertEquals(order, controller.chooseOrder(p1.getNickname(), g.id));

        //Player p1 send a selectColumn request
        controller.selectColumn(p1.getNickname(), 2, g.id, 3);
        assertEquals(2, controller.chooseColumn(p1.getNickname(), g.id));
    }
}