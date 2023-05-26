package it.polimi.ingsw.network.messages;

import com.google.gson.Gson;

/**
 * This class represents a message for outgoing chat massages to all player of the party.
 * It extends the GeneralMessage class to include specific behavior.
 */
public class BroadcastMessage extends GeneralMessage{
    private final String phrase;

    /**
     * Class Constructor and set all the needed attributes
     * @param idGame the uid of a specific game
     * @param lobbyId the uid of a specific lobby
     * @param username the uid of a specific player
     * @param phrase the chat message sent by the player
     */
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

    /**
     * Getter for phrase
     * @return this.pharse
     */
    public String getPhrase() {
        return this.phrase;
    }
}
