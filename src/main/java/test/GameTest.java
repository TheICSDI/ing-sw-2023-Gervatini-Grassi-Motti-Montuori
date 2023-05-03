/** Tests for class Game.java.
 * @author Caterina Motti.
 */
package test;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.Player;
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
    void Game(){
        playerList.add(p1);
        playerList.add(p2);
        playerList.add(p3);
        playerList.add(p4);
        Game g = new Game(playerList);

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