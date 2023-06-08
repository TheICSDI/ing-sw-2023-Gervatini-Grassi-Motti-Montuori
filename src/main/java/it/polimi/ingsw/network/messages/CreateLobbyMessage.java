package it.polimi.ingsw.network.messages;

import com.google.gson.Gson;

/**
 * This class represents a message for creating a lobby in a game.
 * It extends the GeneralMessage class to include specific behavior for creating lobby messages.
 */
public class CreateLobbyMessage extends GeneralMessage{

    private final int limit;
    /**
     * Constructor that initializes a message with the provided parameters.
     * @param message_id uid of the message
     * @param username the unique identification of the user
     */
    public CreateLobbyMessage(int message_id, String username,int limit) {
        super(message_id, Action.CREATELOBBY, -1, username);
        this.limit = limit;
    }

    /**
     * Class constructor and set all the needed attributes
     * @param msg the message to display
     * @param lobby_id the uid of the lobby
     * @param limit the max number of player for that specific lobby
     */
    public CreateLobbyMessage(String msg, int lobby_id, int limit) {
        super(msg, Action.CREATELOBBY, lobby_id, -1);
        this.limit = limit;
    }

    /**
     * Parses a JSON-formatted string to set the message.
     * @param msg a JSON-formatted string
     * @return a fully initialized CreateLobbyMessage Object
     */
    public static CreateLobbyMessage decrypt(String msg){
        return new Gson().fromJson(msg, CreateLobbyMessage.class);
    }

    /**
     * Overrides the toString method to provide a custom string representation.
     */
    @Override
    public String toString(){
        return new Gson().toJson(this);
    }

    /**
     * Gettero of limit
     * @return this.limit
     */
    public int getLimit() {
        return limit;
    }
}
