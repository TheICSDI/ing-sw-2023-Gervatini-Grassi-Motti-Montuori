/** Tests for class CC_08.java.
 * @author Andrea Grassi.
 */
package test.CommonCards;

import main.java.it.polimi.ingsw.model.Cards.CCStrategy;
import main.java.it.polimi.ingsw.model.Cards.CC_08;
import main.java.it.polimi.ingsw.model.Cards.CC_10;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CC_08Test {

    @Test
    void isCompleted() {
        CCStrategy CC8=new CC_08();
        CCGeneral.check(CC8,"JSON/CC/CC08_test.json",1);
    }
}