package test.Message;

import it.polimi.ingsw.model.Position;
import it.polimi.ingsw.network.messages.PickTilesMessage;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.junit.jupiter.api.Test;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PickTilesTest {

    @Test
    void testString()
    {
        int test_id = 0, lobby_id = 0;
        String test_usr = "test_username";


        JSONParser parser = new JSONParser();
        JSONObject file_obj = null, test_obj = null;

        int test_size = 3;
        List<Position> pos = new ArrayList<Position>();
        Position tmp;
        for(int i = 0; i <= test_size; i++)
        {
            tmp = new Position(0, i);
            pos.add(tmp);
        }

        PickTilesMessage test = new PickTilesMessage(test_id, test_usr, pos);

        try {
            FileInputStream pathFile = new FileInputStream("JSON/MessageTest/picktiles_test.json");
            file_obj = (JSONObject) parser.parse(new InputStreamReader(pathFile));
            test_obj = (JSONObject) parser.parse(test.toString());

        } catch (ParseException | IOException e) {
            e.printStackTrace();
        }

        assertEquals(file_obj, test_obj);
    }
}
