package it.polimi.ingsw.network.messages;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

/**
 * This class represents a message for selecting the order of the tiles.
 * It extends the GeneralMessage class to include specific behavior for selecting the order of the tile.
 */
public class SelectOrderMessage extends GeneralMessage{
    private List<Integer> order;

    /**
     * Constructor that initializes a message with the provided parameters.
     * @param messageId id of the message.
     * @param nickname of the player.
     * @param order a list with the position in the chosen order.
     */
    public SelectOrderMessage(int messageId, String nickname, List<Integer> order,int idGame) {
        super(messageId, Action.SO,-1, nickname);
        this.order = order;
        this.gameId = idGame;
    }

    /**
     * Parses a JSON-formatted string to set the message.
     * @param msg a JSON-formatted string.
     * @return a fully initialized SelectOrderMessage Object.
     */
    public static SelectOrderMessage decrypt(String msg){
        return new Gson().fromJson(msg, SelectOrderMessage.class);
    }

    /**
     * Gets the selected order.
     */
    public void getOrder(List<Integer> order) {
        order.addAll(this.order);
    }

    @Override
    public String toString(){
        return new Gson().toJson(this);
    }
}
