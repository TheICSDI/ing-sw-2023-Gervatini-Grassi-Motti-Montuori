/** Represents a common goal card.
 * @author Marco Gervatini */
package main.java.it.polimi.ingsw.model.Cards;
import main.java.it.polimi.ingsw.model.Player;

public class CommonCard{
    private int points = 8;
    private final boolean firstCard;
    private final CCStrategy strategy;

    /** Creates a common goal card given its type.
     *
     * @param currStrategy type of card that has to be created.
     * @param first true only if is the first card of the game, false otherwise. */
    public CommonCard(CCStrategy currStrategy, boolean first){
        this.strategy = currStrategy;
        this.firstCard = first;
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
            //If the player has completed the common goal card
            if(control(p)){
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
    }
}
