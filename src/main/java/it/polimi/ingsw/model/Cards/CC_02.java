package it.polimi.ingsw.model.Cards;

import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.Tile.Tile;
import it.polimi.ingsw.model.Tile.type;


/** CC_02 class implements the logic for checking if the common goal card number 2 is completed by a player.
 * It requires four tiles of the same type in the four corners of the bookshelf.
 * @author Marco Gervatini
 */
public class CC_02 implements CCStrategy {
	private final int id=2;
	@Override
	public int getId(){return this.id;}

	/**
	 * Checks if the common goal is completed.
	 * @param p a player.
	 * @return true only if the common goal card is completed.
	 */
	public boolean isCompleted(Player p) {
		Tile[][] current_shelf = p.getShelf();
		type color;
		boolean end = true;

		//Check if any of the four tile is empty.
		if (current_shelf[0][0].getCategory().equals(type.EMPTY)
				|| current_shelf[5][0].getCategory().equals(type.EMPTY)
				|| current_shelf[0][4].getCategory().equals(type.EMPTY)
				|| current_shelf[5][4].getCategory().equals(type.EMPTY)){
			end=false;
		} else{
			//Check if all the tiles have the same type of the most high-left one.
			color = current_shelf[0][0].getCategory();
			if(!color.equals(current_shelf[5][0].getCategory())
					|| !color.equals(current_shelf[0][4].getCategory())
					|| !color.equals(current_shelf[5][4].getCategory())){
				end = false;
			}
		}
		return end;
    }
}
