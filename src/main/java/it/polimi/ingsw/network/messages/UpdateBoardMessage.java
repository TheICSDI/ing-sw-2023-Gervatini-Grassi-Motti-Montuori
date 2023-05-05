package it.polimi.ingsw.network.messages;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import it.polimi.ingsw.exceptions.InvalidActionException;
import it.polimi.ingsw.exceptions.InvalidKeyException;
import it.polimi.ingsw.model.Tile.type;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 * This class represents a message for warning the update of the board.
 * It extends the GeneralMessage class to include specific behavior.
 */
public class UpdateBoardMessage extends ReplyMessage {


    /**
     * Constructor that initializes a message with the provided parameters.
     *
     */
    public UpdateBoardMessage(Action action, type[][] simpleShelf) {
        super("", action);
        this.simpleBoard = simpleShelf;
    }

    /**
     * Constructor that parses a JSON-formatted string and initializes the message.
     * @param msg a JSON-formatted string
     */
    /*
    public UpdateBoardMessage(String msg) throws ParseException, InvalidActionException, InvalidKeyException {
        super(msg);
        JSONParser parser = new JSONParser();
        JSONObject msg_obj = (JSONObject) parser.parse(msg);

        // Validates that the 'action' key in the JSON object matches the expected action for this message type.
        if(!msg_obj.get("action").toString().equals(Action.UPDATEBOARD.toString()))
        {
            throw new InvalidActionException("Invalid UpdateBoardMessage encoding");
        }
        if(!msg_obj.containsKey("simpleShelf"))
        {
            throw new InvalidKeyException("Missing key: simpleShelf");
        }
        //
    }

     */

    /**
     * Overrides the toString method to provide a custom string representation.
     */
    @Override
    public String toString()
    {
        return new Gson().toJson(this);
    }

    public static UpdateBoardMessage decrypt(String json){
        return new Gson().fromJson(json,UpdateBoardMessage.class);
    }

    public type[][] getSimpleBoard(){return this.simpleBoard;}
}
