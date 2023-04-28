package main.java.it.polimi.ingsw.network.messages;

public class StartGameMessage extends GeneralMessage {

    public StartGameMessage(int message_id, String username) {
        super(message_id, Action.STARTGAME, username);
    }

    @Override
    public String toString()
    {
        return super.startMessage() +
                "}";
    }
}
