package it.polimi.ingsw.network.messages;

import com.google.gson.Gson;
import it.polimi.ingsw.model.Tile.Tile;

import java.util.List;

/**
 * This class represents a message that contains information about chosen tiles.
 * It extends the GeneralMessage class to include specific behavior related to the chosen tiles.
 */
public class ChosenTilesMessage extends GeneralMessage{
    private final boolean toOrder;
    public ChosenTilesMessage(List<Tile> tile, boolean toOrder) {
        super("", Action.CHOSENTILES, -1, -1);
        this.tiles = tile;
        this.toOrder = toOrder;
    }

    /**
     * Parses a JSON-formatted string to set the message.
     * @param json a JSON-formatted string
     * @return a fully initialized ChosenTilesMessage Object
     */
    public static ChosenTilesMessage decrypt(String json){
        return new Gson().fromJson(json, ChosenTilesMessage.class);
    }

    /**
     * Overrides the toString method to provide a custom string representation.
     */
    @Override
    public String toString() {
        return new Gson().toJson(this);
    }

    /**
     * Getter for toOrder
     * @return this.toOrder
     */
    public boolean getToOrder(){
        return toOrder;
    }
}
