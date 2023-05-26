package it.polimi.ingsw.network.messages;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

/**
 * This class represents a message for selecting the order of the tiles.
 * It extends the GeneralMessage class to include specific behavior for selecting the order of the tile.
 */
public class SelectOrderMessage extends GeneralMessage{

    private List<Integer> order = new ArrayList<>();

    /**
     * Constructor that initializes a message with the provided parameters.
     * @param message_id uid of the message
     * @param username uid of the user
     * @param order a list with the position in the chosen order
     */
    public SelectOrderMessage(int message_id, String username, List<Integer> order,int idGame) {
        super(message_id, Action.SO,-1, username);
        this.order = order;
        this.gameId = idGame;
    }

    /**
     * Parses a JSON-formatted string to set the message.
     * @param msg a JSON-formatted string
     * @return a fully initialized SelectOrderMessage Object
     */
    public static SelectOrderMessage decrypt(String msg){
        return new Gson().fromJson(msg, SelectOrderMessage.class);
    }

    /**
     * Overrides the toString method to provide a custom string representation.
     */
    @Override
    public String toString(){
        return new Gson().toJson(this);
    }

    /**
     * Getter of order
     */
    public void getOrder(List<Integer> order) {
        order.addAll(this.order);
    }
}
