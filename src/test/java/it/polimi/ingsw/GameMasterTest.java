package it.polimi.ingsw;

import it.polimi.ingsw.Server.Exceptions.ConstructorException;
import it.polimi.ingsw.Shared.Cards.Card;
import it.polimi.ingsw.Server.Exceptions.NoSpaceException;
import it.polimi.ingsw.Server.Model.GameMaster;
import it.polimi.ingsw.Server.Model.GroupGoals.CommonGoal;
import it.polimi.ingsw.Shared.JsonSupportClasses.Position;
import it.polimi.ingsw.Server.Model.MainBoard;
import it.polimi.ingsw.Server.Model.PlayerClasses.Player;
import junit.framework.TestCase;

import java.io.FileNotFoundException;
import java.util.ArrayList;

import static it.polimi.ingsw.Shared.Cards.CardColor.*;
import static it.polimi.ingsw.Shared.Cards.CardColor.LIGHTBLUE;

public class GameMasterTest extends TestCase {

    private ArrayList<Player> players = new ArrayList<>();
    private CommonGoal[] commonGoals;
    private final MainBoard mainBoard = new MainBoard();
    private Position[] positions;
    private Card board[][];
    private Card[] tmp = new Card[6];
    Card[] cards;


    public void testAddNewPlayer() {
        System.out.println("start test addPlayer \n");

        GameMaster gameMaster = new GameMaster();
        int initialPlayerCount = gameMaster.getPlayerArray().size();
        String newPlayerID = "player1";
        int newPlayerIndex = gameMaster.addNewPlayer(newPlayerID);

        if (gameMaster.getPlayerArray().size() != initialPlayerCount + 1) {
            System.out.println("Test failed1");
        }
        if (!gameMaster.getPlayerArray().get(newPlayerIndex - 1).getPlayerID().equals(newPlayerID)) {
            System.out.println("Test failed2");
        }
        gameMaster.addNewPlayer("player2");

        System.out.println("end test addPlayer\n");
    }

    public void testSetCommonGoal() throws ConstructorException {
        System.out.println("start testSetCommonGoal");



        GameMaster gameMaster = new GameMaster();
        for(int i=0; i<12; i++){
            System.out.println(i);

            gameMaster.setCommonGoal(i,0);
        }
        System.out.println("end testSetCommonGoal");
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
        for (Position a : validPositions) {
            if (!board[a.getX()][a.getY()].getColor().equals(EMPTY)) {
                assertFalse("positions not filled", false);
            }
        }
        System.out.println("end test FillBoard");
    }

    public void testEndGameCalcPoint() throws FileNotFoundException {
        System.out.println("start test endGameCalcPoints");

        int sum;
        GameMaster gamemaster = new GameMaster();
        Player player = new Player("Billone");
        player.setPlayerTarget(1);
        gamemaster.addNewPlayer("Billone");


        player.checkSpots();
        player.checkPlayerTarget();
        sum = player.getPointSum();
        assertEquals(0, sum);

        System.out.println("end test endGameCalcPoint");
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
        System.out.println("start test addCard");

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
        System.out.println("end test addCard");
    }
}