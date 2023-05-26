package it.polimi.ingsw.network.messages;

import com.google.gson.Gson;

/**
 * This class represents a simple reply.
 * It extends GeneralMessage and represents mainly the action "INGAMEEVENT", "ERROR"
 */
public class SimpleReply extends GeneralMessage{

    /**
     * Class constructor and set al needed attributes
     * @param msg the message to display
     * @param action the type of message
     */
    public SimpleReply(String msg, Action action){
        super(msg, action, -1, -1);
    }

    /**
     * Parses a JSON-formatted string to set the message.
     * @param json a JSON-formatted string
     * @return a fully initialized BroadcastMessage Object
     */
    public static SimpleReply decrypt(String json){
        return new Gson().fromJson(json, SimpleReply.class);
    }

    /**
     * Overrides the toString method to provide a custom string representation.
     */
    @Override
    public String toString(){
        return new Gson().toJson(this);
    }
}
