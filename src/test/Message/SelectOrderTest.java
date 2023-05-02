package test.Message;

import main.java.it.polimi.ingsw.network.messages.SelectOrderMessage;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.junit.jupiter.api.Test;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SelectOrderTest {

    @Test
    void testString()
    {
        int test_id = 0, lobby_id = 0;
        String test_usr = "test_username";


        JSONParser parser = new JSONParser();
        JSONObject file_obj = null, test_obj = null;

        int test_size = 3;
        ArrayList<Integer> order = new ArrayList<Integer>();
        for(int i = 0; i <= test_size; i++)
        {
            order.add(i);
        }

        SelectOrderMessage test = new SelectOrderMessage(test_id, lobby_id, test_usr, order);

        try {
            FileInputStream pathFile = new FileInputStream("JSON/MessageTest/selectorder_test.json");
            file_obj = (JSONObject) parser.parse(new InputStreamReader(pathFile));
            test_obj = (JSONObject) parser.parse(test.toString());

        } catch (ParseException | IOException e) {
            e.printStackTrace();
        }

        assertEquals(file_obj, test_obj);
    }
}
