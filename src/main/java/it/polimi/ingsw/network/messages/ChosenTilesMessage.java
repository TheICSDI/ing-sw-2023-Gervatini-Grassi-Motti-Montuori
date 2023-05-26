package it.polimi.ingsw.network.messages;

import com.google.gson.Gson;
import it.polimi.ingsw.model.Tile.Tile;

import java.util.List;

public class ChosenTilesMessage extends GeneralMessage{
    private boolean toOrder;
    public ChosenTilesMessage(List<Tile> tile, boolean toOrder) {
        super("", Action.CHOSENTILES, -1, -1);
        this.tiles = tile;
        this.toOrder = toOrder;
    }

    public static ChosenTilesMessage decrypt(String json){
        return new Gson().fromJson(json, ChosenTilesMessage.class);
    }

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }

    public boolean isToOrder(){
        return toOrder;
    }
}
