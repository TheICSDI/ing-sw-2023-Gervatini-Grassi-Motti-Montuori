package it.polimi.ingsw.network.messages;

import com.google.gson.Gson;

/**
 * This class represents a message for setting a username for the game.
 * It extends the GeneralMessage class to include specific behavior for setting a username for the game.
 */
public class SetNameMessage extends GeneralMessage{
    private boolean Available;

    /**
     * Constructor that initializes a message with the provided parameters.
     *
     * @param user uid of the user
     * @param Available nickname available
     */
    public SetNameMessage(String user,boolean Available){
        super(-1,Action.SETNAME,-1,user);
        this.Available=Available;
    }

    /**
     * Parses a JSON-formatted string to set the message.
     * @param msg a JSON-formatted string
     * @return a fully initialized SetNameMessage Object
     */
    public static SetNameMessage decrypt(String msg){
        return new Gson().fromJson(msg, SetNameMessage.class);
    }

    /**
     * Overrides the toString method to provide a custom string representation.
     */
    @Override
    public String toString(){
        return new Gson().toJson(this);
    }

    /**
     * Getter of Available
     * @return
     */
    @Override
    public boolean isAvailable() {
        return this.Available;
    }
}
