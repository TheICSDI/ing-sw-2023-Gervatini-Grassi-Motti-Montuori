package test.CommonCards;

import main.java.it.polimi.ingsw.model.Cards.CCStrategy;
import main.java.it.polimi.ingsw.model.Cards.CC_01;
/*
    import main.java.it.polimi.ingsw.model.Player;
    import main.java.it.polimi.ingsw.model.Tile.Tile;

    import org.json.simple.JSONArray;
    import org.json.simple.JSONObject;
    import org.json.simple.parser.JSONParser;
    import org.json.simple.parser.ParseException;

    import java.io.FileInputStream;
    import java.io.FileNotFoundException;
    import java.io.IOException;
    import java.io.InputStreamReader;

    import static org.junit.jupiter.api.Assertions.assertFalse;
    import static org.junit.jupiter.api.Assertions.assertTrue;
*/
import org.junit.jupiter.api.Test;

class CC_01Test {
/*
    @Test
    void isCompleted() {
        CCStrategy CC01=new CC_01();
        int num_row = 6;
        int num_col = 5;
        Tile[][] shelf = new Tile[num_row][num_col];
        Player p = new Player("test");
        Tile tipo;

        for(int i = 0; i < num_row; i++)
        {
            for(int j = 0; j < num_col; j++)
            {
                shelf[i][j] = new Tile("empty");
            }
        }
        JSONParser parser = new JSONParser();
        JSONArray file = null;
        try{
            FileInputStream pathFile = new FileInputStream("JSON/CC/CC01_test.json");
            file = (JSONArray) parser.parse(new InputStreamReader(pathFile));
        }catch (FileNotFoundException e){
            e.printStackTrace();
        }catch (ParseException e){
            e.printStackTrace();
        }catch (IOException e){
            e.printStackTrace();
        }

        JSONObject temp = (JSONObject) file.get(0);
        JSONArray tiles = (JSONArray) temp.get("tiles");
        for(int i = 0; i < tiles.size(); i++)
        {
            JSONObject pos = (JSONObject) tiles.get(i);
            tipo = new Tile(pos.get("type").toString());
            int x = Integer.parseInt(pos.get("x").toString());
            int y = Integer.parseInt(pos.get("y").toString());
            shelf[x][y] = tipo;
        }

        p.setShelf(shelf);
        assertTrue(CC01.isCompleted(p));
    } */
    @Test
    void isCompleted() {
        CCStrategy CC01=new CC_01();
        CCGeneral.check(CC01,"JSON/CC/CC01_test.json");
    }
}
/*
    The first shelf represents the best-case scenario without possible "IndexOutOfBound" or groups larger than 2, and returns true.
    The second shelf has possible "IndexOutOfBound" occurrences, but no groups larger than 2, and also returns true.
    The third shelf has both "IndexOutOfBound" occurrences and groups larger than 2, yet still returns true.
    The fourth shelf has possible groups larger than 2, repeated less than 6 times, and returns false.
 */