package it.polimi.ingsw;

import com.google.gson.Gson;
import it.polimi.ingsw.server.Exceptions.ConnectionControllerManagerException;
import it.polimi.ingsw.server.Exceptions.ConstructorException;
import it.polimi.ingsw.server.Exceptions.LengthException;
import it.polimi.ingsw.shared.exceptions.addPlayerToGameException;
import it.polimi.ingsw.shared.JsonSupportClasses.JsonUrl;
import it.polimi.ingsw.shared.JsonSupportClasses.PositionWithColor;
import it.polimi.ingsw.server.Controller;
import it.polimi.ingsw.shared.Cards.Card;
import it.polimi.ingsw.server.Model.PlayerClasses.Player;
import junit.framework.TestCase;
import org.junit.Test;

import java.io.*;
import java.rmi.RemoteException;
import java.util.ArrayList;

import java.util.Objects;

import static it.polimi.ingsw.shared.Cards.CardColor.*;

public class ControllerTest extends TestCase {

    Controller test;
    private final int timeout = 2;
    private JsonUrl jsonUrl;
    Card[][] board;


    private PositionWithColor[] jsonCreate(String name) throws FileNotFoundException {
        Gson gson = new Gson();
        InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream(jsonUrl.getUrl(name));
        if(inputStream == null) throw new FileNotFoundException();
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        return new Gson().fromJson(bufferedReader, PositionWithColor[].class);
    }

    private void setNotRandomBoard(String url) throws FileNotFoundException {
        test.fixMainBoard(this.jsonCreate(url));
    }

    private  Card[][] setNotRandomPlayerBoard(String url) throws FileNotFoundException{
        Card[][] board = new Card[5][6];
        PositionWithColor[] pos = this.jsonCreate(url);
        for(int i=0; i<5;i++){
            for (int j=0; j<6;j++){
                board[i][j]= new Card(EMPTY);
            }
        }
        for(PositionWithColor p : pos){
            board[p.getX()][p.getY()]= new Card(p.getColor());
        }
        return board;
    }

    @Test
    public void testStartGame() throws addPlayerToGameException {
        System.out.println("START TEST StartGame\n");

        boolean ex = false;
        //test 1
        System.out.println("3 player entry test:\n");
        test = new Controller();

        assertEquals(4, test.getMaxPlayerNumber());
        assertFalse(test.startGame("piero"));

        assertEquals(0, test.getPlayerNumber());
        assertEquals(-1, test.getCurrentPlayer());

        test.addNewPlayer("piero");     //3 player join the game
        test.setPlayerOnline("piero");
        assertEquals(1, test.getPlayerNumber());
        test.addNewPlayer("pino");
        test.setPlayerOnline("pino");
        assertEquals(2, test.getPlayerNumber());
        test.addNewPlayer("pierino");
        test.setPlayerOnline("pierino");
        assertEquals(3, test.getPlayerNumber());

        assertFalse(test.startGame("pino"));       //not allowed player try to start the game
        assertEquals(-1, test.getCurrentPlayer());
        assertTrue (test.startGame("piero"));       //allowed player try to start the game
        assertEquals(0, test.getCurrentPlayer());

        //print main board
        board = test.getMainBoard();
        for(int y=8;y>=0;y--){
            System.out.println(board[0][y].getColor().toString()+"\t"+board[1][y].getColor().toString()+"\t"+board[2][y].getColor().toString()+"\t"+board[3][y].getColor().toString()+"\t"+board[4][y].getColor().toString()+"\t"+board[5][y].getColor().toString()+"\t"+board[6][y].getColor().toString()+"\t"+board[7][y].getColor().toString()+"\t"+board[8][y].getColor().toString());
        }
        System.out.println("\n\n");

        //test 2
        System.out.println("4 playerr entry test:\n");
        test = new Controller();                        //create new standard game
        assertEquals(4, test.getMaxPlayerNumber());
        assertEquals(0, test.getPlayerNumber());
        assertEquals(-1, test.getCurrentPlayer());

        test.addNewPlayer("piero");     //4 player join the game
        test.setPlayerOnline("piero");
        assertEquals(1, test.getPlayerNumber());
        test.addNewPlayer("pino");
        test.setPlayerOnline("pino");
        assertEquals(2, test.getPlayerNumber());
        test.addNewPlayer("pierino");
        test.setPlayerOnline("pierino");
        assertEquals(3, test.getPlayerNumber());
        test.addNewPlayer("pinuccia");
        test.setPlayerOnline("pinuccia");
        assertEquals(4, test.getPlayerNumber());   //the game is full it will be start autonomous

        assertEquals(0, test.getCurrentPlayer());

        try{                                                //player try to join started game
            test.addNewPlayer("francoAkerino");
        }catch (addPlayerToGameException e){
            ex = true;
        }
        assertTrue (ex);
        ex = false;

        assertFalse(test.startGame("piero"));      //allowed player try to start already started game

        //print main board
        board = test.getMainBoard();
        for(int y=8;y>=0;y--){
            System.out.println(board[0][y].getColor().toString()+"\t"+board[1][y].getColor().toString()+"\t"+board[2][y].getColor().toString()+"\t"+board[3][y].getColor().toString()+"\t"+board[4][y].getColor().toString()+"\t"+board[5][y].getColor().toString()+"\t"+board[6][y].getColor().toString()+"\t"+board[7][y].getColor().toString()+"\t"+board[8][y].getColor().toString());
        }
        System.out.println("\n\n");

        //test 3
        System.out.println("3 player entry test:\n");
        test = new Controller(3);                        //create new game with max 3 players
        test.addNewPlayer("piero");     //3 player join the game
        test.setPlayerOnline("piero");
        assertEquals(1, test.getPlayerNumber());
        test.addNewPlayer("pino");
        test.setPlayerOnline("pino");
        assertEquals(2, test.getPlayerNumber());
        test.addNewPlayer("pierino");
        test.setPlayerOnline("pierino");
        assertEquals(3, test.getPlayerNumber());   //the game is full it will be start autonomous

        assertEquals(0, test.getCurrentPlayer());

        //print main board
        board = test.getMainBoard();
        for(int y=8;y>=0;y--){
            System.out.println(board[0][y].getColor().toString()+"\t"+board[1][y].getColor().toString()+"\t"+board[2][y].getColor().toString()+"\t"+board[3][y].getColor().toString()+"\t"+board[4][y].getColor().toString()+"\t"+board[5][y].getColor().toString()+"\t"+board[6][y].getColor().toString()+"\t"+board[7][y].getColor().toString()+"\t"+board[8][y].getColor().toString());
        }
        System.out.println("\n\n");

        System.out.println("\nEND TEST\n");
    }
    @Test
    public void testTakeCard() throws addPlayerToGameException, InterruptedException, FileNotFoundException {
        System.out.println("START TEST TakeCard\n");

        PositionWithColor[] cards = new PositionWithColor[2];
        test = new Controller(3);                        //create new game with max 3 players
        test.setTimeout(timeout);                   //set timeout at 2 seconds

        test.addNewPlayer("piero");     //4 players join the game
        assertEquals(1, test.getPlayerNumber());
        test.setPlayerOnline("piero");
        test.addNewPlayer("pino");
        assertEquals(2, test.getPlayerNumber());
        test.setPlayerOnline("pino");
        test.addNewPlayer("pierino");
        assertEquals(3, test.getPlayerNumber());   //the game is full it will be start autonomous
        test.setPlayerOnline("pierino");

        assertEquals(0, test.getCurrentPlayer());

        //test 1
        System.out.println("test1: \n");
        Thread.sleep((timeout*1000)+15);         //wait player time limit to make a move
        assertEquals(1, test.getCurrentPlayer());
        test.setTimeout(180);                           //set timeout at 3 minutes (default)

        this.setNotRandomBoard("mainBoard3Players");                       //fill the main board with predetermined colors

        //print main board
        board = test.getMainBoard();
        for(int y=8;y>=0;y--){
            System.out.println(board[0][y].getColor().toString()+"\t"+board[1][y].getColor().toString()+"\t"+board[2][y].getColor().toString()+"\t"+board[3][y].getColor().toString()+"\t"+board[4][y].getColor().toString()+"\t"+board[5][y].getColor().toString()+"\t"+board[6][y].getColor().toString()+"\t"+board[7][y].getColor().toString()+"\t"+board[8][y].getColor().toString());
        }
        System.out.println("\n\n");

        //test 2 (takeCard invalid, isn't player turn)
        System.out.println("test2: \n");
        cards[0] =new PositionWithColor(5,0,0, GREEN);
        cards[1] =new PositionWithColor(5,1,0, BLUE);
        assertFalse(test.takeCard(0, cards, test.getNotCurrentPlayerID()));
        assertEquals(1, test.getCurrentPlayer());

        //test 3 (takeCard invalid, too much cards taken)
        System.out.println("test3: \n");
        cards = new PositionWithColor[4];
        cards[0] =new PositionWithColor(5,0,0, GREEN);
        cards[1] =new PositionWithColor(5,1,0, BLUE);
        cards[2] =new PositionWithColor(5,1,0, BLUE);
        cards[3] =new PositionWithColor(5,1,0, BLUE);
        assertFalse(test.takeCard(0, cards, test.getCurrentPlayerID()));
        assertEquals(1, test.getCurrentPlayer());

        //test 4 (takeCard valid) --
        System.out.println("test4: \n");
        cards = new PositionWithColor[2];
        cards[0] =new PositionWithColor(5,0,0, GREEN);
        cards[1] =new PositionWithColor(5,1,0, BLUE);

        assertTrue(test.takeCard(0,cards,  test.getCurrentPlayerID()));
        System.out.println(test.getCurrentPlayer());
        assertEquals(2, test.getCurrentPlayer());


        //print main board
        board = test.getMainBoard();
        for(int y=8;y>=0;y--){
            System.out.println(board[0][y].getColor().toString()+"\t"+board[1][y].getColor().toString()+"\t"+board[2][y].getColor().toString()+"\t"+board[3][y].getColor().toString()+"\t"+board[4][y].getColor().toString()+"\t"+board[5][y].getColor().toString()+"\t"+board[6][y].getColor().toString()+"\t"+board[7][y].getColor().toString()+"\t"+board[8][y].getColor().toString());
        }
        System.out.println("\n\n");

        //test 5 (takeCard invalid, card no near empty)
        System.out.println("test5: \n");
        cards = new PositionWithColor[2];
        cards[0] =new PositionWithColor(4,4,0, BLUE);
        cards[1] =new PositionWithColor(4,3,0, LIGHTBLUE);
        assertFalse(test.takeCard(0, cards, test.getCurrentPlayerID()));
        assertEquals(2, test.getCurrentPlayer());

        //test 6 (takeCard invalid, card no near empty)
        System.out.println("test5: \n");
        cards = new PositionWithColor[2];
        cards[0] =new PositionWithColor(4,7,0, PINK);
        cards[1] =new PositionWithColor(4,6,0, LIGHTBLUE);
        assertFalse(test.takeCard(0, cards, test.getCurrentPlayerID()));
        assertEquals(2, test.getCurrentPlayer());

        //test 7 (takeCard invalid, card no near empty)
        System.out.println("test5: \n");
        cards = new PositionWithColor[2];
        cards[0] =new PositionWithColor(8,5,0, PINK);
        cards[1] =new PositionWithColor(7,5,0, LIGHTBLUE);
        assertFalse(test.takeCard(0, cards, test.getCurrentPlayerID()));
        assertEquals(2, test.getCurrentPlayer());

        System.out.println("\nEND TEST\n");
    }
    @Test
    public void testOffline_OnlinePlayer() throws addPlayerToGameException, InterruptedException {
        System.out.println("START TEST Offline_OnlinePlayer\n");

        ArrayList<Player> players = new ArrayList<>();      //gaming order array list for this test
        players.add(new Player("piero"));
        players.add(new Player("pino"));
        players.add(new Player("pierino"));
        players.add(new Player("pierina"));


        test = new Controller(4);                        //create new game with max 4 players
        test.setTimeout(timeout);                   //set timeout at 2 seconds



        test.addNewPlayer("piero");     //4 player join the game
        test.setPlayerOnline("piero");
        assertEquals(1, test.getPlayerNumber());
        test.addNewPlayer("pino");
        test.setPlayerOnline("pino");
        assertEquals(2, test.getPlayerNumber());
        test.addNewPlayer("pierino");
        test.setPlayerOnline("pierino");
        assertEquals(3, test.getPlayerNumber());
        test.addNewPlayer("pierina");

        test.setPlayerOnline("pierina");
        assertEquals(4, test.getPlayerNumber());   //the game is full it will be start autonomous


        test.setNotRandomPlayerOrder(players);

        assertEquals(0, test.getCurrentPlayer());

        //test 1
        System.out.println("test1: \n");
        Thread.sleep((timeout*1000)+5);         //wait player time limit to make a move
        assertEquals(1, test.getCurrentPlayer());

        Thread.sleep((timeout*1000)+5);         //wait player time limit to make a move
        assertEquals(2, test.getCurrentPlayer());

        test.setPlayerOffline("pierina");             //pierina lost connection

        Thread.sleep((timeout*1000)+5);         //wait player time limit to make a move
        assertEquals(0, test.getCurrentPlayer());

        test.setPlayerOnline("pierina");               //pierina reconnect
        test.setPlayerOffline("pierino");               //pierino lost  connection

        Thread.sleep((timeout*1000)+5);         //wait player time limit to make a move
        assertEquals(1, test.getCurrentPlayer());
        Thread.sleep((timeout*1000)+2);         //wait player time limit to make a move
        assertEquals(3, test.getCurrentPlayer());

        //test 2
        System.out.println("\ntest2: \n");

        test = new Controller(4);                        //create new game with max 4 players
        test.setTimeout(timeout);                   //set timeout at 2 seconds

        test.addNewPlayer("piero");     //4 player join the game
        test.setPlayerOnline("piero");
        assertEquals(1, test.getPlayerNumber());
        test.addNewPlayer("pino");
        test.setPlayerOnline("pino");
        assertEquals(2, test.getPlayerNumber());
        test.addNewPlayer("pierino");
        test.setPlayerOnline("pierino");
        assertEquals(3, test.getPlayerNumber());
        test.addNewPlayer("pierina");
        test.setPlayerOnline("pierina");
        assertEquals(4, test.getPlayerNumber());   //the game is full it will be start autonomous

        test.setNotRandomPlayerOrder(players);

        assertEquals(0, test.getCurrentPlayer());
        Thread.sleep((timeout*1000)+5);         //wait player time limit to make a move
        assertEquals(1, test.getCurrentPlayer());

        test.setPlayerOffline("pino");               //pino lost  connection
        test.setPlayerOffline("pierina");            //pierina lost  connection


        Thread.sleep((timeout*1000)+5);         //wait player time limit to make a move
        assertEquals(2, test.getCurrentPlayer());

        Thread.sleep((timeout*1000)+5);         //wait player time limit to make a move

        assertEquals(0, test.getCurrentPlayer());

        test.setPlayerOnline("pino");               //pino reconnect
        test.setPlayerOnline("piero");               //piero reconnect
        test.setPlayerOffline("pierina");            //pierina lost  connection

        Thread.sleep((timeout*1000)+5);         //wait player time limit to make a move
        assertEquals(1, test.getCurrentPlayer());

        Thread.sleep((timeout*1000)+5);         //wait player time limit to make a move
        assertEquals(2, test.getCurrentPlayer());

        Thread.sleep((timeout*1000)+5);         //wait player time limit to make a move
        assertEquals(0, test.getCurrentPlayer());

        System.out.println("\nEND TEST\n");
    }
    @Test
    public void testRefil() throws addPlayerToGameException, FileNotFoundException {
        System.out.println("START TEST testRefil\n");

        test = new Controller(2);
        test.addNewPlayer("piero");     //2 player join the game
        test.setPlayerOnline("piero");
        assertEquals(1, test.getPlayerNumber());
        test.addNewPlayer("pino");
        test.setPlayerOnline("pino");
        assertEquals(2, test.getPlayerNumber());   //the game is full it will be start autonomous
        assertEquals(0, test.getCurrentPlayer());

        ArrayList<PositionWithColor> tmpCards = new ArrayList<>();             //clear all the mainBoard
        for (int i = 0; i<9; i++){
            for (int j=0; j<9; j++){
                tmpCards.add(new PositionWithColor(i,j,0, EMPTY));
            }
        }
        PositionWithColor[] cardsArray = tmpCards.toArray(new PositionWithColor[0]);
        test.fixMainBoard(cardsArray);
        //print main board


        this.setNotRandomBoard("quiteVoidMainBoard");                       //fill the main board with predetermined colors
        //print main board
        board = test.getMainBoard();
        for(int y=8;y>=0;y--){
            System.out.println(board[0][y].getColor().toString()+"\t"+board[1][y].getColor().toString()+"\t"+board[2][y].getColor().toString()+"\t"+board[3][y].getColor().toString()+"\t"+board[4][y].getColor().toString()+"\t"+board[5][y].getColor().toString()+"\t"+board[6][y].getColor().toString()+"\t"+board[7][y].getColor().toString()+"\t"+board[8][y].getColor().toString());
        }
        System.out.println("\n\n");

        //test 1

        PositionWithColor[] cards = new PositionWithColor[2];
        cards[0] =new PositionWithColor(4,4,0,  BLUE);
        cards[1] =new PositionWithColor(4,3,0, LIGHTBLUE);
        assert(test.takeCard(0, cards, test.getCurrentPlayerID()));

        assertEquals(1, test.getCurrentPlayer());
        board = test.getMainBoard();
        Card empty = new Card(EMPTY);
        assert(!Objects.equals(test.getMainBoard()[4][7], empty));
        assert(!Objects.equals(test.getMainBoard()[4][4], empty));
        assert(!Objects.equals(test.getMainBoard()[1][4], empty));

        //print main board
        for(int y=8;y>=0;y--){
            System.out.println(board[0][y].getColor().toString()+"\t"+board[1][y].getColor().toString()+"\t"+board[2][y].getColor().toString()+"\t"+board[3][y].getColor().toString()+"\t"+board[4][y].getColor().toString()+"\t"+board[5][y].getColor().toString()+"\t"+board[6][y].getColor().toString()+"\t"+board[7][y].getColor().toString()+"\t"+board[8][y].getColor().toString());
        }
        System.out.println("\n\n");
        System.out.println("\nEND TEST\n");
    }
    @Test
    public void testCalcEndGamePoint() throws addPlayerToGameException, FileNotFoundException, ConstructorException, InterruptedException, LengthException {
        System.out.println("START TEST testCalcEndGamePoint\n");

        PositionWithColor[] cards = new PositionWithColor[2];

        ArrayList<Player> players = new ArrayList<>();      //gaming order array list for this test
        players.add(new Player("piero"));
        players.add(new Player("pino"));
        players.add(new Player("pierino"));

        test = new Controller(3);

        test.addNewPlayer("piero");     //3 player join the game
        test.setPlayerOnline("piero");
        assertEquals(1, test.getPlayerNumber());
        test.addNewPlayer("pino");
        test.setPlayerOnline("pino");
        assertEquals(2, test.getPlayerNumber());
        test.addNewPlayer("pierino");
        test.setPlayerOnline("pierino");
        assertEquals(3, test.getPlayerNumber());  //the game is full it will be start autonomous

        test.setNotRandomPlayerOrder(players);
        this.setNotRandomBoard("mainBoard3Players");                       //fill the main board with predetermined colors

        test.setNotRandomPrivateGaol(new int[]{0, 1, 2});

        test.setBoardNonRandomBoard(this.setNotRandomPlayerBoard("personalBoard3"), players.get(0).getPlayerID());                       //fill the players board with predetermined colors
        test.setBoardNonRandomBoard(this.setNotRandomPlayerBoard("personalBoard2"), players.get(1).getPlayerID());
        test.setBoardNonRandomBoard(this.setNotRandomPlayerBoard("personalBoard1"), players.get(2).getPlayerID());

        test.setNonRandomCommonGoal(3,0);
        test.setNonRandomCommonGoal(2,1);

        System.out.println("test1: (start testing common goal point && end game)");
        cards = new PositionWithColor[2];
        cards[0] =new PositionWithColor(5,1,0, BLUE);
        cards[1] =new PositionWithColor(5,0,0, GREEN);
        assertTrue(test.takeCard(0, cards, "piero"));
        assertEquals(8, test.getPlayerPoint("piero"));
        assertEquals(0, test.getPlayerPoint("pino"));
        assertEquals(0, test.getPlayerPoint("pierino"));

        System.out.println("test2:");
        cards = new PositionWithColor[2];
        cards[0] =new PositionWithColor(3,8,0, BLUE);
        cards[1] =new PositionWithColor(3,7,0, PINK);
        assertTrue(test.takeCard(4, cards, "pino"));
        assertEquals(8, test.getPlayerPoint("piero"));
        assertEquals(6, test.getPlayerPoint("pino"));
        assertEquals(0, test.getPlayerPoint("pierino"));

        System.out.println("test3:");
        cards = new PositionWithColor[2];
        cards[0] =new PositionWithColor(5,6,0, PINK);
        cards[1] =new PositionWithColor(6,6,0, GREEN);
        assertTrue(test.takeCard(0, cards, "pierino"));
        assertEquals(8, test.getPlayerPoint("piero"));
        assertEquals(6, test.getPlayerPoint("pino"));
        assertEquals(9, test.getPlayerPoint("pierino"));

        assertFalse(test.takeCard(0, cards, "pino"));//pino can't play the game is finished

        Thread.sleep(timeout*1000);

        System.out.println("test4:");
        System.out.println();
        assertTrue(test.isEndGame());
        assertTrue(test.getPlayerPoint("piero")>=10);      //verify final point except for private goal
        assertTrue(test.getPlayerPoint("pino")>=8);
        assertTrue(test.getPlayerPoint("pierino")>=12);

        System.out.println("test5: (first player will full the shelf)");
        test = new Controller(3);
        players = new ArrayList<>();      //gaming order array list for this test
        players.add(new Player("piero"));
        players.add(new Player("pino"));
        players.add(new Player("pierino"));

        test.addNewPlayer("piero");     //3 player join the game
        test.setPlayerOnline("piero");
        assertEquals(1, test.getPlayerNumber());
        test.addNewPlayer("pino");
        test.setPlayerOnline("pino");
        assertEquals(2, test.getPlayerNumber());
        test.addNewPlayer("pierino");
        test.setPlayerOnline("pierino");
        assertEquals(3, test.getPlayerNumber());  //the game is full it will be start autonomous

        test.setNotRandomPlayerOrder(players);
        this.setNotRandomBoard("mainBoard3Players");                       //fill the main board with predetermined colors

        test.setNotRandomPrivateGaol(new int[]{0, 1, 2});

        test.setBoardNonRandomBoard(this.setNotRandomPlayerBoard("personalBoard1"), players.get(0).getPlayerID());                       //fill the players board with predetermined colors
        test.setBoardNonRandomBoard(this.setNotRandomPlayerBoard("personalBoard2"), players.get(1).getPlayerID());
        test.setBoardNonRandomBoard(this.setNotRandomPlayerBoard("personalBoard3"), players.get(2).getPlayerID());

        test.setNonRandomCommonGoal(3,0);
        test.setNonRandomCommonGoal(2,1);

        System.out.println("test6: (start testing common goal point && end game)");
        cards = new PositionWithColor[2];
        cards[0] =new PositionWithColor(5,6,0, PINK);
        cards[1] =new PositionWithColor(6,6,0, GREEN);
        assertTrue(test.takeCard(0, cards, "piero"));
        assertEquals(9, test.getPlayerPoint("piero"));
        assertEquals(0, test.getPlayerPoint("pino"));
        assertEquals(0, test.getPlayerPoint("pierino"));

        System.out.println("test7:");
        cards = new PositionWithColor[2];
        cards[0] =new PositionWithColor(3,8,0, BLUE);
        cards[1] =new PositionWithColor(3,7,0, PINK);
        assertTrue(test.takeCard(4, cards, "pino"));
        assertEquals(9, test.getPlayerPoint("piero"));
        assertEquals(8, test.getPlayerPoint("pino"));
        assertEquals(0, test.getPlayerPoint("pierino"));

        System.out.println("test8:");
        cards = new PositionWithColor[2];
        cards[0] =new PositionWithColor(5,1,0, BLUE);
        cards[1] =new PositionWithColor(5,0,0, GREEN);
        assertTrue(test.takeCard(0, cards, "pierino"));
        assertEquals(9, test.getPlayerPoint("piero"));
        assertEquals(8, test.getPlayerPoint("pino"));
        assertEquals(6, test.getPlayerPoint("pierino"));

        assertTrue(!test.takeCard(0, cards, "pino"));//pino can't play the game is finished

        Thread.sleep(timeout*1000);

        System.out.println("test4:");
        System.out.println();
        assertTrue(test.isEndGame());
        assertTrue(test.getPlayerPoint("piero")>=11);      //verify final point except for private goal
        assertTrue(test.getPlayerPoint("pino")>=10);
        assertTrue(test.getPlayerPoint("pierino")>=8);

        System.out.println("\nEND TEST\n");
    }
    @Test
    public void testPlayerCrashedEndGame() throws InterruptedException, ConstructorException, FileNotFoundException, LengthException, addPlayerToGameException {
        System.out.println("START TEST testPlayerCrashedEndGame\n");

        PositionWithColor[] cards = new PositionWithColor[2];

        ArrayList<Player> players = new ArrayList<>();      //gaming order array list for this test
        players.add(new Player("piero"));
        players.add(new Player("pino"));
        players.add(new Player("pierino"));

        test = new Controller(3);

        test.addNewPlayer("piero");     //3 player join the game
        test.setPlayerOnline("piero");
        assertEquals(1, test.getPlayerNumber());
        test.addNewPlayer("pino");
        test.setPlayerOnline("pino");
        assertEquals(2, test.getPlayerNumber());
        test.addNewPlayer("pierino");
        test.setPlayerOnline("pierino");
        assertEquals(3, test.getPlayerNumber());  //the game is full it will be start autonomous

        test.setNotRandomPlayerOrder(players);
        this.setNotRandomBoard("mainBoard3Players");                       //fill the main board with predetermined colors

        test.setNotRandomPrivateGaol(new int[]{0, 1, 2});

        test.setBoardNonRandomBoard(this.setNotRandomPlayerBoard("personalBoard3"), players.get(0).getPlayerID());                       //fill the players board with predetermined colors
        test.setBoardNonRandomBoard(this.setNotRandomPlayerBoard("personalBoard2"), players.get(1).getPlayerID());
        test.setBoardNonRandomBoard(this.setNotRandomPlayerBoard("personalBoard1"), players.get(2).getPlayerID());

        test.setNonRandomCommonGoal(3,0);
        test.setNonRandomCommonGoal(2,1);

        System.out.println("test1: (start testing common goal point && end game)");
        cards = new PositionWithColor[2];
        cards[0] =new PositionWithColor(5,1,0, BLUE);
        cards[1] =new PositionWithColor(5,0,0, GREEN);
        assertTrue(test.takeCard(0, cards, "piero"));
        assertEquals(8, test.getPlayerPoint("piero"));
        assertEquals(0, test.getPlayerPoint("pino"));
        assertEquals(0, test.getPlayerPoint("pierino"));

        test.setPlayerOffline("piero");             //piero lost connection

        System.out.println("test2:");
        cards = new PositionWithColor[2];
        cards[0] =new PositionWithColor(3,8,0, BLUE);
        cards[1] =new PositionWithColor(3,7,0, PINK);
        assertTrue(test.takeCard(4, cards, "pino"));
        assertEquals(8, test.getPlayerPoint("piero"));
        assertEquals(6, test.getPlayerPoint("pino"));
        assertEquals(0, test.getPlayerPoint("pierino"));

        System.out.println("test3:");
        cards = new PositionWithColor[2];
        cards[0] =new PositionWithColor(5,6,0, PINK);
        cards[1] =new PositionWithColor(6,6,0, GREEN);
        assertTrue(test.takeCard(0, cards, "pierino"));
        assertEquals(8, test.getPlayerPoint("piero"));
        assertEquals(6, test.getPlayerPoint("pino"));
        assertEquals(9, test.getPlayerPoint("pierino"));

        assertFalse(test.takeCard(0, cards, "pino"));//pino can't play the game is finished

        Thread.sleep(timeout*1000);

        System.out.println("test4:");
        System.out.println();
        assertTrue(test.isEndGame());
        assertTrue(test.getPlayerPoint("piero")>=10);      //verify final point except for private goal
        assertTrue(test.getPlayerPoint("pino")>=8);
        assertTrue(test.getPlayerPoint("pierino")>=12);

        System.out.println("\nEND TEST\n");
    }

}