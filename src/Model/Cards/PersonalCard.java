/** Rapresent the personal goal card. Each player need one 
 * @author Montuori Giulio
 */
package Model.Cards;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;

import Model.Tile.*;

public class PersonalCard {
	private int id;       //each card has unique id
	public Tile[][] card;

	/*
	 * At the start of the game, depending on the number of player, different integers will be generated
	 * and thanks to the JSON the contructer and the personalCardParser a personal goal card will be generated as a matrix of Tile
	 */

	/**
	 *
	 * @param uid
	 */
	public PersonalCard(int uid)
	{
		this.id = uid;
		this.card = new Tile[6][5];        // fixed size

		personalCardParser();
	}

	/**
	 * A parser for the personal_card.json that contains all 12 personal goal cards
	 */
	private void personalCardParser()   // a parser for the JSON file
	{

		JSONParser parser = new JSONParser();
		JSONArray personalCardFile = null;
		Tile temp;

		try {
			FileInputStream pathFile = new FileInputStream("JSON/personal_card.json");
			personalCardFile = (JSONArray) parser.parse(new InputStreamReader(pathFile));

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		for (int i = 0; i < this.card.length; i++) {
			for (int j = 0; j < this.card[0].length; j++) {
				this.card[i][j] = new Tile("not_accessible"); // filling the rest of the matrix with the NOT_ACCESSIBLE type
			}
		}

		JSONObject card = (JSONObject) personalCardFile.get(this.id);
		JSONArray cardTiles = (JSONArray) card.get("tiles");
		for (int i = 0; i < cardTiles.size(); i++)
		{
			JSONObject coordinate = (JSONObject) cardTiles.get(i);
			temp = new Tile(coordinate.get("type").toString());
			this.card[Integer.parseInt(coordinate.get("x").toString())][Integer.parseInt(coordinate.get("y").toString())] = temp;
		}
	}


		/**Calculate the points given by the personal goal card
		 * @param shelf the player's shelf
		 * @return the points corrisponding the matches between the shelf and cads
		 */
	public int calculatePoints(Tile[][] shelf)
	{
		int matches = 0, score = 0, count = 0;
		int max_matches = 6;

		for(int i = 0; i < this.card.length; i++)
		{
			for(int j = 0; count <= max_matches && j < this.card[0].length; j++)
			{
				count++;
			}
		}

		switch (matches)
		{
			case 1:
				score = 1;
				break;

			case 2:
				score = 2;
				break;

			case 3:
				score = 4;
				break;

			case 4:
				score = 6;
				break;

			case 5:
				score = 9;
				break;

			case 6:
				score = 12;
		}
		return score;
	}
}
	/*
	 * Probably we can ditch the matrix rappresentation of the personal goal cards and switch to one like
	 * the JSON file to discuss
	 */
