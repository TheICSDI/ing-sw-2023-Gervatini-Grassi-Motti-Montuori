package Model.Cards;
import Model.Player;
import Model.Tile.Tile;
import Model.Tile.type;

public class CC_03 implements CCStrategy {
    public boolean isCompleted(Player p) {
        Tile[][] current_shelf = p.getShelf();
        type color;
        boolean end=true;
        /*
        check if any of the four tile is empty.
         */
        if (current_shelf[0][0].getCategory() == type.EMPTY ||
                current_shelf[0][5].getCategory() == type.EMPTY ||
                current_shelf[4][0].getCategory() == type.EMPTY ||
                current_shelf[4][5].getCategory() == type.EMPTY){
            end=false;
        }
        else{
            /*
            check if all the tiles have the same type of the most low-left one.
             */
            color = current_shelf[0][0].getCategory();
            if(color!= current_shelf[0][5].getCategory() ||
                color != current_shelf[4][0].getCategory() ||
                color != current_shelf[4][5].getCategory()){
                end = false;
            }
        }
        return end;
    }
}
