package it.polimi.ingsw.network.messages;

import com.google.gson.Gson;
import it.polimi.ingsw.model.Player;

public class ReloadTokensMessage extends GeneralMessage{
    Player yourself;

    public ReloadTokensMessage(Player p) {
        super("", Action.TOKENS,-1, -1);
        yourself=p;
    }
    /**
     * Parses a JSON-formatted string to set the message.
     * @param json a JSON-formatted string.
     * @return a fully initialized OtherPlayersMessage Object.
     */
    public static ReloadTokensMessage decrypt(String json){
        return new Gson().fromJson(json,ReloadTokensMessage.class);
    }

    /**
     * Gets the player.
     * */
    public Player getYourself() {
        return this.yourself;
    }

    @Override
    public String toString(){
        return new Gson().toJson(this);
    }

}
