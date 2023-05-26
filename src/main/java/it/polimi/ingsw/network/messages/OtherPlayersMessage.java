package it.polimi.ingsw.network.messages;

import com.google.gson.Gson;
import it.polimi.ingsw.model.Player;

public class OtherPlayersMessage extends GeneralMessage{
    private Player p;

    public OtherPlayersMessage(Player p) {
        super("", Action.SHOWOTHERS, -1, -1);
        this.p = p;
    }

    public static OtherPlayersMessage decrypt(String json){
        return new Gson().fromJson(json,OtherPlayersMessage.class);
    }

    @Override
    public String toString(){
        return new Gson().toJson(this);
    }

    public Player getP() {
        return p;
    }
}
