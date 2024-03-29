package it.polimi.ingsw.test.model;

import it.polimi.ingsw.model.Cards.*;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.Tile.Tile;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

/** Tests for class CommonCard.java.
 * @author Caterina Motti.
 */
class CommonCardTest {
    CCStrategy n2 = new CC_02();
    CCStrategy n6 = new CC_06();
    CCStrategy n11 = new CC_11();
    CommonCard c1 = new CommonCard(n2, true);
    CommonCard c2 = new CommonCard(n6, false);
    CommonCard c3 = new CommonCard(n11, false);
    Player p1 = new Player("CLR");

    /** Parser for shelf_test.json. It returns a player with a full shelf that complete common card goal n2 and n6. */
    private Player Parser(){
        Player p = new Player("Jhonny");
        JSONParser parser = new JSONParser();
        JSONArray common_card_test_File = null;

        try {
            common_card_test_File = (JSONArray) parser.parse(new InputStreamReader(Objects.requireNonNull(getClass().getResourceAsStream("/JSON/common_card_test.json"))));

        } catch (ParseException | IOException e) {
            e.printStackTrace();
        }

        for(int index = 0; index < common_card_test_File.size(); index++) {
            JSONObject tmp = (JSONObject) common_card_test_File.get(index);

            int indexX = Integer.parseInt(tmp.get("x").toString());
            int indexY = Integer.parseInt(tmp.get("y").toString());
            String t = tmp.get("type").toString();
            p.getShelf()[indexX][indexY] = new Tile(t,1);
        }
        return p;
    }

    @Test
    void control() {
        //Empty shelf
        assertFalse(c1.control(p1));
        assertFalse(c2.control(p1));
        assertFalse(c3.control(p1));

        //Full shelf that completed the common goal card n2 and n6, but no c11
        p1 = Parser();
        assertTrue(c1.control(p1));
        assertTrue(c2.control(p1));
        assertFalse(c3.control(p1));
    }

    @Test
    void givePoints() {
        //Empty shelf
        assertEquals(0, p1.getScoreToken1());
        assertEquals(0, p1.getScoreToken2());

        //Full shelf that completed the common goal card n2 and n6
        p1 = Parser();
        c1.givePoints(p1,4);
        c2.givePoints(p1,4);
        assertEquals(8, p1.getScoreToken1());
        assertEquals(8, p1.getScoreToken2());
        Player p2 = Parser();
        c1.givePoints(p2,4);
        assertEquals(4, p2.getScoreToken1());
    }
}