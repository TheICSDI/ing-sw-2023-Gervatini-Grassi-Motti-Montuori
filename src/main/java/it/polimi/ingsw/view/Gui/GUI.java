package it.polimi.ingsw.view.Gui;

import it.polimi.ingsw.model.Lobby;
import it.polimi.ingsw.model.Player;

import it.polimi.ingsw.model.Board;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.Position;
import it.polimi.ingsw.model.Lobby;
import it.polimi.ingsw.model.Tile.Tile;
import it.polimi.ingsw.model.Tile.type;
import it.polimi.ingsw.network.messages.Action;
import it.polimi.ingsw.view.View;

import java.util.List;
import java.util.Map;

import static javafx.application.Application.launch;

public class GUI implements View
{

    @Override
    public void init(){

    }

    @Override
    public String askUsername() {
        return null;
    }

    @Override
    public void printUsername(String username, boolean isAvailable) {

    }

    @Override
    public void createLobby(String lobbyName, int maxPlayers) {

    }

    @Override
    public void showLobby(List<Lobby> Lobbies) {
    }

    @Override
    public void showLobby(List<String> usrs, int num_usrs) {
    }

    @Override
    public void showBoard(type[][] simpleBoard, Action action) {

    }

    @Override
    public void showChosenTiles(List<Tile> tiles) {

    }

    @Override
    public void showCommons(List<Integer> cc) {

    }

    @Override
    public void showOthers(Map<String, Player> others) {

    }

    public void showOthers(Map<String, Player> others){

    }
    @Override
    public void joinLobby(int lobby_id) {

    }

    @Override
    public void exitLobby(int lobby_id) {

    }

    @Override
    public void startGame(int lobby_id) {

    }

    @Override
    public void pickTiles(int lobby_id, Position pos) {

    }

    @Override
    public void selectOrder(int lobby_id, List<Integer> order) {

    }

    @Override
    public void selectColumn(int lobby_id, int col) {

    }

    @Override
    public void updateBoard(int lobby_id, Board board) {

    }

    @Override
    public void updateLobby(int lobby_id, List<String> players) {

    }

    @Override
    public void displayError(String msg) {

    }

    @Override
    public void displayMessage(String msg) {

    }

    @Override
    public void help() {

    }
}
