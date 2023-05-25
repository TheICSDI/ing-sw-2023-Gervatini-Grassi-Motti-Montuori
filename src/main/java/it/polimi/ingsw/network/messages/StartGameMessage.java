package it.polimi.ingsw.network.messages;

import com.google.gson.Gson;
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
    public StartGameMessage(int message_id,int idLobby, String username) {
        super(message_id, Action.STARTGAME, idLobby, username);
    }

    /**
     * Parses a JSON-formatted string to set the message.
     * @param msg a JSON-formatted string
     * @return a fully initialized StartGameMessage Object
     */
    public static StartGameMessage decrypt(String msg){
        return new Gson().fromJson(msg, StartGameMessage.class);
    }

    /**
     * Overrides the toString method to provide a custom string representation.
     */
    @Override
    public String toString(){
        return new Gson().toJson(this);
    }
}
