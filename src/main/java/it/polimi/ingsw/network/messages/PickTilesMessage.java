package main.java.it.polimi.ingsw.network.messages;

import main.java.it.polimi.ingsw.model.Position;

import java.util.ArrayList;

public class PickTilesMessage extends GeneralMessage{
    private final ArrayList<Position> pos;
    public PickTilesMessage(int message_id, String username, ArrayList<Position> pos) {
        super(message_id, Action.PICKTILES, username);
        this.pos = pos;
    }

    @Override
    public String toString()
    {
        StringBuilder pos_string = new StringBuilder("\"position\":[");

        for(Position item : pos)
        {
            pos_string.append("{\"x\":").append(item.getX()).append(",");
            pos_string.append("\"y\":").append(item.getY()).append("},");
        }

        int i_last = pos_string.length() - 1;
        pos_string.replace(i_last, i_last + 1, "]");

        return super.startMessage() + "," +
                pos_string.toString() +
                "}";
    }
}
