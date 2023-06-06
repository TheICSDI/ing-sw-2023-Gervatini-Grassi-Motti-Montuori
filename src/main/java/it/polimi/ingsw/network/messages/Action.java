/** Enumeration of the possibles type of messages that the clients can send to the server in order to play the game.
 * @author Marco Gervatini, Andrea Grassi, Giulio Montuori. */
package it.polimi.ingsw.network.messages;

public enum Action {
    CREATELOBBY,
    SHOWLOBBY,
    JOINLOBBY,
    EXITLOBBY,
    STARTGAME,
    POINTS,
    WINNER,
    ENDGAME,
    ENDGAMETOKEN,
    PT,
    SO,
    SC,
    UPDATEBOARD,
    UPDATESHELF,
    UPDATELOBBY,
    SETNAME,
    INGAMEEVENT,
    TURN,
    CHOSENTILES,
    SHOWPERSONAL,
    C,
    CA,
    SHOWCOMMONS,
    COMMONCOMPLETED,
    SHOWOTHERS,
    PING,
    ERROR,
    RECONNECT
}
