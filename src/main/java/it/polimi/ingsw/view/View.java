package it.polimi.ingsw.view;

import it.polimi.ingsw.model.Cards.PersonalCard;
import it.polimi.ingsw.model.Lobby;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.Tile.Tile;

import java.util.List;
import java.util.Map;

public interface View {
    String chooseConnection();
    String askUsername();
    void printUsername(String username, boolean isAvailable);
    void createLobby(String lobbyName);
    void showLobby(List<Lobby> Lobbies);
    void showBoard(Tile[][] Board);
    void showShelf(Tile[][] Shelf);
    void showPersonal(PersonalCard PC);
    void showChosenTiles(List<Tile> tiles,boolean toOrder);
    void showCommons(List<Integer> cc);
    void commonCompleted(String msg, boolean first, String whoCompleted);
    void showOthers(Map<String,Player> others);
    void updateOthers(Map<String,Player> others);
    void startGame(String message);
    void showPoints(String message);
    void winner(String message);
    void endGame();
    void endGameToken(String player);
    void displayError(String msg);
    void displayMessage(String msg);
    void playersTurn(String msg, boolean firstTurn);
    void showChat(String msg);
    void help();
    String getInput();
}

