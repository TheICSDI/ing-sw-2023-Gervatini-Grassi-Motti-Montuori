package it.polimi.ingsw.network.messages;

import com.google.gson.Gson;
import it.polimi.ingsw.model.Lobby;
import org.ietf.jgss.GSSContext;

import java.io.Serializable;
import java.util.List;

//Stampino di risposta del server, dato che spesso una stringa non basta
public class ReplyMessage implements Serializable {
    private String messaggio;

    public String getMessaggio() {
        return messaggio;
    }

    public ReplyMessage(String msg){
        messaggio=msg;
    }

    public String toString(){
        return new Gson().toJson(this);
    }

    public void print(){
        System.out.println(this.messaggio);
    }
    public static ReplyMessage decrypt(String json){
        return new Gson().fromJson(json,ReplyMessage.class);
    }


}
