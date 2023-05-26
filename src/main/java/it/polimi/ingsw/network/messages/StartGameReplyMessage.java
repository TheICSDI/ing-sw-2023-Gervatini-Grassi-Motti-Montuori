package it.polimi.ingsw.network.messages;

import com.google.gson.Gson;

public class StartGameReplyMessage extends GeneralMessage{

    public StartGameReplyMessage(String msg,int idGame){
        super(msg, Action.STARTGAME, -1, idGame);
        this.gameStart = true;
    }

    public static StartGameReplyMessage decrypt(String json){
        return new Gson().fromJson(json,StartGameReplyMessage.class);
    }

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
