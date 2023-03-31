package Model.Cards;
import Model.Player;
import Model.Tile.Tile;
import Model.Tile.type;

import java.util.ArrayList;
import java.util.List;

public class CC_09 implements CCStrategy {
    public boolean isCompleted(Player p) {
        Tile[][] current_shelf = p.getShelf();
        type current_tile_color;
        List<type> current_column_colors = new ArrayList<>();
        int ok_column=0,i,j;
        boolean end= false;
        /*
        assuming that the ok_columns have to be exactly 2 and no more.
         */
        for(i=0;i<5;i++){
            for(j=0;j<6;j++){
                current_tile_color = current_shelf[i][j].getCategory();
                if(current_tile_color!= type.EMPTY &&
                        current_tile_color!= type.NOT_ACCESSIBLE &&
                        ! current_column_colors.contains(current_tile_color)){
                    current_column_colors.add(current_tile_color);

                }
            }
            /*
            other case in which tile can't be of the NOT_ACCESSIBLE type.
             */
            if(current_column_colors.size() == 6){
                ok_column++;
            }
            current_column_colors.clear();
        }
        if(ok_column == 2){
            end= true;
        }
        return end;
    }
}
