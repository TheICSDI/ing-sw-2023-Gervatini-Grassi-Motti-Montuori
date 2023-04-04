package it.polimi.ingsw.Model.Cards;


import it.polimi.ingsw.Model.Player;
import it.polimi.ingsw.Model.Tile.Tile;
import it.polimi.ingsw.Model.Tile.type;

import java.util.ArrayList;
import java.util.List;

public class CC_09 implements CCStrategy {
    public boolean isCompleted(Player p) {
        Tile[][] current_shelf = p.getShelf();
        type current_tile_color;
        List<type> current_column_colors = new ArrayList<>();
        int ok_column=0,i,j;
        boolean end= false;

        for(i=0;i<5;i++){
            for(j=0;j<6;j++){
                current_tile_color = current_shelf[i][j].getCategory();
                /*
                controls that every tile in a column isn't empty and isn't not_accesible (the second is formally  a
                not reachable state) if the type of [i][j] isn't yet been found in the column it will be added to the list,
                when the column had been checked the method verify if in it are six different type of tile, which means that the column
                has all the types in here.
                */
                if(current_tile_color!= type.EMPTY &&
                        current_tile_color!= type.NOT_ACCESSIBLE &&
                        ! current_column_colors.contains(current_tile_color)){
                    current_column_colors.add(current_tile_color);

                }
            }
            if(current_column_colors.size() == 6){
                ok_column++;
            }
            current_column_colors.clear();
        }
        /*
        assuming that the ok_columns have to be exactly 2 and no more.
         */
        if(ok_column == 2){
            end= true;
        }
        return end;
    }
}
