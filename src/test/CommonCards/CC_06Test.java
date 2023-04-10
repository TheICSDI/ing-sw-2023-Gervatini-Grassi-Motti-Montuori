package test.CommonCards;

import main.java.it.polimi.ingsw.model.Cards.CCStrategy;
import main.java.it.polimi.ingsw.model.Cards.CC_06;
import org.junit.jupiter.api.Test;

class CC_06Test {

    @Test
    void isCompleted() {
        CCStrategy CC6=new CC_06();
        CCGeneral.check(CC6,"JSON/CC/CC06_test.json");
    }
}