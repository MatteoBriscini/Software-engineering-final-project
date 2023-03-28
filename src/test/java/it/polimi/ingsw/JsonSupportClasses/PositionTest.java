package it.polimi.ingsw.JsonSupportClasses;

import it.polimi.ingsw.Cards.Card;
import it.polimi.ingsw.Cards.CardColor;
import junit.framework.TestCase;

import java.util.Arrays;

import static it.polimi.ingsw.Cards.CardColor.BLUE;
import static it.polimi.ingsw.Cards.CardColor.EMPTY;

public class PositionTest extends TestCase {

    private Position test;

    public void testGetNeighbors() {
        System.out.println("START TEST \n");


        test = new Position(3, 2);
        Position[] result = test.getNeighbors();
        assert (result.length == 4);
        assert (result[0].getX() == 2);
        assert (result[1].getX() == 3);
        assert (result[2].getX() == 4);
        assert (result[3].getX() == 3);

        assert (result[0].getY() == 2);
        assert (result[1].getY() == 1);
        assert (result[2].getY() == 2);
        assert (result[3].getY() == 3);

        test = new Position(0, 0);
        result = test.getNeighbors();
        assert (result.length == 2);
        assert (result[0].getX() == 1);
        assert (result[1].getX() == 0);

        assert (result[0].getY() == 0);
        assert (result[1].getY() == 1);

        test = new Position(4, 0);
        result = test.getNeighbors();
        assert (result.length == 2);
        assert (result[0].getX() == 3);
        assert (result[1].getX() == 4);

        assert (result[0].getY() == 0);
        assert (result[1].getY() == 1);

        test = new Position(0, 5);
        result = test.getNeighbors();
        assert (result.length == 2);
        assert (result[0].getX() == 0);
        assert (result[1].getX() == 1);

        assert (result[0].getY() == 4);
        assert (result[1].getY() == 5);

        test = new Position(4, 5);
        result = test.getNeighbors();
        assert (result.length == 2);
        assert (result[0].getX() == 3);
        assert (result[1].getX() == 4);

        assert (result[0].getY() == 5);
        assert (result[1].getY() == 4);


        System.out.println("\nEND TEST\n");
    }
    public void testPickable() {
        System.out.println("START TEST \n");

        test = new Position(0,0);
        int k = 0;
        CardColor[] testCase = {EMPTY, EMPTY, EMPTY, BLUE, BLUE, BLUE, BLUE, BLUE, BLUE};
        Card[][] board = new Card[9][9];
        for(int i=0; i<board.length; i++) {
            for(int j=0; j<board.length; j++) {
                board[i][j] = new Card(testCase[k]);
                k++;
                if(k>=9)k=4; //the rest of the matrix is full blue
            }
        }
        assert (!test.pickable(board));

        test = new Position(8,8);
        assert (!test.pickable(board));

        test = new Position(1,1);
        assert (test.pickable(board));

        test = new Position(0,3);
        assert (test.pickable(board));

        System.out.println("\nEND TEST\n");
    }
}