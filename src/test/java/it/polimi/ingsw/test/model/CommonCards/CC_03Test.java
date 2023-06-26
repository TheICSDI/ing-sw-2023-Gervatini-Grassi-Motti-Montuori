/** Tests for class CC03.java.
 * @author Andrea Grassi.
 */
package it.polimi.ingsw.test.model.CommonCards;

import it.polimi.ingsw.model.Cards.CCStrategy;
import it.polimi.ingsw.model.Cards.CC_03;
import org.junit.jupiter.api.Test;

class CC_03Test {

    @Test
    void isCompleted() {
        CCStrategy CC3=new CC_03();
        CCGeneral.check(CC3,"/JSON/CC/CC03_test.json");
    }
}