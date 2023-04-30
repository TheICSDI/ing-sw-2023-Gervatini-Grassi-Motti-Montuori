package main.java.it.polimi.ingsw.network.messages;

public enum Action {
    SETNAME,
    CREATELOBBY,
    SHOWLOBBY,
    JOINLOBBY,
    EXITLOBBY,
    STARTGAME,
    PICKTILES,
    SELECTORDER,
    PUTTILES,

    //server commands, for how is structured the message type this is the simplest implementation
    REDOCOMMAND
}
