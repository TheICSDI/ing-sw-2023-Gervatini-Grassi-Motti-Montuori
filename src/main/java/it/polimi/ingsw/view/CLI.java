package main.java.it.polimi.ingsw.view;

import main.java.it.polimi.ingsw.model.Board;
import main.java.it.polimi.ingsw.model.Tile.Tile;

public class CLI implements typeView {

    public void show(Board board){
        for (int i=0;i<board.getNumRows();i++){
            for (int j = 0; j < board.getNumCols(); j++) {
                System.out.print(board.board[i][j].getColor()+ "   ");
            }
            System.out.println();
        }
    }
}
