package it.polimi.ingsw.network.messages;

import com.google.gson.Gson;

/**
 * This class represents a message for starting a game.
 * It extends the GeneralMessage class to include specific behavior.
 */
public class StartGameMessage extends GeneralMessage {

    private int nPlayers;

    /**
     * Constructor that initializes a message with the provided parameters.
     * @param messageId id of the message.
     * @param nickname of the player.
     */
    public StartGameMessage(int messageId, int idLobby, String nickname) {
        super(messageId, Action.STARTGAME, idLobby, nickname);
    }

    /**
     * Constructor that initializes a message with the provided parameters.
     * @param msg the message to display.
     * @param idGame id of the game.
     */
    public StartGameMessage(String msg,int idGame, int nPlayers){
        super(msg, Action.STARTGAME, -1, idGame);
        this.gameStart = true;
        this.nPlayers=nPlayers;
    }
    
    /**
     * Parses a JSON-formatted string to set the message.
     * @param msg a JSON-formatted string.
     * @return a fully initialized StartGameMessage Object.
     */
    public static StartGameMessage decrypt(String msg){
        return new Gson().fromJson(msg, StartGameMessage.class);
    }

    /** Gets number of players.*/
    public int getNPlayers() {
        return nPlayers;
    }

    @Override
    public String toString(){
        return new Gson().toJson(this);
    }
}
