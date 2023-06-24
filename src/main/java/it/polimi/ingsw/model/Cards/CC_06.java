/** CC_06 class implements the logic for checking if the common goal card number 6 is completed by a player.
 * It requires the player to have eight tiles of the same type.
 * @author Marco Gervatini. */
package it.polimi.ingsw.model.Cards;

import it.polimi.ingsw.model.Tile.type;
import it.polimi.ingsw.model.Player;

public class CC_06 implements CCStrategy {
    private final int id=6;
    @Override
    public int getId(){return this.id;}

    /**
     * Checks if the common goal is completed.
     * @param p a player.
     * @return true only if the common goal card is completed.
     */
    public boolean isCompleted(Player p) {
        //For each type (but no not_accessible or empty)
        for(type t : type.values()){
            if(!t.equals(type.NOT_ACCESSIBLE) && !t.equals(type.EMPTY)){
                int count = 0;
                //For each element in the shelf of the player
                for(int i = 0; i < p.getNumRows(); i++){
                    for(int j = 0; j < p.getNumCols(); j++){
                        //It increments the count
                        if(p.getShelf()[i][j].getCategory().equals(t)){
                            count++;
                            //It returns true only if it has found 8 tiles of the same time
                            if(count == 8){
                                return true;
                            }
                        }
                    }
                }
            }
        }
        return false;
    }
}
