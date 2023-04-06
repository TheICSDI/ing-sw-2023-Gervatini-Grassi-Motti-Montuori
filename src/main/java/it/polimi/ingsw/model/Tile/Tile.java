package main.java.it.polimi.ingsw.model.Tile;


/** Represents a tile of the game.
 * @author Marco Gervatini, Giulio Montuori.
 */
public class Tile {
	private final type category;

	/** Creates a tile according to a given type. */
	public Tile(String any) {
		this.category = type.valueOf(any.toUpperCase());
	}

	/** Gets the category of the tile. */
	public type getCategory() {
        return this.category;
  	}
}

