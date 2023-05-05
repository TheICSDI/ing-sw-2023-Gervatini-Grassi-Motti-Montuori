package it.polimi.ingsw.network.messages;

import com.google.gson.Gson;

public class CreateLobbyReplyMessage extends ReplyMessage{
    public CreateLobbyReplyMessage(String msg,int idLobby,int limit) {
        super(msg,Action.CREATELOBBY);
        this.limit=limit;
        this.idLobby=idLobby;
    }

    public static CreateLobbyReplyMessage decrypt(String json){
        return new Gson().fromJson(json, CreateLobbyReplyMessage.class);
    }

}
