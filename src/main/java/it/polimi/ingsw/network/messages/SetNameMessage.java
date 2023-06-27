package it.polimi.ingsw.network.messages;

import com.google.gson.Gson;

/**
 * This class represents a message for setting a nickname for the game.
 * It extends the GeneralMessage class to include specific behavior for setting a nickname for the game.
 */
public class SetNameMessage extends GeneralMessage{
    private final boolean available;

    /**
     * Constructor that initializes a message with the provided parameters.
     * @param nickname chosen by the client.
     * @param available true only if the chosen nickname is available.
     */
    public SetNameMessage(String nickname, boolean available){
        super(-1,Action.SETNAME,-1,nickname);
        this.available = available;
    }

    /**
     * Parses a JSON-formatted string to set the message.
     * @param msg a JSON-formatted string.
     * @return a fully initialized SetNameMessage Object.
     */
    public static SetNameMessage decrypt(String msg){
        return new Gson().fromJson(msg, SetNameMessage.class);
    }

    /** Gets the value about the availability of the chosen nickname. */
    @Override
    public boolean isAvailable() {
        return this.available;
    }

    @Override
    public String toString(){
        return new Gson().toJson(this);
    }
}
