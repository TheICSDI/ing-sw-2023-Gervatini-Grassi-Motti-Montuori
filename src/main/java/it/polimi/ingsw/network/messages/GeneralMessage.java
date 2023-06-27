package it.polimi.ingsw.network.messages;

import com.google.gson.Gson;
import it.polimi.ingsw.model.Tile.Tile;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * This abstract class represents a general message in a client-server communication.
 * It implements the Serializable interface to allow object serialization.
 */
public abstract class GeneralMessage implements Serializable {
    private int messageId;
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
     * @param message_id the id of the message.
     * @param action the type of message.
     * @param nickname of the player.
     * @param lobbyId the id of the lobby.
     */
    //The old general message
    public GeneralMessage(int message_id, Action action, int lobbyId, String nickname) {
        this.messageId = message_id;
        this.action = action;
        this.lobbyId = lobbyId;
        this.username = nickname;
    }

    /**
     * Constructor that initializes a message with the provided parameters.
     * @param msg the message to display.
     * @param action the type of the message.
     * @param lobbyId the unique identification of the lobby.
     * @param gameId the unique identification of the game.
     */
    //The old ReplyMessage
    public GeneralMessage(String msg, Action action, int lobbyId, int gameId){
        this.message = msg;
        this.action = action;
        this.lobbyId = lobbyId;
        this.gameId = gameId;
    }

    /**
     * Reads only the action of the message.
     * @param msg Message received.
     * @return action of the msg.
     */
    public static Action identify(String msg) throws ParseException {
        JSONParser parser = new JSONParser();
        JSONObject msg_obj = (JSONObject) parser.parse(msg);
        return Action.valueOf(msg_obj.get("action").toString());
    }

    /**
     * Gets the id of the message.
     */
    public int getMessageId() {
        return this.messageId;
    }

    /**
     * Gets the action of the message.
     */
    public Action getAction() {
        return this.action;
    }

    /**
     * Gets the lobby id.
     */
    public int getIdLobby() {
        return this.lobbyId;
    }

    /**
     * Gets the nickname of the player.
     */
    public String getUsername() {
        return this.username;
    }

    /**
     * Gets the gameId.
     */
    public int getGameId() {
        return gameId;
    }

    /**
     * Gets the message.
     */
    public String getMessage(){
        return this.message;
    }

    /**
     * Gets the simpleBoard.
     */
    public Tile[][] getSimpleBoard() {
        return this.simpleBoard;
    }

     /**
     * Gets the tiles.
     */
    public void getTiles(List<Tile> tiles) {
        tiles.addAll(this.tiles);
    }

    /**
     * Gets the list of common goal cards' ids.
     */
    public void getCC(List<Integer> cc){
        cc.addAll(this.cc);
    }

    /** Gets the value about the availability of the chosen nickname. */
    public boolean isAvailable(){return true;}

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
