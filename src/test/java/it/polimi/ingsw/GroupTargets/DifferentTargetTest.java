package it.polimi.ingsw.GroupTargets;

import it.polimi.ingsw.Cards.*;
import it.polimi.ingsw.Cards.CardColor;
import junit.framework.TestCase;

import static it.polimi.ingsw.Cards.CardColor.*;

public class DifferentTargetTest extends TestCase {
    private final DifferentTarget testIObj = new DifferentTarget();
    private final Card[] Cards = new Card[3];
    public void testDifferent() {
        System.out.println("START TEST \n");

        //test case 1 (all empty)
        Cards[0] = new Card(EMPTY, 0);
        Cards[1] = new Card(EMPTY, 1);
        Cards[2] = new Card(EMPTY, 2);
        boolean check = testIObj.different(Cards);
        assert (check == false);

        //test case 2 (all different but with an empty)
        check = true;
        Cards[0] = new Card(WHITE, 0);
        Cards[1] = new Card(YELLOW, 1);
        Cards[2] = new Card(EMPTY, 2);
        check = testIObj.different(Cards);
        assert (check == false);

        //test case 3 (all different)
        Cards[0] = new Card(WHITE, 0);
        Cards[1] = new Card(YELLOW, 1);
        Cards[2] = new Card(GREEN, 2);
        check = testIObj.different(Cards);
        assert (check == true);

        //test case 4 (not all different)
        Cards[0] = new Card(YELLOW, 0);
        Cards[1] = new Card(YELLOW, 1);
        Cards[2] = new Card(GREEN, 2);
        check = testIObj.different(Cards);
        assert (check == false);

        System.out.println("END TEST \n");
    }
}