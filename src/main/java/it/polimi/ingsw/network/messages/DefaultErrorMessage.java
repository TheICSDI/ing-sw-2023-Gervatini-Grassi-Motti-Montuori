package it.polimi.ingsw.network.messages;

import com.google.gson.Gson;

/**
 * This class represents a message of error.
 * It extends the GeneralMessage class to include specific behavior.
 */
public class DefaultErrorMessage extends GeneralMessage{
    public String msg;

    /**
     * Class constructor and set all the needed attributes
     * @param msg the error message
     */
    public DefaultErrorMessage(String msg) {
        super(-1,Action.ERROR,-1,"");
        this.msg = msg;
    }

    /**
     * Parses a JSON-formatted string to set the message.
     * @param json a JSON-formatted string
     * @return a fully initialized DefaultErrorMessage Object
     */
    public static DefaultErrorMessage decrypt(String json){
        return new Gson().fromJson(json, DefaultErrorMessage.class);
    }

    /**
     * Overrides the toString method to provide a custom string representation.
     */
    @Override
    public String toString() {
        return msg;
    }
}
