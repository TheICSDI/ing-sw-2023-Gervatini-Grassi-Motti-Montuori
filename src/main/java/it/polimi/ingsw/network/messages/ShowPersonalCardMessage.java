package it.polimi.ingsw.network.messages;

import com.google.gson.Gson;

public class ShowPersonalCardMessage extends GeneralMessage{
    public ShowPersonalCardMessage(){
        super(-1,Action.SHOWPERSONAL,-1,"");
    }


    public static ShowPersonalCardMessage decrypt(String json){
        return new Gson().fromJson(json, ShowPersonalCardMessage.class);
    }

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
