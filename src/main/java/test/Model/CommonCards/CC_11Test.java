/** Tests for class CC_11.java.
 * @author Andrea Grassi.
 */
package test.Model.CommonCards;

import it.polimi.ingsw.model.Cards.CCStrategy;
import it.polimi.ingsw.model.Cards.CC_11;
import org.junit.jupiter.api.Test;


class CC_11Test {
    @Test
    void isCompleted() {
        CCStrategy CC11=new CC_11();
        CCGeneral.check(CC11,"JSON/CC/CC11_test.json");
    }
}