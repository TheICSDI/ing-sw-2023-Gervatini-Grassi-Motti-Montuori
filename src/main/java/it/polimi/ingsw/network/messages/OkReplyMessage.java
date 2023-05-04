package it.polimi.ingsw.network.messages;

public class OkReplyMessage extends ReplyMessage{

    public OkReplyMessage(String msg) {
        super(msg,Action.ERROR);
    }

    @Override
    public void print() {}
    /*public static OkReplyMessage decrypt(String json){
        return new Gson().fromJson(json,OkReplyMessage.class);
    }*/
}
