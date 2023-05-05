package it.polimi.ingsw.network.messages;

public enum Action {
    CREATELOBBY,
    SHOWLOBBY,
    JOINLOBBY,
    EXITLOBBY,
    STARTGAME,
    ENDGAME,
    PICKTILES,
    SELECTORDER,
    SELECTCOLUMN,
    UPDATEBOARD,
    UPDATELOBBY,
    SETNAME,

    ERROR//magari da rivedere, mi serve un figlio di general message in caso trovi errore per dirli cosa fare
}
