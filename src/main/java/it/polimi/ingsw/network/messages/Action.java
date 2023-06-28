package it.polimi.ingsw.network.messages;

/** Enumeration of the possibles type of messages between client and server.
 * @author Marco Gervatini, Andrea Grassi, Giulio Montuori. */
public enum Action {
    CREATELOBBY,
    SHOWLOBBY,
    JOINLOBBY,
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
    SHOWSHELF,
    SHOWBOARD,
    C,
    CA,
    SHOWCOMMONS,
    COMMONCOMPLETED,
    SHOWOTHERS,
    PING,
    ERROR,
    TOKENS,
    RECONNECT;
}
