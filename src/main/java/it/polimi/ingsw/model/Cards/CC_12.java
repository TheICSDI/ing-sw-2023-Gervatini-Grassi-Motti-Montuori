package it.polimi.ingsw.model.Cards;

import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.Tile.type;

/** CC_12 class implements the logic for checking if the common goal card number 12 is completed by a player.
 * It requires the player to have five columns of increasing or decreasing height.
 * Starting from the first column on the left or on the right, each next column must be made of exactly one more tile.
 * Tiles can be of any type.
 * @author Marco Gervatini.
 */
public class CC_12 implements CCStrategy {
    private final int id=12;
    @Override
    public int getId(){return this.id;}

    /**
     * Checks if the common goal is completed.
     * @param p a player.
     * @return true only if the common goal card is completed.
     */
    public boolean isCompleted(Player p) {
        //number of empty spaces in each frist column of each diagonal
        int[] set_point = {0, 1, 4, 5};
        type currType;
        boolean end = false;
        for (int k = 0; k < set_point.length && !end; k++) {
            end = true;
            for (int j = 0; j < p.getNumCols() && end; j++) {
                for (int i = 0; i < p.getNumRows() && end; i++) {
                    //checks that the frist set_point[k] of the column are empty
                    currType = p.getShelf()[i][j].getCategory();
                    if (i < set_point[k]) {
                        //before setpoint all places must be empty
                        if (!currType.equals(type.EMPTY)) {
                            end = false;
                        }
                    } else {
                        //after setpoint all places must be full
                        if (currType.equals(type.EMPTY)) {
                            end = false;
                        }
                    }
                }
                //updates the number of empty spaces in the next column: if the pyramid is ascending the empty spaces
                //must be less, otherwise must be more.
                if (k < 2) {
                    set_point[k]++;
                } else {
                    set_point[k]--;
                }
            }
        }
        return end;
    }
}
