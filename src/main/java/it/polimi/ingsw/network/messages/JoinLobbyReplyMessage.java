package it.polimi.ingsw.network.messages;

import com.google.gson.Gson;

public class JoinLobbyReplyMessage extends ReplyMessage{
    public JoinLobbyReplyMessage(String msg,int idLobby) {
        super(msg,Action.JOINLOBBY );
        this.idLobby=idLobby;
    }
    public static JoinLobbyReplyMessage decrypt(String json){
        return new Gson().fromJson(json,JoinLobbyReplyMessage.class);
    }

}
