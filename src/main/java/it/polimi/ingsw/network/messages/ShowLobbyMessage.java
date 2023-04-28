package main.java.it.polimi.ingsw.network.messages;

public class ShowLobbyMessage extends GeneralMessage{

    public ShowLobbyMessage(int message_id, String username) {
        super(message_id, Action.SHOWLOBBY, username);
    }

    @Override
    public String toString()
    {
        return super.startMessage() +
                "}";
    }
}
