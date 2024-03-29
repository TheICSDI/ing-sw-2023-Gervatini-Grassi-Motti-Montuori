package it.polimi.ingsw.test.model;

import it.polimi.ingsw.model.Lobby;
import it.polimi.ingsw.model.Player;
import org.junit.jupiter.api.Test;

import java.util.InputMismatchException;

import static org.junit.jupiter.api.Assertions.*;

/** Tests for class Lobby.java.
 * @author Caterina Motti.
 */
class LobbyTest {
    Player p1 = new Player("CLR");
    Player p2 = new Player("Jhonny");
    Player p3 = new Player("Mayhem");
    Player p4 = new Player("Fozy");
    Player p5 = new Player("AndreDeGrass");
    Lobby l1 = new Lobby(p1,4);
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
    void isPlayerInLobby(){
        //The lobby contains only p1
        assertTrue(l1.isPlayerInLobby(p1));
        assertFalse(l1.isPlayerInLobby(p2));
        assertFalse(l1.isPlayerInLobby(p3));
        assertFalse(l1.isPlayerInLobby(p4));
        assertFalse(l1.isPlayerInLobby(p5));

        //Two players join the lobby
        l1.Join(p2);
        l1.Join(p3);
        assertTrue(l1.isPlayerInLobby(p1));
        assertTrue(l1.isPlayerInLobby(p2));
        assertTrue(l1.isPlayerInLobby(p3));
        assertFalse(l1.isPlayerInLobby(p4));
        assertFalse(l1.isPlayerInLobby(p5));
    }
}