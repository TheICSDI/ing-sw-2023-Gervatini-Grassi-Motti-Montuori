/** Tests for class CC_10.java.
 * @author Andrea Grassi.
 */
package it.polimi.ingsw.test.model.CommonCards;

import it.polimi.ingsw.model.Cards.CCStrategy;
import it.polimi.ingsw.model.Cards.CC_10;
import org.junit.jupiter.api.Test;

class CC_10Test {

    @Test
    void isCompleted() {
        CCStrategy CC10=new CC_10();
        CCGeneral.check(CC10,"/JSON/CC/CC10_test.json");
    }
}