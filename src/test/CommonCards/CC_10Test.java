/** Tests for class CC_10.java.
 * @author Andrea Grassi.
 */
package test.CommonCards;

import main.java.it.polimi.ingsw.model.Cards.CCStrategy;
import main.java.it.polimi.ingsw.model.Cards.CC_10;
import org.junit.jupiter.api.Test;

class CC_10Test {

    @Test
    void isCompleted() {
        CCStrategy CC10=new CC_10();
        CCGeneral.check(CC10,"JSON/CC/CC10_test.json");
    }
}