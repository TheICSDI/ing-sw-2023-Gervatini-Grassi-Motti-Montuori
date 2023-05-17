/** Tests for class CC_08.java.
 * @author Andrea Grassi.
 */
package it.polimi.ingsw.model.CommonCards;

import it.polimi.ingsw.model.Cards.CCStrategy;
import it.polimi.ingsw.model.Cards.CC_08;
import org.junit.jupiter.api.Test;


class CC_08Test {

    @Test
    void isCompleted() {
        CCStrategy CC8=new CC_08();
        CCGeneral.check(CC8,"JSON/CC/CC08_test.json");
    }
}