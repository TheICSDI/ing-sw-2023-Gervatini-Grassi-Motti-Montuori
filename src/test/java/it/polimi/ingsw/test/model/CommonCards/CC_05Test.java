package it.polimi.ingsw.test.model.CommonCards;

import it.polimi.ingsw.model.Cards.CCStrategy;
import it.polimi.ingsw.model.Cards.CC_05;
import org.junit.jupiter.api.Test;

/** Tests for class CC_05.java.
 * @author Andrea Grassi.
 */
class CC_05Test {

    @Test
    void isCompleted() {
        CCStrategy CC5=new CC_05();
        CCGeneral.check(CC5,"/JSON/CC/CC05_test.json");
    }
}