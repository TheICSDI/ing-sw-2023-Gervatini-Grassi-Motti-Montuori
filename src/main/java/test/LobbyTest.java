/** Tests for class Lobby.java.
 * @author Caterina Motti.
 */
package test;

import it.polimi.ingsw.model.Lobby;
import it.polimi.ingsw.model.Player;
import org.junit.jupiter.api.Test;

import java.util.InputMismatchException;

import static org.junit.jupiter.api.Assertions.*;

class LobbyTest {
    Player p1 = new Player("CLR",null);
    Player p2 = new Player("Jhonny",null);
    Player p3 = new Player("Mayhem",null);
    Player p4 = new Player("Fozy",null);
    Player p5 = new Player("AndreDeGrass",null);
    Lobby l1 = new Lobby(p1,2);
    Lobby l2 = new Lobby(p2,2);

    @Test
    void Lobby(){
        //lobbyId is automatically assigned by the constructor, it is unique for each lobby
        assertNotEquals(l1.lobbyId, l2.lobbyId);
    }

    @Test
    void join() {
        //The created lobby contains only Player p1
        assertTrue(l1.Players.contains(p1));
        assertFalse(l1.Players.contains(p2));
        assertFalse(l1.Players.contains(p3));
        assertFalse(l1.Players.contains(p4));
        assertFalse(l1.Players.contains(p5));

        //Some player joining
        l1.Join(p2);
        l1.Join(p3);
        assertTrue(l1.Players.contains(p2));
        assertTrue(l1.Players.contains(p3));

        //Exception
        //case 1: the player that wants to join is already in the lobby
        Throwable ex1 = assertThrows(InputMismatchException.class, () -> {
            l1.Join(p3);
        });
        assertEquals("The player " + p3.getNickname() + " is already in!", ex1.getMessage());

        //case 2: the lobby has already 4 players (maximum for the game's rules)
        l1.Join(p4);
        assertTrue(l1.Players.contains(p4));
        assertEquals(4, l1.Players.size());
        Throwable ex2 = assertThrows(InputMismatchException.class, () -> {
            l1.Join(p5);
        });
        assertEquals("The lobby " + l1.lobbyId + " is full!", ex2.getMessage());
    }

    @Test
    void leave() {
        //Create a lobby with some players
        l1.Join(p2);
        l1.Join(p3);
        assertTrue(l1.Players.contains(p2));
        assertTrue(l1.Players.contains(p3));

        //p2 and p3 leave the lobby
        l1.Leave(p1);
        l1.Leave(p2);
        l1.Leave(p3);
        assertFalse(l1.Players.contains(p1));
        assertFalse(l1.Players.contains(p2));
        assertFalse(l1.Players.contains(p3));
        assertTrue(l1.Players.isEmpty());

        //Exception
        //the player is not in the lobby
        Throwable ex2 = assertThrows(InputMismatchException.class, () -> {
            l1.Leave(p5);
        });
        assertEquals("The player " + p5.getNickname() + " is not in the lobby!", ex2.getMessage());
    }
}