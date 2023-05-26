package it.polimi.ingsw.network.messages;

import com.google.gson.Gson;

public class JoinLobbyReplyMessage extends GeneralMessage{
    public JoinLobbyReplyMessage(String msg,int idLobby) {
        super(msg, Action.JOINLOBBY, idLobby, -1 );
    }

    public static JoinLobbyReplyMessage decrypt(String json){
        return new Gson().fromJson(json,JoinLobbyReplyMessage.class);
    }

    @Override
    public String toString(){
        return new Gson().toJson(this);
    }
}
