package it.polimi.ingsw.network.messages;

public class ShowPersonalCardMessage extends GeneralMessage{
    public ShowPersonalCardMessage(){
        super(-1,Action.SHOWPERSONAL,-1,"");
    }
}
