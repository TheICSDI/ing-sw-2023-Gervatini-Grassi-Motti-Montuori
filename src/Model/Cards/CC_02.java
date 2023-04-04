package Model.Cards;

import Model.Player;
import Model.Tile.type;
import Model.Tile.Tile;

/**
 * CC_02 class implements the logic for checking if the first common goal card is completed by a player.
 * The first common goal card requires four tiles of the same type in the four corners of the bookshelf.
 */
public class CC_02 implements CCStrategy {
	/**
	 * Checks if the common goal is completed
	 * @param p Player
	 * @return a boolean if the common goal card is completed
	 */
	public boolean isCompleted(Player p) {
	Tile[][] current_shelf = p.getShelf();
	type color;
	boolean end=true;
	
	// check if any of the four tile is empty.

	if (current_shelf[0][0].getCategory() == type.EMPTY ||
			current_shelf[0][5].getCategory() == type.EMPTY ||
			current_shelf[4][0].getCategory() == type.EMPTY ||
			current_shelf[4][5].getCategory() == type.EMPTY){
		end=false;
	}
	else{
		// check if all the tiles have the same type of the most low-left one.

		color = current_shelf[0][0].getCategory();
		if(color!= current_shelf[0][5].getCategory() ||
				color != current_shelf[4][0].getCategory() ||
				color != current_shelf[4][5].getCategory()){
			end = false;
		}
	}
	return end;
    }
}
