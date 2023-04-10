package test.CommonCards;

import main.java.it.polimi.ingsw.model.Cards.CCStrategy;
import main.java.it.polimi.ingsw.model.Cards.CC_02;
import org.junit.jupiter.api.Test;


class CC_02Test {

    @Test
    void isCompleted() {
        CCStrategy CC2=new CC_02();
        CCGeneral.check(CC2,"JSON/CC/CC02_test.json");
    }
}