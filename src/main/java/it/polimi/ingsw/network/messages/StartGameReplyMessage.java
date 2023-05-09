package it.polimi.ingsw.network.messages;

import com.google.gson.Gson;

public class StartGameReplyMessage extends ReplyMessage {

    public StartGameReplyMessage(String msg,int idGame){
        super(msg,Action.STARTGAME);
        gameStart=true;
        this.idGame =idGame;
    }

    public static StartGameReplyMessage decrypt(String json){
        return new Gson().fromJson(json,StartGameReplyMessage.class);
    }

}
