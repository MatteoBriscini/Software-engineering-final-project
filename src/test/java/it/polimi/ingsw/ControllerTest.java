package it.polimi.ingsw;

import com.google.gson.Gson;
import it.polimi.ingsw.Server.Exceptions.addPlayerToGameException;
import it.polimi.ingsw.Shared.JsonSupportClasses.PositionWithColor;
import it.polimi.ingsw.Server.Controller;
import it.polimi.ingsw.Shared.Cards.Card;
import it.polimi.ingsw.Server.Model.PlayerClasses.Player;
import junit.framework.TestCase;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;

import java.util.Objects;

import static it.polimi.ingsw.Shared.Cards.CardColor.*;

public class ControllerTest extends TestCase {

    Controller test;
    private final int timeout = 2;
    Card[][] board;
    String url1 = "src/main/json/testJson/mainBoard3Players.json";

    String url2 = "src/main/json/testJson/quiteVoidMainBoard.json";

    private PositionWithColor[] jsonCreate(String url) throws FileNotFoundException {
        Gson gson = new Gson();
        FileReader fileJson = new FileReader(url);
        return new Gson().fromJson(fileJson, PositionWithColor[].class);
    }

    private void setNotRandomBoard(String url) throws FileNotFoundException {
        test.fixMainBoard(this.jsonCreate(url));
    }


    public void testStartGame() throws addPlayerToGameException {
        System.out.println("START TEST StartGame\n");

        boolean ex = false;
        //test 1
        System.out.println("3 playerr entry test:\n");
        test = new Controller();

        assert (test.getMaxPlayerNumber() == 4);
        assert (!test.startGame("piero"));
        assert (test.getPlayerNumber() == 0);
        assert (test.getCurrentPlayer() == -1);

        test.addNewPlayer("piero");     //3 player join the game
        assert (test.getPlayerNumber() == 1);
        test.addNewPlayer("pino");
        assert (test.getPlayerNumber() == 2);
        test.addNewPlayer("pierino");
        assert (test.getPlayerNumber() == 3);

        assert (!test.startGame("pino"));       //not allowed player try to start the game
        assert (test.getCurrentPlayer() == -1);
        assert (test.startGame("piero"));       //allowed player try to start the game
        assert (test.getCurrentPlayer() == 0);

        //print main board
        board = test.getMainBoard();
        for(int y=8;y>=0;y--){
            System.out.println(board[0][y].getColor().toString()+"\t"+board[1][y].getColor().toString()+"\t"+board[2][y].getColor().toString()+"\t"+board[3][y].getColor().toString()+"\t"+board[4][y].getColor().toString()+"\t"+board[5][y].getColor().toString()+"\t"+board[6][y].getColor().toString()+"\t"+board[7][y].getColor().toString()+"\t"+board[8][y].getColor().toString());
        }
        System.out.println("\n\n");

        //test 2
        System.out.println("4 playerr entry test:\n");
        test = new Controller();                        //create new standard game
        assert (test.getMaxPlayerNumber() == 4);
        assert (test.getPlayerNumber() == 0);
        assert (test.getCurrentPlayer() == -1);

        test.addNewPlayer("piero");     //4 player join the game
        assert (test.getPlayerNumber() == 1);
        test.addNewPlayer("pino");
        assert (test.getPlayerNumber() == 2);
        test.addNewPlayer("pierino");
        assert (test.getPlayerNumber() == 3);
        test.addNewPlayer("pinuccia");
        assert (test.getPlayerNumber() == 4);   //the game is full it will be start autonomous

        assert (test.getCurrentPlayer() == 0);

        try{                                                //player try to join started game
            test.addNewPlayer("francoAkerino");
        }catch (addPlayerToGameException e){
            ex = true;
        }
        assert (ex);
        ex = false;

        assert (!test.startGame("piero"));      //allowed player try to start already started game

        //print main board
        board = test.getMainBoard();
        for(int y=8;y>=0;y--){
            System.out.println(board[0][y].getColor().toString()+"\t"+board[1][y].getColor().toString()+"\t"+board[2][y].getColor().toString()+"\t"+board[3][y].getColor().toString()+"\t"+board[4][y].getColor().toString()+"\t"+board[5][y].getColor().toString()+"\t"+board[6][y].getColor().toString()+"\t"+board[7][y].getColor().toString()+"\t"+board[8][y].getColor().toString());
        }
        System.out.println("\n\n");

        //test 3
        System.out.println("3 player entry test:\n");
        test = new Controller(3);                        //create new game with max 3 players
        test.addNewPlayer("piero");     //4 player join the game
        assert (test.getPlayerNumber() == 1);
        test.addNewPlayer("pino");
        assert (test.getPlayerNumber() == 2);
        test.addNewPlayer("pierino");
        assert (test.getPlayerNumber() == 3);   //the game is full it will be start autonomous

        assert (test.getCurrentPlayer() == 0);

        //print main board
        board = test.getMainBoard();
        for(int y=8;y>=0;y--){
            System.out.println(board[0][y].getColor().toString()+"\t"+board[1][y].getColor().toString()+"\t"+board[2][y].getColor().toString()+"\t"+board[3][y].getColor().toString()+"\t"+board[4][y].getColor().toString()+"\t"+board[5][y].getColor().toString()+"\t"+board[6][y].getColor().toString()+"\t"+board[7][y].getColor().toString()+"\t"+board[8][y].getColor().toString());
        }
        System.out.println("\n\n");

        System.out.println("\nEND TEST\n");
    }
    public void testTakeCard() throws addPlayerToGameException, InterruptedException, FileNotFoundException {
        System.out.println("START TEST TakeCard\n");

        PositionWithColor[] cards = new PositionWithColor[2];
        test = new Controller(3);                        //create new game with max 3 players
        test.setTimeout(timeout);                   //set timeout at 2 seconds

        test.addNewPlayer("piero");     //4 player join the game
        assert (test.getPlayerNumber() == 1);
        test.addNewPlayer("pino");
        assert (test.getPlayerNumber() == 2);
        test.addNewPlayer("pierino");
        assert (test.getPlayerNumber() == 3);   //the game is full it will be start autonomous

        assert (test.getCurrentPlayer() == 0);

        //test 1
        System.out.println("test1: \n");
        Thread.sleep((timeout*1000)+15);         //wait player time limit to make a move
        assert (test.getCurrentPlayer() == 1);
        test.setTimeout(180);                           //set timeout at 3 minutes (default)

        this.setNotRandomBoard(url1);                       //fill the main board with predetermined colors

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
        assert (!test.takeCard(0,cards, test.getNotCurrentPlayerID()));
        assert (test.getCurrentPlayer() == 1);

        //test 3 (takeCard invalid, too much cards taken)
        System.out.println("test3: \n");
        cards = new PositionWithColor[4];
        cards[0] =new PositionWithColor(5,0,0, GREEN);
        cards[1] =new PositionWithColor(5,1,0, BLUE);
        cards[2] =new PositionWithColor(5,1,0, BLUE);
        cards[3] =new PositionWithColor(5,1,0, BLUE);
        assert (!test.takeCard(0,cards, test.getCurrentPlayerID()));
        assert (test.getCurrentPlayer() == 1);

        //test 4 (takeCard valid) --
        System.out.println("test4: \n");
        cards = new PositionWithColor[2];
        cards[0] =new PositionWithColor(5,0,0, GREEN);
        cards[1] =new PositionWithColor(5,1,0, BLUE);

        assert (test.takeCard(0,cards,  test.getCurrentPlayerID()));
        assert (test.getCurrentPlayer() == 2);


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
        assert (!test.takeCard(0,cards,  test.getCurrentPlayerID()));
        assert (test.getCurrentPlayer() == 2);

        //test 6 (takeCard invalid, card no near empty)
        System.out.println("test5: \n");
        cards = new PositionWithColor[2];
        cards[0] =new PositionWithColor(4,7,0, PINK);
        cards[1] =new PositionWithColor(4,6,0, LIGHTBLUE);
        assert (!test.takeCard(0,cards,  test.getCurrentPlayerID()));
        assert (test.getCurrentPlayer() == 2);

        //test 7 (takeCard invalid, card no near empty)
        System.out.println("test5: \n");
        cards = new PositionWithColor[2];
        cards[0] =new PositionWithColor(8,5,0, PINK);
        cards[1] =new PositionWithColor(7,5,0, LIGHTBLUE);
        assert (!test.takeCard(0,cards,  test.getCurrentPlayerID()));
        assert (test.getCurrentPlayer() == 2);

        System.out.println("\nEND TEST\n");
    }

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
        assert (test.getPlayerNumber() == 1);
        test.addNewPlayer("pino");
        assert (test.getPlayerNumber() == 2);
        test.addNewPlayer("pierino");
        assert (test.getPlayerNumber() == 3);
        test.addNewPlayer("pierina");
        assert (test.getPlayerNumber() == 4);   //the game is full it will be start autonomous

        test.setNotRandomPlayerOrder(players);

        assert (test.getCurrentPlayer() == 0);

        //test 1
        System.out.println("test1: \n");
        Thread.sleep((timeout*1000)+5);         //wait player time limit to make a move
        assert (test.getCurrentPlayer() == 1);

        Thread.sleep((timeout*1000)+5);         //wait player time limit to make a move
        assert (test.getCurrentPlayer() == 2);

        test.setPlayerOffline("pierina");             //pierina lost connection

        Thread.sleep((timeout*1000)+5);         //wait player time limit to make a move
        assert (test.getCurrentPlayer() == 0);

        test.setPlayerOnline("pierina");               //pierina reconnect
        test.setPlayerOffline("pierino");               //pierino lost  connection

        Thread.sleep((timeout*1000)+5);         //wait player time limit to make a move
        assert (test.getCurrentPlayer() == 1);

        Thread.sleep((timeout*1000)+5);         //wait player time limit to make a move
        assert (test.getCurrentPlayer() == 3);

        //test 2
        System.out.println("\ntest2: \n");

        test = new Controller(4);                        //create new game with max 4 players
        test.setTimeout(timeout);                   //set timeout at 2 seconds

        test.addNewPlayer("piero");     //4 player join the game
        assert (test.getPlayerNumber() == 1);
        test.addNewPlayer("pino");
        assert (test.getPlayerNumber() == 2);
        test.addNewPlayer("pierino");
        assert (test.getPlayerNumber() == 3);
        test.addNewPlayer("pierina");
        assert (test.getPlayerNumber() == 4);   //the game is full it will be start autonomous

        test.setNotRandomPlayerOrder(players);

        assert (test.getCurrentPlayer() == 0);
        Thread.sleep((timeout*1000)+5);         //wait player time limit to make a move
        assert (test.getCurrentPlayer() == 1);

        test.setPlayerOffline("pino");               //pino lost  connection
        test.setPlayerOffline("pierina");            //pierina lost  connection

        Thread.sleep((timeout*1000)+5);         //wait player time limit to make a move
        assert (test.getCurrentPlayer() == 2);

        Thread.sleep((timeout*1000)+5);         //wait player time limit to make a move
        assert (test.getCurrentPlayer() == 0);

        test.setPlayerOnline("pino");               //pino reconnect
        test.setPlayerOnline("piero");               //piero reconnect
        test.setPlayerOffline("pierina");            //pierina lost  connection

        Thread.sleep((timeout*1000)+5);         //wait player time limit to make a move
        assert (test.getCurrentPlayer() == 1);

        Thread.sleep((timeout*1000)+5);         //wait player time limit to make a move
        assert (test.getCurrentPlayer() == 2);

        Thread.sleep((timeout*1000)+5);         //wait player time limit to make a move
        assert (test.getCurrentPlayer() == 0);

        System.out.println("\nEND TEST\n");
    }

    public void testRefil() throws addPlayerToGameException, FileNotFoundException {
        System.out.println("START TEST testRefil\n");

        test = new Controller(2);
        test.addNewPlayer("piero");     //2 player join the game
        assert (test.getPlayerNumber() == 1);
        test.addNewPlayer("pino");
        assert (test.getPlayerNumber() == 2);   //the game is full it will be start autonomous
        assert (test.getCurrentPlayer() == 0);

        ArrayList<PositionWithColor> tmpCards = new ArrayList<>();             //clear all the mainBoard
        for (int i = 0; i<9; i++){
            for (int j=0; j<9; j++){
                tmpCards.add(new PositionWithColor(i,j,0, EMPTY));
            }
        }
        PositionWithColor[] cardsArray = tmpCards.toArray(new PositionWithColor[0]);
        test.fixMainBoard(cardsArray);
        //print main board


        this.setNotRandomBoard(url2);                       //fill the main board with predetermined colors
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
        test.takeCard(0, cards, test.getCurrentPlayerID());
        assert (test.getCurrentPlayer() == 1);
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

    public void testCalcEndGamePoint() throws addPlayerToGameException {
        test = new Controller(4);

        test.addNewPlayer("piero");     //4 player join the game
        assert (test.getPlayerNumber() == 1);
        test.addNewPlayer("pino");
        assert (test.getPlayerNumber() == 2);
        test.addNewPlayer("pierino");
        assert (test.getPlayerNumber() == 3);
        test.addNewPlayer("pierina");
        assert (test.getPlayerNumber() == 4);   //the game is full it will be start autonomous
        test.endGame();
    }
}