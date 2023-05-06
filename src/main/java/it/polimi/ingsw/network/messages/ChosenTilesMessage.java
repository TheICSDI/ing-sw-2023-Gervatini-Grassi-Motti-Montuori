package it.polimi.ingsw.network.messages;

import com.google.gson.Gson;
import it.polimi.ingsw.model.Tile.Tile;

import java.util.List;

public class ChosenTilesMessage extends ReplyMessage{
    public ChosenTilesMessage(List<Tile> tile) {
        super("", Action.CHOSENTILES);
        this.tiles=tile;
    }

    public static ChosenTilesMessage decrypt(String json){
        return new Gson().fromJson(json, ChosenTilesMessage.class);
    }

}
