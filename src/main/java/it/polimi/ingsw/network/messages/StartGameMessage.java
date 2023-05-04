package it.polimi.ingsw.network.messages;

import it.polimi.ingsw.exceptions.InvalidActionException;
import it.polimi.ingsw.exceptions.InvalidKeyException;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 * This class represents a message for starting a game.
 * It extends the GeneralMessage class to include specific behavior.
 */
public class StartGameMessage extends GeneralMessage {

    /**
     * Constructor that initializes a message with the provided parameters.
     * @param message_id uid of the message
     * @param username uid of the user
     */
    public StartGameMessage(int message_id, String username) {
        super(message_id, Action.STARTGAME, -1, username);
    }

    /**
     * Constructor that parses a JSON-formatted string and initializes the message.
     * @param msg a JSON-formatted string
     */
    public StartGameMessage(String msg) throws ParseException, InvalidActionException, InvalidKeyException {
        super(msg);
        JSONParser parser = new JSONParser();
        JSONObject msg_obj = (JSONObject) parser.parse(msg);

        // Validates that the 'action' key in the JSON object matches the expected action for this message type.
        if(!msg_obj.get("action").toString().equals(Action.STARTGAME.toString()))
        {
            throw new InvalidActionException("Invalid StartGameMessage encoding");
        }
    }

    /**
     * Overrides the toString method to provide a custom string representation.
     */
    @Override
    public String toString()
    {
        return super.startMessage() +
                "}";
    }
}
