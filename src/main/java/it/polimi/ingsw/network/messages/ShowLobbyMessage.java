package it.polimi.ingsw.network.messages;

import com.google.gson.Gson;
import it.polimi.ingsw.exceptions.InvalidActionException;
import it.polimi.ingsw.exceptions.InvalidKeyException;
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
     * Parses a JSON-formatted string to set the message.
     * @param msg a JSON-formatted string
     * @return a fully initialized ShowLobbyMessage Object
     */
    public static ShowLobbyMessage decrypt(String msg){
        return new Gson().fromJson(msg, ShowLobbyMessage.class);
    }

    /**
     * Overrides the toString method to provide a custom string representation.
     */
    @Override
    public String toString(){
        return new Gson().toJson(this);
    }
}
