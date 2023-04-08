/** Tests for class CC_11.java.
 * @author Andrea Grassi.
 */
package test.CommonCards;

import main.java.it.polimi.ingsw.model.Cards.CCStrategy;
import main.java.it.polimi.ingsw.model.Cards.CC_11;
import main.java.it.polimi.ingsw.model.Cards.CommonCard;
import main.java.it.polimi.ingsw.model.Player;
import main.java.it.polimi.ingsw.model.Tile.Tile;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.junit.jupiter.api.Test;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import static org.junit.jupiter.api.Assertions.*;

class CC_11Test {

    @Test
    void isCompleted() {
        CCStrategy CC11=new CC_11();
        CCGeneral.check(CC11,"JSON/CC/CC11_test.json",1);
    }
}