package it.polimi.ingsw.GroupTargets;

import it.polimi.ingsw.Cards.Card;
import it.polimi.ingsw.Cards.CardColor;
import it.polimi.ingsw.Exceptions.NoSpaceException;
import it.polimi.ingsw.PlayerClasses.PlayerBoard;
import junit.framework.TestCase;

import static it.polimi.ingsw.Cards.CardColor.*;

public class RainbowRowsAndColumnsGoalsTest extends TestCase {
    private RainbowRowsAndColumnsGoals test5 = new RainbowRowsAndColumnsGoals(6,1,3, 4);
    private RainbowRowsAndColumnsGoals test8 = new RainbowRowsAndColumnsGoals(5,1,3, 3);
    private RainbowRowsAndColumnsGoals test9 = new RainbowRowsAndColumnsGoals(6,5,5, 2);
    private RainbowRowsAndColumnsGoals test10 = new RainbowRowsAndColumnsGoals(5,6,6, 2);

    private PlayerBoard playerBoard;

    private Card[] tmp = new Card[30];
    private CardColor[] tmpcol1 = {GREEN,WHITE,PINK,BLUE,YELLOW,EMPTY,BLUE,GREEN,GREEN,GREEN,GREEN,EMPTY,GREEN,LIGHTBLUE,YELLOW,PINK,WHITE,GREEN,BLUE,PINK,GREEN,WHITE,YELLOW,EMPTY,GREEN,GREEN,GREEN,GREEN,GREEN,EMPTY};
    private CardColor[] tmpcol2 = {WHITE,LIGHTBLUE,YELLOW,BLUE,YELLOW,EMPTY,YELLOW,LIGHTBLUE,YELLOW,PINK,YELLOW,EMPTY,WHITE,WHITE,YELLOW,PINK,LIGHTBLUE,EMPTY,BLUE,BLUE,YELLOW,PINK,YELLOW,YELLOW,BLUE,PINK,YELLOW,PINK,YELLOW,YELLOW};
    private CardColor[] tmpcol3 = {WHITE,PINK,BLUE,LIGHTBLUE,GREEN,YELLOW,YELLOW,YELLOW,YELLOW,BLUE,YELLOW,YELLOW,BLUE,GREEN,PINK,WHITE,LIGHTBLUE,YELLOW,YELLOW,YELLOW,YELLOW,YELLOW,YELLOW,YELLOW,BLUE,WHITE,GREEN,YELLOW,LIGHTBLUE,PINK};
    private CardColor[] tmpcol4 = {LIGHTBLUE,YELLOW,BLUE,GREEN,PINK,WHITE,LIGHTBLUE,BLUE,GREEN,PINK,WHITE,YELLOW,LIGHTBLUE,GREEN,PINK,WHITE,YELLOW,BLUE,LIGHTBLUE,PINK,WHITE,YELLOW,BLUE,GREEN,LIGHTBLUE,WHITE,YELLOW,BLUE,GREEN,PINK};
    private CardColor[] tmpcol5 = {WHITE,GREEN,PINK,YELLOW,LIGHTBLUE,BLUE,WHITE,WHITE,PINK,WHITE,PINK,PINK,BLUE,WHITE,GREEN,PINK,LIGHTBLUE,YELLOW,GREEN,GREEN,GREEN,BLUE,GREEN,GREEN,GREEN,GREEN,GREEN,GREEN,GREEN,LIGHTBLUE};
    private CardColor[] tmpcol6 ={BLUE,YELLOW,LIGHTBLUE,PINK,WHITE,GREEN,WHITE,WHITE,GREEN,WHITE,WHITE,GREEN,LIGHTBLUE,WHITE,GREEN,PINK,BLUE,YELLOW,YELLOW,YELLOW,YELLOW,YELLOW,YELLOW,WHITE,PINK,BLUE,WHITE,BLUE,BLUE,BLUE};

    Card[][] board= new Card[6][5];

    public void testCheck() {
        System.out.println("START TEST \n");


        playerBoard = new PlayerBoard();


        //testcase 0
        //checks out that no one point is given if the board is empty


        System.out.println("TEST 0\n");
        board=playerBoard.getBoard();

        for(int y=5;y>=0;y--){
            System.out.println(board[0][y].getColor().toString()+"\t"+board[1][y].getColor().toString()+"\t"+board[2][y].getColor().toString()+"\t"+board[3][y].getColor().toString()+"\t"+board[4][y].getColor().toString());
        }

        if(test5.check(board))
            System.out.println("\n\n4 rows point");
        if(test8.check(board))
            System.out.println("\n\n\3 columns point");
        if(test9.check(board))
            System.out.println("\n\nRainbow rows point");
        if(test10.check(board))
            System.out.println("\n\nRainbow columns point");

        System.out.println("END TEST 0\n");


        //test case 1
        System.out.println("---------\n\nTEST 1");
        for(int i=0;i<30;i++) tmp[i]=new Card(tmpcol1[i]);
        int j=0;

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

        if(test5.check(board))
            System.out.println("\n\n4 rows point");
        if(test8.check(board))
            System.out.println("\n\n3 columns point");
        if(test9.check(board))
            System.out.println("\n\nRainbow rows point");
        if(test10.check(board))
            System.out.println("\n\nRainbow columns point");

        System.out.println("\n\nEND TEST 1\n");


        //test case 2
        System.out.println("---------\n\nTEST 2");
        for(int i=0;i<30;i++) tmp[i]=new Card(tmpcol2[i]);
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

        if(test5.check(board))
        System.out.println("\n\n4 rows point");
        if(test8.check(board))
            System.out.println("\n\n3 columns point");
        if(test9.check(board))
            System.out.println("\n\nRainbow rows point");
        if(test10.check(board))
            System.out.println("\n\nRainbow columns point");

        System.out.println("\n\nEND TEST 2\n");


        //test case 3

        System.out.println("---------\n\nTEST 3");
        for(int i=0;i<30;i++) tmp[i]=new Card(tmpcol3[i]);
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

        if(test5.check(board))
            System.out.println("\n\n4 rows point");
        if(test8.check(board))
            System.out.println("\n\n3 columns point");
        if(test9.check(board))
            System.out.println("\n\nRainbow rows point");
        if(test10.check(board))
            System.out.println("\n\nRainbow columns point");

        System.out.println("\n\nEND TEST 3\n");

        //test case 4
        System.out.println("---------\n\nTEST 4");
        for(int i=0;i<30;i++) tmp[i]=new Card(tmpcol4[i]);
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


        if(test5.check(board))
            System.out.println("\n\n4 rows point");
        if(test8.check(board))
            System.out.println("\n\n3 columns point");
        if(test9.check(board))
            System.out.println("\n\nRainbow rows point");
        if(test10.check(board))
            System.out.println("\n\nRainbow columns point");


        System.out.println("\n\nEND TEST 4\n");



        //test case 6
        System.out.println("---------\n\nTEST 6");
        for(int i=0;i<30;i++) tmp[i]=new Card(tmpcol6[i]);
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


        if(test5.check(board))
            System.out.println("\n\n4 rows point");
        if(test8.check(board))
            System.out.println("\n\n3 columns point");
        if(test9.check(board))
            System.out.println("\n\nRainbow rows point");
        if(test10.check(board))
            System.out.println("\n\nRainbow columns point");


        System.out.println("\n\nEND TEST 6\n");

        //test case 7
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


        if(test5.check(board))
            System.out.println("\n\n4 rows point");
        if(test8.check(board))
            System.out.println("\n\n3 columns point");
        if(test9.check(board))
            System.out.println("\n\nRainbow rows point");
        if(test10.check(board))
            System.out.println("\n\nRainbow columns point");


        System.out.println("\n\nEND TEST 7\n");
    }
}