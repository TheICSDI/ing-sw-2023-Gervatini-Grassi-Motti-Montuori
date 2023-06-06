package it.polimi.ingsw.network.messages;

import com.google.gson.Gson;
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
public abstract class GeneralMessage implements Serializable {

    private int message_id;
    private final Action action;
    protected int lobbyId;
    private String message;
    protected String username;
    protected int gameId;
    List<Integer> cc = new ArrayList<>();
    Tile[][] simpleBoard;
    protected List<Tile> tiles;
    protected boolean gameStart = false;

    /**
     * Constructor that initializes a message with the provided parameters.
     * @param message_id the unique identification of the message
     * @param action the type of message
     * @param username the unique identification of the user
     * @param lobby_id the unique identification of the lobby
     */
    //The old general message
    public GeneralMessage(int message_id, Action action, int lobby_id, String username)
    {
        this.message_id = message_id;
        this.action = action;
        this.lobbyId = lobby_id;
        this.username = username;
    }
    /**
     * Constructor that initializes a message with the provided parameters.
     * @param msg the message to display
     * @param action the type of the message
     * @param lobby_id the unique identification of the lobby
     * @param game_id the unique identification of the game
     */
    //The old ReplyMessage
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
    public static Action identify(String msg) throws ParseException {
        JSONParser parser = new JSONParser();
        JSONObject msg_obj = (JSONObject) parser.parse(msg);
        return Action.valueOf(msg_obj.get("action").toString());
    }

    /**
     * Getter for message_id
     * @return this.message_id
     */
    public int getMessage_id() {
        return this.message_id;
    }

    /**
     * Getter for action
     * @return this.action
     */
    public Action getAction() {
        return this.action;
    }

    /**
     * Getter for lobbyId
     * @return this.lobbyId
     */
    public int getIdLobby() {
        return this.lobbyId;
    }

    /**
     * Getter for username
     * @return this.username
     */
    public String getUsername() {
        return this.username;
    }

    /**
     * Getter for gameId
     * @return this.gameId
     */
    public int getGameId() {
        return gameId;//Da sistemare
    }

    /**
     * Getter for message
     * @return this.message
     */
    public String getMessage(){
        return this.message;
    }

    /**
     * Getter for simpleBoard
     * @return this.simpleBoard
     */
    public Tile[][] getSimpleBoard() {
        return this.simpleBoard;
    }

     /**
     * Getter for tiles
     */
    public void getTiles(List<Tile> tiles) {
        tiles.addAll(this.tiles);
    }

    /**
     * Getter for cc
     */
    public void getCC(List<Integer> cc){
        cc.addAll(this.cc);
    }

    public boolean isAvailable(){return true;}

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
