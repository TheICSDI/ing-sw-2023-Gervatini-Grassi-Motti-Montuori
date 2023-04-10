package test.CommonCards;

import main.java.it.polimi.ingsw.model.Cards.CCStrategy;
import main.java.it.polimi.ingsw.model.Cards.CC_07;
import main.java.it.polimi.ingsw.model.Cards.CC_12;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CC_12Test {

    @Test
    void isCompleted() {
        CCStrategy CC12=new CC_12();
        CCGeneral.check(CC12,"JSON/CC/CC12_test.json");
    }
}