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
     * Class constructor and set all needed attributes
     * @param p all the information of the player
     */
    public OtherPlayersMessage(Player p) {
        super("", Action.SHOWOTHERS, -1, -1);
        this.p = p;
    }

    /**
     * Parses a JSON-formatted string to set the message.
     * @param json a JSON-formatted string
     * @return a fully initialized OtherPlayersMessage Object
     */
    public static OtherPlayersMessage decrypt(String json){
        return new Gson().fromJson(json,OtherPlayersMessage.class);
    }

    /**
     * Overrides the toString method to provide a custom string representation.
     */
    @Override
    public String toString(){
        return new Gson().toJson(this);
    }

    /**
     * Getter for p
     * @return this.p
     */
    public Player getP() {
        return this.p;
    }
}
