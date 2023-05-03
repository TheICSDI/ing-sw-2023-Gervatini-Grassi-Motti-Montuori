package it.polimi.ingsw.network.messages;

public enum Action {
    SETNAME,
    CREATELOBBY,
    SHOWLOBBY,
    JOINLOBBY,
    EXITLOBBY,
    STARTGAME,
    PICKTILES,
    SELECTORDER,
    SELECTCOLUMN,

    ERROR//magari da rivedere, mi serve un figlio di general message in caso trovi errore per dirli cosa fare
}
