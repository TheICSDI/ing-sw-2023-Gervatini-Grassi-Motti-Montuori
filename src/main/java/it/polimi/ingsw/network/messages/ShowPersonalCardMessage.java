package it.polimi.ingsw.network.messages;

import com.google.gson.Gson;

/**
 * This class represents a message for sending and requesting personal cards.
 * It extends the GeneralMessage class to include specific behavior.
 */
public class ShowPersonalCardMessage extends GeneralMessage{

    private final String personalId;

    /**
     * Class constructor and set all need attributes
     * @param personalId the uid of a personal card
     */
    public ShowPersonalCardMessage(String personalId){
        super(-1,Action.SHOWPERSONAL,-1,"");
        this.personalId = personalId;
    }

    /**
     * Parses a JSON-formatted string to set the message.
     * @param json a JSON-formatted string
     * @return a fully initialized ShowPersonalCardMessage Object
     */
    public static ShowPersonalCardMessage decrypt(String json){
        return new Gson().fromJson(json, ShowPersonalCardMessage.class);
    }

    /**
     * Overrides the toString method to provide a custom string representation.
     */
    @Override
    public String toString() {
        return new Gson().toJson(this);
    }

    /**
     * Getter of personalId
     * @return this.personalId
     */
    public String  getPersonalId() {
        return this.personalId;
    }
}
