/** Tests for class Game.java.
 * @author Caterina Motti.
 */
package test;

import main.java.it.polimi.ingsw.model.Game;
import main.java.it.polimi.ingsw.model.Player;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GameTest {
    Player p1 = new Player("CLR");
    Player p2 = new Player("Jhonny");
    Player p3 = new Player("Mayhem");
    Player p4 = new Player("Fozy");
    List<Player> playerList = new ArrayList<>();



    @Test
    void startGame() {
        playerList.add(p1);
        playerList.add(p2);
        playerList.add(p3);
        playerList.add(p4);
        Game g = new Game(playerList);

    }

    @Test
    void calculateWinner() {
        playerList.add(p1);
        playerList.add(p2);
        playerList.add(p3);
        playerList.add(p4);
        //Game g = new Game(playerList);

        //assertEquals(p4, g.calculateWinner());
    }
}