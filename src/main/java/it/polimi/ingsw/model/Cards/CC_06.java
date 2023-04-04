package main.java.it.polimi.ingsw.model.Cards;

import main.java.it.polimi.ingsw.model.Tile.type;
import main.java.it.polimi.ingsw.model.Player;
import main.java.it.polimi.ingsw.model.Tile.Tile;

public class CC_06 implements CCStrategy {
    @Override
    public boolean isCompleted(Player p) {
        int i,j,count,limit=8;
        Tile[][] current_shelf = p.getShelf();
        for(type t : type.values()){
            /*
            not accessible is impossible to have in a shelf but however...
             */
            if(t != type.NOT_ACCESSIBLE && t !=  type.EMPTY){
                count =0;
                for(i=0;i<5;i++){
                    for(j=0;j<6;j++){
                        if(current_shelf[i][j].getCategory() == t){
                            count++;
                        }
                    }
                }
                /*
                assuming that the rule not accept a shelf with more tiles of the same color compared to the number requested.
                 */
                if(count == limit){
                    return true;
                }
            }
        }
        return false;
    }
}
