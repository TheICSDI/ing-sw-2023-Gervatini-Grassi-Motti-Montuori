package it.polimi.ingsw.network.messages;

import com.google.gson.Gson;

public class DefaultErrorMessage extends GeneralMessage{
    public String msg;
    public DefaultErrorMessage(String msg) {
        super(-1,Action.ERROR,-1,"");
        this.msg = msg;
    }

    public static DefaultErrorMessage decrypt(String json){
        return new Gson().fromJson(json, DefaultErrorMessage.class);
    }

    @Override
    public String toString() {
        return msg;
    }
}
