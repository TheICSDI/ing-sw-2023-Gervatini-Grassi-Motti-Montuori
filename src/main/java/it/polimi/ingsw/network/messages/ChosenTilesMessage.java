package it.polimi.ingsw.network.messages;

import com.google.gson.Gson;
import it.polimi.ingsw.model.Tile.Tile;

import java.util.List;

public class ChosenTilesMessage extends ReplyMessage{
    private boolean toOrder;
    public ChosenTilesMessage(List<Tile> tile, boolean toOrder) {
        super("", Action.CHOSENTILES);
        this.tiles=tile;
        this.toOrder=toOrder;
    }

    public static ChosenTilesMessage decrypt(String json){
        return new Gson().fromJson(json, ChosenTilesMessage.class);
    }

    public boolean isToOrder(){
        return toOrder;
    }
}
