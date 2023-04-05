package main.java.it.polimi.ingsw.model.Cards;

import main.java.it.polimi.ingsw.model.Player;
import main.java.it.polimi.ingsw.model.Tile.Tile;
import main.java.it.polimi.ingsw.model.Tile.type;

/*
    check if the shape of the full part of the shelf is a composition of order high columns.
 */
public class CC_12 implements CCStrategy {
    public boolean isCompleted(Player p) {
        boolean end=false,control=true;
        int i,j;
        Tile[][] current_shelf = p.getShelf();
        type curr_tile_color;
        /*
        first loop control che growing pyramid
         */
        for(i = 0;i < 5 && control; i++){
            for(j = 0; j < 6; j++){
                curr_tile_color = current_shelf[i][j].getCategory();
                /*
                as usual control on NOT_ACCESIBLE should be redundant.
                 */
                /*
                dependently by where is located the tile it has to be full or empty the two ifs check this.
                 */
                if(i <= j && (curr_tile_color.equals(type.EMPTY) || curr_tile_color.equals(type.NOT_ACCESSIBLE))){
                    control = false;
                }
                if(i > j && !curr_tile_color.equals(type.EMPTY)){
                    control = false;
                }
            }
        }
        if(control){
            end = true;
        }
        /*
        second loop control che not descending pyramid
         */
        for(i = 0, control = true; i < 5 && control && !end; i++){
            for(j = 0; j < 6; j++){
                curr_tile_color = current_shelf[i][j].getCategory();
                /*
                dependently by where is located the tile it has to be full or empty the two ifs check this.
                 */
                if((i+j <= 4) && (curr_tile_color.equals(type.EMPTY) || curr_tile_color.equals(type.NOT_ACCESSIBLE))){
                    control = false;
                }
                if(i > j && !curr_tile_color.equals(type.EMPTY)){
                    control = false;
                }
            }
        }
        if(control){
            end = true;
        }
        return end;
    }
}
