package it.polimi.ingsw.network.messages;

import com.google.gson.Gson;

public class ShowCommonCards extends GeneralMessage{
    public ShowCommonCards(){
        super(-1,Action.SHOWCOMMONS,-1,"");
    }

    public static ShowCommonCards decrypt(String json){
        return new Gson().fromJson(json, ShowCommonCards.class);
    }

    @Override
    public String toString(){
        return new Gson().toJson(this);
    }

}
