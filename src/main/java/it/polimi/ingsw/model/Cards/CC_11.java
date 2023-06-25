/** CC_11 class implements the logic for checking if the common goal card number 11 is completed by a player.
 * It requires the player to have five tiles of the same type forming an X.
 * @author Andrea Grassi.
 */
package it.polimi.ingsw.model.Cards;

import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.Tile.type;

public class CC_11 implements CCStrategy {
    private final int id=11;
    @Override
    public int getId(){return this.id;}

    /**
     * Checks if the common goal is completed.
     * @param p a player.
     * @return true only if the common goal card is completed.
     */
    public boolean isCompleted(Player p) {
        int nRow=6;
        int nCol=5;
        for (int i = 0; i < nRow-2; i++) {
            for (int j = 0; j < nCol-2; j++) {
                if(!p.getShelf()[i][j].getCategory().equals(type.EMPTY)) {
                    type curr_type = p.getShelf()[i][j].getCategory();
                    if(p.getShelf()[i+2][j].getCategory().equals(curr_type)
							&& p.getShelf()[i][j+2].getCategory().equals(curr_type)
							&& p.getShelf()[i+1][j+1].getCategory().equals(curr_type)
							&& p.getShelf()[i+2][j+2].getCategory().equals(curr_type) ){
                        return true;
                    }
                }
            }
        }
        return false;
    }
}
