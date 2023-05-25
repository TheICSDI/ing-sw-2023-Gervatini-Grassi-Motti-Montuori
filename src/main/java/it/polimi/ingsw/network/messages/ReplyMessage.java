package it.polimi.ingsw.network.messages;

import com.google.gson.Gson;
import it.polimi.ingsw.model.Tile.Tile;
import it.polimi.ingsw.model.Tile.type;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

//Stampino di risposta del server, dato che spesso una stringa non basta
public class ReplyMessage extends GeneralMessage implements Serializable {
    protected List<Tile> tiles;
    private final String message;

    protected boolean gameStart=false;
    Tile[][] simpleBoard;
    List<Integer> cc=new ArrayList<>();
    public void getCC(List<Integer> cc){
        cc.addAll(this.cc);
    }

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

    public Tile[][] getSimpleBoard() {
        return simpleBoard;
    }

    public void getTiles(List<Tile> tiles) {
        tiles.addAll(this.tiles);
    }
}