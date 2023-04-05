package main.java.it.polimi.ingsw.model.Cards;

import main.java.it.polimi.ingsw.model.Player;
import main.java.it.polimi.ingsw.model.Tile.Tile;
import main.java.it.polimi.ingsw.model.Tile.type;
import main.java.polimi.ingsw.Model.Tile.*;

public class CC_11 implements CCStrategy {
    public boolean isCompleted(Player p) {
        Tile[][] curr_shelf=p.getShelf();
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 4; j++) {
                if(!curr_shelf[i][j].getCategory().equals(type.EMPTY)) {
                    type curr_type = curr_shelf[i][j].getCategory();
                    if(curr_shelf[i+2][j].getCategory().equals(curr_type) 
							&& curr_shelf[i][j+2].getCategory().equals(curr_type)
							&& curr_shelf[i+1][j+1].getCategory().equals(curr_type)
							&& curr_shelf[i+2][j+2].getCategory().equals(curr_type) ){
                        return true;
                    }
                }
            }
        }
        return false;
    }
}
