package it.polimi.ingsw.network.messages;

import it.polimi.ingsw.exceptions.InvalidActionException;
import it.polimi.ingsw.exceptions.InvalidKeyException;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 * This class represents a message for warning any type of update of the lobby.
 * It extends the GeneralMessage class to include specific behavior.
 */
public class UpdateLobbyMessage extends GeneralMessage {

    /**
     * Constructor that initializes a message with the provided parameters.
     * @param message_id uid of the message
     * @param username uid of the user
     * @param lobby_id uid of the lobby
     */
    public UpdateLobbyMessage(int message_id, int lobby_id, String username) {
        super(message_id, Action.UPDATELOBBY, lobby_id, username);
    }

    /**
     * Constructor that parses a JSON-formatted string and initializes the message.
     * @param msg a JSON-formatted string
     */
    public UpdateLobbyMessage(String msg) throws ParseException, InvalidActionException, InvalidKeyException {
        super(msg);
        JSONParser parser = new JSONParser();
        JSONObject msg_obj = (JSONObject) parser.parse(msg);

        // Validates that the 'action' key in the JSON object matches the expected action for this message type.
        if(!msg_obj.get("action").toString().equals(Action.UPDATELOBBY.toString()))
        {
            throw new InvalidActionException("Invalid UpdateLobbyMessage encoding");
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