package it.polimi.ingsw.SupportClasses;

import it.polimi.ingsw.Cards.Card;
import it.polimi.ingsw.Cards.CardColor;
import it.polimi.ingsw.JsonSupportClasses.Position;
import junit.framework.TestCase;

import static it.polimi.ingsw.Cards.CardColor.*;

public class NColorsGroupTest extends TestCase {

    private final NColorsGroup testIObj  = new NColorsGroup();
    private final Card[] Cards5 = new Card[5];
    private final Card[] Cards3 = new Card[3];

    public void testNColorsCheckEqual() {

        System.out.println("START EQUAL TEST \n");

        //test case 1 (all empty)
        Cards3[0] = new Card(EMPTY);
        Cards3[1] = new Card(EMPTY);
        Cards3[2] = new Card(EMPTY);

        boolean check = testIObj.nColorsCheck(Cards3,1 ,1);
        assert (!check);

        //test case 2 (all equal but with an empty)

        Cards3[0] = new Card(YELLOW);
        Cards3[1] = new Card(YELLOW);
        Cards3[2] = new Card(EMPTY);
        check = testIObj.nColorsCheck(Cards3,1 ,1);
        assert (!check);

        //test case 3 (all equal)
        Cards3[0] = new Card(YELLOW);
        Cards3[1] = new Card(YELLOW);
        Cards3[2] = new Card(YELLOW);
        check = testIObj.nColorsCheck(Cards3,1 ,1);
        assert (check);

        //test case 4 (not all equal)
        Cards3[0] = new Card(YELLOW);
        Cards3[1] = new Card(YELLOW);
        Cards3[2] = new Card(GREEN);
        check = testIObj.nColorsCheck(Cards3,1 ,1);
        assert (!check);

        System.out.println("END TEST \n");
    }

    public void testNColorsCheckDifferent() {

        System.out.println("START DIFFERENT TEST \n");

        //test case 1 (all empty)
        Cards3[0] = new Card(EMPTY);
        Cards3[1] = new Card(EMPTY);
        Cards3[2] = new Card(EMPTY);
        boolean check = testIObj.nColorsCheck(Cards3, 3,3);
        assert (!check);

        //test case 2 (all different but with an empty)
        check = true;
        Cards3[0] = new Card(WHITE);
        Cards3[1] = new Card(YELLOW);
        Cards3[2] = new Card(EMPTY);
        check = testIObj.nColorsCheck(Cards3, 3,3);
        assert (!check);

        //test case 3 (all different)
        Cards3[0] = new Card(WHITE);
        Cards3[1] = new Card(YELLOW);
        Cards3[2] = new Card(GREEN);
        check = testIObj.nColorsCheck(Cards3, 3,3);
        assert (check);

        //test case 4 (not all different)
        Cards3[0] = new Card(YELLOW);
        Cards3[1] = new Card(YELLOW);
        Cards3[2] = new Card(GREEN);
        check = testIObj.nColorsCheck(Cards3, 3, 3);
        assert (!check);

        System.out.println("END TEST \n");
    }
    public void testNColorsCheck() {
        System.out.println("START TEST \n");

        //test case 1 (all empty)
        Cards5[0] = new Card(EMPTY);
        Cards5[1] = new Card(EMPTY);
        Cards5[2] = new Card(EMPTY);
        Cards5[3] = new Card(EMPTY);
        Cards5[4] = new Card(EMPTY);
        int min = 2;
        int max = 3;
        boolean check = testIObj.nColorsCheck(Cards5, min, max);
        assert (!check);

        //test case 2 (possible true but with an empty)
        check = true;
        Cards5[0] = new Card(YELLOW);
        Cards5[1] = new Card(YELLOW);
        Cards5[2] = new Card(EMPTY);
        Cards5[3] = new Card(YELLOW);
        Cards5[4] = new Card(GREEN);
        check = testIObj.nColorsCheck(Cards5, min, max);
        assert (!check);

        //test case 3 (valid combination for true)
        Cards5[0] = new Card(YELLOW);
        Cards5[1] = new Card(YELLOW);
        Cards5[2] = new Card(GREEN);
        Cards5[3] = new Card(YELLOW);
        Cards5[4] = new Card(GREEN);
        check = testIObj.nColorsCheck(Cards5, min, max);
        assert (check);

        //test case 4 (valid combination for true)
        check = false;
        Cards5[0] = new Card(YELLOW);
        Cards5[1] = new Card(WHITE);
        Cards5[2] = new Card(GREEN);
        Cards5[3] = new Card(YELLOW);
        Cards5[4] = new Card(GREEN);
        check = testIObj.nColorsCheck(Cards5, min, max);
        assert (check);

        //test case 5 (too much different color)
        Cards5[0] = new Card(PINK);
        Cards5[1] = new Card(WHITE);
        Cards5[2] = new Card(GREEN);
        Cards5[3] = new Card(YELLOW);
        Cards5[4] = new Card(GREEN);
        check = testIObj.nColorsCheck(Cards5, min, max);
        assert (!check);

        //test case 5 (too less different color)
        check = true;
        Cards5[0] = new Card(YELLOW);
        Cards5[1] = new Card(YELLOW);
        Cards5[2] = new Card(YELLOW);
        Cards5[3] = new Card(YELLOW);
        Cards5[4] = new Card(YELLOW);
        check = testIObj.nColorsCheck(Cards5, min, max);
        assert (!check);

        System.out.println("END TEST \n");
    }
}