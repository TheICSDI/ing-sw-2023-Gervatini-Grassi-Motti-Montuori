package it.polimi.ingsw.network.messages;

import com.google.gson.Gson;

public class ChatMessage extends ReplyMessage{
    private final String phrase;
    private final String recipient;
    public ChatMessage(String username, String phrase, String recipient){
        super("", Action.C);
        this.username=username;
        this.phrase = phrase;
        this.recipient = recipient;
    }

    public String getRecipient() {
        return this.recipient;
    }

    public String getPhrase() {
        return this.phrase;
    }
    @Override
    public String toString()
    {
        return  new Gson().toJson(this);
    }

    public static ChatMessage decrypt(String json){
        return new Gson().fromJson(json,ChatMessage.class);
    }
}
