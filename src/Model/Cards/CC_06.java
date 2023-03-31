package Model.Cards;

import Model.Player;
import Model.Tile.Tile;
import Model.Tile.type;

public class CC_06 implements CCStrategy {
    @Override
    public boolean isCompleted(Player p) {
        int i=0,j=0,count,limit=8;
        Tile[][] current_shelf = p.getShelf();
        for(type t : type.values()){
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
