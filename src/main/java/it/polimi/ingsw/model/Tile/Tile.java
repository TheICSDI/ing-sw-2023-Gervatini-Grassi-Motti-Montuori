package it.polimi.ingsw.model.Tile;


/** Represents a tile of the game.
 * @author Marco Gervatini, Giulio Montuori.
 */
public class Tile {
	private final type category;
	private String color;
	private char initial;
	private String image;

	public String getImage() {
		return image;
	}

	/** Creates a tile according to a given type. */
	public Tile(String any) {
		this.category = type.valueOf(any.toUpperCase());
		switch (category){
			case NOT_ACCESSIBLE -> {
				color =/*"\u001b[48;2;33;19;13m"*/"\u001b[48;2;117;61;34m";
				initial=' ';
			}
			case EMPTY -> {  //"\u001b[48;2;<R code>;<G code>;<B code> + output text" //Formato per usare i colori rgb se serve
				color ="\033[48;5;94m";
				initial=' ';
			}
			case CATS -> {
				color = "\033[42m";
				initial='C';
				image="/Images/item tiles/Gatti1.1.png";
			}
			case TROPHIES -> {
				color = "\033[46m";
				initial='T';
				image="/Images/item tiles/Trofei1.1.png";
			}
			case FRAMES -> {
				color = "\033[44m";
				initial='F';
				image="/Images/item tiles/Cornici1.1.png";
			}
			case PLANTS -> {
				color = "\033[45m";
				initial='P';
				image="/Images/item tiles/Piante1.1.png";
			}
			case BOOKS -> {
				color = "\033[47m";
				initial='B';
				image="/Images/item tiles/Libri1.1.png";
			}
			case GAMES -> {
				color = "\033[43m";
				initial='G';
				image="/Images/item tiles/Giochi1.1.png";
			}
		}
	}

	/** Gets the category of the tile. */
	public type getCategory() {
        return this.category;
  	}
	public String getColor() { return color;}
	public char getInitial() {return initial;}
}

