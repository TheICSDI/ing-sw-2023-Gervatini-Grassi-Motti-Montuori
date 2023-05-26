package it.polimi.ingsw.network.messages;

import com.google.gson.Gson;

/**
 * This class represents a message for outgoing chat messages for a specific player of a party.
 * It extends the GeneralMessage class to include specific behavior.
 */
public class ChatMessage extends GeneralMessage{
    private final String phrase;
    private final String recipient;
    /**
     * Class Constructor and set all the needed attributes
     * @param username the uid of the sender
     * @param phrase the chat message sent by the player
     * @param recipient the uid of the recipient
     */
    public ChatMessage(String username, String phrase, String recipient){
        super("", Action.C, -1,-1 );
        this.username = username;
        this.phrase = phrase;
        this.recipient = recipient;
    }

    /**
     * Parses a JSON-formatted string to set the message.
     * @param json a JSON-formatted string
     * @return a fully initialized ChatMessage Object
     */
    public static ChatMessage decrypt(String json){
        return new Gson().fromJson(json,ChatMessage.class);
    }

    /**
     * Overrides the toString method to provide a custom string representation.
     */
    @Override
    public String toString() {
        return  new Gson().toJson(this);
    }

    /**
     * Getter for recipient
     * @return this.recipient
     */
    public String getRecipient() {
        return this.recipient;
    }

    /**
     * Getter for phrase
     * @return this.phrase
     */
    public String getPhrase() {
        return this.phrase;
    }
}
