package main.java.it.polimi.ingsw.network.messages;

import java.io.Serializable;


// composed by the command to call and possible params that it needs
//Serializable implementation may be changed or fixed
public abstract class GeneralMessage implements Serializable {
    private final int message_id;
    private final Action action;
    private final String username;
    // List<String> params = new ArrayList<>();

    public GeneralMessage(int message_id, Action action, String username)
    {
        this.message_id = message_id;
        this.action = action;
        this.username = username;
    }

    public String startMessage()
    {
        return "{" +
                "\"message_id\":" + this.message_id +
                "\"action\":\"" + this.action + "\"" +
                "\"username\":\"" + this.username + "\"";
    }
}
