package it.polimi.ingsw.GroupTargets;

import it.polimi.ingsw.Cards.Card;
import junit.framework.TestCase;

import static it.polimi.ingsw.Cards.CardColor.*;

public class NElementCheckTest extends TestCase {
    private final int min = 2;
    private final int max = 3;
    private final NElementCheck testIObj = new NElementCheck();
    private final Card[] Cards = new Card[5];
    public void testNColorsCheck() {
        System.out.println("START TEST \n");

        //test case 1 (all empty)
        Cards[0] = new Card(EMPTY, 0);
        Cards[1] = new Card(EMPTY, 1);
        Cards[2] = new Card(EMPTY, 2);
        Cards[3] = new Card(EMPTY, 1);
        Cards[4] = new Card(EMPTY, 2);
        boolean check = testIObj.nColorsCheck(Cards, min, max);
        assert (check == false);

        //test case 2 (possible true but with an empty)
        check = true;
        Cards[0] = new Card(YELLOW, 0);
        Cards[1] = new Card(YELLOW, 1);
        Cards[2] = new Card(EMPTY, 2);
        Cards[3] = new Card(YELLOW, 1);
        Cards[4] = new Card(GREEN, 2);
        check = testIObj.nColorsCheck(Cards, min, max);
        assert (check == false);

        //test case 3 (valid combination for true)
        Cards[0] = new Card(YELLOW, 0);
        Cards[1] = new Card(YELLOW, 1);
        Cards[2] = new Card(GREEN, 2);
        Cards[3] = new Card(YELLOW, 1);
        Cards[4] = new Card(GREEN, 2);
        check = testIObj.nColorsCheck(Cards, min, max);
        assert (check == true);

        //test case 4 (valid combination for true)
        check = false;
        Cards[0] = new Card(YELLOW, 0);
        Cards[1] = new Card(WHITE, 1);
        Cards[2] = new Card(GREEN, 2);
        Cards[3] = new Card(YELLOW, 1);
        Cards[4] = new Card(GREEN, 2);
        check = testIObj.nColorsCheck(Cards, min, max);
        assert (check == true);

        //test case 5 (too much different color)
        Cards[0] = new Card(PINK, 0);
        Cards[1] = new Card(WHITE, 1);
        Cards[2] = new Card(GREEN, 2);
        Cards[3] = new Card(YELLOW, 1);
        Cards[4] = new Card(GREEN, 2);
        check = testIObj.nColorsCheck(Cards, min, max);
        assert (check == false);

        //test case 5 (too less different color)
        check = true;
        Cards[0] = new Card(YELLOW, 0);
        Cards[1] = new Card(YELLOW, 1);
        Cards[2] = new Card(YELLOW, 2);
        Cards[3] = new Card(YELLOW, 1);
        Cards[4] = new Card(YELLOW, 2);
        check = testIObj.nColorsCheck(Cards, min, max);
        assert (check == false);

        System.out.println("END TEST \n");
    }
}