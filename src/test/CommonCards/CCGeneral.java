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
    //Read input in common cards from a json(JSONName), and tests it
    public static void check(CCStrategy CCcard,String JSONName,int casiPositivi){//nel file metti prima i casi che devono dare true e poi quelli false
                                                                                //casi positivi è un int che dice quanti casi pos hai messo
        int nRow=6;
        int nCol=5;
        Tile[][] shelf=new Tile[nCol][nRow];

        JSONParser parser = new JSONParser();
        JSONArray CC_test_File = null;

        for (int i = 0; i < nCol; i++) {
            for (int j = 0; j < nRow; j++) {
                shelf[i][j]=new Tile("EMPTY");
            }
        }
        Player p=new Player("test");
        p.setShelf(shelf);
        assertFalse(CCcard.isCompleted(p));
        try {
            FileInputStream pathFile = new FileInputStream(JSONName);
            CC_test_File = (JSONArray) parser.parse(new InputStreamReader(pathFile));

        } catch (ParseException | IOException e) {
            e.printStackTrace();
        }
        for (int card = 0; card < CC_test_File.size(); card++) {
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
            if(card<casiPositivi){
                assertTrue(CCcard.isCompleted(p));
            }else{
                assertFalse(CCcard.isCompleted(p));
            }
        }
    }
}
