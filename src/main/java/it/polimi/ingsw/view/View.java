package it.polimi.ingsw.view;

import it.polimi.ingsw.model.Board;
import it.polimi.ingsw.model.Cards.PersonalCard;
import it.polimi.ingsw.model.Lobby;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.Position;
import it.polimi.ingsw.model.Tile.Tile;

import java.util.List;
import java.util.Map;

public interface View {

    String showMain();
    String askUsername();
    void printUsername(String username, boolean isAvailable);
    void createLobby(String lobbyName/*, int maxPlayers*/);
    void showLobby(List<Lobby> Lobbies);
    void showBoard(Tile[][] Board);
    void showShelf(Tile[][] Shelf);
    void showPersonal(PersonalCard PC);
    void showChosenTiles(List<Tile> tiles,boolean toOrder);
    void showCommons(List<Integer> cc);
    void showOthers(Map<String,Player> others);
    void joinLobby(int lobby_id);
    void exitLobby(int lobby_id);
    void startGame(String message);
    void pickTiles(int lobby_id, Position pos);
    void selectOrder(int lobby_id, List<Integer> order);
    void selectColumn(int lobby_id, int col);
    void updateBoard(int lobby_id, Board board);
    // void endGame(int lobbyId, List<PlayerScore> scores); // da controllare input
    void updateLobby(int lobby_id, List<String> players);
    void displayError(String msg);
    void displayMessage(String msg);
    void help();
    String getInput();

}

