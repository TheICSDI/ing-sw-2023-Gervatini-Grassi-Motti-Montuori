package it.polimi.ingsw.network.messages;

import com.google.gson.Gson;

public class CommonCompletedMessage extends GeneralMessage{

    private boolean first;
    private String whoCompleted;

    public CommonCompletedMessage(String msg,boolean first,String whoCompleted) {
        super(msg, Action.COMMONCOMPLETED,-1, -1);
        this.first=first;
        this.whoCompleted=whoCompleted;
    }

    public boolean getFirst(){return first;}

    public String getWhoCompleted(){return whoCompleted;}

    /**
     * Parses a JSON-formatted string to set the message.
     * @param json a JSON-formatted string
     * @return a fully initialized CommonCompletedMessage Object
     */
    public static CommonCompletedMessage decrypt(String json){
        return new Gson().fromJson(json, CommonCompletedMessage.class);
    }

    /**
     * Overrides the toString method to provide a custom string representation.
     */
    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
