package it.polimi.ingsw.network.messages;

public class DefaultErrorMessage extends GeneralMessage{
    public String msg;
    public DefaultErrorMessage(String msg) {
        super(-1,Action.ERROR,-1,"");
        this.msg = msg;
    }

    @Override
    public String toString() {
        return msg;
    }
}
