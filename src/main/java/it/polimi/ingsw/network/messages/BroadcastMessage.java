package it.polimi.ingsw.network.messages;

import com.google.gson.Gson;

/**
 * This class represents a message for setting a username for the game.
 * It extends the GeneralMessage class to include specific behavior for setting a username for the game.
 */
public class BroadcastMessage extends GeneralMessage{
    private final String phrase;
    public BroadcastMessage(int idGame, int lobbyId, String username, String phrase){
        super("", Action.CA, lobbyId, idGame);
        this.gameId = idGame;
        this.phrase = phrase;
        this.lobbyId = lobbyId;
        this.username = username;

    }

    /**
     * Parses a JSON-formatted string to set the message.
     * @param json a JSON-formatted string
     * @return a fully initialized BroadcastMessage Object
     */
    public static BroadcastMessage decrypt(String json){
        return new Gson().fromJson(json,BroadcastMessage.class);
    }

    /**
     * Overrides the toString method to provide a custom string representation.
     */
    @Override
    public String toString()
    {
        return  new Gson().toJson(this);
    }

    public String getPhrase() {
        return this.phrase;
    }
}
