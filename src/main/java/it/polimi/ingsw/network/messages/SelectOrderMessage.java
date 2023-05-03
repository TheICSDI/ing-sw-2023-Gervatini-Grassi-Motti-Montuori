package it.polimi.ingsw.network.messages;

import it.polimi.ingsw.exceptions.InvalidActionException;
import it.polimi.ingsw.exceptions.InvalidKeyException;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.util.ArrayList;
import java.util.List;

/**
 * This class represents a message for selecting the order of the tiles.
 * It extends the GeneralMessage class to include specific behavior for selecting the order of the tile.
 */
public class SelectOrderMessage extends GeneralMessage{

    private List<Integer> order = new ArrayList<>();

    /**
     * Constructor that initializes a message with the provided parameters.
     * @param message_id uid of the message
     * @param username uid of the user
     * @param order a list with the position in the chosen order
     */
    public SelectOrderMessage(int message_id, String username, List<Integer> order) {
        super(message_id, Action.SELECTORDER,-1, username);
        this.order = order;
    }

    /**
     * Constructor that parses a JSON-formatted string and initializes the message.
     * @param msg a JSON-formatted string
     */
    public SelectOrderMessage(String msg) throws ParseException, InvalidActionException, InvalidKeyException {
        super(msg);
        JSONParser parser = new JSONParser();
        JSONObject msg_obj = (JSONObject) parser.parse(msg);

        // Validates that the 'action' key in the JSON object matches the expected action for this message type.
        if(!msg_obj.get("action").toString().equals(Action.SELECTORDER.toString()))
        {
            throw new InvalidActionException("Invalid SelectOrderMessage encoding");
        }

        // Parsing the JSONARRAY into the Arraylist this.order
        Integer tmp;
        JSONArray order = (JSONArray) msg_obj.get("order");
        for (Object item : order) {

            tmp = Integer.getInteger(item.toString());
            this.order.add(tmp);
        }
    }

    /**
     * Overrides the toString method to provide a custom string representation.
     */
    @Override
    public String toString()
    {
        StringBuilder order_string = new StringBuilder("\"order\":[");

        for(Integer item : order)
        {
            order_string.append(item.intValue()).append(",");
        }

        int i_last = order_string.length() - 1;
        order_string.replace(i_last, i_last + 1, "]");

        return super.startMessage() + "," +
                order_string.toString() +
                "}";
    }

    public List<Integer> getOrder() {
        return order;
    }
}
