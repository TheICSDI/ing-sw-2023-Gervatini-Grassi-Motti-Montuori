package it.polimi.ingsw.network.messages;

import com.google.gson.Gson;
import it.polimi.ingsw.model.Lobby;

import java.util.List;

/**
 * This class represents a message for showing lobbies.
 * It extends the GeneralMessage class to include specific behavior.
 */
public class ShowLobbyMessage extends GeneralMessage{

    private List<Lobby> Lobbies;

    /**
     * Constructor that initializes a message with the provided parameters.
     * @param message_id uid of the message
     * @param username uid of the user
     */
    public ShowLobbyMessage(int message_id, String username) {
        super(message_id, Action.SHOWLOBBY, -1, username);
    }

    /**
     * Class construcor and set all needed attributes
     * @param msg the message to display
     * @param Lobbies list of all the lobby
     */
    public ShowLobbyMessage(String msg, List<Lobby> Lobbies) {
        super(msg, Action.SHOWLOBBY, -1, -1);
        this.Lobbies = Lobbies;
    }

    /**
     * Parses a JSON-formatted string to set the message.
     * @param json a JSON-formatted string
     * @return a fully initialized ShowLobbyMessage Object
     */
    public static ShowLobbyMessage decrypt(String json){
        return new Gson().fromJson(json, ShowLobbyMessage.class);
    }

    /**
     * Overrides the toString method to provide a custom string representation.
     */
    @Override
    public String toString(){
        return new Gson().toJson(this);
    }

    /**
     * Getter of lobbies
     * @return this.lobbies
     */
    public List<Lobby> getLobbies() {
        return this.Lobbies;
    }
}
