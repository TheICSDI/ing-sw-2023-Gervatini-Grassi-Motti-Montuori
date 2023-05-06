package it.polimi.ingsw.network.messages;

import com.google.gson.Gson;
import it.polimi.ingsw.model.Tile.Tile;

import java.util.List;

public class PickedTilesMessage extends ReplyMessage{
    public PickedTilesMessage(List<Tile> tile) {
        super("", Action.CHOSENTILES);
        this.tiles.addAll(tile);
    }

    public static PickedTilesMessage decrypt(String json){
        return new Gson().fromJson(json,PickedTilesMessage.class);
    }

}
