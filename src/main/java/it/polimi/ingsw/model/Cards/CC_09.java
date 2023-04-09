/**
 * CC_09 class implements the logic for checking if the common goal card number 9 is completed by a player.
 * It requires the player to have two columns each formed by 6 different types of tiles.
 * @author Marco Gervatini
 */
package main.java.it.polimi.ingsw.model.Cards;
import main.java.it.polimi.ingsw.model.Player;
import main.java.it.polimi.ingsw.model.Tile.type;

import java.util.ArrayList;
import java.util.List;

public class CC_09 implements CCStrategy {
    /**
     * Checks if the common goal is completed.
     *
     * @param p a player.
     * @return true only if the common goal card is completed.
     */
    public boolean isCompleted(Player p) {
        //List of types encountered in a column
        List<type> currColTypes = new ArrayList<>();
        int validCols = 0;

        //For each column
        for(int j = 0; j < p.getNumCols(); j++){
            //Clear the list of types encountered in the column
            currColTypes.clear();
            //For each row
            for(int i = 0; i < p.getNumRows(); i++){
                type currType = p.getShelf()[i][j].getCategory();
                //If the current tile is not empty and the current column does not contain this type
                if(!currType.equals(type.EMPTY) && !currColTypes.contains(currType)){
                    currColTypes.add(currType);
                }
            }
            //If the columns has many types as the number of rows then it is a valid column
            if(currColTypes.size() == p.getNumRows()){
                validCols++;
                //It returns true if the number of valid columns is greater than 2
                if(validCols >= 2){
                   return true;
                }
            }
        }
        return false;
    }
}
