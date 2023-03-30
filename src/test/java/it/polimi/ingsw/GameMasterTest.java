package it.polimi.ingsw;

import it.polimi.ingsw.Cards.Card;
import it.polimi.ingsw.Exceptions.NoSpaceException;
import it.polimi.ingsw.GroupTargets.CommonGoal;
import it.polimi.ingsw.JsonSupportClasses.Position;
import it.polimi.ingsw.PlayerClasses.Player;
import junit.framework.TestCase;

import java.io.FileNotFoundException;
import java.util.ArrayList;

import static it.polimi.ingsw.Cards.CardColor.*;
import static it.polimi.ingsw.Cards.CardColor.LIGHTBLUE;

public class GameMasterTest extends TestCase {

    private ArrayList<Player> players = new ArrayList<>();
    private CommonGoal[] commonGoals;
    private final MainBoard mainBoard = new MainBoard();
    private Position[] positions;
    private Card board[][];
    private Card[] tmp = new Card[6];
    Card[] cards;


    public void testAddNewPlayer(){
        System.out.println("start test addPlayer \n");

        GameMaster gameMaster = new GameMaster();
        int initialPlayerCount = gameMaster.getPlayerArray().size();
        String newPlayerID = "player1";
        int newPlayerIndex = gameMaster.addNewPlayer(newPlayerID);

        if (gameMaster.getPlayerArray().size() != initialPlayerCount + 1 ) {
            System.out.println("Test failed1");
        }
        if (!gameMaster.getPlayerArray().get(newPlayerIndex - 1).getPlayerID().equals(newPlayerID)) {
            System.out.println("Test failed2");
        }

        System.out.println("end test addPlayer\n");
    }

    public void testSetCommonGoal() throws FileNotFoundException {

    }

    public void testSetPrivateGoal() throws FileNotFoundException {
        System.out.println("start test privateGoal \n");

        ArrayList<Player> players = new ArrayList<>();
        players.add(new Player("Matteo"));
        players.add(new Player("Luca"));
        players.add(new Player("Riccardo"));
        players.add(new Player("Davide"));

        int[] privateGoalIDs = {1, 2, 3, 4};

        // Call setPlayerTarget on each player
        for (int i = 0; i < players.size(); i++) {
            players.get(i).setPlayerTarget(privateGoalIDs[i]);
        }

        System.out.println("end test privateGoal \n");
    }

    public void testFillMainBoard() {
        System.out.println("start test FillBoard");

        MainBoard mainBoard = new MainBoard();

        // Generate an array of Position objects representing valid positions
        Position[] validPositions = {new Position(0, 0), new Position(1, 1), new Position(2, 2)};

        // Call fillMainBoard with the valid positions
        boolean result = mainBoard.fillBoard(validPositions);

        //check if the positions were filled
        board = mainBoard.getBoard();
        for (Position a : positions) {
            if (!board[a.getX()][a.getY()].getColor().equals(EMPTY)) {
                assertFalse("positions not filled", false);
            }
        }
        System.out.println("end test FillBoard");
    }

    public void testEndGameCalcPoint() {
        //useless if the other methods that add points are working on their own tests
    }

    public void testPlayerAddPoints() {
        System.out.println("start test PlayerAddPoints\n");

        int sum = 0;

        ArrayList<Player> players = new ArrayList<>();
        players.add(new Player("Matteo"));

        players.get(0).updatePointSum(1); //give 1 point to matteo
        sum = players.get(0).getPointSum();
        System.out.println(sum + "\n"); //print the points added to matteo
        assertEquals(1, sum);

        System.out.println("end test PlayerAddPoints\n");
    }

    public void testAddCard() {


        GameMaster gameMaster = new GameMaster();
        Player player = new Player("GiakomoBillone"); //check if the exception is thrown if the column is full
        player.getBoard();

        tmp[0]= new Card(BLUE); //filling the column
        tmp[1]= new Card(WHITE);
        tmp[2] =new Card(LIGHTBLUE);
        tmp[3]= new Card(BLUE);
        tmp[4]= new Card(WHITE);
        tmp[5] =new Card(LIGHTBLUE);
        try {
            player.addCard(0, tmp);
        } catch (NoSpaceException e) {
            System.out.println("too much cards0");
        }

        try {
            gameMaster.addCard(0, tmp, 0);
        } catch (NoSpaceException e) {
            System.out.println("too much cards0");
        }

    }

    public void testSetAlreadyScored() {
    }

    public void testCheckCommonGoal() {
    }
}