package it.polimi.ingsw.network.messages;

import com.google.gson.Gson;
import it.polimi.ingsw.model.Player;

public class OtherPlayersMessage extends ReplyMessage{
    private Player p;


    public OtherPlayersMessage(Player p) {
        super("", Action.SHOWOTHERS);
        this.p = p;
    }


    public Player getP() {
        return p;
    }
    public static OtherPlayersMessage decrypt(String json){
        return new Gson().fromJson(json,OtherPlayersMessage.class);
    }
}
