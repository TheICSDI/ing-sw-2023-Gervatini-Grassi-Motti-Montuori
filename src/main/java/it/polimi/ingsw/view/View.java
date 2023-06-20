/** Interface that declare all methods necessary for display the game.
 * All available client's views (GUI and CLI) implements this interface.*/
package it.polimi.ingsw.view;

import it.polimi.ingsw.model.Cards.PersonalCard;
import it.polimi.ingsw.model.Lobby;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.Tile.Tile;

import java.util.List;
import java.util.Map;

public interface View {
    /** It enables the client to choose the type of connection (socket or RMI).
     * @return "1" for socket, "2" for RMI. */
    String chooseConnection();

    /** It asks the client a unique nickname.
     * @return chosen nickname. */
    String askNickname();

    /** It shows the result of the nickname choice.
     * @param nickname chosen nickname.
     * @param isAvailable true only if the nickname is available, false otherwise.
     */
    void checkNickname(String nickname, boolean isAvailable);

    /** It shows all the created lobbies passed by parameter. */
    void showLobby(List<Lobby> Lobbies);

    /** It manages the set-up of the game. */
    void startGame(String message);

    /** It notifies all players of the game about the current player's turn.
     * @param msg message to be shown.
     * @param firstTurn true only if is the first turn of the player.
     */
    void playersTurn(String msg, boolean firstTurn);

    /** It shows the board of the game, passed by parameter. */
    void showBoard(Tile[][] Board);

    /** It shows the shelf passed by parameter. */
    void showShelf(Tile[][] Shelf);

    /** It shows the personal card passed by parameter. */
    void showPersonal(PersonalCard PC);

    /** It shows the common goal cards of the game, passed by parameter. */
    void showCommons(List<Integer> cc);

    /** It shows the chosen tiles.
     * @param tiles chosen tiles.
     * @param toOrder true only if the tiles need to be ordered, false otherwise. */
    void showChosenTiles(List<Tile> tiles, boolean toOrder);

    /**
     * It notifies the players if anyone has completed a common goal card.
     * @param msg message to be shown.
     * @param whoCompleted the player that completed the goal.
     * @param first true only if the player is the first one to complete that goal.
     */
    void commonCompleted(String msg, String whoCompleted, boolean first);

    /** It shows the shelf of the other players of the game.
     * @param others map of the other players.
     */
    void showOthers(Map<String, Player> others);

    /** It gives the end game token to the player passed by parameter. */
    void endGameToken(String player);

    /** It manages the end of the game. */
    void endGame();

    /** It shows the points for each player. */
    void showPoints(String message);

    /** It shows the winner by displaying. */
    void winner(String message);

    /** It shows a simple message from the game logic.*/
    void displayMessage(String msg);

    /** It shows a chat message. */
    void showChat(String msg);

    /** It shows how to use the view. */
    void help();

    /** It sends the input to the server. */
    String getInput();
}

