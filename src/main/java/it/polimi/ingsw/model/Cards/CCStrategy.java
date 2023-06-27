package it.polimi.ingsw.model.Cards;

import it.polimi.ingsw.model.Player;

/** Iterface that implements the strategy pattern for the common goal cards.
 * @author Caterina Motti, Andrea Grassi. */
public interface CCStrategy {
    /** Checks if the given player has completed the common goal.
     * @return true only if the gived player has completed the common goal, false otherwise. */
    boolean isCompleted(Player p);

    /** It gets the id of the common goal card. */
    int getId();
}
