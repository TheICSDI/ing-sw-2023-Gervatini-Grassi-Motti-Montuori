package it.polimi.ingsw.network.messages;

import com.google.gson.Gson;

/**
 * This class represents a ping message for both RMI and socket connection.
 * It extends the GeneralMessage class to include specific behavior.
 */
public class PingMessage extends GeneralMessage{

    /**
     * Constructor that initializes a message with the provided parameters.
     * @param username id of the user.
     */
    public PingMessage(String username) {
        super(-1, Action.PING,-1,username);
    }

    /**
     * Parses a JSON-formatted string to set the message.
     * @param json a JSON-formatted string.
     * @return a fully initialized PingMessage Object.
     */
    public static PingMessage decrypt(String json){
        return new Gson().fromJson(json,PingMessage.class);
    }

    public String toString(){
        return new Gson().toJson(this);
    }
}
