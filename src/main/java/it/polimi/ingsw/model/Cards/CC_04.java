/** CC_04 class implements the logic for checking if the common goal card number 4 is completed by a player.
 * It requires the player to have two groups, each containing four tiles of the same type in a 2x2 square.
 * @author Giulio Montuori
 */
package it.polimi.ingsw.model.Cards;

import it.polimi.ingsw.model.Tile.Tile;
import it.polimi.ingsw.model.Tile.type;
import it.polimi.ingsw.model.Player;

public class CC_04 implements CCStrategy {
    private final int id=4;
    @Override
    public int getId(){return this.id;}

    /**
     * Checks if the common goal is completed.
     * @param p a player.
     * @return true only if the common goal card is completed.
     */
    public boolean isCompleted(Player p)
    {
        Tile[][] current_shelf = p.getShelf();
        int num_row = current_shelf.length;
        int num_col = current_shelf[0].length;
        int count = 0;
        type current_tile;

        for (int i = 0; i < num_row - 1; i++)
        {
            for (int j = 0; j < num_col - 1; j++)
            {
                current_tile = current_shelf[i][j].getCategory();

                if(!current_tile.equals(type.EMPTY))
                {
                    //Check if there is a 2x2 square
                    if (current_tile.equals(current_shelf[i][j + 1].getCategory())               // Tile right
                            && current_tile.equals(current_shelf[i + 1][j].getCategory())        // Tile down
                            && current_tile.equals(current_shelf[i + 1][j + 1].getCategory()))   // Tile down-right
                    {
                        // Check if there are no additional adjacent tiles of the same type
                        if (!(
                                (i > 0 && current_tile.equals(current_shelf[i - 1][j].getCategory()))                            // Tile up
                                        || (i > 0 && current_tile.equals(current_shelf[i - 1][j + 1].getCategory()))             // Tile up-right
                                        || (j < num_col - 2 && current_tile.equals(current_shelf[i][j + 2].getCategory()))       // Tile right-right
                                        || (j < num_col - 2 && current_tile.equals(current_shelf[i + 1][j + 2].getCategory()))   // Tile down-right-right
                                        || (i < num_row - 2 && current_tile.equals(current_shelf[i + 2][j + 1].getCategory()))   // Tile down-down-right
                                        || (i < num_row - 2 && current_tile.equals(current_shelf[i + 2][j].getCategory()))       // Tile down-down
                                        || (j > 0 && current_tile.equals(current_shelf[i + 1][j - 1].getCategory()))             // Tile down-left
                                        || (j > 0 && current_tile.equals(current_shelf[i][j - 1].getCategory()))))               // Tile left
                        {
                            count++;
                            if (count == 2) {
                                return true;
                            }
                        }
                    }
                }

            }
        }

        return false;
    }
}
