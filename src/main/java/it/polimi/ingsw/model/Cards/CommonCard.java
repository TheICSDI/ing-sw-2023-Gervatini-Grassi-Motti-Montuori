/** Represents a common goal card.
 * @author Marco Gervatini, Caterina Motti */
package it.polimi.ingsw.model.Cards;
import it.polimi.ingsw.model.Player;

public class CommonCard{
    private int points;
    private final boolean firstCard;
    private final CCStrategy strategy;

    /** Creates a common goal card given its type.
     * @param currStrategy type of card that has to be created.
     * @param first true only if is the first card of the game, false otherwise.
     * @param nPlayers number of players of the game. */
    public CommonCard(CCStrategy currStrategy, boolean first, int nPlayers){
        this.strategy = currStrategy;
        this.firstCard = first;
        this.points = 2 * nPlayers;
    }

    /** Controls if the given player has completed the common goal card.
     *
     * @return true only if the player has completed the common goal card, false otherwise. */
    public boolean control(Player p){
        return strategy.isCompleted(p);
    }

    /** Sets the score token of the given player according to how many points are still available. */
    public void givePoints(Player p){
        if(this.points > 0){
            //If it is the first card of the game then it will modify the first score token
            if(this.firstCard){
                if(p.getScoreToken1() == 0){
                    p.setScoreToken1(points);
                    this.points = points - 2;
                }
            } else {
                //Otherwise it will modify the second score token
                if(p.getScoreToken2() == 0){
                    p.setScoreToken2(points);
                    this.points = points - 2;
                }
            }
        }
    }

    public int getPoints() {
        return points;
    }
}
