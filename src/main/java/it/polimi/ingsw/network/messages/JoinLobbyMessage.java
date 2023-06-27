package it.polimi.ingsw.network.messages;

import com.google.gson.Gson;

/**
 * This class represents a message for joining a lobby in a game.
 * It extends the GeneralMessage class to include specific behavior for joining lobby messages.
 */
public class JoinLobbyMessage extends GeneralMessage{

    /**
     * Constructor that initializes a message with the provided parameters.
     * @param message_id id of the message.
     * @param lobbyId id of lobby.
     * @param nickname id of the player.
     */
    public JoinLobbyMessage(int message_id, int lobbyId, String nickname) {
        super(message_id, Action.JOINLOBBY, lobbyId, nickname);
    }

    /**
     * Class constructor and set all needed attributes.
     * @param msg the message to display.
     * @param idLobby the id of the lobby.
     */
    public JoinLobbyMessage(String msg,int idLobby) {
        super(msg, Action.JOINLOBBY, idLobby, -1 );
    }

    /**
     * Parses a JSON-formatted string to set the message.
     * @param msg a JSON-formatted string.
     * @return a fully initialized JoinLobbyMessage Object.
     */
    public static JoinLobbyMessage decrypt(String msg){
        return new Gson().fromJson(msg, JoinLobbyMessage.class);
    }

    @Override
    public String toString(){
        return new Gson().toJson(this);
    }
}
