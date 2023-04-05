package main.java.it.polimi.ingsw.model.Cards;

import main.java.it.polimi.ingsw.model.Player;
import main.java.it.polimi.ingsw.model.Tile.Tile;
import main.java.it.polimi.ingsw.model.Tile.type;

/**
 * CC_01 class implements the logic for checking if the first common goal card is completed by a player.
 * The first common goal card requires the player to have at least six groups, each containing at least two tiles of the same type.
 */
public class CC_01 implements CCStrategy {
    /**
     * Checks if the common goal is completed
     * @param p Player
     * @return a boolean value true if count >= 6
     */
    public boolean isCompleted(Player p)
    {
        Tile[][] current_shelf = p.getShelf();
        int count = 0;
        int num_row = current_shelf.length;
        int num_col = current_shelf[0].length;
        type current_tile;

        for(int i = 0; i < num_row; i++)
        {
            for(int j = 0; j < num_col && !current_shelf[i][j].getCategory().equals(type.EMPTY); j++)
            {
                current_tile = current_shelf[i][j].getCategory();
                        // Check if the current tile is NOT part of a group in the previous row or column
                if ( !(
                        (j != 0 && current_shelf[i][j - 1].getCategory().equals(current_tile))
                        || (i != 0 && current_shelf[i - 1][j].getCategory().equals(current_tile)) )
                        // Check if the current tile is part of a group in the next row or column
                        &&( (j + 1 != num_col && current_shelf[i][j + 1].getCategory().equals(current_tile))
                        || (i + 1 != num_row && current_shelf[i + 1][j].getCategory().equals(current_tile))) )
                {
                    count++;
                    if (count == 6) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
}
