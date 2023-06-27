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
     * @param messageId id of the message.
     * @param nickname id of the player.
     * @param col the selected column by the player.
     */
    public SelectColumnMessage(int messageId, String nickname, int col,int idGame) {
        super(messageId, Action.SC, -1, nickname);
        this.col = col;
        this.gameId =idGame;
    }

    /**
     * Parses a JSON-formatted string to set the message.
     * @param msg a JSON-formatted string.
     * @return a fully initialized SelectColumnMessage Object.
     */
    public static SelectColumnMessage decrypt(String msg){
        return new Gson().fromJson(msg, SelectColumnMessage.class);
    }

    /**
     * Gets the selected column.
     */
    public int getCol() {
        return col;
    }

    @Override
    public String toString(){
        return new Gson().toJson(this);
    }
}