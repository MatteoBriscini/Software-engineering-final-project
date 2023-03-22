package it.polimi.ingsw.PlayerClasses;

import it.polimi.ingsw.Cards.Card;
import junit.framework.TestCase;

import static it.polimi.ingsw.Cards.CardColor.*;

public class PlayerBoardTest extends TestCase {

    public void testPlayerBoard(){

        System.out.println("Test 1 start");

        PlayerBoard PB = new PlayerBoard();
        Card[][] board = PB.getBoard();
        for(int x = 0; x < 5; x++){
            for(int y = 0; y < 6; y++){
                assert(board[x][y].getColor().equals(EMPTY));
            }
        }

        System.out.println("Test 1 end");

    }

    public void testGetBoard() {

        System.out.println("Test 2 start");

        PlayerBoard PB = new PlayerBoard();
        Card[][] board = PB.getBoard();
        assert(board.equals(PB.getBoard()));

        System.out.println("Test 2 end");

    }

    public void testAddCard() {

        int i, j;
        Card[] cards = new Card[3];
        Card[][] board = new Card[5][6];
        PlayerBoard playerBoard = new PlayerBoard();
        boolean val = false;

        cards[0] = new Card(PINK);
        cards[1] = new Card(BLUE);
        cards[2] = new Card(PINK);
        for(i = 0; i < 2; i++){
            try {
                playerBoard.addCard(0, cards);

            } catch (NoSpaceException e) {
                System.out.println("TOO MANY CARDS");
            }
        }
        board = playerBoard.getBoard();
        for (i = 5; i >=  0; i--){
            System.out.println(board[0][i].getColor());
        }

        for(i = 0; i < 3; i++){
            try {
                playerBoard.addCard(1, cards);

            } catch (NoSpaceException e) {
                System.out.println("TOO MANY CARDS");
            }
        }
        board = playerBoard.getBoard();
        for (j = 5; j >=  0; j--){
            System.out.println(board[1][j].getColor());
        }

        for(j = 2; j < 5; j++){
            for(i = 0; i < 2; i++){
                try {
                    val = playerBoard.addCard(j, cards);

                } catch (NoSpaceException e) {
                    System.out.println("TOO MANY CARDS");
                }
            }
        }

        System.out.println(val);

    }
}