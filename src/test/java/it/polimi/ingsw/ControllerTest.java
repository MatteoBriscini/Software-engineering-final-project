package it.polimi.ingsw;

import it.polimi.ingsw.Cards.Card;
import it.polimi.ingsw.Exceptions.addPlayerToGameException;
import it.polimi.ingsw.JsonSupportClasses.PositionWithColor;
import junit.framework.TestCase;

public class ControllerTest extends TestCase {

    Controller test;
    private final int timeout = 2;

    public void testStartGame() throws addPlayerToGameException {
        System.out.println("START TEST StartGame\n");

        boolean ex = false;
        //test 1
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

        //test 2
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

        //test 3
        test = new Controller(3);                        //create new game with max 3 players
        test.addNewPlayer("piero");     //4 player join the game
        assert (test.getPlayerNumber() == 1);
        test.addNewPlayer("pino");
        assert (test.getPlayerNumber() == 2);
        test.addNewPlayer("pierino");
        assert (test.getPlayerNumber() == 3);   //the game is full it will be start autonomous

        assert (test.getCurrentPlayer() == 0);

        System.out.println("\nEND TEST\n");
    }
    public void testTakeCard() throws addPlayerToGameException, InterruptedException {
        System.out.println("START TEST TakeCard\n");
        PositionWithColor[] cards = new PositionWithColor[3];

        test = new Controller(3);                        //create new game with max 3 players
        test.addNewPlayer("piero");     //4 player join the game
        assert (test.getPlayerNumber() == 1);
        test.addNewPlayer("pino");
        assert (test.getPlayerNumber() == 2);
        test.addNewPlayer("pierino");
        assert (test.getPlayerNumber() == 3);   //the game is full it will be start autonomous

        assert (test.getCurrentPlayer() == 0);

        test.setTimeout(timeout);                   //set timeout at 2 seconds
        Thread.sleep((timeout*1000)+5);         //wait player time limit to make a move
        assert (test.getCurrentPlayer() == 1);
        test.setTimeout(180);                           //set timeout at 3 minutes (default)

        assert (!test.takeCard(0,cards, "pierino"));

        System.out.println("\nEND TEST\n");
    }


}