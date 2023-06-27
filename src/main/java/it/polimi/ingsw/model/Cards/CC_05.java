package it.polimi.ingsw.model.Cards;

import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.Tile.type;

import java.util.HashSet;
import java.util.Set;

/** CC_05 class implements the logic for checking if the common goal card number 5 is completed by a player.
 * It requires the player to have three columns each formed by 6 tiles of maximum three different types.
 * One column can show the same or a different combination of another column.
 * @author Andrea Grassi. */
public class CC_05 implements CCStrategy {
    private final int id=5;
    @Override
    public int getId(){return this.id;}

    /**
     * Checks if the common goal is completed.
     * @param p a player.
     * @return true only if the common goal card is completed.
     */
    public boolean isCompleted(Player p) {
        //number of valid columns
        int ValidColumns = 0;
        //Set of different types already encountered in a column
        Set<type> Types;
        boolean Valid;

        //For each column
        for (int j = 0; j < p.getNumCols(); j++) {
            Valid = true;
            Types = new HashSet<>();
            //For each row in the selected column
            for (int i = 0; i < p.getNumRows() && Valid; i++) {
                //If the element is empty, it is automatically false
                if(p.getShelf()[i][j].getCategory().equals(type.EMPTY)){
                    Valid = false;
                }
                Types.add(p.getShelf()[i][j].getCategory());
                if(Types.size() > 3){
                    Valid = false;
                }
            }

            //If there are no more than three different types in the same column, it is added to the valid ones
            if(Valid){
                ValidColumns++;
                //It returns true if the number of valid columns equal 3
                if(ValidColumns == 3){
                    return true;
                }
            }
        }
        return false;
    }
}
