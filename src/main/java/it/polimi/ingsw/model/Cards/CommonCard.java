package it.polimi.ingsw.model.Cards;
import it.polimi.ingsw.model.Player;

/** Represents a common goal card.
 * @author Marco Gervatini, Caterina Motti */
public class CommonCard{
    private int points;
    private final boolean firstCard;
    private final CCStrategy strategy;

    /** Creates a common goal card given its type.
     * @param currStrategy type of card that has to be created.
     * @param first true only if is the first card of the game, false otherwise. */
    public CommonCard(CCStrategy currStrategy, boolean first){
        this.strategy = currStrategy;
        this.firstCard = first;
        this.points = 8;
    }

    /** Controls if the given player has completed the common goal card.
     * @return true only if the player has completed the common goal card, false otherwise. */
    public boolean control(Player p){
        return strategy.isCompleted(p);
    }

    /** Sets the score token of the given player according to how many points are still available.
     * @param p a player.
     * @param commonPoints number of available points. */
    public void givePoints(Player p, int commonPoints){
        if(this.points > 0){
            //If it is the first card of the game then it will modify the first score token
            if(this.firstCard){
                p.setScoreToken1(points);
                this.points = points - commonPoints;
            } else {
                //Otherwise it will modify the second score token
                p.setScoreToken2(points);
                this.points = points - commonPoints;
            }
        }
    }

    /** Gets the points of the common card. */
    public int getPoints() {
        return points;
    }
}
