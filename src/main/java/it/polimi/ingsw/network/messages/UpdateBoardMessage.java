package it.polimi.ingsw.network.messages;

import com.google.gson.Gson;
import it.polimi.ingsw.model.Tile.Tile;
import it.polimi.ingsw.model.Tile.type;

/**
 * This class represents a message for warning the update of the board.
 * It extends the GeneralMessage class to include specific behavior.
 */
public class UpdateBoardMessage extends GeneralMessage{

    public UpdateBoardMessage(Action action, Tile[][] simpleShelf) {
        super("", action, -1, -1);
        this.simpleBoard = simpleShelf;
    }

    public static UpdateBoardMessage decrypt(String json){
        return new Gson().fromJson(json,UpdateBoardMessage.class);
    }

    /**
     * Overrides the toString method to provide a custom string representation.
     */
    @Override
    public String toString() {
        return new Gson().toJson(this);
    }

}
