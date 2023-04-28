package main.java.it.polimi.ingsw.network.messages;

public class CreateLobbyMessage extends GeneralMessage{
    public CreateLobbyMessage(int message_id, String username) {
        super(message_id, Action.CREATELOBBY, username);
    }

    @Override
    public String toString()
    {
        return super.startMessage() +
                "}";
    }
}
