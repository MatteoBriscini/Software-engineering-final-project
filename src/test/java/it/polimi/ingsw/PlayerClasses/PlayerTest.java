package it.polimi.ingsw.PlayerClasses;

import it.polimi.ingsw.Cards.Card;
import junit.framework.TestCase;

import static it.polimi.ingsw.Cards.CardColor.*;

public class PlayerTest extends TestCase {
    private Player player = new Player("player");
    private Card[] tmp = new Card[3];

    private PlayerBoard playerBoard;
    Card[][] board= new Card[6][5];

    public PlayerBoard getPlayerBoard() {
        return playerBoard;
    }

    //testing addCard
    public void testAddCard(){
        System.out.println("START TEST ADDCARD \n");

        tmp[0]= new Card(BLUE);
        tmp[1]= new Card(WHITE);
        tmp[2] =
        try {
            player.addCard(0, tmp);
        } catch (NoSpaceException e) {
            System.out.println("too much cards0");
        }


        System.out.println("end test addCard");
    }

    public void testCheckSpots() {
        System.out.println("START TEST \n");
        int i,j;

        //test case 1 (check in initial case if sum = 0 )
        player.checkSpots();
        int sum;
        sum = player.getPointSum();
        assert (sum == 0);


        // building library for test

        tmp[0]= new Card(BLUE);
        tmp[1]= new Card(WHITE);
        try {
            player.addCard(0, tmp);
        } catch (NoSpaceException e) {
            System.out.println("too much cards0");
        }

        tmp[0]= new Card(LIGHTBLUE);
        tmp[1]= new Card(PINK);
        try {
            player.addCard(0, tmp);
        } catch (NoSpaceException e) {
            System.out.println("too much cards1");
        }


        tmp[0]= new Card(WHITE);
        tmp[1]= new Card(BLUE);
        try {
            player.addCard(0, tmp);
        } catch (NoSpaceException e) {
            System.out.println("too much cards2");
        }


        tmp[0]= new Card(BLUE);
        tmp[1]= new Card(WHITE);
        try {
            player.addCard(1, tmp);
        } catch (NoSpaceException e) {
            System.out.println("too much cards3");
        }

        tmp[0]= new Card(LIGHTBLUE);
        tmp[1]= new Card(PINK);
        try {
            player.addCard(1, tmp);
        } catch (NoSpaceException e) {
            System.out.println("too much cards4");
        }

        tmp[0]= new Card(WHITE);
        tmp[1]= new Card(BLUE);
        try {
            player.addCard(1, tmp);
        } catch (NoSpaceException e) {
            System.out.println("too much cards5");
        }

        tmp[0]= new Card(GREEN);
        tmp[1]= new Card(PINK);
        try {
            player.addCard(2, tmp);
        } catch (NoSpaceException e) {
            System.out.println("too much cards6");
        }

        tmp[0]= new Card(BLUE);
        tmp[1]= new Card(YELLOW);
        try {
            player.addCard(2, tmp);
        } catch (NoSpaceException e) {
            System.out.println("too much cards7");
        }

        tmp[0]= new Card(LIGHTBLUE);
        tmp[1]= new Card(GREEN);
        try {
            player.addCard(2, tmp);
        } catch (NoSpaceException e) {
            System.out.println("too much cards8");
        }

        tmp[0]= new Card(GREEN);
        tmp[1]= new Card(PINK);
        try {
            player.addCard(3, tmp);
        } catch (NoSpaceException e) {
            System.out.println("too much cards9");
        }

        tmp[0]= new Card(BLUE);
        tmp[1]= new Card(YELLOW);
        try {
            player.addCard(3, tmp);
        } catch (NoSpaceException e) {
            System.out.println("too much cards9");
        }

        tmp[0]= new Card(LIGHTBLUE);
        tmp[1]= new Card(GREEN);
        try {
            player.addCard(3, tmp);
        } catch (NoSpaceException e) {
            System.out.println("too much cards9");
        }

        tmp[0]= new Card(YELLOW);
        tmp[1]= new Card(YELLOW);
        try {
            player.addCard(4, tmp);
        } catch (NoSpaceException e) {
            System.out.println("too much cards9");
        }

        tmp[0]= new Card(GREEN);
        tmp[1]= new Card(GREEN);
        try {
            player.addCard(4, tmp);
        } catch (NoSpaceException e) {
            System.out.println("too much cards9");
        }

        tmp[0]= new Card(PINK);
        tmp[1]= new Card(YELLOW);
        try {
            player.addCard(4, tmp);
        } catch (NoSpaceException e) {
            System.out.println("too much cards9");
        }

        player.checkSpots();
        assert (player.getPointSum() == 0);


        System.out.println("end test");
    }

}
