package it.polimi.ingsw.network.messages;

public class DefaultErrorMessage extends GeneralMessage{

    public DefaultErrorMessage() {
        super(-1,Action.ERROR,-1,"");
    }

    @Override
    public String toString() {
        return "Error";
    }
}
