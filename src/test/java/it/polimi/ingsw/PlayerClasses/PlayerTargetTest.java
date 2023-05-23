package it.polimi.ingsw.PlayerClasses;

import it.polimi.ingsw.shared.Cards.Card;
import static it.polimi.ingsw.shared.Cards.CardColor.*;

import it.polimi.ingsw.server.Exceptions.NoSpaceException;
import it.polimi.ingsw.server.Model.PlayerClasses.Player;
import it.polimi.ingsw.server.Model.PlayerClasses.PlayerBoard;
import it.polimi.ingsw.server.Model.PlayerClasses.PlayerTarget;
import junit.framework.TestCase;
import org.junit.Test;

import java.io.FileNotFoundException;

public class PlayerTargetTest extends TestCase {

    PlayerBoard board = new PlayerBoard();
    Card[] cards = new Card[1];
    Card[][] testboard = board.getBoard();

    @Test
    public void testCheckTarget() throws FileNotFoundException, NoSpaceException {

        System.out.println("PlayerTarget test start");

        System.out.println("\n");

        int val;

        Player playerT = new Player("TEST");
        PlayerTarget target;
       // playerT.setBoard(board);


        System.out.println("Checking first player target point");

        cards[0] = new Card(PINK);

        for(int i = 0; i < 6; i++){

            board.addCard( 0, cards);

        }

        playerT.setPlayerTarget(0);

        target = playerT.getPersonalTarget();


        val = target.checkTarget(board);

        assertEquals(1, val);

        System.out.println("ok");
        System.out.println("\n");


        System.out.println("Checking second player target point");

        cards[0] = new Card(BLUE);

        for(int i = 0; i < 6; i++){

            board.addCard( 2, cards);

        }

        val = target.checkTarget(board);


        assertEquals(2, val);

        System.out.println("ok");
        System.out.println("\n");


        System.out.println("Checking third player target point");

        cards[0] = new Card(GREEN);

        for(int i = 0; i < 6; i++){

            board.addCard( 4, cards);

        }

        val = target.checkTarget(board);


        assertEquals(4, val);

        System.out.println("ok");
        System.out.println("\n");


        System.out.println("Checking player target with wrong card color");

        cards[0] = new Card(GREEN);

        for(int i = 0; i < 6; i++){

            board.addCard( 1, cards);

        }

        val = target.checkTarget(board);

        assertEquals(4, val);

        System.out.println("ok");
        System.out.println("\n");


        System.out.println("PlayerTarget test end");

    }
}