package it.polimi.ingsw.PlayerClasses;


import com.google.gson.Gson;
import it.polimi.ingsw.Cards.Card;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Arrays;
import java.util.HashSet;

import static it.polimi.ingsw.Cards.CardColor.EMPTY;

public class Player {


    //Attributes

    private static String playerID;
    private static int pointSum;
    //private int pointArray;
    private static PlayerBoard board;
    private static PlayerTarget personalTarget;
    private boolean alreadyUsed1[][]= new boolean[5][6];
    private int elementCombo;



    //Constructor

    public Player(String playerID){

        this.playerID = playerID;
        pointSum = 0;
        board = new PlayerBoard();

    }

    public void addCard(int column, Card[] cards) throws NoSpaceException {
        board.addCard(column, cards);
    }
    //Set Methods

    public void setBoard(PlayerBoard board) {
        this.board = board;
    }


    private void setPlayerTarget(int targetNumber) throws FileNotFoundException {  //exception file not found
        String path = "src/main/YetToDecide.json";   //file path
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
        return pointSum;
    }


    //Other Methods

    public void updatePointSum(int pointSum) {
        this.pointSum += pointSum;
    }

    public void checkPlayerTarget(){
        int result = personalTarget.checkTarget(board);
        updatePointSum(result);
    }

    //get points for spots
    public void checkSpots(){
        Card[][] tmpBoard = board.getBoard();
        int i,j;
        for (i=0; i<4;i++){
            for (j=0; j<5; j++){
                if (!alreadyUsed1[i][j] && (allEqual(new Card[]{tmpBoard[i][j], tmpBoard[i+1][j]}) || allEqual(new Card[]{tmpBoard[i][j], tmpBoard[i][j+1]}))) {
                    this.used(tmpBoard, i, j);

                    //assign points for the spots
                    switch(elementCombo){
                        case 3: this.updatePointSum(2);
                        break;
                        case 4: this.updatePointSum(3);
                        break;
                        case 5: this.updatePointSum(5);
                        break;
                        case 6: this.updatePointSum(8);
                        break;
                    }
                    elementCombo = 0;

                }

            }
        }

    }

    private boolean allEqual (Card[] cards) {
        HashSet<Card> hs = new HashSet<>(Arrays.asList(cards));          //create hash set with same element of the array
        for (Card c : hs){                                              //verify there are no empty card
            if (c.getColor().equals(EMPTY)) {return false;}
        }
        return hs.size() == 1 ;                                         //if hs size is equal 1 all element are equals
    }

    //iteration for adjacency
    private void used (Card[][] board,int i,int j){
        alreadyUsed1[i][j] = true;
        elementCombo += 1;
        if (i>0 && i<5 && !alreadyUsed1[i-1][j] && allEqual(new Card[]{board[i][j], board[i-1][j]})) this.used(board, i-1, j);
        if (j>0 && j<6 && !alreadyUsed1[i][j-1] && allEqual(new Card[]{board[i][j], board[i][j-1]})) this.used(board, i, j-1);
        if (i+1<5 && !alreadyUsed1[i+1][j] &&  allEqual(new Card[]{board[i][j], board[i+1][j]})) this.used(board, i+1, j);
        if (j+1<6 && !alreadyUsed1[i][j+1] && allEqual(new Card[]{board[i][j], board[i][j+1]})) this.used(board, i, j+1);
    }


}
