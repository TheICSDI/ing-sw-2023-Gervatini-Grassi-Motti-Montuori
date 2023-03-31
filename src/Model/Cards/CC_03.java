package Model.Cards;
import Model.Player;
import Model.Tile.Tile;
import Model.Tile.type;

public class CC_03 implements CCStrategy {
    public boolean isCompleted(Player p) {
        Tile[][] current_shelf = p.getShelf();
        boolean end=true;
        if (current_shelf[0][0].getCategory() == type.EMPTY ||
                current_shelf[0][5].getCategory() == type.EMPTY ||
                current_shelf[4][0].getCategory() == type.EMPTY ||
                current_shelf[4][5].getCategory() == type.EMPTY){
            end=false;
        }
        else{
            if(current_shelf[0][0].getCategory() != current_shelf[0][5].getCategory() ||
                current_shelf[0][0].getCategory() != current_shelf[4][0].getCategory() ||
                current_shelf[0][0].getCategory() != current_shelf[4][5].getCategory()){
                end = false;
            }
        }
        return end;
    }
}
