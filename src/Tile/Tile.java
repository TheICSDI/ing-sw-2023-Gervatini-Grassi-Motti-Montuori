package Tile;

public class Tile {
    private type category;

    public Tile(String any)
		{
			switch (any)
			{
				case "plants":
					this.category = type.PLANTS;
					break;

				case "frames":
					this.category = type.PLANTS;
					break;

				case "cats":
					this.category = type.PLANTS;
					break;

				case "books":
					this.category = type.PLANTS;
					break;

				case "games":
					this.category = type.PLANTS;
					break;

				case "trophies":
					this.category = type.PLANTS;
					break;
			}

    }

    public type getCategory() {
        return category;
    }
}

