package it.polimi.ingsw.network.messages;

import com.google.gson.Gson;
import it.polimi.ingsw.model.Player;

/**
 * This class represents a message for requesting the shelf of other players in the game.
 * It extends the GeneralMessage class to include specific behavior.
 */
public class OtherPlayersMessage extends GeneralMessage{
    private final Player p;

    /**
     * Constructor that initializes a message with the provided parameters.
     * @param p a player.
     */
    public OtherPlayersMessage(Player p) {
        super("", Action.SHOWOTHERS, -1, -1);
        this.p = p;
    }

    /**
     * Parses a JSON-formatted string to set the message.
     * @param json a JSON-formatted string.
     * @return a fully initialized OtherPlayersMessage Object.
     */
    public static OtherPlayersMessage decrypt(String json){
        return new Gson().fromJson(json,OtherPlayersMessage.class);
    }

    /**
     * Gets the player.
     * */
    public Player getP() {
        return this.p;
    }

    @Override
    public String toString(){
        return new Gson().toJson(this);
    }
}
