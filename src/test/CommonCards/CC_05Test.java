package test.CommonCards;

import main.java.it.polimi.ingsw.model.Cards.CCStrategy;
import main.java.it.polimi.ingsw.model.Cards.CC_05;
import main.java.it.polimi.ingsw.model.Cards.CC_08;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CC_05Test {

    @Test
    void isCompleted() {
        CCStrategy CC5=new CC_05();
        CCGeneral.check(CC5,"JSON/CC05_test.json",1);
    }
}