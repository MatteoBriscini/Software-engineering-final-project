package it.polimi.ingsw.GroupTargets;

import it.polimi.ingsw.Shared.Cards.Card;
import it.polimi.ingsw.Shared.Cards.CardColor;
import it.polimi.ingsw.Server.Exceptions.NoSpaceException;
import it.polimi.ingsw.Server.Model.PlayerClasses.PlayerBoard;
import it.polimi.ingsw.Server.Model.GroupGoals.EightEqualsGoal;
import junit.framework.TestCase;

import static it.polimi.ingsw.Shared.Cards.CardColor.*;
import static it.polimi.ingsw.Shared.Cards.CardColor.EMPTY;

public class EightEqualsGoalTest extends TestCase {

    private EightEqualsGoal test = new EightEqualsGoal();

    private PlayerBoard playerBoard;

    private Card[] tmp = new Card[30];

    private CardColor[] tmpcol1 ={WHITE,WHITE,EMPTY,EMPTY,EMPTY,EMPTY,  WHITE,WHITE,EMPTY,EMPTY,EMPTY,EMPTY,  PINK,PINK,WHITE,WHITE,EMPTY,EMPTY,  PINK,WHITE,WHITE,WHITE,EMPTY,EMPTY,  EMPTY,EMPTY,EMPTY,EMPTY,EMPTY,EMPTY};

    private CardColor[] tmpcol2 ={PINK,WHITE,BLUE,YELLOW,PINK,GREEN, WHITE,WHITE,PINK,BLUE,YELLOW,GREEN,  YELLOW,GREEN,BLUE,WHITE,BLUE,BLUE,  PINK,PINK,PINK,PINK,LIGHTBLUE,LIGHTBLUE,LIGHTBLUE,  PINK,YELLOW,GREEN,LIGHTBLUE,WHITE,YELLOW};

    private CardColor[] tmpcol3 ={PINK,PINK,PINK,PINK,EMPTY,EMPTY,   EMPTY,EMPTY,EMPTY,EMPTY,EMPTY,EMPTY,  PINK,EMPTY,EMPTY,EMPTY,EMPTY,EMPTY,     BLUE,PINK,EMPTY,EMPTY,EMPTY,EMPTY,   PINK,EMPTY,EMPTY,EMPTY,EMPTY,EMPTY};
    Card[][] board= new Card[6][5];
    public void testCheck() {
        System.out.println("START TEST \n");


        playerBoard = new PlayerBoard();
        int j=0;


        //testcase 0
        //checks out that no one point is given if the board is empty


        System.out.println("TEST 0\n");
        board=playerBoard.getBoard();

        for(int y=5;y>=0;y--){
            System.out.println(board[0][y].getColor().toString()+"\t"+board[1][y].getColor().toString()+"\t"+board[2][y].getColor().toString()+"\t"+board[3][y].getColor().toString()+"\t"+board[4][y].getColor().toString());
        }

        assert (!test.check(board));
        System.out.println("\nEND TEST 0\n");


        //testcase 1
        System.out.println("---------\n\nTEST 1");
        for(int i=0;i<30;i++) tmp[i]=new Card(tmpcol1[i]);
        j=0;

        playerBoard = new PlayerBoard();
        for(int i=0;i<30;i+=3) {
            try {
                playerBoard.addCard(j, new Card[]{tmp[i], tmp[i + 1], tmp[i + 2]});
            } catch (NoSpaceException e) {
                System.out.println("TOO MANY CARDS1");
            }
            if (i % 3 == 0 && i % 2 != 0)
                j++;
        }

        board=playerBoard.getBoard();
        for(int y=5;y>=0;y--){
            System.out.println(board[0][y].getColor().toString()+"\t"+board[1][y].getColor().toString()+"\t"+board[2][y].getColor().toString()+"\t"+board[3][y].getColor().toString()+"\t"+board[4][y].getColor().toString());
        }

        assert (test.check(board));
        System.out.println("\nEND TEST 1\n");


        //testcase 2
        System.out.println("---------\n\nTEST 2");
        for(int i=0;i<30;i++) tmp[i]=new Card(tmpcol2[i]);
        j=0;

        playerBoard = new PlayerBoard();
        for(int i=0;i<30;i+=3) {
            try {
                playerBoard.addCard(j, new Card[]{tmp[i], tmp[i + 1], tmp[i + 2]});
            } catch (NoSpaceException e) {
                System.out.println("TOO MANY CARDS1");
            }
            if (i % 3 == 0 && i % 2 != 0)
                j++;
        }

        board=playerBoard.getBoard();
        for(int y=5;y>=0;y--){
            System.out.println(board[0][y].getColor().toString()+"\t"+board[1][y].getColor().toString()+"\t"+board[2][y].getColor().toString()+"\t"+board[3][y].getColor().toString()+"\t"+board[4][y].getColor().toString());
        }

        assert (test.check(board));
        System.out.println("\nEND TEST 2\n");



        //testcase 3
        System.out.println("---------\n\nTEST 3");
        for(int i=0;i<30;i++) tmp[i]=new Card(tmpcol3[i]);
        j=0;

        playerBoard = new PlayerBoard();
        for(int i=0;i<30;i+=3) {
            try {
                playerBoard.addCard(j, new Card[]{tmp[i], tmp[i + 1], tmp[i + 2]});
            } catch (NoSpaceException e) {
                System.out.println("TOO MANY CARDS1");
            }
            if (i % 3 == 0 && i % 2 != 0)
                j++;
        }

        board=playerBoard.getBoard();
        for(int y=5;y>=0;y--){
            System.out.println(board[0][y].getColor().toString()+"\t"+board[1][y].getColor().toString()+"\t"+board[2][y].getColor().toString()+"\t"+board[3][y].getColor().toString()+"\t"+board[4][y].getColor().toString());
        }

        assert (!test.check(board));
        System.out.println("\nEND TEST 3\n");



        //testcase 7
        //checks that no one point is given if the whole board is fulfilled with an only color

        System.out.println("---------\n\nTEST 7");
        for(int i=0;i<30;i++) tmp[i]=new Card(BLUE);
        j=0;

        playerBoard = new PlayerBoard();
        for(int i=0;i<30;i+=3) {
            try {
                playerBoard.addCard(j, new Card[]{tmp[i], tmp[i + 1], tmp[i + 2]});
            } catch (NoSpaceException e) {

            }
            if (i % 3 == 0 && i % 2 != 0)
                j++;
        }

        board=playerBoard.getBoard();
        for(int y=5;y>=0;y--){
            System.out.println(board[0][y].getColor().toString()+"\t"+board[1][y].getColor().toString()+"\t"+board[2][y].getColor().toString()+"\t"+board[3][y].getColor().toString()+"\t"+board[4][y].getColor().toString());
        }

        assert (test.check(board));
        System.out.println("\nEND TEST 7\n");
    }
}