package test.Message;

import it.polimi.ingsw.network.messages.StartGameMessage;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.junit.jupiter.api.Test;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class StartGameTest {

    @Test
    void testString()
    {
        /*int test_id = 0, lobby_id = 0;
        String test_usr = "test_username";

        //StartGameMessage test = new StartGameMessage(test_id, lobby_id, test_usr);

        JSONParser parser = new JSONParser();
        JSONObject file_obj = null, test_obj = null;


        try {
            FileInputStream pathFile = new FileInputStream("JSON/MessageTest/startgame_test.json");
            file_obj = (JSONObject) parser.parse(new InputStreamReader(pathFile));
            test_obj = (JSONObject) parser.parse(test.toString());

        } catch (ParseException | IOException e) {
            e.printStackTrace();
        }

        assertEquals(file_obj, test_obj);*/
    }
}
