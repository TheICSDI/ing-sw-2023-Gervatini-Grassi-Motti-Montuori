package it.polimi.ingsw.network.messages;

import com.google.gson.Gson;

/**
 * This class represents a message for selecting a column in game.
 * It extends the GeneralMessage class to include specific behavior for selecting a column in game.
 */
public class SelectColumnMessage extends GeneralMessage {
    private final int col;

    /**
     * Constructor that initializes a message with the provided parameters.
     * @param message_id uid of the message
     * @param username uid of the user
     * @param col the selected column by the user
     */
    public SelectColumnMessage(int message_id, String username, int col,int idGame) {
        super(message_id, Action.SC, -1, username);
        this.col = col;
        this.gameId =idGame;
    }

    /**
     * Parses a JSON-formatted string to set the message.
     * @param msg a JSON-formatted string
     * @return a fully initialized SelectColumnMessage Object
     */
    public static SelectColumnMessage decrypt(String msg){
        return new Gson().fromJson(msg, SelectColumnMessage.class);
    }

    /**
     * Overrides the toString method to provide a custom string representation.
     */
    @Override
    public String toString(){
        return new Gson().toJson(this);
    }

    public int getCol() {
        return col;
    }
}