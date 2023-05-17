package it.polimi.ingsw.model.CommonCards;

import it.polimi.ingsw.model.Cards.CCStrategy;
import it.polimi.ingsw.model.Cards.CC_09;
import org.junit.jupiter.api.Test;

class CC_09Test {

    @Test
    void isCompleted() {
        CCStrategy CC09=new CC_09();
        CCGeneral.check(CC09,"JSON/CC/CC09_test.json");
    }
}