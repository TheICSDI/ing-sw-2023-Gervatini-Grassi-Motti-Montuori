/** Tests for class CC04.java.
 * @author Giulio Montuori.
 */
package test.CommonCards;

import it.polimi.ingsw.model.Cards.CCStrategy;
import it.polimi.ingsw.model.Cards.CC_04;
import org.junit.jupiter.api.Test;

/** Tests for class CC04.java
 * @author Giulio Montuori
 */
class CC_04Test {
    @Test
    void isCompleted() {
        CCStrategy CC4=new CC_04();
        CCGeneral.check(CC4,"JSON/CC/CC04_test.json");
    }
}
/*
    The first shelf represents a case with only two 2x2 squares, and returns true.
    The second shelf represents a case with two 2x2 squares and additional tiles surrounding them, and also returns true.
    The third shelf has various tiles and two 2x2 squares, but they are of the same type and connected, so it returns false.
    From the fourth to the eleventh shelves, all eight sides are tested individually to check if each specific case is handled, and all return false.
 */