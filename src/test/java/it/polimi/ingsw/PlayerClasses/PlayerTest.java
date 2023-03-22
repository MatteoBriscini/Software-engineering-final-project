package it.polimi.ingsw.PlayerClasses;

import it.polimi.ingsw.Cards.Card;
import junit.framework.TestCase;

import static it.polimi.ingsw.Cards.CardColor.*;

public class PlayerTest extends TestCase {
    private Player player = new Player("player");
    private Card[] tmp = new Card[3];


    public void testCheckSpots() {
        System.out.println("start test");

        //test case 1 (check in initial case if sum = 0 )
        player.checkSpots();
        int sum;
        sum = player.getPointSum();
        System.out.println(sum);
        assert (sum == 0);

        //test case 2 (check if spots are correctly identified)
        tmp[0]= new Card(BLUE);
        tmp[1]= new Card(BLUE);
        tmp[2]=new Card(BLUE);
        System.out.println("end test");
    }

}