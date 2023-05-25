package it.polimi.ingsw.network.messages;

import com.google.gson.Gson;

/**
 * This class represents a message for exiting a lobby in a game.
 * It extends the GeneralMessage class to include specific behavior for exit lobby messages.
 */
public class ExitLobbyMessage extends GeneralMessage {

    /**
     * Constructor that initializes a message with the provided parameters.
     * @param message_id uid of the message
     * @param lobby_id uid of lobby
     * @param username uid of the user
     */
    public ExitLobbyMessage(int message_id, int lobby_id, String username) {
        super(message_id, Action.EXITLOBBY, lobby_id, username);
    }

    /**
     * Parses a JSON-formatted string to set the message.
     * @param msg a JSON-formatted string
     * @return a fully initialized ExitLobbyMessage Object
     */
    public static ExitLobbyMessage decrypt(String msg){
        return new Gson().fromJson(msg, ExitLobbyMessage.class);
    }

    /**
     * Overrides the toString method to provide a custom string representation.
     */
    @Override
    public String toString(){
        return new Gson().toJson(this);
    }
}
