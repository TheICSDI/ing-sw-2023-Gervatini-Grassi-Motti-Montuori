package main.java.it.polimi.ingsw.network.messages;

public class WrongDatasRecivedMessage extends GeneralMessage{
    public WrongDatasRecivedMessage(int message_id, String username) {
        super(message_id, Action.REDOCOMMAND, username);
    }

    @Override
    public String toString()
    {//va cambiato il metodo
        return startMessage() +
                "}";
    }
}
