package it.polimi.ingsw.network.messages;

import com.google.gson.Gson;

/**
 * This class represents a message for ending the game.
 * It extends the GeneralMessage class to include specific behavior.
 */
public class EndGameMessage extends GeneralMessage {

    /**
     * Constructor that initializes a message with the provided parameters.
     * @param message_id uid of the message
     * @param username uid of the user
     * @param lobby_id uid of the lobby
     */
    public EndGameMessage(int message_id, int lobby_id, String username) {
        super(message_id, Action.ENDGAME, lobby_id, username);
    }

    /**
     * Parses a JSON-formatted string to set the message.
     * @param msg a JSON-formatted string
     * @return a fully initialized EndGameMessage Object
     */
    public static EndGameMessage decrypt(String msg){
        return new Gson().fromJson(msg, EndGameMessage.class);
    }

    /**
     * Overrides the toString method to provide a custom string representation.
     */
    @Override
    public String toString(){
        return new Gson().toJson(this);
    }
}
