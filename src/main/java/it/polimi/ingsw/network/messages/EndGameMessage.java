package it.polimi.ingsw.network.messages;

import com.google.gson.Gson;

/**
 * This class represents a message for ending the game.
 * It extends the GeneralMessage class to include specific behavior.
 */
public class EndGameMessage extends GeneralMessage {

    /**
     * Constructor that initializes a message with the provided parameters.
     * @param messageId id of the message.
     * @param nickname of the player.
     * @param lobbyId id of the lobby.
     */
    public EndGameMessage(int messageId, int lobbyId, String nickname) {
        super(messageId, Action.ENDGAME, lobbyId, nickname);
    }

    /**
     * Parses a JSON-formatted string to set the message.
     * @param msg a JSON-formatted string.
     * @return a fully initialized EndGameMessage Object.
     */
    public static EndGameMessage decrypt(String msg){
        return new Gson().fromJson(msg, EndGameMessage.class);
    }

    @Override
    public String toString(){
        return new Gson().toJson(this);
    }
}
