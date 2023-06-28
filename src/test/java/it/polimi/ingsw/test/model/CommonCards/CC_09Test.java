package it.polimi.ingsw.test.model.CommonCards;

import it.polimi.ingsw.model.Cards.CCStrategy;
import it.polimi.ingsw.model.Cards.CC_09;
import org.junit.jupiter.api.Test;

/** Tests for class CC_09.java.
 * @author Andrea Grassi.
 */
class CC_09Test {

    @Test
    void isCompleted() {
        CCStrategy CC09 = new CC_09();
        CCGeneral.check(CC09,"/JSON/CC/CC09_test.json");
    }
}