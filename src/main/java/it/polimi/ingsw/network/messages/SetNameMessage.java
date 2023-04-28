package main.java.it.polimi.ingsw.network.messages;

public class SetNameMessage extends GeneralMessage{
    public SetNameMessage(int message_id, String username) {
        super(message_id, Action.SETNAME, username);
    }

    @Override
    public String toString()
    {
        return startMessage() +
                "}";
    }
}
