package it.polimi.ingsw.GroupTargets;

import it.polimi.ingsw.Cards.Card;
import it.polimi.ingsw.PlayerClasses.NoSpaceException;
import it.polimi.ingsw.PlayerClasses.PlayerBoard;
import junit.framework.TestCase;

import java.io.FileNotFoundException;

import static it.polimi.ingsw.Cards.CardColor.*;

public class GT2_7_11Test extends TestCase {

    private GT2_7_11 test;
    private final PlayerBoard playerBoard = new PlayerBoard();

    private Card[] cards = new Card[3];



    public void testCheck() {
        System.out.println("START TEST \n");

        //check initial state (corner pattern)
        test = new GT2_7_11(2);
        boolean val = test.check(playerBoard.getBoard());
        assert (!val);

        val = true;
        //check initial state (diagonAlley pattern)
        test = new GT2_7_11(7);
        val = test.check(playerBoard.getBoard());
        assert (!val);

        //fill the board for false test
        cards[0] = new Card(PINK);
        cards[1] = new Card(BLUE);
        cards[2] = new Card(PINK);
        try {
            playerBoard.addCard(0, cards);
        } catch (NoSpaceException e) {
            System.out.println("TOO MUCH CARDS");
        }
        cards[0] = new Card(PINK);
        cards[1] = new Card(GREEN);
        cards[2] = new Card(GREEN);
        try {
            playerBoard.addCard(0, cards);
        } catch (NoSpaceException e) {
            System.out.println("TOO MUCH CARDS");
        }

        cards[0] = new Card(PINK);
        cards[1] = new Card(LIGHTBLUE);
        cards[2] = new Card(BLUE);
        try {
            playerBoard.addCard(1, cards);
        } catch (NoSpaceException e) {
            System.out.println("TOO MUCH CARDS");
        }
        cards[0] = new Card(GREEN);
        cards[1] = new Card(YELLOW);
        cards[2] = new Card(PINK);
        try {
            playerBoard.addCard(1, cards);
        } catch (NoSpaceException e) {
            System.out.println("TOO MUCH CARDS");
        }

        System.out.println("END TEST \n");
    }
}