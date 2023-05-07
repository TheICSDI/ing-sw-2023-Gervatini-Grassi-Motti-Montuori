package it.polimi.ingsw.network.messages;

import com.google.gson.Gson;

import java.util.List;

public class SendCommonCards extends ReplyMessage{
    public SendCommonCards(List<Integer> cc){
        super("",Action.SHOWCOMMONS);
        this.cc.addAll(cc);
    }

    public static SendCommonCards decrypt(String json){
        return new Gson().fromJson(json,SendCommonCards.class);
    }
}
