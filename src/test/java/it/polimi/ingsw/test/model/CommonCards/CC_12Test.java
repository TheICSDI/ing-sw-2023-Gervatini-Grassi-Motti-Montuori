package it.polimi.ingsw.test.model.CommonCards;

import it.polimi.ingsw.model.Cards.CCStrategy;
import it.polimi.ingsw.model.Cards.CC_12;
import org.junit.jupiter.api.Test;

/** Tests for class CC_12.java.
 * @author Andrea Grassi.
 */
class CC_12Test {

    @Test
    void isCompleted() {
        CCStrategy CC12=new CC_12();
        CCGeneral.check(CC12,"/JSON/CC/CC12_test.json");
    }
}