package it.polimi.ingsw.network.messages;

import com.google.gson.Gson;
import it.polimi.ingsw.exceptions.InvalidActionException;
import it.polimi.ingsw.exceptions.InvalidKeyException;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class BroadcastMessage extends ReplyMessage{
    private final String phrase;
    public BroadcastMessage(int idGame, int lobbyId, String username, String phrase){
        super("", Action.CA);
        this.idGame = idGame;
        this.phrase = phrase;
        this.idLobby = lobbyId;
        this.username = username;

    }
    public String getPhrase() {
        return this.phrase;
    }
    @Override
    public String toString()
    {
        return  new Gson().toJson(this);
    }

    public static BroadcastMessage decrypt(String json){
        return new Gson().fromJson(json,BroadcastMessage.class);
    }
}
