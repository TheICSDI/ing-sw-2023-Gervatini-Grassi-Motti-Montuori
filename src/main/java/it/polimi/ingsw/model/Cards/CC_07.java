package main.java.it.polimi.ingsw.model.Cards;

import main.java.it.polimi.ingsw.model.Player;
import main.java.it.polimi.ingsw.model.Tile.Tile;
import main.java.it.polimi.ingsw.model.Tile.type;

public class CC_07 implements CCStrategy {
    public boolean isCompleted(Player p) {
        Tile[][] curr_shelf =p.getShelf();
        int i, j, count,k,l;
        type curr, first;//curr si the actual tile of the control, first is the  first tile of the diagonal.
        boolean end = false;
        int[][] start_positions = {{0, 0}, {1,0}, {0,4}, {1,4}};
        int[][] updates = {{1, 1}, {1, 1}, {1, -1}, {1, -1}};
        // da 00 44 da 10 54 da 04 40 14 50
        /*
        internal loop check the five tiles of a diagonal, from the one set by start
        external loop change the start tile (by start_position) and decide the way in which indices move
        (by updates)

         */
        for(k=0;k<4&& !end;k++){
            i = start_positions[k][0];//row set
            j = start_positions[k][1];//column set
            count=0;//count set
            first = curr_shelf[i][j].getCategory();//color to check set
            if(!first.equals(type.EMPTY)) {
                for (l = 0; l < 5; i = i + updates[k][0], j = j + updates[k][1], l++) {
                    //updates regulate if j is ++ or --
                    curr = curr_shelf[i][j].getCategory();
                    if (curr.equals(first)) {
                        count++;
                    }
                }
            }
            if (count >= 5) {
                end = true;
            }
        }
        return end;
    }

}
