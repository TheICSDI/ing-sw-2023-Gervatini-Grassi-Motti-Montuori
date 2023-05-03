package test;

import it.polimi.ingsw.model.Cards.PersonalCard;
import it.polimi.ingsw.model.Tile.Tile;
import it.polimi.ingsw.model.Tile.type;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;


import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

import static org.junit.jupiter.api.Assertions.*;

/** Tests for class PersonalCard.java
 * @author Giulio Montuori
 */
class PersonalCardTest {

    Tile[][] PC00 = {{new Tile("plants"), new Tile("empty"), new Tile("frames"), new Tile("empty"), new Tile("empty"),},
            {new Tile("empty"), new Tile("empty"), new Tile("empty"), new Tile("empty"), new Tile("cats"),},
            {new Tile("empty"), new Tile("empty"), new Tile("empty"), new Tile("books"), new Tile("empty"),},
            {new Tile("empty"), new Tile("games"), new Tile("empty"), new Tile("empty"), new Tile("empty"),},
            {new Tile("empty"), new Tile("empty"), new Tile("empty"), new Tile("empty"), new Tile("empty"),},
            {new Tile("empty"), new Tile("empty"), new Tile("trophies"), new Tile("empty"), new Tile("empty"),}};

    Tile[][] PC01 = {{new Tile("empty"), new Tile("empty"), new Tile("empty"), new Tile("empty"), new Tile("empty"),},
            {new Tile("empty"), new Tile("plants"), new Tile("empty"), new Tile("empty"), new Tile("empty"),},
            {new Tile("cats"), new Tile("empty"), new Tile("games"), new Tile("empty"), new Tile("empty"),},
            {new Tile("empty"), new Tile("empty"), new Tile("empty"), new Tile("empty"), new Tile("books"),},
            {new Tile("empty"), new Tile("empty"), new Tile("empty"), new Tile("trophies"), new Tile("empty"),},
            {new Tile("empty"), new Tile("empty"), new Tile("empty"), new Tile("empty"), new Tile("frames"),}};
    Tile[][] PC02 = {{new Tile("empty"), new Tile("empty"), new Tile("empty"), new Tile("empty"), new Tile("empty"),},
            {new Tile("frames"), new Tile("empty"), new Tile("empty"), new Tile("games"), new Tile("empty"),},
            {new Tile("empty"), new Tile("empty"), new Tile("plants"), new Tile("empty"), new Tile("empty"),},
            {new Tile("empty"), new Tile("cats"), new Tile("empty"), new Tile("empty"), new Tile("trophies"),},
            {new Tile("empty"), new Tile("empty"), new Tile("empty"), new Tile("empty"), new Tile("empty"),},
            {new Tile("books"), new Tile("empty"), new Tile("empty"), new Tile("empty"), new Tile("empty"),}};
    Tile[][] PC03 = {{new Tile("empty"), new Tile("empty"), new Tile("empty"), new Tile("empty"), new Tile("games"),},
            {new Tile("empty"), new Tile("empty"), new Tile("empty"), new Tile("empty"), new Tile("empty"),},
            {new Tile("trophies"), new Tile("empty"), new Tile("frames"), new Tile("empty"), new Tile("empty"),},
            {new Tile("empty"), new Tile("empty"), new Tile("empty"), new Tile("plants"), new Tile("empty"),},
            {new Tile("empty"), new Tile("books"), new Tile("cats"), new Tile("empty"), new Tile("empty"),},
            {new Tile("empty"), new Tile("empty"), new Tile("empty"), new Tile("empty"), new Tile("empty"),}};
    Tile[][] PC04 = {{new Tile("empty"), new Tile("empty"), new Tile("empty"), new Tile("empty"), new Tile("empty"),},
            {new Tile("empty"), new Tile("trophies"), new Tile("empty"), new Tile("empty"), new Tile("empty"),},
            {new Tile("empty"), new Tile("empty"), new Tile("empty"), new Tile("empty"), new Tile("empty"),},
            {new Tile("empty"), new Tile("frames"), new Tile("books"), new Tile("empty"), new Tile("empty"),},
            {new Tile("empty"), new Tile("empty"), new Tile("empty"), new Tile("empty"), new Tile("plants"),},
            {new Tile("games"), new Tile("empty"), new Tile("empty"), new Tile("cats"), new Tile("empty"),}};
    Tile[][] PC05 = {{new Tile("empty"), new Tile("empty"), new Tile("trophies"), new Tile("empty"), new Tile("cats"),},
            {new Tile("empty"), new Tile("empty"), new Tile("empty"), new Tile("empty"), new Tile("empty"),},
            {new Tile("empty"), new Tile("empty"), new Tile("empty"), new Tile("books"), new Tile("empty"),},
            {new Tile("empty"), new Tile("empty"), new Tile("empty"), new Tile("empty"), new Tile("empty"),},
            {new Tile("empty"), new Tile("games"), new Tile("empty"), new Tile("frames"), new Tile("empty"),},
            {new Tile("plants"), new Tile("empty"), new Tile("empty"), new Tile("empty"), new Tile("empty"),}};
    Tile[][] PC06 = {{new Tile("cats"), new Tile("empty"), new Tile("empty"), new Tile("empty"), new Tile("empty"),},
            {new Tile("empty"), new Tile("empty"), new Tile("empty"), new Tile("frames"), new Tile("empty"),},
            {new Tile("empty"), new Tile("plants"), new Tile("empty"), new Tile("empty"), new Tile("empty"),},
            {new Tile("trophies"), new Tile("empty"), new Tile("empty"), new Tile("empty"), new Tile("empty"),},
            {new Tile("empty"), new Tile("empty"), new Tile("empty"), new Tile("empty"), new Tile("games"),},
            {new Tile("empty"), new Tile("empty"), new Tile("books"), new Tile("empty"), new Tile("empty"),}};
    Tile[][] PC07 = {{new Tile("empty"), new Tile("empty"), new Tile("empty"), new Tile("empty"), new Tile("frames"),},
            {new Tile("empty"), new Tile("cats"), new Tile("empty"), new Tile("empty"), new Tile("empty"),},
            {new Tile("empty"), new Tile("empty"), new Tile("trophies"), new Tile("empty"), new Tile("empty"),},
            {new Tile("plants"), new Tile("empty"), new Tile("empty"), new Tile("empty"), new Tile("empty"),},
            {new Tile("empty"), new Tile("empty"), new Tile("empty"), new Tile("books"), new Tile("empty"),},
            {new Tile("empty"), new Tile("empty"), new Tile("empty"), new Tile("games"), new Tile("empty"),}};
    Tile[][] PC08 = {{new Tile("empty"), new Tile("empty"), new Tile("games"), new Tile("empty"), new Tile("empty"),},
            {new Tile("empty"), new Tile("empty"), new Tile("empty"), new Tile("empty"), new Tile("empty"),},
            {new Tile("empty"), new Tile("empty"), new Tile("cats"), new Tile("empty"), new Tile("empty"),},
            {new Tile("empty"), new Tile("empty"), new Tile("empty"), new Tile("empty"), new Tile("books"),},
            {new Tile("empty"), new Tile("trophies"), new Tile("empty"), new Tile("empty"), new Tile("plants"),},
            {new Tile("frames"), new Tile("empty"), new Tile("empty"), new Tile("empty"), new Tile("empty"),}};
    Tile[][] PC09 = {{new Tile("empty"), new Tile("empty"), new Tile("empty"), new Tile("empty"), new Tile("trophies"),},
            {new Tile("empty"), new Tile("games"), new Tile("empty"), new Tile("empty"), new Tile("empty"),},
            {new Tile("books"), new Tile("empty"), new Tile("empty"), new Tile("empty"), new Tile("empty"),},
            {new Tile("empty"), new Tile("empty"), new Tile("empty"), new Tile("cats"), new Tile("empty"),},
            {new Tile("empty"), new Tile("frames"), new Tile("empty"), new Tile("empty"), new Tile("empty"),},
            {new Tile("empty"), new Tile("empty"), new Tile("empty"), new Tile("plants"), new Tile("empty"),}};
    Tile[][] PC10 = {{new Tile("empty"), new Tile("empty"), new Tile("plants"), new Tile("empty"), new Tile("empty"),},
            {new Tile("empty"), new Tile("books"), new Tile("empty"), new Tile("empty"), new Tile("empty"),},
            {new Tile("games"), new Tile("empty"), new Tile("empty"), new Tile("empty"), new Tile("empty"),},
            {new Tile("empty"), new Tile("empty"), new Tile("frames"), new Tile("empty"), new Tile("empty"),},
            {new Tile("empty"), new Tile("empty"), new Tile("empty"), new Tile("empty"), new Tile("cats"),},
            {new Tile("empty"), new Tile("empty"), new Tile("empty"), new Tile("trophies"), new Tile("empty"),}};
    Tile[][] PC11 = {{new Tile("empty"), new Tile("empty"), new Tile("books"), new Tile("empty"), new Tile("empty"),},
            {new Tile("empty"), new Tile("plants"), new Tile("empty"), new Tile("empty"), new Tile("empty"),},
            {new Tile("empty"), new Tile("empty"), new Tile("frames"), new Tile("empty"), new Tile("empty"),},
            {new Tile("empty"), new Tile("empty"), new Tile("empty"), new Tile("trophies"), new Tile("empty"),},
            {new Tile("empty"), new Tile("empty"), new Tile("empty"), new Tile("empty"), new Tile("games"),},
            {new Tile("cats"), new Tile("empty"), new Tile("empty"), new Tile("empty"), new Tile("empty"),}};
    @Test
    void PersonalCard()
    {
        int num_col = 5;
        int num_row = 6;
        ArrayList<Tile[][]> all_cards = new ArrayList<Tile[][]>();

        // list of all personal cards
        all_cards.add(this.PC00);
        all_cards.add(this.PC01);
        all_cards.add(this.PC02);
        all_cards.add(this.PC03);
        all_cards.add(this.PC04);
        all_cards.add(this.PC05);
        all_cards.add(this.PC06);
        all_cards.add(this.PC07);
        all_cards.add(this.PC08);
        all_cards.add(this.PC09);
        all_cards.add(this.PC10);
        all_cards.add(this.PC11);

        int max_id = 12;
        // Iterate over each card in arraylist
        for(int card = 0; card < max_id; card++)
        {
            PersonalCard temp = new PersonalCard(card);
            // assertEqual on very cell
            for(int i = 0; i < num_row; i++)
            {
                for(int j = 0; j < num_col; j++)
                {
                    // Building an extra error message
                    String message = String.format("Card %d, x = %d, y = %d", card, i, j);
                    assertEquals(temp.getCard()[i][j].getCategory(), all_cards.get(card)[i][j].getCategory(), message);
                }
            }
        }
    }
    /*
        Checks if the json file is correct
     */
    @Test
    void calculatePoints()
    {
        int num_row = 6;
        int num_col = 5;
        Tile[][] shelf = new Tile[num_row][num_col];

        JSONParser parser = new JSONParser();
        JSONArray file = null;
        try{
            FileInputStream pathFile = new FileInputStream("JSON/personal_card_test.json");
            file = (JSONArray) parser.parse(new InputStreamReader(pathFile));
        }catch (FileNotFoundException e){
            e.printStackTrace();
        }catch (ParseException e){
            e.printStackTrace();
        }catch (IOException e){
            e.printStackTrace();
        }
        // Iterate over each card in the JSON file
        for (int card = 0; card < file.size(); card++) {
            // Initialize the shelf with empty tiles
            for (int i = 0; i < num_row; i++) {
                for (int j = 0; j < num_col; j++) {
                    shelf[i][j]=new Tile("EMPTY");
                }
            }
            // Read the tiles from the JSON file
            JSONObject tmp1 = (JSONObject) file.get(card);
            JSONArray tiles= (JSONArray) tmp1.get("tiles");
            for(int index = 0; index < tiles.size(); index++) {
                JSONObject tile = (JSONObject) tiles.get(index);

                int indexX = Integer.parseInt(tile.get("x").toString());
                int indexY = Integer.parseInt(tile.get("y").toString());
                String t = tile.get("type").toString();
                shelf[indexX][indexY] = new Tile(t);
            }
            // Get from file the type of assert needed
            int assert_result = Integer.parseInt(tmp1.get("assert").toString());
            // Building an extra error message
            String message = String.format("\nTest number: %d,", card);
            // Building the personal card
            PersonalCard pc00 = new PersonalCard(0);
            assertEquals(pc00.calculatePoints(shelf), assert_result);
        }
    }
    /*
       -The first six shelves test the correct positioning of the tiles, starting from the maximum to the minimum number,
        and return the corresponding score value based on the correct positions with the personal card.
       -The last six shelves test the correct positioning of all tiles except one, and for each shelf,
        the incorrect tile changes, with all shelves returning a score of 9.
     */
}