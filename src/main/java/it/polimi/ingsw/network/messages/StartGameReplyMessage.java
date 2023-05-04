package it.polimi.ingsw.network.messages;

import com.google.gson.Gson;

public class StartGameReplyMessage extends ReplyMessage {
    public StartGameReplyMessage(String msg){
        super(msg);
        gameStart=true;
    }
    public static StartGameReplyMessage decrypt(String json){
        return new Gson().fromJson(json,StartGameReplyMessage.class);
    }

}
