package it.polimi.ingsw.network.messages;

import com.google.gson.Gson;

/**
 * Represents a message for outgoing chat massages to all player of the party.
 * It extends the GeneralMessage class to include specific behavior.
 */
public class BroadcastMessage extends GeneralMessage{
    private final String phrase;

    /**
     * Class Constructor and set all the needed attributes.
     * @param idGame the id of a game.
     * @param lobbyId the id of a lobby.
     * @param nickname of a player.
     * @param phrase the chat message sent by the player.
     */
    public BroadcastMessage(int idGame, int lobbyId, String nickname, String phrase){
        super("", Action.CA, lobbyId, idGame);
        this.gameId = idGame;
        this.phrase = phrase;
        this.lobbyId = lobbyId;
        this.username = nickname;

    }

    /**
     * Parses a JSON-formatted string to set the message.
     * @param json a JSON-formatted string.
     * @return a fully initialized BroadcastMessage Object.
     */
    public static BroadcastMessage decrypt(String json){
        return new Gson().fromJson(json,BroadcastMessage.class);
    }

    /**
     * Gets the phrase.
     */
    public String getPhrase() {
        return this.phrase;
    }

    @Override
    public String toString()
    {
        return  new Gson().toJson(this);
    }
}
