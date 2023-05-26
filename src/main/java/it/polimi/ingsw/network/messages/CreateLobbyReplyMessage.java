package it.polimi.ingsw.network.messages;

import com.google.gson.Gson;

public class CreateLobbyReplyMessage extends GeneralMessage{

    private int limit;
    public CreateLobbyReplyMessage(String msg, int lobby_id, int limit) {
        super(msg, Action.CREATELOBBY, lobby_id, -1);
        this.limit = limit;
    }

    public static CreateLobbyReplyMessage decrypt(String json){
        return new Gson().fromJson(json, CreateLobbyReplyMessage.class);
    }

    @Override
    public String toString(){
        return new Gson().toJson(this);
    }

    public int getLimit() {
        return this.limit;
    }
}
