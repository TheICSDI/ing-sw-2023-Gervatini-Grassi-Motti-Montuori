package it.polimi.ingsw.network.messages;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

/**
 * This class represents a message for sending and requesting common cards.
 * It extends the GeneralMessage class to include specific behavior.
 */
public class ShowCommonCards extends GeneralMessage{

    public ShowCommonCards(List<Integer> cc){
        super("", Action.SHOWCOMMONS, -1, -1);
        this.cc.addAll(cc);
    }

    /**
     * Parses a JSON-formatted string to set the message.
     * @param json a JSON-formatted string
     * @return a fully initialized SendCommonCardMessage Object
     */
    public static ShowCommonCards decrypt(String json){
        return new Gson().fromJson(json, ShowCommonCards.class);
    }

    /**
     * Overrides the toString method to provide a custom string representation.
     */
    @Override
    public String toString(){
        return new Gson().toJson(this);
    }
}
