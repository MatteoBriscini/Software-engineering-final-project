package it.polimi.ingsw.GroupTargets;

import it.polimi.ingsw.shared.Cards.Card;
import it.polimi.ingsw.shared.Cards.CardColor;
import it.polimi.ingsw.server.Exceptions.ConstructorException;
import it.polimi.ingsw.server.Exceptions.NoSpaceException;
import it.polimi.ingsw.server.Model.PlayerClasses.PlayerBoard;
import it.polimi.ingsw.server.Model.GroupGoals.CouplesAndPokersGoals;
import junit.framework.TestCase;
import org.junit.Test;

import static it.polimi.ingsw.shared.Cards.CardColor.*;

public class CouplesAndPokersGoalsTest extends TestCase {

    /**
     * parameters
     */
    private CouplesAndPokersGoals test;
    private PlayerBoard playerBoard;
    private Card[] cards = new Card[3];

    /**
     * array with the color sequence for create the actual test
     */
    private CardColor[] testCase2 = {YELLOW, WHITE, PINK, PINK, WHITE, BLUE, YELLOW, YELLOW, WHITE, WHITE, BLUE, BLUE, YELLOW, WHITE, PINK, PINK, WHITE, BLUE, WHITE, WHITE, PINK, WHITE, GREEN, WHITE, LIGHTBLUE, LIGHTBLUE, WHITE, WHITE, GREEN, GREEN};
    private CardColor[] testCase3 = {GREEN, GREEN, GREEN, GREEN, WHITE, WHITE, WHITE, GREEN, LIGHTBLUE, LIGHTBLUE, LIGHTBLUE, LIGHTBLUE, WHITE, WHITE, LIGHTBLUE, LIGHTBLUE, WHITE, WHITE, PINK, WHITE, LIGHTBLUE, WHITE, BLUE, BLUE,PINK, PINK, PINK, WHITE, BLUE, BLUE};

    /**
     * this method add all card in the player board to create a test case, at the and he print the player board
     * @param testCase is the array with the sequence of card color for the specific test case
     */
    @Test
    private void createAndPrintTable (CardColor[] testCase){
        Card[][] board= new Card[6][5];
        int i, j, k = 0;
        for (j=0; j< 10; j++){
            for(i= 0; i <cards.length; i++){
                cards[i] = new Card(testCase[k]);
                k++;
            }
            try {
                playerBoard.addCard(j/2, cards);
            } catch (NoSpaceException e) {
                System.out.println("TOO MUCH CARDS");
            }
        }

        board=playerBoard.getBoard(); //print the board
        for(int y=5;y>=0;y--){
            System.out.println(board[0][y].getColor().toString()+"\t"+board[1][y].getColor().toString()+"\t"+board[2][y].getColor().toString()+"\t"+board[3][y].getColor().toString()+"\t"+board[4][y].getColor().toString()+"\t");
        }
        System.out.println("\n");
    }

    /**
     * actual test case
     * @throws ConstructorException exception threw from the OneColorPatternGoals constructor
     */
    @Test
    public void testCheck() throws ConstructorException {
        int i, j, k = 0;
        Card[][] board= new Card[6][5];
        System.out.println("START TEST \n");

        playerBoard = new PlayerBoard();


        //check initial state (create a new board and verify if si all empty, al goal have to return false)
        for (Card[] array : playerBoard.getBoard()){
                    for(Card c: array){
                        assertEquals (EMPTY, c.getColor());
                    }
        }

        test = new CouplesAndPokersGoals(2,6);                      //constructor for couple goal
        boolean val = test.check(playerBoard.getBoard());
        assertFalse(val);

        test = new CouplesAndPokersGoals(4,4);                      //constructor for poker goal
        val = test.check(playerBoard.getBoard());
        assertFalse(val);


        //test case 1 (board full blue, have to return false)
        for (j=0; j< 10; j++){
            for(i= 0; i <cards.length; i++){
                cards[i] = new Card(BLUE);
            }
            try {
                playerBoard.addCard(j/2, cards);
            } catch (NoSpaceException e) {
                System.out.println("TOO MUCH CARDS");
            }
        }

        board=playerBoard.getBoard(); //print the board
        for(int y=5;y>=0;y--){
            System.out.println(board[0][y].getColor().toString()+"\t"+board[1][y].getColor().toString()+"\t"+board[2][y].getColor().toString()+"\t"+board[3][y].getColor().toString()+"\t"+board[4][y].getColor().toString()+"\t");
        }
        System.out.println("\n");

        test = new CouplesAndPokersGoals(4,4);
        val = test.check(playerBoard.getBoard());
        assertFalse(val);
        test = new CouplesAndPokersGoals(2,6);
        val = test.check(playerBoard.getBoard());
        assertFalse(val);

        //test case 2 (couple goal have to return true, poker have to return false)
        playerBoard = new PlayerBoard();
        createAndPrintTable(testCase2);

        test = new CouplesAndPokersGoals(4,4);
        val = test.check(playerBoard.getBoard());
        assertFalse(val);
        test = new CouplesAndPokersGoals(2,6);
        val = test.check(playerBoard.getBoard());
        assertTrue(val);

        //test case 3 (couple goal have to return true, poker have to return true)
        playerBoard = new PlayerBoard();
        createAndPrintTable(testCase3);

        test = new CouplesAndPokersGoals(4,4);
        val = test.check(playerBoard.getBoard());
        assertTrue (val);
        test = new CouplesAndPokersGoals(2,6);
        val = test.check(playerBoard.getBoard());
        assertTrue (val);

        System.out.println("END TEST WITH NO ERROR \n");
    }

}