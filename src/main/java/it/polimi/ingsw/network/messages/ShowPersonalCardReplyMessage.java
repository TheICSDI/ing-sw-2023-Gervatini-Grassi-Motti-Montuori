package it.polimi.ingsw.network.messages;

import com.google.gson.Gson;

public class ShowPersonalCardReplyMessage extends GeneralMessage{

    private String personalId;
    public ShowPersonalCardReplyMessage(String personalId, Action action){
        super(-1,Action.SHOWPERSONAL,-1,"");
        this.personalId = personalId;
    }

    public static  ShowPersonalCardReplyMessage decrypt(String json){
        return new Gson().fromJson(json, ShowPersonalCardReplyMessage.class);
    }


    @Override
    public String toString() {
        return new Gson().toJson(this);
    }

    public String  getPersonalId() {
        return this.personalId;
    }
}
