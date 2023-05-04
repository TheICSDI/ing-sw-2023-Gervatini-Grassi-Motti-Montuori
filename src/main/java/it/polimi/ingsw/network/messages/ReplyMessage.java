package it.polimi.ingsw.network.messages;

import com.google.gson.Gson;

import java.io.Serializable;

//Stampino di risposta del server, dato che spesso una stringa non basta
public class ReplyMessage implements Serializable {
    private String message;

    public String getMessage() {
        return message;
    }

    public ReplyMessage(String msg){
        message =msg;
    }

    public String toString(){
        return new Gson().toJson(this);
    }

    public void print(){
        System.out.println(this.message);
    }
    public static ReplyMessage decrypt(String json){
        return new Gson().fromJson(json,ReplyMessage.class);
    }


}
