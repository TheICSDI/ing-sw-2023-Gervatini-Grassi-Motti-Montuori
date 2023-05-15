package it.polimi.ingsw.view;

import it.polimi.ingsw.model.Board;
import it.polimi.ingsw.model.Lobby;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.Position;
import it.polimi.ingsw.model.Tile.Tile;
import it.polimi.ingsw.model.Tile.type;
import it.polimi.ingsw.network.messages.Action;

import java.util.List;
import java.util.Map;

public interface View {

    String showMain();

    String askUsername();
    void printUsername(String username, boolean isAvailable);
    void createLobby(String lobbyName, int maxPlayers);
    void showLobby(List<Lobby> Lobbies);

    void showLobby(List<String> usrs, int num_usrs);

    void showBoard(type[][] simpleBoard, Action action);
    void showChosenTiles(List<Tile> tiles);
    void showCommons(List<Integer> cc);
    void showOthers(Map<String,Player> others);
    void joinLobby(int lobby_id);
    void exitLobby(int lobby_id);
    void startGame(int lobby_id);
    void pickTiles(int lobby_id, Position pos);
    void selectOrder(int lobby_id, List<Integer> order);
    void selectColumn(int lobby_id, int col);
    void updateBoard(int lobby_id, Board board);
    // void endGame(int lobbyId, List<PlayerScore> scores); // da controllare input
    void updateLobby(int lobby_id, List<String> players);
    void displayError(String msg);
    void displayMessage(String msg);
    void help();
}

