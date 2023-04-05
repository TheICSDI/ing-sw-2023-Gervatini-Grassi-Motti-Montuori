package main.java.it.polimi.ingsw.model.Cards;

import main.java.it.polimi.ingsw.model.Tile.type;
import main.java.it.polimi.ingsw.model.Player;
import main.java.it.polimi.ingsw.model.Tile.Tile;

public class CC_06 implements CCStrategy {
    @Override
    public boolean isCompleted(Player p) {
        int i,j,count,limit=8;
        Tile[][] curr_shelf =p.getShelf();
        int num_row = curr_shelf.length;
        int num_col = curr_shelf[0].length;
        for(type t : type.values()){
            /*
            not accessible is impossible to have in a shelf but however...
             */
            if(!t.equals(type.NOT_ACCESSIBLE) && !t.equals(type.EMPTY)){
                count = 0;
                for(i = 0; i < num_row; i++){
                    for(j = 0; j < num_col; j++){
                        if(curr_shelf[i][j].getCategory().equals(t)){
                            count++;
                        }
                    }
                }
                /*
                UPDATE caused by the newest information about game's rules interpretation the count control can be:
                or of Exact 8 tiles but counted at EVERY single insertion (means up to 3 times per turn)
                or more equal 8 tiles ONLY A TIME for turn.
                 */
                if(count == limit){
                    return true;
                }
            }
        }
        return false;
    }
}
