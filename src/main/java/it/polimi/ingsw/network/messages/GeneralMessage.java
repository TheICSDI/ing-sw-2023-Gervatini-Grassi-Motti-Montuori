package it.polimi.ingsw.network.messages;

import it.polimi.ingsw.exceptions.InvalidKeyException;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.Serializable;

/**
 * This abstract class represents a general message in a client-server communication.
 * It implements the Serializable interface to allow for object serialization.
 */

//ToDo REFRACTOR DI TUTTI I MESSAGGI, UNIFORMAZIONE DEL GSON E PULIZIA GENERALE DEL CODICE
public abstract class GeneralMessage implements Serializable {

    private final int message_id;
    private final Action action;
    protected int idLobby;
    protected int idGame;
    protected String username;
    protected int limit;


    /**
     * Constructor that initializes a message with the provided parameters.
     * @param message_id the unique identification of the message (probabilmente da rimuovere)
     * @param action the unique identification of the message
     * @param username the unique identification of the user
     */
    public GeneralMessage(int message_id, Action action, int lobby_id, String username)
    {
        this.message_id = message_id;
        this.action = action;
        this.idLobby = lobby_id;
        this.username = username;
    }

    /**
     * Constructor that parses a JSON-formatted string and initializes a GeneralMessage.
     * @param msg a JSON-formatted string
      */
    public GeneralMessage(String msg) throws ParseException, InvalidKeyException
    {
        JSONParser parser = new JSONParser();
        JSONObject msg_obj = (JSONObject) parser.parse(msg);

        // Validates that the required keys are present in the JSON object.
        if(!msg_obj.containsKey("message_id")) {
            throw new InvalidKeyException("Missing key: message_id");
        }
        if(!msg_obj.containsKey("lobby_id")) {
            throw new InvalidKeyException("Missing key: lobby_id");
        }
        if(!msg_obj.containsKey("username")) {
            throw new InvalidKeyException("Missing key: username");
        }
        if(!msg_obj.containsKey("action")) {
            throw new InvalidKeyException("Missing key: action");
        }

        // Assigns the values from the JSON object to the instance variables.
        this.message_id = Integer.parseInt(msg_obj.get("message_id").toString());
        this.action = Action.valueOf(msg_obj.get("action").toString());
        this.idLobby = Integer.parseInt(msg_obj.get("lobby_id").toString());
        this.username = msg_obj.get("username").toString();
    }

    /**
     * The start of the JSON message to send
     * @return the of the JSON
     */
    public String startMessage()
    {
        return "{" +
                "\"message_id\":" + this.message_id + "," +
                "\"action\":\"" + this.action + "\"," +
                "\"lobby_id\":" + this.idLobby + "," +
                "\"username\":\"" + this.username + "\"";
    }

    public int getMessage_id() {
        return message_id;
    }

    public Action getAction() {
        return action;
    }

    public int getIdLobby() {
        return idLobby;
    }

    public String getUsername() {
        return username;
    }

    public int getIdGame() {
        return idGame;//Da sistemare
    }
    /**
     * Reads only the action of the message
     * @param msg Message received
     * @return action of the msg
     */
    public static Action identify(String msg) throws ParseException, InvalidKeyException {
        JSONParser parser = new JSONParser();
        JSONObject msg_obj = (JSONObject) parser.parse(msg);
        if(!msg_obj.containsKey("action")) {
            throw new InvalidKeyException("Missing key: action");
        }
        return Action.valueOf(msg_obj.get("action").toString());
    }

    public int getLimit(){return limit;}
    public String getMessage(){return null;}
}
