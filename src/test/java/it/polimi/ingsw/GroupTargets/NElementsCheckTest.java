package it.polimi.ingsw.GroupTargets;

import it.polimi.ingsw.Cards.Card;
import junit.framework.TestCase;

import static it.polimi.ingsw.Cards.CardColor.*;

public class NElementsCheckTest extends TestCase {
    /**
     * parameters
     */
    private final NElementsCheck testIObj = new NElementsCheck();
    private final Card[] Cards = new Card[5];

    /**
     * actual test
     */
    public void testNColorsCheck() {
        System.out.println("START TEST \n");

        //test case 1 (all empty)
        Cards[0] = new Card(EMPTY);
        Cards[1] = new Card(EMPTY);
        Cards[2] = new Card(EMPTY);
        Cards[3] = new Card(EMPTY);
        Cards[4] = new Card(EMPTY);
        int min = 2;
        int max = 3;
        boolean check = testIObj.nColorsCheck(Cards, min, max);
        assert (check == false);

        //test case 2 (possible true but with an empty)
        check = true;
        Cards[0] = new Card(YELLOW);
        Cards[1] = new Card(YELLOW);
        Cards[2] = new Card(EMPTY);
        Cards[3] = new Card(YELLOW);
        Cards[4] = new Card(GREEN);
        check = testIObj.nColorsCheck(Cards, min, max);
        assert (check == false);

        //test case 3 (valid combination for true)
        Cards[0] = new Card(YELLOW);
        Cards[1] = new Card(YELLOW);
        Cards[2] = new Card(GREEN);
        Cards[3] = new Card(YELLOW);
        Cards[4] = new Card(GREEN);
        check = testIObj.nColorsCheck(Cards, min, max);
        assert (check == true);

        //test case 4 (valid combination for true)
        check = false;
        Cards[0] = new Card(YELLOW);
        Cards[1] = new Card(WHITE);
        Cards[2] = new Card(GREEN);
        Cards[3] = new Card(YELLOW);
        Cards[4] = new Card(GREEN);
        check = testIObj.nColorsCheck(Cards, min, max);
        assert (check == true);

        //test case 5 (too much different color)
        Cards[0] = new Card(PINK);
        Cards[1] = new Card(WHITE);
        Cards[2] = new Card(GREEN);
        Cards[3] = new Card(YELLOW);
        Cards[4] = new Card(GREEN);
        check = testIObj.nColorsCheck(Cards, min, max);
        assert (check == false);

        //test case 5 (too less different color)
        check = true;
        Cards[0] = new Card(YELLOW);
        Cards[1] = new Card(YELLOW);
        Cards[2] = new Card(YELLOW);
        Cards[3] = new Card(YELLOW);
        Cards[4] = new Card(YELLOW);
        check = testIObj.nColorsCheck(Cards, min, max);
        assert (check == false);

        System.out.println("END TEST \n");
    }
}