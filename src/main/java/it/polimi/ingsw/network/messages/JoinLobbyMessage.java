package main.java.it.polimi.ingsw.network.messages;

public class JoinLobbyMessage extends GeneralMessage{
    private final int lobby_id;
    public JoinLobbyMessage(int message_id, String username, int lobby_id) {
        super(message_id, Action.JOINLOBBY, username);
        this.lobby_id = lobby_id;
    }

    @Override
    public String toString()
    {
        return super.startMessage() + "," +
                "\"lobby_id\":" + this.lobby_id +
                "}";
    }
}
