package test.CommonCards;

import main.java.it.polimi.ingsw.model.Cards.CCStrategy;
import main.java.it.polimi.ingsw.model.Cards.CC_03;
import main.java.it.polimi.ingsw.model.Cards.CC_05;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CC_03Test {

    @Test
    void isCompleted() {
        CCStrategy CC3=new CC_03();
        CCGeneral.check(CC3,"JSON/CC/CC03_test.json",1);
        /*,

      ,

      ,

      */
    }
}