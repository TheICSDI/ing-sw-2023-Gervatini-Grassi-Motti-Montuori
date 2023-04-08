/** Represent a lobby in the game. Players can join or leave lobbies whenever they want.
 * In the lobby the game has not  yet. It is possible to show each lobby to see which people are
 * participating.
 * Each lobby is represented by a unique number.
 * @author Andrea Grassi */
package main.java.it.polimi.ingsw.model;

import java.util.*;

public class Lobby {
    int lobbyNumber;
    List<Player> Players = new ArrayList<>();

    /** Create a new lobby given a player "creator" and an unique number. */
    public Lobby(Player Creator, int n){
        lobbyNumber = n;
        Players.add(Creator);
    }

    /** Adds a player "joiner" to the lobby, only if the total number of player is less than four. */
    public void Join(Player Joiner){
        if(isLobbyFull()) throw new InputMismatchException("The lobby " + lobbyNumber + " is full!");
        else Players.add(Joiner);
    }

    /** Remove a player "leaver" from the lobby. */
    public void Leave(Player Leaver){Players.remove(Leaver);}

    /** Graphically shows the partecipant of the lobby*/
    public void Show(){
        System.out.print("Lobby" + this.lobbyNumber + ": ");
        for (Player p: Players) {
            System.out.print(p.getNickname());
        }
        System.out.println("");
    }

    /** Return true only if there is more available space in the lobby.
     * A lobby is considered full when it reaches 4 players (maximum for the game)*/
    private boolean isLobbyFull(){
        return Players.size() == 4;
    }

}


// Lobby1 : Mayhem, Fozy
// LObby2: Andrea Grassi, Andrew FAts, AndreDeGrass
// Lobby3: Gynephobia
