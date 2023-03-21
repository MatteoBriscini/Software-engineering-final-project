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
    private boolean alreadyUsed[][]= new boolean[5][6];
    private int elementCombo;



    //Constructor

    public Player(String playerID){

        this.playerID = playerID;
        pointSum = 0;
        board = new PlayerBoard();

    }


    //Set Methods

    public void setBoard(PlayerBoard board) {
        this.board = board;
    }


    private void setPlayerTarget(int targetNumber) throws FileNotFoundException {  //eccezzione se file non trovato
        String path = "src/main/json/UserSimple.json";   //dichiaro il path
        FileReader fileJson = new FileReader(path);      //file eseguibile

        Gson gson = new Gson();
        PlayerTarget[] targets = gson.fromJson(fileJson, PlayerTarget[].class);       //chiamo il costruttore su array di user passando i paramentri nel json

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


    //get points for spots
    public void checkSpots(){
        Card[][] tmpBoard = board.getBoard();
        int i,j;
        for (i=0; i<4;i++){
            for (j=0; j<5; j++){
                if (allEqual(new Card[]{tmpBoard[i][j], tmpBoard[i+1][j]}) || allEqual(new Card[]{tmpBoard[i][j], tmpBoard[i][j+1]})) {
                    this.used(tmpBoard, i, j);

                    //assign points for the spots
                    switch(elementCombo){
                        case 3: this.updatePointSum(2);
                        case 4: this.updatePointSum(3);
                        case 5: this.updatePointSum(5);
                        case 6: this.updatePointSum(8);
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
        alreadyUsed[i][j] = true;
        elementCombo += 1;
        if (i>0 && allEqual(new Card[]{board[i][j], board[i-1][j]})) this.used(board, i-1, j);
        if (j>0 && allEqual(new Card[]{board[i][j], board[i][j-1]})) this.used(board, i, j-1);
        if (i+1<5 && allEqual(new Card[]{board[i][j], board[i+1][j]})) this.used(board, i+1, j);
        if (j+1<4 && allEqual(new Card[]{board[i][j], board[i][j+1]})) this.used(board, i, j+1);
    }


}
