package it.polimi.ingsw.network.messages;

import com.google.gson.Gson;
import it.polimi.ingsw.model.Position;

import java.util.ArrayList;
import java.util.List;

/**
 * This class represents a message for picking tiles in a game.
 * It extends the GeneralMessage class to include specific behavior for picking tiles in a game.
 */
public class PickTilesMessage extends GeneralMessage{
    private final List<Position> pos;

    /**
     * Constructor that initializes a message with the provided parameters.
     * @param messageId id of the message.
     * @param nickname of the player.
     * @param pos and array with position of the piked tiles.
     */
    public PickTilesMessage(int messageId, String nickname, List<Position> pos,int idGame) {
        super(messageId, Action.PT, -1, nickname);
        this.pos = pos;
        this.gameId = idGame;
    }

    /**
     * Parses a JSON-formatted string to set the message.
     * @param msg a JSON-formatted string.
     * @return a fully initialized PickTilesMessage Object.
     */
    public static PickTilesMessage decrypt(String msg){
        return new Gson().fromJson(msg, PickTilesMessage.class);
    }


    /**
     * Gets the list of positions.
     */
    public void getPos(List<Position> pos) {
        pos.addAll(this.pos);
    }

    @Override
    public String toString(){
        return new Gson().toJson(this);
    }
}
