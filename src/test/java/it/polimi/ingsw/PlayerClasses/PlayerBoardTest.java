package it.polimi.ingsw.PlayerClasses;

import it.polimi.ingsw.Cards.Card;
import it.polimi.ingsw.Exceptions.NoSpaceException;
import junit.framework.TestCase;

import static it.polimi.ingsw.Cards.CardColor.*;

public class PlayerBoardTest extends TestCase {


    int i, j;
    Card[] cards = new Card[3];
    Card[][] board;
    PlayerBoard playerBoard = new PlayerBoard();
    boolean val = false;


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
                val = playerBoard.addCard(1, cards);

            } catch (NoSpaceException e) {
                System.out.println("TOO MANY CARDS");
            }
        }
        board = playerBoard.getBoard();
        for (j = 5; j >=  0; j--){
            System.out.println(board[1][j].getColor());
        }

        System.out.println(val);

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

        cards[2] = new Card(EMPTY);

        for (i = 0; i < 5; i++){
            for (j = 0; j < 6; j++){
                board[i][j] = new Card(EMPTY);
            }
        }

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
                playerBoard.addCard(2, cards);

            } catch (NoSpaceException e) {
                System.out.println("TOO MANY CARDS");
            }
        }
        board = playerBoard.getBoard();
        for (i = 5; i >=  0; i--){
            System.out.println(board[2][i].getColor());
        }

        cards[1] = new Card(EMPTY);

        for(i = 0; i < 3; i++){
            try {
                playerBoard.addCard(1, cards);

            } catch (NoSpaceException e) {
                System.out.println("TOO MANY CARDS");
            }
        }
        board = playerBoard.getBoard();
        for (i = 5; i >=  0; i--){
            System.out.println(board[1][i].getColor());
        }


    }
}