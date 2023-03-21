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
        Cards[0] = new Card(EMPTY, 0);
        Cards[1] = new Card(EMPTY, 1);
        Cards[2] = new Card(EMPTY, 2);

        boolean check = testIObj.allEqual(Cards);
        assert (check == false);

        //test case 2 (all equal but with an empty)
        check = true;
        Cards[0] = new Card(YELLOW, 0);
        Cards[1] = new Card(YELLOW, 1);
        Cards[2] = new Card(EMPTY, 2);
        check = testIObj.allEqual(Cards);
        assert (check == false);

        //test case 3 (all equal)
        Cards[0] = new Card(YELLOW, 0);
        Cards[1] = new Card(YELLOW, 1);
        Cards[2] = new Card(YELLOW, 2);
        check = testIObj.allEqual(Cards);
        assert (check == true);

        //test case 4 (not all equal)
        Cards[0] = new Card(YELLOW, 0);
        Cards[1] = new Card(YELLOW, 1);
        Cards[2] = new Card(GREEN, 2);
        check = testIObj.allEqual(Cards);
        assert (check == false);

        System.out.println("END TEST \n");
    }
}