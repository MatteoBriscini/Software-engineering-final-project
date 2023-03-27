package it.polimi.ingsw;

import junit.framework.TestCase;

public class ControllerTest extends TestCase {

    Controller test;

    public void testAddPlayer() {
    }

    public void testWaitForEndGame() {
        test = new Controller();

        test.setPlayerNum(2);
        System.out.println(test.getCurrentPlayer());
        test.turn();
        System.out.println(test.getCurrentPlayer());
        test.waitForEndGame();
        test.turn();
        System.out.println(test.getCurrentPlayer());
        test.turn();
        System.out.println(test.getCurrentPlayer());
        test.turn();
        System.out.println(test.getCurrentPlayer());
        test.turn();
        System.out.println(test.getCurrentPlayer());

        System.out.println("\nstart test2");

        test = new Controller();
        test.setPlayerNum(2);
        System.out.println(test.getCurrentPlayer());
        test.turn();
        System.out.println(test.getCurrentPlayer());
        test.turn();
        System.out.println(test.getCurrentPlayer());
        test.waitForEndGame();
        test.endGame();
        System.out.println(test.getCurrentPlayer());
        test.turn();
        System.out.println(test.getCurrentPlayer());
    }

    public void testWaitForPlayerResponse() throws InterruptedException {
        test = new Controller();
        test.setPlayerNum(2);
        test.turn();
        System.out.println(test.getCurrentPlayer());
        Thread.sleep(19*1000);                              //aspettare un pochino di piu
        System.out.println(test.getCurrentPlayer());
        System.out.println(test.getCurrentPlayer());
        System.out.println(test.getCurrentPlayer());
    }
}