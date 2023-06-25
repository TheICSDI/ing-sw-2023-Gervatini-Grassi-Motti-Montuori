/** Represent a lobby in the game. Players can join or leave lobbies whenever they want.
 * In the lobby the game has not  yet. It is possible to show each lobby to see which people are
 * participating.
 * Each lobby is represented by a unique number.
 * @author Andrea Grassi, Caterina Motti */
package it.polimi.ingsw.model;


import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;

public class Lobby {
    private static int count = 0;
    public final int lobbyId;
    public final int limit;
    public List<Player> Players = new ArrayList<>();

    /** Creates a new lobby given a player "creator" and a unique number automatically generated. */
    public Lobby(Player Creator, int limit){
        Players.add(Creator);
        //The static attribute make the id unique
        count++;
        this.lobbyId = count;
        this.limit = limit;
    }

    /** Adds a player "joiner" to the lobby, only if the total number of player is less than four.
     * @throws InputMismatchException if the lobby is full or if the player is already in the lobby. */
    public void Join(Player Joiner){
        if(isLobbyFull()) throw new InputMismatchException("The lobby " + lobbyId + " is full!");
        else if (this.Players.contains(Joiner)){
            throw new InputMismatchException("The player " + Joiner.getNickname() + " is already in!");
        } else Players.add(Joiner);
    }

    /** Removes a player "leaver" from the lobby.
     * @throws InputMismatchException if the player passed by parameter is not in the lobby. */
    public void Leave(Player Leaver){
        if(!Players.contains(Leaver)) {
            throw new InputMismatchException("The player " + Leaver.getNickname() + " is not in the lobby!");
        } else Players.remove(Leaver);
    }

    /** Returns true only if there is more available space in the lobby.
     * A lobby is considered full when it reaches 4 players (maximum for the game)*/
    private boolean isLobbyFull(){
        return Players.size() == limit;
    }

    /** Checks if a certain player is in this lobby.
     * @param p player to check
     * @return true only if the given player is in the lobby, false otherwise.
     */
    public boolean isPlayerInLobby(Player p){
        for (Player player: this.Players) {
            if(p.getNickname().equals(player.getNickname())){
                return true;
            }
        }
        return false;
    }
}