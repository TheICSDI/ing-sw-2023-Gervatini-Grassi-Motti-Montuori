package it.polimi.ingsw.Model.Cards;


import it.polimi.ingsw.Model.Player;
import it.polimi.ingsw.Model.Tile.Tile;
import it.polimi.ingsw.Model.Tile.type;

public class CC_07 implements CCStrategy {
    public boolean isCompleted(Player p) {
        Tile[][] current_shelf = p.getShelf();
        int i,j,count;
        type curr,first;//curr si the actual tile of the control, first is the  first tile of the diagonal.
        boolean end=false;
        /*
        four possible existing diagonals and so four loop to check the state of the shelf.
         */
        /*
        first loop check the diagonal having as extremes [0][0] and [4][4]
         */
        first = current_shelf[0][0].getCategory();
        for(i=0,j=0,count =0;i<5;i++,j++) {
            curr = current_shelf[i][j].getCategory();
            if (curr == first && curr != type.EMPTY) {
                count++;
            }
        }
        if(count == 5){
            end = true;
        }
        /*
        second loop check the diagonal having as extremes [0][1] and [4][5]
         */
        first = current_shelf[0][1].getCategory();
        for(i=0,j=1,count =0;i<5 && !end;i++,j++) {
            curr = current_shelf[i][j].getCategory();
            if (curr == first && curr != type.EMPTY) {
                count++;
            }
        }
        if(count == 5){
            end = true;
        }
        /*
        third loop check the diagonal having as extremes [0][4] and [4][0]
         */
        first = current_shelf[4][0].getCategory();
        for(i=0,j=0,count =0;i>=0 && !end;i--,j++) {
            curr = current_shelf[i][j].getCategory();
            if (curr == first && curr != type.EMPTY) {
                count++;
            }
        }
        if(count == 5){
            end = true;
        }
        /*
        fourth loop check the diagonal having as extremes [4][5] and [0][1]
         */
        first = current_shelf[4][1].getCategory();
        for(i=0,j=0,count =0;i>=0 && !end;i--,j++) {
            curr = current_shelf[i][j].getCategory();
            if (curr == first && curr != type.EMPTY) {
                count++;
            }
        }
        if(count == 5){
            end = true;
        }
        return end;
    }
}
