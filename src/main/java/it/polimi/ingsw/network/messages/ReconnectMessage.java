package it.polimi.ingsw.network.messages;

import com.google.gson.Gson;

public class ReconnectMessage extends GeneralMessage{


    public ReconnectMessage(int lobby_id,int gameId,String username) {
        super(-1,Action.RECONNECT, lobby_id,username);
        this.gameId=gameId;
    }

    public static ReconnectMessage decrypt(String json){
        return new Gson().fromJson(json,ReconnectMessage.class);
    }
}
