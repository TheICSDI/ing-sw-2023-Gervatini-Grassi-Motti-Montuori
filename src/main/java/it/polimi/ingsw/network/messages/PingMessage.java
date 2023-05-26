package it.polimi.ingsw.network.messages;

import com.google.gson.Gson;

/**
 * This class represents a ping message for rmi connection.
 */
public class PingMessage extends GeneralMessage{

    /**
     * Class constructor and set all needed attributes
     * @param username
     */
    public PingMessage(String username) {
        super(-1, Action.PING,-1,username);
    }

    /**
     * Parses a JSON-formatted string to set the message.
     * @param json a JSON-formatted string
     * @return a fully initialized PingtMessage Object
     */
    public static PingMessage decrypt(String json){
        return new Gson().fromJson(json,PingMessage.class);
    }

    /**
     * Overrides the toString method to provide a custom string representation.
     */
    public String toString(){
        return new Gson().toJson(this);
    }
}
