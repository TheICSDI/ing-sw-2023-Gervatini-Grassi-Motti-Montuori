package it.polimi.ingsw.model.Cards;

import it.polimi.ingsw.model.Tile.Tile;
import it.polimi.ingsw.model.Tile.type;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Objects;

/** Represents the personal goal card. Each player has one.
 * At the start of the game, depending on the number of player, different integers will be randomically generated.
 * Thanks to the constructor a personal goal card will be generated as a matrix of tiles.
 * @author Giulio Montuori.
 */
public class PersonalCard{
	private final int id;
	private final Tile[][] card;
	private final int numRows = 6;
	private final int numCols = 5;

	/** Creates a personal goal card based on the id.
	 * Each card has unique id.
	 * @param uid represent the number of the card to be created. It goes from 0 to 11. */
	public PersonalCard(int uid) {
		this.id = uid;
		this.card = new Tile[numRows][numCols]; //fixed size
		personalCardParser();
	}

	/** A parser for the personal_card.json that contains all 12 personal goal cards. */
	private void personalCardParser() {
		JSONParser parser = new JSONParser();
		JSONArray personalCardFile = null;
		Tile temp;

		try {
			//FileInputStream pathFile = new FileInputStream("JSON/personal_card.json");
			personalCardFile = (JSONArray) parser.parse(new InputStreamReader(Objects.requireNonNull(getClass().getResourceAsStream("/JSON/personal_card.json"))));

		} catch (ParseException | IOException e) {
			e.printStackTrace();
		}

		//Fill the card with empty tiles
		for (int i = 0; i < this.numRows; i++) {
			for (int j = 0; j < this.numCols; j++) {
				this.card[i][j] = new Tile("empty",0);
			}
		}

		JSONObject card = (JSONObject) personalCardFile.get(this.id);
		JSONArray cardTiles = (JSONArray) card.get("tiles");
		for (int i = 0; i < cardTiles.size(); i++) {
			JSONObject coordinate = (JSONObject) cardTiles.get(i);
			temp = new Tile(coordinate.get("type").toString(),0);
			int x = Integer.parseInt(coordinate.get("x").toString());
			int y = Integer.parseInt(coordinate.get("y").toString());
			this.card[x][y] = temp;
		}
	}

	/** Calculate the points given by the personal goal card according to the shelf of the player.
	 * @param shelf the player's shelf.
	 * @return the points corresponding the matches between the shelf and the personal goal card.
	 */
	public int calculatePoints(Tile[][] shelf) {
		int matches = 0, score = 0;
		int max_matches = 6;

		for(int i = 0; i < this.numRows; i++) {
			for(int j = 0; matches != max_matches && j < this.numCols; j++) {
				if(!shelf[i][j].getCategory().equals(type.EMPTY)
						&& shelf[i][j].getCategory().equals(this.card[i][j].getCategory())) {
					matches++;
				}
			}
		}
		switch (matches) {
			case 1 -> score = 1;
			case 2 -> score = 2;
			case 3 -> score = 4;
			case 4 -> score = 6;
			case 5 -> score = 9;
			case 6 -> score = 12;
		}
		return score;
	}

	/** Gets the personal card. */
	public Tile[][] getCard() {
		return card;
	}

	/** Gets the id of the personal card. */
	public int getId() {
		return id;
	}
}
