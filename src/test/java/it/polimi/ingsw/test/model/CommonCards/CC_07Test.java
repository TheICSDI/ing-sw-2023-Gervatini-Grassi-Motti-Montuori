package it.polimi.ingsw.test.model.CommonCards;

import it.polimi.ingsw.model.Cards.CCStrategy;
import it.polimi.ingsw.model.Cards.CC_07;
import org.junit.jupiter.api.Test;

class CC_07Test {

    @Test
    void isCompleted() {
        CCStrategy CC07=new CC_07();
        CCGeneral.check(CC07,"/JSON/CC/CC07_test.json");
    }
}