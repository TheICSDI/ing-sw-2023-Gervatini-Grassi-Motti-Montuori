package it.polimi.ingsw.network.messages;

import it.polimi.ingsw.exceptions.InvalidActionException;
import it.polimi.ingsw.exceptions.InvalidKeyException;
import it.polimi.ingsw.model.Position;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.util.ArrayList;
import java.util.List;

/**
 * This class represents a message for picking tiles in a game.
 * It extends the GeneralMessage class to include specific behavior for picking tiles in a game.
 */
public class PickTilesMessage extends GeneralMessage{

    private List<Position> pos = new ArrayList<Position>();

    /**
     * Constructor that initializes a message with the provided parameters.
     * @param message_id uid of the message
     * @param username uid of the user
     * @param pos and array with position of the piked tiles
     */
    public PickTilesMessage(int message_id, String username, List<Position> pos) {
        super(message_id, Action.PICKTILES, -1, username);
        this.pos = pos;
    }

    /**
     * Constructor that parses a JSON-formatted string and initializes the message.
     * @param msg a JSON-formatted string
     */
    public PickTilesMessage(String msg) throws ParseException, InvalidActionException, InvalidKeyException {
        super(msg);
        JSONParser parser = new JSONParser();
        JSONObject msg_obj = (JSONObject) parser.parse(msg);

        // Validates that the 'action' key in the JSON object matches the expected action for this message type.
        if(!msg_obj.get("action").toString().equals(Action.PICKTILES.toString()))
        {
            throw new InvalidActionException("Invalid PickTilesMessage encoding");
        }

        // Parsing the JSONARRAY into the Arraylist this.pos
        int x, y;
        Position tmp;
        JSONArray pos = (JSONArray) msg_obj.get("position");
        for (Object po : pos) {

            JSONObject item = (JSONObject) po;
            x = Integer.parseInt(item.get("x").toString());
            y = Integer.parseInt(item.get("y").toString());
            tmp = new Position(x, y);
            this.pos.add(tmp);
        }
    }

    /**
     * Overrides the toString method to provide a custom string representation.
     */
    @Override
    public String toString()
    {
        StringBuilder pos_string = new StringBuilder("\"position\":[");

        for(Position item : pos)
        {
            pos_string.append("{\"x\":").append(item.getX()).append(",");
            pos_string.append("\"y\":").append(item.getY()).append("},");
        }

        int i_last = pos_string.length() - 1;
        pos_string.replace(i_last, i_last + 1, "]");

        return super.startMessage() + "," +
                pos_string.toString() +
                "}";
    }

    public List<Position> getPos() {
        return pos;
    }
}
