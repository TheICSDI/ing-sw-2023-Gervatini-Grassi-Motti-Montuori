/**
 * CC_07 class implements the logic for checking if the common goal card number 7 is completed by a player.
 * It requires the player to have five tiles of the same type forming a diagonal.
 * @author Marco Gervatini
 */
package it.polimi.ingsw.model.Cards;

import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.Tile.type;

public class CC_07 implements CCStrategy {
    /**
     * Checks if the common goal is completed.
     *
     * @param p a player.
     * @return true only if the common goal card is completed.
     */
    public boolean isCompleted(Player p) {
        int i, j, count;
        type curr, first;//curr si the actual tile of the control, first is the  first tile of the diagonal.
        boolean end = false;
        int[][] startPositions = {{0, 0}, {1,0}, {0,4}, {1,4}};
        int[][] updates = {{1, 1}, {1, 1}, {1, -1}, {1, -1}};
        // da 00 44 da 10 54 da 04 40 14 50
        /*
        internal loop check the five tiles of a diagonal, from the one set by start
        external loop change the start tile (by start_position) and decide the way in which indices move
        (by updates)
         */
        for(int k = 0; k < startPositions.length && !end; k++){
            i = startPositions[k][0];//row set
            j = startPositions[k][1];//column set
            count = 0;//count set
            first = p.getShelf()[i][j].getCategory();//color to check set
            if(!first.equals(type.EMPTY)) {
                for (int l = 0; l < 5; i = i + updates[k][0], j = j + updates[k][1], l++) {
                    //updates regulate if j is ++ or --
                    curr = p.getShelf()[i][j].getCategory();
                    if (curr.equals(first)) {
                        count++;
                        if (count >= 5) {
                            end = true;
                        }
                    }
                }
            }

        }
        return end;
    }

}
