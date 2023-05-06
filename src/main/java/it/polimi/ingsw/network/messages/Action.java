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
    UPDATESHELF,
    UPDATELOBBY,
    SETNAME,

    INGAMEEVENT, //AZIONE PER INVIARE MESSAGGI DURANTE IL GAME

    CHOSENTILES,

    //TODO da implementare messaggi per mostrare le common e le personal goal
    ERROR
}
