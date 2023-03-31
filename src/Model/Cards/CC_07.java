package Model.Cards;

import Model.Player;
import Model.Tile.Tile;
import Model.Tile.type;

public class CC_07 implements CCStrategy {
    public boolean isCompleted(Player p) {
        Tile[][] current_shelf = p.getShelf();
        int i,j,count;
        boolean end=false;
        /*
        four possible existing diagonals and so four loop to check the state of the shelf.
         */
        for(i=0,j=0,count =0;i<5 && j<5 && !end;i++,j++){
            if(current_shelf[i][j].getCategory() == current_shelf[0][0].getCategory() ||
                current_shelf[i][j].getCategory() == type.EMPTY &&
                current_shelf[i][j].getCategory() == type.NOT_ACCESSIBLE){
                count++;
            }
            if(count == 5){
                end = true;
            }
        }
        for(i=0,j=1,count =0;i<5 && j<6 && !end;i++,j++){
            if(current_shelf[i][j].getCategory() == current_shelf[0][0].getCategory() ||
                current_shelf[i][j].getCategory() == type.EMPTY &&
                current_shelf[i][j].getCategory() == type.NOT_ACCESSIBLE){
                count++;
            }
            if(count == 5){
                end = true;
            }
        }
        for(i=4,j=0,count =0;i>0 && j<5 && !end;i--,j++){
            if(current_shelf[i][j].getCategory() == current_shelf[0][0].getCategory() ||
                current_shelf[i][j].getCategory() == type.EMPTY &&
                current_shelf[i][j].getCategory() == type.NOT_ACCESSIBLE){
                count++;
            }
            if(count == 5){
                end = true;
            }
        }
        for(i=4,j=1,count =0;i<5 && j<6 && !end;i--,j++){
            if(current_shelf[i][j].getCategory() == current_shelf[0][0].getCategory() ||
                current_shelf[i][j].getCategory() == type.EMPTY &&
                current_shelf[i][j].getCategory() == type.NOT_ACCESSIBLE){
                count++;
            }
            if(count == 5){
                end = true;
            }
        }
        return end;
    }
}
