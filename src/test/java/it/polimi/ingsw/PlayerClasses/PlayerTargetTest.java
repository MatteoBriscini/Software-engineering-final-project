package it.polimi.ingsw.PlayerClasses;

import it.polimi.ingsw.Shared.Cards.Card;
import static it.polimi.ingsw.Shared.Cards.CardColor.*;

import it.polimi.ingsw.Server.Exceptions.NoSpaceException;
import it.polimi.ingsw.Server.Model.PlayerClasses.Player;
import it.polimi.ingsw.Server.Model.PlayerClasses.PlayerBoard;
import it.polimi.ingsw.Server.Model.PlayerClasses.PlayerTarget;
import junit.framework.TestCase;

import java.io.FileNotFoundException;

public class PlayerTargetTest extends TestCase {

    PlayerBoard board = new PlayerBoard();
    Card[] cards = new Card[1];
    Card[][] testboard = board.getBoard();

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

        assert(val == 1);

        System.out.println("ok");
        System.out.println("\n");


        System.out.println("Checking second player target point");

        cards[0] = new Card(BLUE);

        for(int i = 0; i < 6; i++){

            board.addCard( 2, cards);

        }

        val = target.checkTarget(board);


        assert(val == 2);

        System.out.println("ok");
        System.out.println("\n");


        System.out.println("Checking third player target point");

        cards[0] = new Card(GREEN);

        for(int i = 0; i < 6; i++){

            board.addCard( 4, cards);

        }

        val = target.checkTarget(board);


        assert(val == 4);

        System.out.println("ok");
        System.out.println("\n");


        System.out.println("Checking player target with wrong card color");

        cards[0] = new Card(GREEN);

        for(int i = 0; i < 6; i++){

            board.addCard( 1, cards);

        }

        val = target.checkTarget(board);

        assert(val == 4);

        System.out.println("ok");
        System.out.println("\n");


        System.out.println("PlayerTarget test end");

    }
}