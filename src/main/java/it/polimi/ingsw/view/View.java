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
    void createLobby(String lobbyName/*, int maxPlayers*/);
    void showLobby(List<Lobby> Lobbies);
    void showBoard(Tile[][] Board);
    void showShelf(Tile[][] Shelf);
    void showPersonal(PersonalCard PC);
    void showChosenTiles(List<Tile> tiles,boolean toOrder);
    void showCommons(List<Integer> cc);
    void showOthers(List<Player> others);
    void updateOthers(List<Player> others);
    void joinLobby(int lobby_id);
    void exitLobby(int lobby_id);
    void startGame(String message);
    void endGame(); // da controllare input
    void displayError(String msg);
    void displayMessage(String msg);
    void playersTurn(String msg);
    void showChat(String msg);
    void help();
    String getInput();
}

