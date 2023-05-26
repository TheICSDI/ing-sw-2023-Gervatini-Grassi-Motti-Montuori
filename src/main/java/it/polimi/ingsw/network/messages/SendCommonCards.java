package it.polimi.ingsw.network.messages;

import com.google.gson.Gson;

import java.util.List;

public class SendCommonCards extends GeneralMessage{

    public SendCommonCards(List<Integer> cc){
        super("", Action.SHOWCOMMONS, -1, -1);
        this.cc.addAll(cc);
    }

    public static SendCommonCards decrypt(String json){
        return new Gson().fromJson(json,SendCommonCards.class);
    }

    @Override
    public String toString(){
        return new Gson().toJson(this);
    }
}
