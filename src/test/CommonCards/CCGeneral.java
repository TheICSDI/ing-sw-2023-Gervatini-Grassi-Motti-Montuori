package test.CommonCards;

import main.java.it.polimi.ingsw.model.Cards.CCStrategy;
import main.java.it.polimi.ingsw.model.Cards.CC_11;
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
     * General check for all common cards, reads one or more shelves from a json file and checks if the respective goal is reached
     * @param CCcard Which goal needs to be checked
     * @param JSONName Name of the file to read the shelves from
     * @param positiveCases number of shelves that reached the goal(in the json you need to write positive shelves first)
     */
    public static void check(CCStrategy CCcard,String JSONName,int positiveCases){
        Player p=new Player("test");
        int nRow=6;
        int nCol=5;
        Tile[][] shelf=new Tile[nRow][nCol];

        JSONParser parser = new JSONParser();
        JSONArray CC_test_File = null;
        try {
            FileInputStream pathFile = new FileInputStream(JSONName);
            CC_test_File = (JSONArray) parser.parse(new InputStreamReader(pathFile));

        } catch (ParseException | IOException e) {
            e.printStackTrace();
        }
        for (int card = 0; card < CC_test_File.size(); card++) {
            for (int i = 0; i < nRow; i++) {
                for (int j = 0; j < nCol; j++) {
                    shelf[i][j]=new Tile("EMPTY");
                }
            }
            JSONObject tmp1 = (JSONObject) CC_test_File.get(card);
            JSONArray tiles= (JSONArray) tmp1.get("tiles");
            for(int index = 0; index < tiles.size(); index++) {
                JSONObject tile = (JSONObject) tiles.get(index);

                int indexX = Integer.parseInt(tile.get("x").toString());
                int indexY = Integer.parseInt(tile.get("y").toString());
                String t = tile.get("type").toString();
                shelf[indexX][indexY] = new Tile(t);
            }
            p.setShelf(shelf);
            if(card<positiveCases){
                assertTrue(CCcard.isCompleted(p));
            }else{
                assertFalse(CCcard.isCompleted(p));
            }
        }
    }
}
