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
        Game g = new Game(playerList);

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
}