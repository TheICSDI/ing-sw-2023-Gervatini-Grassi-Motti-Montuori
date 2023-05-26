package it.polimi.ingsw.network.messages;

import com.google.gson.Gson;

public class SimpleReply extends GeneralMessage{

    public SimpleReply(String msg, Action action){
        super(msg, action, -1, -1);
    }

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
