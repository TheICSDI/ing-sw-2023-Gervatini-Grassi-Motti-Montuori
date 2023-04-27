package main.java.it.polimi.ingsw.model.Tile;


/** Represents a tile of the game.
 * @author Marco Gervatini, Giulio Montuori.
 */
public class Tile {
	private final type category;
	private String color;

	/** Creates a tile according to a given type. */
	public Tile(String any) {

		this.category = type.valueOf(any.toUpperCase());
		switch (category){
			case NOT_ACCESSIBLE, EMPTY -> color ="\033[40m";
			case CATS -> color = "\033[42m";
			case TROPHIES -> color = "\033[46m";
			case FRAMES -> color = "\033[44m";
			case PLANTS -> color = "\033[0;105m";
			case BOOKS -> color = "\033[47m";
			case GAMES -> color = "\033[43m";
		}
	}

	/** Gets the category of the tile. */
	public type getCategory() {
        return this.category;
  	}
	public String getColor() { return color;}
}

