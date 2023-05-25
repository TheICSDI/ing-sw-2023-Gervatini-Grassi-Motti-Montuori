package it.polimi.ingsw.model.Cards;

import it.polimi.ingsw.model.Player;

public interface CCStrategy {

    boolean isCompleted(Player p); //TODO TESTARLE TUTTE PERCHE ALCUNE DAN PROBLEMI
    int getId();
}
