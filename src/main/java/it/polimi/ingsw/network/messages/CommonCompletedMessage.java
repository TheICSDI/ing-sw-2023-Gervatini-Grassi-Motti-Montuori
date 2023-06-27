package it.polimi.ingsw.network.messages;

import com.google.gson.Gson;

/**
 * Represents a message that contains information about who completed a common goal card.
 * It extends the GeneralMessage class.
 */
public class CommonCompletedMessage extends GeneralMessage{
    private final boolean first;
    private final String whoCompleted;

    /**
     * Constructor that initializes a message with the provided parameters.
     * @param msg the message.
     * @param first true only if the common gaol card is the first of the game, false otherwise.
     * @param whoCompleted the player's nickname that completed the common goal card.
     */
    public CommonCompletedMessage(String msg, boolean first, String whoCompleted) {
        super(msg, Action.COMMONCOMPLETED,-1, -1);
        this.first=first;
        this.whoCompleted=whoCompleted;
    }

    /** Gets the value of first. */
    public boolean getFirst(){return first;}

    /** Gets the nickname of the player that completed the common goal card.*/
    public String getWhoCompleted(){return whoCompleted;}

    /**
     * Parses a JSON-formatted string to set the message.
     * @param json a JSON-formatted string.
     * @return a fully initialized CommonCompletedMessage Object.
     */
    public static CommonCompletedMessage decrypt(String json){
        return new Gson().fromJson(json, CommonCompletedMessage.class);
    }

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
