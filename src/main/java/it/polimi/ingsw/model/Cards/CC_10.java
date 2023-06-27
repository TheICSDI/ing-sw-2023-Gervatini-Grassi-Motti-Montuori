package it.polimi.ingsw.model.Cards;

import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.Tile.type;

import java.util.HashSet;
import java.util.Set;

/** CC_10 class implements the logic for checking if the common goal card number 10 is completed by a player.
 * It requires the player to have two rows each formed by 5 different types of tiles. One line can show the
 * same or a different combination of the other line
 * @author Andrea Grassi.
 */
public class CC_10  implements CCStrategy {
    private final int id=10;
    @Override
    public int getId(){return this.id;}

    /**
     * Checks if the common goal is completed.
     * @param p a player.
     * @return true only if the common goal card is completed.
     */
    public boolean isCompleted(Player p) {
        //Number of valid rows
        int ValidRows = 0;
        //Set of different types already encountered in a column
        Set<type> Types;
        boolean Valid;

        //For each row
        for (int i = 0; i < p.getNumRows(); i++) {
            Valid = true;
            Types = new HashSet<>();
            //For each column
            for (int j = 0; j < p.getNumCols() && Valid; j++) {
                //If the element is empty, it is automatically false
                if(p.getShelf()[i][j].getCategory().equals(type.EMPTY)){
                    Valid = false;
                }
                Types.add(p.getShelf()[i][j].getCategory());
            }
            //If there are five different types in the same row, it is added to the valid ones
            if(Types.size() == 5 && Valid){
                ValidRows++;
                //It returns true only if the number of valid rows is grater than 2
                if(ValidRows >= 2){
                    return true;
                }
            }
        }
		return false;
    }
}
