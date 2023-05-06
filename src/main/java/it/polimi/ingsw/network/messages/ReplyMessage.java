package it.polimi.ingsw.network.messages;

import com.google.gson.Gson;
import it.polimi.ingsw.model.Tile.Tile;
import it.polimi.ingsw.model.Tile.type;

import java.io.Serializable;
import java.util.List;

//Stampino di risposta del server, dato che spesso una stringa non basta
public class ReplyMessage extends GeneralMessage implements Serializable {
    protected List<Tile> tiles;
    private final String message;
    protected int idLobby;
    protected boolean gameStart=false;
    type[][] simpleBoard;

    public String getMessage() {
        return message;
    }

    public ReplyMessage(String msg,Action action){
        super(-1,action,-1,"");
        message = msg;
    }

    public String toString(){
        return new Gson().toJson(this);
    }

    /**
     * Converts the message in string and prints, useful if overriden in a subclass with more complex data to print
     */
    public void print(){
        System.out.println(this.message);
    }

    /**
     * Translate a message from json to an object
     * @param json String to translate
     * @return translated object
     */
    public static ReplyMessage decrypt(String json){
        return new Gson().fromJson(json,ReplyMessage.class);
    }

    /**
     * Useful to transfer the player into a game from a lobby
     * @return true if the game is starting
     */
    public boolean getGameStart(){return gameStart;}
    public int getIdLobby(){return idLobby;}

    public type[][] getSimpleBoard() {
        return simpleBoard;
    }

    public List<Tile> getTiles() {
        return tiles;
    }
}
