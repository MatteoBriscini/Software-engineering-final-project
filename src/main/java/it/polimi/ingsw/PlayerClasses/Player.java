package it.polimi.ingsw.PlayerClasses;


import com.google.gson.Gson;
import it.polimi.ingsw.Cards.Card;
import it.polimi.ingsw.Exceptions.NoSpaceException;
import it.polimi.ingsw.SupportClasses.NColorsGroup;
import it.polimi.ingsw.SupportClasses.RecursiveUsed;
import it.polimi.ingsw.SupportClasses.RecursiveUsedSupport;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Arrays;
import java.util.HashSet;

import static it.polimi.ingsw.Cards.CardColor.EMPTY;

public class Player {


    //Attributes

    private static String playerID;
    private int pointSum;
    //private int pointArray;
    private static PlayerBoard board;
    private static PlayerTarget personalTarget;
    private boolean alreadyUsed1[][]= new boolean[5][6];
    private int elementCombo;
    private final NColorsGroup equal = new NColorsGroup();
    private final RecursiveUsed recursiveUsed = new RecursiveUsed();



    //Constructor

    public Player(String playerID){

        this.playerID = playerID;
        pointSum = 0;
        board = new PlayerBoard();

    }

    public boolean addCard(int column, Card[] cards) throws NoSpaceException {
       return board.addCard(column, cards);
    }
    //Set Methods

    public void setBoard(PlayerBoard board) {
        this.board = board;
    }


    /**
     * Reads the JSON file and assigns the PlayerTarget to the Player
     * @param targetNumber, indicates which of the twelve PersonalTargets is to be assigned to the player
     * @targets is an array of objects PlayerTarget
     * @throws FileNotFoundException
     */
    public void setPlayerTarget(int targetNumber) throws FileNotFoundException {  //exception file not found
        String path = "src/main/json/PlayerTarget.json";   //file path
        FileReader fileJson = new FileReader(path);      //file executable

        Gson gson = new Gson();
        PlayerTarget[] targets = gson.fromJson(fileJson, PlayerTarget[].class);       //Call constructor on PlayerTarget array by passing the json file attributes

        this.personalTarget = targets[targetNumber];
    }


    //Get Methods

    public String getPlayerID() {
        return playerID;
    }

    public int getPointSum() {
        return this.pointSum;
    }

    public Card[][] getBoard(){
        return Player.board.getBoard();
    }

    public PlayerTarget getPersonalTarget() {
        return personalTarget;
    }

    //Other Methods

    public void updatePointSum(int pointSum) {

        this.pointSum += pointSum;
        System.out.println(pointSum);
    }

    public void checkPlayerTarget(){
        int result = personalTarget.checkTarget(board);
        updatePointSum(result);
    }

    //get points for spots
    public void checkSpots() {
        Card[][] tmpBoard = board.getBoard();
        int i, j;
        alreadyUsed1 = new boolean[5][6];
        for (i = 0; i < 5; i++) {
            for (j = 0; j < 6; j++) {
                if (!alreadyUsed1[i][j] && (i + 1 < 5 && (equal.nColorsCheck(new Card[]{tmpBoard[i][j], tmpBoard[i + 1][j]},1,1)) || (j + 1 < 6 && equal.nColorsCheck(new Card[]{tmpBoard[i][j], tmpBoard[i][j + 1]},1,1)))) {
                    RecursiveUsedSupport used = recursiveUsed.used(tmpBoard, i, j, alreadyUsed1, 0);
                    alreadyUsed1 = used.getAlreadyUsed();
                    elementCombo = used.getElementCombo();

                    //assign points for the spots
                    switch (elementCombo) {
                        case 3:
                            this.updatePointSum(2);
                            break;
                        case 4:
                            this.updatePointSum(3);
                            break;
                        case 5:
                            this.updatePointSum(5);
                            break;
                    }

                    if (elementCombo >= 6) {        //assign points for exception spots bigger than 6 cards
                        this.updatePointSum(8);
                    }

                    elementCombo = 0;

                }

            }
        }

    }


}
