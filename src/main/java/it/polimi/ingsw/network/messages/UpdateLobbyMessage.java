package it.polimi.ingsw.network.messages;

import com.google.gson.Gson;
/**
 * This class represents a message for warning any type of update of the lobby.
 * It extends the GeneralMessage class to include specific behavior.
 */
public class UpdateLobbyMessage extends GeneralMessage {

    /**
     * Constructor that initializes a message with the provided parameters.
     * @param messageId id of the message.
     * @param nickname of the player.
     * @param lobbyId id of the lobby.
     */
    public UpdateLobbyMessage(int messageId, int lobbyId, String nickname) {
        super(messageId, Action.UPDATELOBBY, lobbyId, nickname);
    }

    /**
     * Parses a JSON-formatted string to set the message.
     * @param msg a JSON-formatted string.
     * @return a fully initialized UpdateLobbyMessage Object.
     */
    public static UpdateLobbyMessage decrypt(String msg){
        return new Gson().fromJson(msg, UpdateLobbyMessage.class);
    }

    @Override
    public String toString(){
        return new Gson().toJson(this);
    }
}