package main.java.it.polimi.ingsw.network.messages;

import main.java.it.polimi.ingsw.exceptions.InvalidActionException;
import main.java.it.polimi.ingsw.exceptions.InvalidKeyException;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 * This class represents a message for showing lobbies.
 * It extends the GeneralMessage class to include specific behavior.
 */
public class ShowLobbyMessage extends GeneralMessage{

    /**
     * Constructor that initializes a message with the provided parameters.
     * @param message_id uid of the message
     * @param username uid of the user
     */
    public ShowLobbyMessage(int message_id, String username) {
        super(message_id, Action.SHOWLOBBY, -1, username);
        //lobby_id == -1 when the player isn't in any lobby
    }

    /**
     * Constructor that parses a JSON-formatted string and initializes the message.
     * @param msg a JSON-formatted string
     */
    public ShowLobbyMessage(String msg) throws ParseException, InvalidActionException, InvalidKeyException {
        super(msg);
        JSONParser parser = new JSONParser();
        JSONObject msg_obj = (JSONObject) parser.parse(msg);

        // Validates that the 'action' key in the JSON object matches the expected action for this message type.
        if(!msg_obj.get("action").toString().equals(Action.SHOWLOBBY.toString()))
        {
            throw new InvalidActionException("Invalid ShowLobbyMessage encoding");
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
