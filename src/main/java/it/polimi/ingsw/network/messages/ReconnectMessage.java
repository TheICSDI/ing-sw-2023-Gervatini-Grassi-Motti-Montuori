package it.polimi.ingsw.network.messages;

import com.google.gson.Gson;

/**
 * This class represents a message of reconnection.
 * It extends the GeneralMessage class to include specific behavior.
 */
public class ReconnectMessage extends GeneralMessage{

    /**
     * Constructor that initializes a message with the provided parameters.
     * @param lobbyId id of the lobby.
     * @param gameId id of the game.
     * @param nickname of the player.
     */
    public ReconnectMessage(int lobbyId, int gameId, String nickname) {
        super(-1,Action.RECONNECT, lobbyId,nickname);
        this.gameId = gameId;
    }

    /**
     * Parses a JSON-formatted string to set the message.
     * @param json a JSON-formatted string.
     * @return a fully initialized ReconnectMessage Object.
     */
    public static ReconnectMessage decrypt(String json){
        return new Gson().fromJson(json,ReconnectMessage.class);
    }

    @Override
    public String toString(){
        return new Gson().toJson(this);
    }
}
