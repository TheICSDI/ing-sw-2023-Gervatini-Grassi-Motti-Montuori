package it.polimi.ingsw.view;

import it.polimi.ingsw.model.Board;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.Position;
import it.polimi.ingsw.model.Tile.Tile;
import it.polimi.ingsw.model.Tile.type;
import it.polimi.ingsw.network.messages.Action;

import java.io.PrintStream;
import java.util.List;
import java.util.Scanner;

public class CLI implements View{

    private PrintStream out;

    public CLI(){
        this.out = System.out;
    }

    @Override
    public String askUsername(){
        Scanner input = new Scanner(System.in);
        out.print("Enter your nickname: ");
        return input.nextLine();
    }

    @Override
    public void printUsername(String username, boolean isAvailable){

        if(isAvailable) {
            out.println("Username set correctly\n");
        }
        else {
            out.println("Username not available\n");
        }
    }

    @Override
    public void showBoard(type[][] board){
        Tile[][] shelf=recreateShelf(board);
        for (int i = 0; i < shelf.length; i++){  //stampa della board
            for (int j = 0; j < shelf[0].length; j++) {
                out.print(shelf[i][j].getColor() + " " + shelf[i][j].getInitial() + " ");
            }
            System.out.println("\033[0m");
        }
        System.out.println();
    }

    @Override
    public void showShelf(type[][] simpleBoard) {

    }

    @Override
    public void createLobby(String lobbyName, int maxPlayers){

    }

    @Override
    public void showLobby(List<String> usrs, int num_usrs){

    }

    @Override
    public void joinLobby(int lobby_id){

    }

    @Override
    public void exitLobby(int lobby_id){

    }

    @Override
    public void startGame(int lobby_id){

    }

    @Override
    public void pickTiles(int lobby_id, Position pos){

    }

    @Override
    public void selectOrder(int lobby_id, List<Integer> order){

    }

    @Override
    public void selectColumn(int lobby_id, int col){

    }

    @Override
    public void updateBoard(int lobby_id, Board board){

    }

    // void endGame(int lobbyId, List<PlayerScore> scores); // da controllare input

    @Override
    public void updateLobby(int lobby_id, List<String> players){

    }

    @Override
    public void displayError(String msg){

    }

    @Override
    public void displayMessage(String msg){

    }

    /**
     * Prints board, every player's shelf and chosen tiles
     * @param board board status
     * @param players all players
     * @param chosenTiles tiles chosen, if there are any
     */
/*
    //statica perchè non ha senso creare un oggetto cli, non so se è il massimo una funzione così vedremo
    // tile.getColor cambia il colore dello sfondo se messo in una print, 3 spazi per fare un quadrato
    //get initial per mettere una lettera dentro le tile e renderle più riconoscibili
    public static void show(Board board, List<Player> players,List<Tile> chosenTiles){
        for (int i=0;i<board.getNumRows();i++){  //stampa della board
            for (int j = 0; j < board   .getNumCols(); j++) {
                System.out.print(board.board[i][j].getColor()+ " "+board.board[i][j].getInitial()+" ");
            }
            System.out.println("\033[0m");
        }
        System.out.print("\n \n \n");
        if(chosenTiles != null){  //stampa delle tile prese
            for (Tile chosenTile : chosenTiles) {
                System.out.println(chosenTile.getColor() + " " +chosenTile.getInitial() + " ");
            }
        }else System.out.println("\033[0m");
        System.out.println("\n \n \n");
        for (int i = 0; i < players.get(0).getNumRows(); i++) { //stampa delle shelf dei player
            for (Player p:
                 players) {
                System.out.print(p.getNickname()+" "+p.getTotalPoints()+"  ");//sono da contare bene gli spazi ma si vedrà poi quando avrò un feedback

            }
            System.out.println();
            for (Player player : players) {
                System.out.print("\033[0m ");
                for (int k = 0; k < players.get(0).getNumCols(); k++) {
                    System.out.print(player.getShelf()[i][k].getColor()+" "+player.getShelf()[i][k].getInitial()+" ");
                    System.out.print("\033[0m   ");
                }
                System.out.println("\033[0m");
            }
        }

    }
    */
    public Tile[][] recreateShelf(type[][] simpleShelf){
        Tile[][] shelf=new Tile[simpleShelf.length][simpleShelf[0].length];
        for (int i = 0; i < simpleShelf.length; i++) {
            for (int j = 0; j < simpleShelf[0].length; j++) {
                shelf[i][j]=new Tile(String.valueOf(simpleShelf[i][j]));
            }
        }
        return shelf;
    }

}
