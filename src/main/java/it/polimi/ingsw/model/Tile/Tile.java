package main.java.it.polimi.ingsw.model.Tile;

public class Tile
{
	private type category;
	public Tile(String any)
	{
		switch (any)
		{
			case "plants":
				this.category = type.PLANTS;
				break;

			case "frames":
				this.category = type.FRAMES;
				break;

			case "cats":
				this.category = type.CATS;
				break;

			case "books":
				this.category = type.BOOKS;
				break;

			case "games":
				this.category = type.GAMES;
				break;

			case "trophies":
				this.category = type.TROPHIES;
				break;

			case "not_accessible":
				this.category = type.NOT_ACCESSIBLE;
				break;

			case "empty":
				this.category = type.EMPTY;
				break;
			//should we add a default options of NOT_ACCESSIBLE ?
		}
	}

	public type getCategory() {
        return category;
  }
}

