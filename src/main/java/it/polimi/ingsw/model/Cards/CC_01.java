/**
 * CC_01 class implements the logic for checking if the common goal card number 1 is completed by a player.
 * It requires the player to have at least six groups, each containing at least two tiles of the same type.
 * @author Giulio Montuori
 */
package it.polimi.ingsw.model.Cards;

import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.Tile.Tile;
import it.polimi.ingsw.model.Tile.type;

public class CC_01 implements CCStrategy {
    private final int id=1;
    @Override
    public int getId(){return this.id;}

    /**
     * Checks if the common goal is completed.
     *
     * @param p a player.
     * @return true only if count the player has at least six groups that respect the rule of the card.
     */
    public boolean isCompleted(Player p)
    {
        Tile[][] current_shelf = p.getShelf();
        int count = 0;
        int num_row = current_shelf.length;
        int num_col = current_shelf[0].length;
        int new_j, new_i;
        type current_tile;

        for(int i = 0; i < num_row; i++)
        {
            for(int j = 0; j < num_col; j++)
            {
                new_i = 0;
                new_j = 0;
                current_tile = current_shelf[i][j].getCategory();
                if(!current_tile.equals(type.EMPTY)) {

                    if (!(
                            // The current tile is not the same as the tile to the left
                            (j != 0 && current_shelf[i][j - 1].getCategory() == current_tile)
                                    // The current tile is not the same as the tile above
                                    || (i != 0 && current_shelf[i - 1][j].getCategory() == current_tile)
                                    // The next tile to the right is not the same as the tile to its right
                                    || (j + 2 < num_col && current_shelf[i][j + 1].getCategory() == current_tile && current_shelf[i][j + 2].getCategory() == current_tile)
                                    // The next tile down is not the same as the tile below it
                                    || (i + 2 < num_row && current_shelf[i + 1][j].getCategory() == current_tile && current_shelf[i + 2][j].getCategory() == current_tile))
                            && (
                            // The current tile is the same as the tile to the right
                            (j + 1 < num_col && current_shelf[i][j + 1].getCategory() == current_tile)
                                    // The current tile is the same as the tile below
                                    || (i + 1 < num_row && current_shelf[i + 1][j].getCategory() == current_tile))) {

                        count++;
                        if (count == 6) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }
}