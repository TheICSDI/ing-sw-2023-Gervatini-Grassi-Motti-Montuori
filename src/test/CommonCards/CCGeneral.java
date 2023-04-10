/** Parser for JSON files. Used to test common goal cards.
 * @author Andrea Grassi, Giulio Montuori.
 */
package test.CommonCards;

import main.java.it.polimi.ingsw.model.Cards.CCStrategy;
import main.java.it.polimi.ingsw.model.Player;
import main.java.it.polimi.ingsw.model.Tile.Tile;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CCGeneral {
    /**
     * General check for all common cards, reads one or more shelves from a json file and checks if the respective
     * goal is reached.
     *
     * @param CCard common goal card to be checked.
     * @param JSONName name of the file to read the shelves from.
     */
    public static void check(CCStrategy CCard, String JSONName){
        Player p = new Player("test");
        int nRow = p.getNumRows();
        int nCol = p.getNumCols();
        Tile[][] shelf = new Tile[nRow][nCol];

        JSONParser parser = new JSONParser();
        JSONArray CC_test_File = null;
        try {
            FileInputStream pathFile = new FileInputStream(JSONName);
            CC_test_File = (JSONArray) parser.parse(new InputStreamReader(pathFile));

        } catch (ParseException | IOException e) {
            e.printStackTrace();
        }
        // Iterate over each card in the JSON file
        for (int card = 0; card < CC_test_File.size(); card++) {
            // Initialize the shelf with empty tiles
            for (int i = 0; i < nRow; i++) {
                for (int j = 0; j < nCol; j++) {
                    shelf[i][j] = new Tile("EMPTY");
                }
            }
            // Read the tiles from the JSON file
            JSONObject tmp1 = (JSONObject) CC_test_File.get(card);
            JSONArray tiles= (JSONArray) tmp1.get("tiles");
            for(int index = 0; index < tiles.size(); index++) {
                JSONObject tile = (JSONObject) tiles.get(index);

                int indexX = Integer.parseInt(tile.get("x").toString());
                int indexY = Integer.parseInt(tile.get("y").toString());
                String t = tile.get("type").toString();
                shelf[indexX][indexY] = new Tile(t);
            }
            // Set the shelf for the player and check the common card goal
            p.setShelf(shelf);
            // Get from file the type of assert needed
            boolean assert_type = (boolean) tmp1.get("assert");
            // Building an extra error message
            String message = String.format("\nTest number: %d", card);
            if(assert_type){
                assertTrue(CCard.isCompleted(p), message);
            }else{
                assertFalse(CCard.isCompleted(p), message);
            }
        }
    }
}
