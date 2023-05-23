package it.polimi.ingsw.PlayerClasses;

import it.polimi.ingsw.shared.Cards.Card;
import it.polimi.ingsw.server.Exceptions.NoSpaceException;
import it.polimi.ingsw.server.Model.PlayerClasses.PlayerBoard;
import junit.framework.TestCase;
import org.junit.Test;

import static it.polimi.ingsw.shared.Cards.CardColor.*;

public class PlayerBoardTest extends TestCase {


    int i, j;
    Card[] cards = new Card[3];
    Card[][] board;
    PlayerBoard playerBoard = new PlayerBoard();
    boolean val = false;
    boolean val2 = false;


    @Test
    public void testPlayerBoard(){

        System.out.println("PlayerBoard test 1 start");

        System.out.println("Checking PlayerBoard constructor method");

        PlayerBoard PB = new PlayerBoard();
        Card[][] board = PB.getBoard();
        for(int x = 0; x < 5; x++){
            for(int y = 0; y < 6; y++){
                assertEquals(EMPTY, board[x][y].getColor());
            }
        }

        System.out.println("PlayerBoard test 1 end");

    }

    @Test
    public void testGetBoard() {

        System.out.println("PlayerBoard test 2 start");

        System.out.println("Checking identity");

        PlayerBoard PB = new PlayerBoard();
        Card[][] board = PB.getBoard();
        Card[][] board2 = PB.getBoard();
        //assertTrue();(board.equals(PB.getBoard()));
        for(int i=0; i<4; i++ ){
            for(int j = 0; j<5; j++){
                board[i][j].getColor().equals(board2[i][j]);
            }
        }

        System.out.println("PlayerBoard test 2 end");

    }

    @Test
    public void testAddCard() {

        System.out.println("PlayerBoard test 3 start");

        cards[0] = new Card(PINK);
        cards[1] = new Card(BLUE);
        cards[2] = new Card(PINK);

        System.out.println("Checking insertion with an array of three up to full column");

        for(i = 0; i < 2; i++){
            try {
                playerBoard.addCard(0, cards);

            } catch (NoSpaceException e) {
                val2 = true;
            }
        }
        board = playerBoard.getBoard();
        for (i = 5; i >=  0; i--){
            assertFalse(board[0][i].getColor().equals(EMPTY));
        }
        assertFalse(val2);
        val2 = false;

        System.out.println("Checking full column exception");

        for(i = 0; i < 3; i++){
            try {
                val = playerBoard.addCard(1, cards);

            } catch (NoSpaceException e) {
                val2 = true;
            }
        }

        assertFalse(val);
        assertTrue(val2);
        val2 = false;

        System.out.println("Checking full PlayerBoard check and return value");

        for(j = 2; j < 5; j++){
            for(i = 0; i < 2; i++){
                try {
                    val = playerBoard.addCard(j, cards);

                } catch (NoSpaceException e) {
                    val2 = true;
                }
            }
        }

        assertTrue(val);
        assertFalse(val2);

        System.out.println("Checking insertion with an array of two");

        Card[] cards2 = new Card[2];
        cards2[0] = new Card(BLUE);
        cards2[1] = new Card(PINK);

        playerBoard = new PlayerBoard();

        for(i = 0; i < 2; i++){
            try {
                val = playerBoard.addCard(0, cards2);

            } catch (NoSpaceException e) {
                val2 = true;
            }
        }
        board = playerBoard.getBoard();

        for(int y=5;y>=0;y--){
            System.out.println(board[0][y].getColor().toString()+"\t"+board[1][y].getColor().toString()+"\t"+board[2][y].getColor().toString()+"\t"+board[3][y].getColor().toString()+"\t"+board[4][y].getColor().toString()+"\t");
        }
        System.out.println("\n");

        for (i = 5; i >=  4; i--){
            System.out.println(board[0][i].getColor());
            assertTrue(board[0][i].getColor().equals(EMPTY));
        }

        System.out.println("Checking insertion with an array of two up to full column");


        for(i = 0; i < 3; i++){
            try {
                val = playerBoard.addCard(2, cards2);

            } catch (NoSpaceException e) {
                val2 = true;
            }
        }
        board = playerBoard.getBoard();
        for (i = 5; i >=  0; i--){
            assertFalse(board[2][i].getColor().equals(EMPTY));
        }
        assertFalse(val2);


        System.out.println("Checking insertion with an array of one");

        Card[] cards3 = new Card[1];
        cards3[0] = new Card(BLUE);

        for(i = 0; i < 3; i++){
            try {
                val = playerBoard.addCard(1, cards3);

            } catch (NoSpaceException e) {
                val2 = true;
            }
        }
        board = playerBoard.getBoard();
        for (i = 5; i >=  3; i--){
            assertEquals(EMPTY, board[1][i].getColor());
        }


        System.out.println("Checking insertion with an array of one up to full column");


        for(i = 0; i < 3; i++){
            try {
                val = playerBoard.addCard(1, cards3);

            } catch (NoSpaceException e) {
                val2 = true;
            }
        }
        board = playerBoard.getBoard();
        for (i = 5; i >=  0; i--){
            assertFalse(board[1][i].getColor().equals(EMPTY));
        }

        System.out.println("PlayerBoard test 3 end");

    }
}