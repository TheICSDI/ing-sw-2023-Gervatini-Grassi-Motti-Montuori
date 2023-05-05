package it.polimi.ingsw.network.messages;

import com.google.gson.Gson;
import it.polimi.ingsw.exceptions.InvalidActionException;
import it.polimi.ingsw.exceptions.InvalidKeyException;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 * This class represents a message for setting a username for the game.
 * It extends the GeneralMessage class to include specific behavior for setting a username for the game.
 */
public class SetNameMessage extends GeneralMessage{
    private boolean Available;

    public boolean isAvailable() {
        return Available;
    }

    /**
     * Constructor that initializes a message with the provided parameters.
     *
     * @param user uid of the user
     * @param Available nickname available
     */
    public SetNameMessage(String user,boolean Available){
        super(-1,Action.SETNAME,-1,user);
        this.Available=Available;
    }
    /**
     * Constructor that parses a JSON-formatted string and initializes the message.
     * @param msg a JSON-formatted string
     */
    public SetNameMessage(String msg) throws ParseException, InvalidActionException, InvalidKeyException {
        super(msg);
        JSONParser parser = new JSONParser();
        JSONObject msg_obj = (JSONObject) parser.parse(msg);

        // Validates that the 'action' key in the JSON object matches the expected action for this message type.
        if(!msg_obj.get("action").toString().equals(Action.SETNAME.toString()))
        {
            throw new InvalidActionException("Invalid SetNameMessage encoding");
        }
        if(!msg_obj.containsKey("Available")) {
            throw new InvalidKeyException("Missing key: Available");
        }
        this.Available = (boolean) msg_obj.get("Available");
    }

    /**
     * Overrides the toString method to provide a custom string representation.
     */
    @Override
    public String toString()
    {
        return new Gson().toJson(this);
    }

    public static SetNameMessage decrypt(String json){
        return new Gson().fromJson(json,SetNameMessage.class);
    }

    public void print(){
        if(!Available){
            System.out.print("Nickname not available, try again: ");
        }else{
            System.out.println("Nickname set: "+this.getUsername());
        }
    }
}
