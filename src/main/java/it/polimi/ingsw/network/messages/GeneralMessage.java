package it.polimi.ingsw.network.messages;

import it.polimi.ingsw.exceptions.InvalidKeyException;
import it.polimi.ingsw.model.Tile.Tile;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * This abstract class represents a general message in a client-server communication.
 * It implements the Serializable interface to allow for object serialization.
 */

//ToDo REFRACTOR DI TUTTI I MESSAGGI, UNIFORMAZIONE DEL GSON E PULIZIA GENERALE DEL CODICE
public abstract class GeneralMessage implements Serializable {

    private int message_id;
    private Action action;
    protected int lobbyId;
    private String message;
    protected String username;
    //private String msg;
    protected int gameId;
    List<Integer> cc = new ArrayList<>();
    Tile[][] simpleBoard;
    protected List<Tile> tiles;
    protected boolean gameStart = false;


    /**
     * Constructor that initializes a message with the provided parameters.
     * @param message_id the unique identification of the message (probabilmente da rimuovere)
     * @param action the unique identification of the message
     * @param username the unique identification of the user
     */
    //the old general message
    public GeneralMessage(int message_id, Action action, int lobby_id, String username)
    {
        this.message_id = message_id;
        this.action = action;
        this.lobbyId = lobby_id;
        this.username = username;
    }
    //first type of old ReplyMessage
    public GeneralMessage(String msg, Action action, int lobby_id, int game_id){
        this.message = msg;
        this.action = action;
        this.lobbyId = lobby_id;
        this.gameId = game_id;
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

    public int getMessage_id() {
        return message_id;
    }

    public Action getAction() {
        return action;
    }

    public int getIdLobby() {
        return lobbyId;
    }

    public String getUsername() {
        return username;
    }

    public int getGameId() {
        return gameId;//Da sistemare
    }

    public String getMessage(){
        return this.message;
    }

    public Tile[][] getSimpleBoard() {
        return this.simpleBoard;
    }

    public void getTiles(List<Tile> tiles) {
        tiles.addAll(this.tiles);
    }

    public void getCC(List<Integer> cc){
        cc.addAll(this.cc);
    }
}
