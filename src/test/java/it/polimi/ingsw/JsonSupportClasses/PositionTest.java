package it.polimi.ingsw.JsonSupportClasses;

import it.polimi.ingsw.shared.JsonSupportClasses.Position;
import it.polimi.ingsw.shared.Cards.Card;
import it.polimi.ingsw.shared.Cards.CardColor;
import junit.framework.TestCase;
import org.junit.Test;

import static it.polimi.ingsw.shared.Cards.CardColor.BLUE;
import static it.polimi.ingsw.shared.Cards.CardColor.EMPTY;

public class PositionTest extends TestCase {

    private Position test;

    @Test
    public void testGetNeighbors() {
        System.out.println("START TEST \n");


        test = new Position(3, 2);
        Position[] result = test.getNeighbors();
        assertEquals(4, result.length);
        assertEquals(2, result[0].getX());
        assertEquals(3, result[1].getX());
        assertEquals(4, result[2].getX());
        assertEquals(3, result[3].getX());

        assertEquals(2, result[0].getY());
        assertEquals(1, result[1].getY());
        assertEquals(2, result[2].getY());
        assertEquals(3, result[3].getY());

        test = new Position(0, 0);
        result = test.getNeighbors();
        assertEquals(2, result.length);
        assertEquals(1, result[0].getX());
        assertEquals(0, result[1].getX());

        assertEquals(0, result[0].getY());
        assertEquals(1, result[1].getY());

        test = new Position(8, 0);
        result = test.getNeighbors();
        assertEquals(2, result.length);
        assertEquals(7, result[0].getX());
        assertEquals(8, result[1].getX());

        assertEquals(0, result[0].getY());
        assertEquals(1, result[1].getY());

        test = new Position(0, 8);
        result = test.getNeighbors();
        assertEquals(2, result.length);
        assertEquals(0, result[0].getX());
        assertEquals(1, result[1].getX());

        assertEquals(7, result[0].getY());
        assertEquals(8, result[1].getY());

        test = new Position(8, 8);
        result = test.getNeighbors();
        assertEquals(2, result.length);
        assertEquals(7, result[0].getX());
        assertEquals(8, result[1].getX());

        assertEquals(8, result[0].getY());
        assertEquals(7, result[1].getY());


        System.out.println("\nEND TEST\n");
    }
    @Test
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
        board[8][3] = new Card(EMPTY);
        for(int y=8;y>=0;y--){
            System.out.println(board[0][y].getColor().toString()+"\t"+board[1][y].getColor().toString()+"\t"+board[2][y].getColor().toString()+"\t"+board[3][y].getColor().toString()+"\t"+board[4][y].getColor().toString()+"\t"+board[5][y].getColor().toString()+"\t"+board[6][y].getColor().toString()+"\t"+board[7][y].getColor().toString()+"\t"+board[8][y].getColor().toString());
        }
        assertFalse(test.pickable(board));

        test = new Position(8,8);
        assertTrue (test.pickable(board));

        test = new Position(1,1);
        assertTrue (test.pickable(board));

        test = new Position(0,3);
        assertTrue (test.pickable(board));

        test = new Position(8,3);
        assertFalse(test.pickable(board));

        System.out.println("\nEND TEST\n");
    }
}