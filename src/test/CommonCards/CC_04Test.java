/** Tests for class CC04.java.
 * @author Giulio Montuori.
 */
package test.CommonCards;

import main.java.it.polimi.ingsw.model.Cards.CCStrategy;
import main.java.it.polimi.ingsw.model.Cards.CC_04;
import org.junit.jupiter.api.Test;

class CC_04Test {
    @Test
    void isCompleted() {
        CCStrategy CC4=new CC_04();
        CCGeneral.check(CC4,"JSON/CC/CC04_test.json");
    }
}