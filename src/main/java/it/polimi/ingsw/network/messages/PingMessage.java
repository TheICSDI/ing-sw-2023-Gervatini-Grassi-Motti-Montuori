package it.polimi.ingsw.network.messages;

import com.google.gson.Gson;

public class PingMessage extends GeneralMessage{
    public PingMessage(String username) {
        super(-1, Action.PING,-1,username);
    }

    public static PingMessage decrypt(String json){
        return new Gson().fromJson(json,PingMessage.class);
    }

    public String toString(){
        return new Gson().toJson(this);
    }
}
