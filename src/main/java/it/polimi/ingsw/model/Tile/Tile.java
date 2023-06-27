/** Represents a tile of the game.
 * @author Marco Gervatini, Giulio Montuori.
 */
package it.polimi.ingsw.model.Tile;

public class Tile {
	private final type category;
	private String color;
	private char initial;
	private String image;

	/** Creates a tile according to a given type.
	 * @param any name of the tile's type.
	 * @param imageNumber number of the image. */
	public Tile(String any , int imageNumber) {
		this.category = type.valueOf(any.toUpperCase());
		switch (category){
			case NOT_ACCESSIBLE -> {
				color = "\u001b[48;2;117;61;34m";
				initial = ' ';
			}
			case EMPTY -> {
				color ="\033[48;5;94m";
				initial = ' ';
			}
			case CATS -> {
				color = "\033[42m";
				initial = 'C';
				image = "/Images/item tiles/Gatti1." + imageNumber + ".png";
			}
			case TROPHIES -> {
				color = "\033[46m";
				initial = 'T';
				image = "/Images/item tiles/Trofei1." + imageNumber + ".png";
			}
			case FRAMES -> {
				color = "\033[44m";
				initial='F';
				image="/Images/item tiles/Cornici1." + imageNumber + ".png";
			}
			case PLANTS -> {
				color = "\033[45m";
				initial = 'P';
				image = "/Images/item tiles/Piante1." + imageNumber + ".png";
			}
			case BOOKS -> {
				color = "\u001b[48;2;163;163;157m";
				initial = 'B';
				image = "/Images/item tiles/Libri1." + imageNumber + ".png";
			}
			case GAMES -> {
				color = "\033[43m";
				initial = 'G';
				image = "/Images/item tiles/Giochi1." + imageNumber + ".png";
			}
		}
	}

	/** Creates a tile according to a given type.
	 * @param any name of the tile's type. */
	public Tile(String any) {
		this.category = type.valueOf(any.toUpperCase());
		switch (category){
			case NOT_ACCESSIBLE -> {
				color = "\u001b[48;2;117;61;34m";
				initial = ' ';
			}
			case EMPTY -> {
				color = "\033[48;5;94m";
				initial = ' ';
			}
			case CATS -> {
				color = "\033[42m";
				initial = 'C';
			}
			case TROPHIES -> {
				color = "\033[46m";
				initial = 'T';
			}
			case FRAMES -> {
				color = "\033[44m";
				initial = 'F';
			}
			case PLANTS -> {
				color = "\033[45m";
				initial = 'P';
			}
			case BOOKS -> {
				color = "\033[47m";
				initial = 'B';
			}
			case GAMES -> {
				color = "\033[43m";
				initial = 'G';
			}
		}
	}

	/** Gets the category of the tile. */
	public type getCategory() {
        return this.category;
  	}
	/** Gets the color of the tile. */
	public String getColor() { return color;}
	/** Gets the initial of the tile. */
	public char getInitial() {return initial;}
	/** Gets the path of the tile's image. */
	public String getImage() {
		return image;
	}
}

