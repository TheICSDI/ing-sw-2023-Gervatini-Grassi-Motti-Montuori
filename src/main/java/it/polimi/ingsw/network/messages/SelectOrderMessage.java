package main.java.it.polimi.ingsw.network.messages;

import main.java.it.polimi.ingsw.model.Tile.Tile;

import java.util.ArrayList;

public class SelectOrderMessage extends GeneralMessage{
    private final ArrayList<Tile> order;
    public SelectOrderMessage(int message_id, String username, ArrayList<Tile> order) {
        super(message_id, Action.SELECTORDER, username);
        this.order = order;
    }

    @Override
    public String toString()
    {
        StringBuilder order_string = new StringBuilder("\"order\":[");

        for(Tile item : order)
        {
            order_string.append("{\"tile\":").append(item.getCategory()).append("},");
        }

        int i_last = order_string.length() - 1;
        order_string.replace(i_last, i_last + 1, "]");

        return super.startMessage() + "," +
                order_string.toString() +
                "}";

    }
}
