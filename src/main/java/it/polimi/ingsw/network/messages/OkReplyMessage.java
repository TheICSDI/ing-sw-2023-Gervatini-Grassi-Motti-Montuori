package it.polimi.ingsw.network.messages;

import com.google.gson.Gson;

public class OkReplyMessage extends ReplyMessage{

    public OkReplyMessage(String msg) {
        super(msg);
    }

    @Override
    public void print() {}
    /*public static OkReplyMessage decrypt(String json){
        return new Gson().fromJson(json,OkReplyMessage.class);
    }*/
}
