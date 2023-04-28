package main.java.it.polimi.ingsw.network.messages;

public class ExitLobbyMessage extends GeneralMessage {
    private final int lobby_id;
    public ExitLobbyMessage(int message_id, String username, int lobby_id) {
        super(message_id, Action.EXITLOBBY, username);
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
