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

    private List<Position> pos = new ArrayList<Position>();

    /**
     * Constructor that initializes a message with the provided parameters.
     * @param message_id uid of the message
     * @param username uid of the user
     * @param pos and array with position of the piked tiles
     */
    public PickTilesMessage(int message_id, String username, List<Position> pos,int idGame) {
        super(message_id, Action.PT, -1, username);
        this.pos = pos;
        this.gameId = idGame;
    }

    /**
     * Parses a JSON-formatted string to set the message.
     * @param msg a JSON-formatted string
     * @return a fully initialized PickTilesMessage Object
     */
    public static PickTilesMessage decrypt(String msg){
        return new Gson().fromJson(msg, PickTilesMessage.class);
    }

    /**
     * Overrides the toString method to provide a custom string representation.
     */
    @Override
    public String toString(){
        return new Gson().toJson(this);
    }

    /**
     * Getter of pos
     * @param pos this.pos
     */
    public void getPos(List<Position> pos) {
        pos.addAll(this.pos);
    }
}
