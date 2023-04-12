package it.polimi.ingsw.GroupTargets;

import it.polimi.ingsw.Shared.Cards.Card;
import it.polimi.ingsw.Shared.Cards.CardColor;
import it.polimi.ingsw.Server.Exceptions.ConstructorException;
import it.polimi.ingsw.Server.Exceptions.NoSpaceException;
import it.polimi.ingsw.Server.Model.PlayerClasses.PlayerBoard;
import it.polimi.ingsw.Server.Model.GroupGoals.OneColorPatternGoals;
import junit.framework.TestCase;

import static it.polimi.ingsw.Shared.Cards.CardColor.*;

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

    /**
     * actual test case
     * @throws ConstructorException exception threw from the OneColorPatternGoals constructor
     */
    public void testCheck() throws ConstructorException {
        System.out.println("START TEST \n");
        boolean val;
        Card[][] board= new Card[6][5];

        //check initial state (corner pattern)
        test = new OneColorPatternGoals(2);
        val = test.check(playerBoard.getBoard());
        assert (!val);

        //check initial state (cross pattern)
        test = new OneColorPatternGoals(11);
        val = test.check(playerBoard.getBoard());
        assert (!val);

        //check initial state (diagonAlley pattern)
        test = new OneColorPatternGoals(7);
        val = test.check(playerBoard.getBoard());
        assert (!val);

        //test case 1
        createAndPrintTable(testCase1);
        val = test.check(playerBoard.getBoard());         //check 1 test case (diagonAlley pattern)
        assert (!val);
        test = new OneColorPatternGoals(11);          //check 1 test case (cross pattern)
        val = test.check(playerBoard.getBoard());
        assert (!val);
        test = new OneColorPatternGoals(2);
        val = test.check(playerBoard.getBoard());        //check 1 test case (corner pattern)
        assert (!val);

        playerBoard = new PlayerBoard();
        //test case 2
        createAndPrintTable(testCase2);


        val = test.check(playerBoard.getBoard());       //check 2 test case (corner pattern)
        assert (val);
        test = new OneColorPatternGoals(11);          //check 1 test case (cross pattern)
        val = test.check(playerBoard.getBoard());
        assert (val);
        test = new OneColorPatternGoals(7);          //check 2 test case (diagonAlley pattern)
        val = test.check(playerBoard.getBoard());
        assert (val);


        System.out.println("END TEST WITH NO ERROR \n");
    }
}