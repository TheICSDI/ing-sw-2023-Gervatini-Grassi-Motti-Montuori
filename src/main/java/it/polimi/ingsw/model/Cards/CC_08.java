/**
 * CC_08 class implements the logic for checking if the common goal card number 8 is completed by a player.
 * It requires the player to have four lines each formed by 5 tiles of maximum three different types. One
 * line can show the same or a different combination of another line.
 * @author Andrea Grassi
 */
package it.polimi.ingsw.model.Cards;

import it.polimi.ingsw.model.Tile.type;
import it.polimi.ingsw.model.Player;

import java.util.HashSet;
import java.util.Set;


public class CC_08 implements CCStrategy {
    /**
     * Checks if the common goal is completed.
     *
     * @param p a player.
     * @return true only if the common goal card is completed.
     */
    public boolean isCompleted(Player p) {
        //Number of valid rows
        int ValidRows = 0;
        //Set of different types already encountered in a row
        Set<type> Types;
        boolean Valid;

        //For each row
        for (int i = 0; i < p.getNumRows() ; i++) {
            Valid = true;
            Types = new HashSet<>();
            //For each column
            for (int j = 0; j < p.getNumCols() && Valid; j++) {
                //If the element is empty, it is automatically false
                if(p.getShelf()[i][j].getCategory().equals(type.EMPTY)){
                    Valid = false;
                }
                Types.add(p.getShelf()[i][j].getCategory());
                if(Types.size() > 3){
                    Valid = false;
                }
            }
            //If there are no more than three different types in the same row, it is added to the valid ones
            if(Valid){
                ValidRows++;
                //It returns true if the number of valid rows is more than 4
                if(ValidRows >= 4){
                    return true;
                }
            }
        }
        return false;
        }
}
