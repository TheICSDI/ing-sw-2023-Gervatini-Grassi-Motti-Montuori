package it.polimi.ingsw.network.messages;

public enum Action {
    CREATELOBBY,
    SHOWLOBBY,
    JOINLOBBY,
    EXITLOBBY,
    STARTGAME,
    ENDGAME,
    PT,
    SO,
    SC,
    UPDATEBOARD,
    UPDATESHELF,
    UPDATELOBBY,
    SETNAME,
    INGAMEEVENT, //AZIONE PER INVIARE MESSAGGI DURANTE IL GAME
    CHOSENTILES,
    SHOWPERSONAL,
    C,
    CA,
    SHOWCOMMONS,
    SHOWOTHERS,
    ERROR;

}
