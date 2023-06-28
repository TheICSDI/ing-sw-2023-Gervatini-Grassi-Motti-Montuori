package it.polimi.ingsw.test.model.CommonCards;

import it.polimi.ingsw.model.Cards.CCStrategy;
import it.polimi.ingsw.model.Cards.CC_01;

import org.junit.jupiter.api.Test;

/** Tests for class CC01.java
 * @author Giulio Montuori
 */
class CC_01Test {
    @Test
    void isCompleted() {
        CCStrategy CC01=new CC_01();
        CCGeneral.check(CC01,"/JSON/CC/CC01_test.json");
    }
}
/*
    The first shelf represents the best-case scenario without possible "IndexOutOfBound" or groups larger than 2, and returns true.
    The second shelf has possible "IndexOutOfBound" occurrences, but no groups larger than 2, and also returns true.
    The third shelf has both "IndexOutOfBound" occurrences and groups larger than 2, yet still returns true.
    The fourth shelf has possible groups larger than 2, repeated less than 6 times, and returns false.
 */