package it.polimi.ingsw.network.messages;

import com.google.gson.Gson;
import it.polimi.ingsw.model.Tile.Tile;
import it.polimi.ingsw.model.Tile.type;

/**
 * This class represents a message for warning the update of the board or shelf.
 * It extends the GeneralMessage class to include specific behavior.
 */
public class UpdateBoardMessage extends GeneralMessage{

    /**
     * Class constructor and set all needed attributes
     * @param action the type od update to request board or shelf
     * @param simpleShelf the matrix representing structure of the action
     */
    public UpdateBoardMessage(Action action, Tile[][] simpleShelf) {
        super("", action, -1, -1);
        this.simpleBoard = simpleShelf;
    }

    /**
     * Parses a JSON-formatted string to set the message.
     * @param json a JSON-formatted string
     * @return a fully initialized UpdateBoardMessage Object
     */
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
