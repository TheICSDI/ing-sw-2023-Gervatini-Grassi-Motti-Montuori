package it.polimi.ingsw.network.messages;

import it.polimi.ingsw.exceptions.InvalidActionException;
import it.polimi.ingsw.exceptions.InvalidKeyException;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 * This class represents a message for selecting a column in game.
 * It extends the GeneralMessage class to include specific behavior for selecting a column in game.
 */
public class SelectColumnMessage extends GeneralMessage {
    private final int col;

    /**
     * Constructor that initializes a message with the provided parameters.
     * @param message_id uid of the message
     * @param lobby_id uid of lobby
     * @param username uid of the user
     * @param col the selected column by the user
     */
    public SelectColumnMessage(int message_id, int lobby_id, String username, int col) {
        super(message_id, Action.SELECTCOLUMN, lobby_id, username);
        this.col = col;
    }

    /**
     * Constructor that parses a JSON-formatted string and initializes the message.
     * @param msg a JSON-formatted string
     */
    public SelectColumnMessage(String msg) throws ParseException, InvalidActionException, InvalidKeyException {
        super(msg);
        JSONParser parser = new JSONParser();
        JSONObject msg_obj = (JSONObject) parser.parse(msg);

        // Validates that the 'action' key in the JSON object matches the expected action for this message type.
        if(!msg_obj.get("action").toString().equals(Action.SELECTCOLUMN.toString()))
        {
            throw new InvalidActionException("Invalid SelectColumnMessage encoding");
        }
        if(!msg_obj.containsKey("column")){
            throw new InvalidKeyException("Missing key: column");
        }

        this.col = Integer.parseInt(msg_obj.get("column").toString());
    }

    /**
     * Overrides the toString method to provide a custom string representation.
     */
    @Override
    public String toString()
    {
        return super.startMessage() + "," +
                "\"column\":" + this.col +
                "}";
    }
    public int getCol() {
        return col;
    }
}