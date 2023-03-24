package it.polimi.ingsw.GroupTargets;

import it.polimi.ingsw.Cards.Card;
import junit.framework.TestCase;

import static it.polimi.ingsw.Cards.CardColor.*;

public class EqualTargetTest extends TestCase {
    private final EqualTarget testIObj = new EqualTarget();
    private final Card[] Cards = new Card[3];
    public void testAllEqual() {

        System.out.println("START TEST \n");

        //test case 1 (all empty)
        Cards[0] = new Card(EMPTY);
        Cards[1] = new Card(EMPTY);
        Cards[2] = new Card(EMPTY);

        boolean check = testIObj.allEqual(Cards);
        assert (!check);

        //test case 2 (all equal but with an empty)

        Cards[0] = new Card(YELLOW);
        Cards[1] = new Card(YELLOW);
        Cards[2] = new Card(EMPTY);
        check = testIObj.allEqual(Cards);
        assert (!check);

        //test case 3 (all equal)
        Cards[0] = new Card(YELLOW);
        Cards[1] = new Card(YELLOW);
        Cards[2] = new Card(YELLOW);
        check = testIObj.allEqual(Cards);
        assert (check);

        //test case 4 (not all equal)
        Cards[0] = new Card(YELLOW);
        Cards[1] = new Card(YELLOW);
        Cards[2] = new Card(GREEN);
        check = testIObj.allEqual(Cards);
        assert (!check);

        System.out.println("END TEST \n");
    }
}