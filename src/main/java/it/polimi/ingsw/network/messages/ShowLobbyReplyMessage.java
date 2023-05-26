package it.polimi.ingsw.network.messages;

import com.google.gson.Gson;
import it.polimi.ingsw.model.Lobby;

import java.util.List;

public class ShowLobbyReplyMessage extends GeneralMessage{

    private final List<Lobby> Lobbies;

    public ShowLobbyReplyMessage(String msg, List<Lobby> Lobbies) {
        super(msg, Action.SHOWLOBBY, -1, -1);
        this.Lobbies = Lobbies;
    }

    public static ShowLobbyReplyMessage decrypt(String json){
        return new Gson().fromJson(json,ShowLobbyReplyMessage.class);
    }

    @Override
    public String toString(){
        return new Gson().toJson(this);
    }

    public List<Lobby> getLobbies() {
        return Lobbies;
    }
}
