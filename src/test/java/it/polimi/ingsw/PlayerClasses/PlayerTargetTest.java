package it.polimi.ingsw.PlayerClasses;

import it.polimi.ingsw.Cards.Card;
import static it.polimi.ingsw.Cards.CardColor.*;

import it.polimi.ingsw.Exceptions.NoSpaceException;
import junit.framework.TestCase;

import java.io.FileNotFoundException;

public class PlayerTargetTest extends TestCase {

    PlayerBoard board = new PlayerBoard();
    Card[] cards = new Card[1];
    Card[][] testboard = board.getBoard();

    public void testCheckTarget() throws FileNotFoundException, NoSpaceException {

        int val;

        Player playerT = new Player("TEST");
        PlayerTarget target;
        playerT.setBoard(board);

        cards[0] = new Card(PINK);

        for(int i = 0; i < 6; i++){

            board.addCard( 0, cards);

        }


        /*

        cards = new Card(PINK);
        testboard[0][5] = cards;

        cards = new Card(BLUE);
        testboard[2][5] = cards;

        cards = new Card(GREEN);
        testboard[4][4] = cards;

        cards = new Card(WHITE);
        testboard[3][3] = cards;

        cards = new Card(YELLOW);
        testboard[1][2] = cards;

        cards = new Card(LIGHTBLUE);
        testboard[2][0] = cards;

         */


        playerT.setPlayerTarget(0);

        target = playerT.getPersonalTarget();

        val = target.checkTarget(board);

        testboard = board.getBoard();

        for (int i = 0; i < 5; i++){
            for (int j = 0; j < 6; j++){
                System.out.println(testboard[i][j].getColor());
            }
            System.out.println("\n");
        }

        System.out.println(val);

        cards[0] = new Card(BLUE);

        for(int i = 0; i < 6; i++){

            board.addCard( 2, cards);

        }

        val = target.checkTarget(board);

        testboard = board.getBoard();

        for (int i = 0; i < 5; i++){
            for (int j = 0; j < 6; j++){
                System.out.println(testboard[i][j].getColor());
            }
            System.out.println("\n");
        }

        System.out.println(val);


        cards[0] = new Card(GREEN);

        for(int i = 0; i < 6; i++){

            board.addCard( 4, cards);

        }

        val = target.checkTarget(board);

        testboard = board.getBoard();

        for (int i = 0; i < 5; i++){
            for (int j = 0; j < 6; j++){
                System.out.println(testboard[i][j].getColor());
            }
            System.out.println("\n");
        }

        System.out.println(val);

    }
}