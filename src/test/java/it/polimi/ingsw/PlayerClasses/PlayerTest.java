package it.polimi.ingsw.PlayerClasses;

import it.polimi.ingsw.Cards.Card;
import it.polimi.ingsw.Cards.CardColor;
import it.polimi.ingsw.Exceptions.NoSpaceException;
import junit.framework.TestCase;

import static it.polimi.ingsw.Cards.CardColor.*;

public class PlayerTest extends TestCase {
    private Player player = new Player("player");
    private Card[] tmp = new Card[3];
    private PlayerBoard playerBoard;

    private Card[] tmpC = new Card[30];

    Card[][] board= new Card[4][5]; //library copy for the print used to check if the fill was correct

    //testing addCard
    public void testAddCard(){
        player = new Player("player");
        System.out.println("START TEST ADDCARD \n");
//
        tmp[0]= new Card(BLUE);
        tmp[1]= new Card(WHITE);
        tmp[2] =new Card(LIGHTBLUE);
        try {
            player.addCard(0, tmp);
        } catch (NoSpaceException e) {
            System.out.println("too much cards0");
        }


        System.out.println("end test addCard");
    }

    public void testCheckSpots() {
        player = new Player("player");
        System.out.println("START TEST \n");
        int i,j;

        //test case 1 (check in initial case if sum = 0 ), all spots
        player.checkSpots();
        int sum;
        sum = player.getPointSum();
        assert (sum == 0);


        // building library for test

        tmp[0]= new Card(BLUE);
        tmp[1]= new Card(WHITE);
        tmp[2] =new Card(LIGHTBLUE);
        try {
            player.addCard(0, tmp);
        } catch (NoSpaceException e) {
            System.out.println("too much cards0");
        }

        tmp[0]= new Card(PINK);
        tmp[1]= new Card(WHITE);
        tmp[2] =new Card(BLUE);
        try {
            player.addCard(0, tmp);
        } catch (NoSpaceException e) {
            System.out.println("too much cards0");
        }


        tmp[0]= new Card(BLUE);
        tmp[1]= new Card(WHITE);
        tmp[2] =new Card(LIGHTBLUE);
        try {
            player.addCard(1, tmp);
        } catch (NoSpaceException e) {
            System.out.println("too much cards0");
        }

        tmp[0]= new Card(PINK);
        tmp[1]= new Card(WHITE);
        tmp[2] =new Card(BLUE);
        try {
            player.addCard(1, tmp);
        } catch (NoSpaceException e) {
            System.out.println("too much cards0");
        }

        tmp[0]= new Card(GREEN);
        tmp[1]= new Card(PINK);
        tmp[2] =new Card(BLUE);
        try {
            player.addCard(2, tmp);
        } catch (NoSpaceException e) {
            System.out.println("too much cards0");
        }

        tmp[0]= new Card(YELLOW);
        tmp[1]= new Card(LIGHTBLUE);
        tmp[2] =new Card(GREEN);
        try {
            player.addCard(2, tmp);
        } catch (NoSpaceException e) {
            System.out.println("too much cards0");
        }

        tmp[0]= new Card(GREEN);
        tmp[1]= new Card(PINK);
        tmp[2] =new Card(BLUE);
        try {
            player.addCard(3, tmp);
        } catch (NoSpaceException e) {
            System.out.println("too much cards0");
        }

        tmp[0]= new Card(YELLOW);
        tmp[1]= new Card(LIGHTBLUE);
        tmp[2] =new Card(GREEN);
        try {
            player.addCard(3, tmp);
        } catch (NoSpaceException e) {
            System.out.println("too much cards0");
        }

        tmp[0]= new Card(YELLOW);
        tmp[1]= new Card(YELLOW);
        tmp[2] =new Card(GREEN);
        try {
            player.addCard(4, tmp);
        } catch (NoSpaceException e) {
            System.out.println("too much cards0");
        }

        tmp[0]= new Card(GREEN);
        tmp[1]= new Card(PINK);
        tmp[2] =new Card(YELLOW);
        try {
            player.addCard(4, tmp);
        } catch (NoSpaceException e) {
            System.out.println("too much cards0");
        }

        player.checkSpots();
        assert (player.getPointSum() == 0);

        System.out.println("end test");
    }


    //
    //
    //
    //test case 2 (check in initial case if sum = 20, all spots of 3)
    //
    //
    //
    public void testCheckSpots2() {
        player = new Player("player");
        System.out.println("START TEST 2\n");
        int i, j;

        //test case 2 (check in initial case if sum = 20 )
        player.checkSpots();
        int sum;
        sum = player.getPointSum();
        assert (sum == 0);

        // building library for test

        tmp[0] = new Card(GREEN);
        tmp[1] = new Card(BLUE);
        tmp[2] = new Card(BLUE);
        try {
            player.addCard(0, tmp);
        } catch (NoSpaceException e) {
            System.out.println("too much cards0");
        }

        tmp[0] = new Card(PINK);
        tmp[1] = new Card(PINK);
        tmp[2] = new Card(WHITE);
        try {
            player.addCard(0, tmp);
        } catch (NoSpaceException e) {
            System.out.println("too much cards0");
        }


        tmp[0] = new Card(GREEN);
        tmp[1] = new Card(YELLOW);
        tmp[2] = new Card(BLUE);
        try {
            player.addCard(1, tmp);
        } catch (NoSpaceException e) {
            System.out.println("too much cards0");
        }

        tmp[0] = new Card(PINK);
        tmp[1] = new Card(YELLOW);
        tmp[2] = new Card(WHITE);
        try {
            player.addCard(1, tmp);
        } catch (NoSpaceException e) {
            System.out.println("too much cards0");
        }

        tmp[0] = new Card(GREEN);
        tmp[1] = new Card(YELLOW);
        tmp[2] = new Card(WHITE);
        try {
            player.addCard(2, tmp);
        } catch (NoSpaceException e) {
            System.out.println("too much cards0");
        }

        tmp[0] = new Card(GREEN);
        tmp[1] = new Card(YELLOW);
        tmp[2] = new Card(WHITE);
        try {
            player.addCard(2, tmp);
        } catch (NoSpaceException e) {
            System.out.println("too much cards0");
        }

        tmp[0] = new Card(PINK);
        tmp[1] = new Card(YELLOW);
        tmp[2] = new Card(WHITE);
        try {
            player.addCard(3, tmp);
        } catch (NoSpaceException e) {
            System.out.println("too much cards0");
        }

        tmp[0] = new Card(GREEN);
        tmp[1] = new Card(YELLOW);
        tmp[2] = new Card(BLUE);
        try {
            player.addCard(3, tmp);
        } catch (NoSpaceException e) {
            System.out.println("too much cards0");
        }

        tmp[0] = new Card(PINK);
        tmp[1] = new Card(PINK);
        tmp[2] = new Card(WHITE);
        try {
            player.addCard(4, tmp);
        } catch (NoSpaceException e) {
            System.out.println("too much cards0");
        }

        tmp[0] = new Card(GREEN);
        tmp[1] = new Card(BLUE);
        tmp[2] = new Card(BLUE);
        try {
            player.addCard(4, tmp);
        } catch (NoSpaceException e) {
            System.out.println("too much cards0");
        }

        //print library
        board=player.getBoard();
        for(int y=5;y>=0;y--){
            System.out.println(board[0][y].getColor().toString()+" "+board[1][y].getColor().toString()+" "+board[2][y].getColor().toString()+" "+board[3][y].getColor().toString()+" "+board[4][y].getColor().toString()+" ");
        }
        System.out.println("\n");

        player.checkSpots();
        System.out.println(player.getPointSum());
        assert (player.getPointSum() == 20);


        System.out.println("end test 2");

    }



    //
    //
    //
    //test case 3 (check in initial case if sum = 26, all possible spots variation)
    //
    //
    //



    public void testCheckSpots3() {
        player = new Player("player");
        System.out.println("START TEST 3\n");
        int i, j;

        //test case 3 (check in initial case if sum = 26 )
        player.checkSpots();
        int sum;
        sum = player.getPointSum();
        assert (sum == 0);

        // building library for test

        tmp[0] = new Card(BLUE);
        tmp[1] = new Card(YELLOW);
        tmp[2] = new Card(GREEN);
        try {
            player.addCard(0, tmp);
        } catch (NoSpaceException e) {
            System.out.println("too much cards1");
        }

        tmp[0] = new Card(GREEN);
        tmp[1] = new Card(PINK);
        tmp[2] = new Card(PINK);
        try {
            player.addCard(0, tmp);
        } catch (NoSpaceException e) {
            System.out.println("too much cards2");
        }



        tmp[0] = new Card(BLUE);
        tmp[1] = new Card(YELLOW);
        tmp[2] = new Card(GREEN);
        try {
            player.addCard(1, tmp);
        } catch (NoSpaceException e) {
            System.out.println("too much cards3");
        }

        tmp[0] = new Card(GREEN);
        tmp[1] = new Card(PINK);
        tmp[2] = new Card(PINK);
        try {
            player.addCard(1, tmp);
        } catch (NoSpaceException e) {
            System.out.println("too much cards4");
        }
        tmp[0] = new Card(BLUE);
        tmp[1] = new Card(YELLOW);
        tmp[2] = new Card(GREEN);
        try {
            player.addCard(2, tmp);
        } catch (NoSpaceException e) {
            System.out.println("too much cards5");
        }

        tmp[0] = new Card(GREEN);
        tmp[1] = new Card(WHITE);
        tmp[2] = new Card(PINK);
        try {
            player.addCard(2, tmp);
        } catch (NoSpaceException e) {
            System.out.println("too much cards6");
        }

        tmp[0] = new Card(BLUE);
        tmp[1] = new Card(YELLOW);
        tmp[2] = new Card(GREEN);
        try {
            player.addCard(3, tmp);
        } catch (NoSpaceException e) {
            System.out.println("too much cards7");
        }

        tmp[0] = new Card(GREEN);
        tmp[1] = new Card(WHITE);
        tmp[2] = new Card(PINK);
        try {
            player.addCard(3, tmp);
        } catch (NoSpaceException e) {
            System.out.println("too much cards8");
        }

        tmp[0] = new Card(BLUE);
        tmp[1] = new Card(GREEN);
        tmp[2] = new Card(GREEN);
        try {
            player.addCard(4, tmp);
        } catch (NoSpaceException e) {
            System.out.println("too much cards9");
        }

        tmp[0] = new Card(GREEN);
        tmp[1] = new Card(WHITE);
        tmp[2] = new Card(PINK);
        try {
            player.addCard(4, tmp);
        } catch (NoSpaceException e) {
            System.out.println("too much cards10");
        }

        //print library
        board=player.getBoard();
        for(int y=5;y>=0;y--){
            System.out.println(board[0][y].getColor().toString()+" "+board[1][y].getColor().toString()+" "+board[2][y].getColor().toString()+" "+board[3][y].getColor().toString()+" "+board[4][y].getColor().toString()+" ");
        }
        System.out.println("\n");

        player.checkSpots();
        System.out.println(player.getPointSum());
        assert (player.getPointSum() == 26);


        System.out.println("end test 3");

    }




    //check with empty board (test 4) or with a full board with a single color (test 5)

    public void testCheckSpots4(){
        player = new Player("player");
        System.out.println("START TEST 4\n");
        int i, j;

        //test case 4 (check in initial case if sum = 0 )
        player.checkSpots();
        int sum;
        sum = player.getPointSum();
        assert (sum == 0);


        //print library
        board=player.getBoard();
        for(int y=5;y>=0;y--){
            System.out.println(board[0][y].getColor().toString()+" "+board[1][y].getColor().toString()+" "+board[2][y].getColor().toString()+" "+board[3][y].getColor().toString()+" "+board[4][y].getColor().toString()+" ");
        }
        System.out.println("\n");

        player.checkSpots();
        System.out.println(player.getPointSum());
        assert (player.getPointSum() == 0);

        System.out.println("end test 4");

    }

    public void testCheckSpots5(){
        player = new Player("player");
        System.out.println("START TEST 5\n");
        int i, j;

        //test case 4 (check in initial case if sum = 0 )
        player.checkSpots();
        int sum;
        sum = player.getPointSum();
        assert (sum == 0);

        //FILLING LIBRARY
        tmp[0] = new Card(BLUE);
        tmp[1] = new Card(BLUE);
        tmp[2] = new Card(BLUE);
        try {
            player.addCard(0, tmp);
        } catch (NoSpaceException e) {
            System.out.println("too much cards1");
        }

        tmp[0] = new Card(BLUE);
        tmp[1] = new Card(BLUE);
        tmp[2] = new Card(BLUE);
        try {
            player.addCard(0, tmp);
        } catch (NoSpaceException e) {
            System.out.println("too much cards2");
        }


        tmp[0] = new Card(BLUE);
        tmp[1] = new Card(BLUE);
        tmp[2] = new Card(BLUE);
        try {
            player.addCard(1, tmp);
        } catch (NoSpaceException e) {
            System.out.println("too much cards3");
        }

        tmp[0] = new Card(BLUE);
        tmp[1] = new Card(BLUE);
        tmp[2] = new Card(BLUE);
        try {
            player.addCard(1, tmp);
        } catch (NoSpaceException e) {
            System.out.println("too much cards4");
        }
        tmp[0] = new Card(BLUE);
        tmp[1] = new Card(BLUE);
        tmp[2] = new Card(BLUE);
        try {
            player.addCard(2, tmp);
        } catch (NoSpaceException e) {
            System.out.println("too much cards5");
        }

        tmp[0] = new Card(BLUE);
        tmp[1] = new Card(BLUE);
        tmp[2] = new Card(BLUE);
        try {
            player.addCard(2, tmp);
        } catch (NoSpaceException e) {
            System.out.println("too much cards6");
        }

        tmp[0] = new Card(BLUE);
        tmp[1] = new Card(BLUE);
        tmp[2] = new Card(BLUE);
        try {
            player.addCard(3, tmp);
        } catch (NoSpaceException e) {
            System.out.println("too much cards7");
        }

        tmp[0] = new Card(BLUE);
        tmp[1] = new Card(BLUE);
        tmp[2] = new Card(BLUE);
        try {
            player.addCard(3, tmp);
        } catch (NoSpaceException e) {
            System.out.println("too much cards8");
        }

        tmp[0] = new Card(BLUE);
        tmp[1] = new Card(BLUE);
        tmp[2] = new Card(BLUE);
        try {
            player.addCard(4, tmp);
        } catch (NoSpaceException e) {
            System.out.println("too much cards9");
        }

        tmp[0] = new Card(BLUE);
        tmp[1] = new Card(BLUE);
        tmp[2] = new Card(BLUE);
        try {
            player.addCard(4, tmp);
        } catch (NoSpaceException e) {
            System.out.println("too much cards10");
        }


        //print library
        board=player.getBoard();
        for(int y=5;y>=0;y--){
            System.out.println(board[0][y].getColor().toString()+" "+board[1][y].getColor().toString()+" "+board[2][y].getColor().toString()+" "+board[3][y].getColor().toString()+" "+board[4][y].getColor().toString()+" ");
        }
        System.out.println("\n");

        player.checkSpots();
        System.out.println(player.getPointSum());
        assert (player.getPointSum() == 8);

        System.out.println("end test 5");

    }


    public void testSetPlayerTarget(){



    }


}
