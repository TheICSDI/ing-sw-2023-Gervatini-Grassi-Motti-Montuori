package it.polimi.ingsw.test.model.CommonCards;

import it.polimi.ingsw.model.Cards.CCStrategy;
import it.polimi.ingsw.model.Cards.CC_06;
import org.junit.jupiter.api.Test;

/** Tests for class CC_06.java.
 * @author Andrea Grassi.
 */
class CC_06Test {

    @Test
    void isCompleted() {
        CCStrategy CC6=new CC_06();
        CCGeneral.check(CC6,"/JSON/CC/CC06_test.json");
    }
}