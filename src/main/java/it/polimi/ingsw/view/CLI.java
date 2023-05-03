package it.polimi.ingsw.view;

import it.polimi.ingsw.model.Board;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.Tile.Tile;
import java.util.List;

public class CLI implements typeView {
    /**
     * Prints board, every player's shelf and chosen tiles
     * @param board board status
     * @param players all players
     * @param chosenTiles tiles chosen, if there are any
     */

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
}
