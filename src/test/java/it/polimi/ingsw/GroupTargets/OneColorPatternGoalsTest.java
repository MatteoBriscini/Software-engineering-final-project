package it.polimi.ingsw.GroupTargets;

import it.polimi.ingsw.Cards.Card;
import it.polimi.ingsw.Cards.CardColor;
import it.polimi.ingsw.PlayerClasses.NoSpaceException;
import it.polimi.ingsw.PlayerClasses.PlayerBoard;
import junit.framework.TestCase;

import static it.polimi.ingsw.Cards.CardColor.*;

public class OneColorPatternGoalsTest extends TestCase {

    /**
     * parameters
     */
    private OneColorPatternGoals test;
    private PlayerBoard playerBoard = new PlayerBoard();

    private Card[] cards = new Card[3];

    /**
     * array with the color sequence for create the actual test
     */
    private CardColor[] testCase1 = {PINK, BLUE, PINK, PINK, GREEN, GREEN, PINK, LIGHTBLUE, BLUE, GREEN, YELLOW, PINK, PINK, PINK, LIGHTBLUE, BLUE, PINK, PINK, PINK, GREEN, YELLOW, LIGHTBLUE, BLUE, PINK, PINK, YELLOW, PINK, PINK, LIGHTBLUE, PINK};
    private CardColor[] testCase2 = {BLUE, YELLOW, PINK, YELLOW, LIGHTBLUE, BLUE, BLUE, BLUE, BLUE, PINK, BLUE, LIGHTBLUE, BLUE, BLUE, BLUE, BLUE, PINK, LIGHTBLUE, BLUE, BLUE, BLUE, GREEN, LIGHTBLUE, GREEN, BLUE, BLUE, YELLOW, YELLOW, LIGHTBLUE, BLUE};

    /**
     * this method add all card in the player board to create a test case, at the and he print the player board
     * @param testCase is the array with the sequence of card color for the specific test case
     */
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

    public void testCheck() {
        System.out.println("START TEST \n");
        Card[][] board= new Card[6][5];

        //check initial state (corner pattern)
        test = new OneColorPatternGoals(2);
        boolean val = test.check(playerBoard.getBoard());
        assert (!val);

        val = true;
        //check initial state (diagonAlley pattern)
        test = new OneColorPatternGoals(7);
        val = test.check(playerBoard.getBoard());
        assert (!val);

        //test case 1
        createAndPrintTable(testCase1);
        val = test.check(playerBoard.getBoard());         //check 1 test case (diagonAlley pattern)
        assert (!val);
        test = new OneColorPatternGoals(2);
        val = test.check(playerBoard.getBoard());        //check 1 test case (corner pattern)
        assert (!val);

        playerBoard = new PlayerBoard();
        //test case 2
        createAndPrintTable(testCase2);

        //check 2 test case (corner pattern)
        val = test.check(playerBoard.getBoard());
        assert (val);

        //check 2 test case (diagonAlley pattern)
        test = new OneColorPatternGoals(7);
        val = test.check(playerBoard.getBoard());
        assert (val);


        System.out.println("END TEST WITH NO ERROR \n");
    }
}