package it.polimi.ingsw.PlayerClasses;

import junit.framework.TestCase;

public class PlayerTest extends TestCase {
    private Player player = new Player("player");

    public void testCheckSpots() {
        System.out.println("start test");
        player.checkSpots();
        int sum;
        sum = player.getPointSum();
        System.out.println(sum);
        assert (sum == 0);
        System.out.println("end test");
    }

}