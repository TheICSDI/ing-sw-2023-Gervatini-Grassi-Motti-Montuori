package it.polimi.ingsw.test.model.CommonCards;

import it.polimi.ingsw.model.Cards.CCStrategy;
import it.polimi.ingsw.model.Cards.CC_02;
import org.junit.jupiter.api.Test;


class CC_02Test {

    @Test
    void isCompleted() {
        CCStrategy CC2=new CC_02();
        CCGeneral.check(CC2,"JSON/CC/CC02_test.json");
    }
}