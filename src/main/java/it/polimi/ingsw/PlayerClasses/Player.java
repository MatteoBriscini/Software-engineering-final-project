package it.polimi.ingsw.PlayerClasses;


import com.google.gson.Gson;

import java.io.FileNotFoundException;
import java.io.FileReader;

public class Player {


    //Attributes

    private static String playerID;
    private static int pointSum;
    //private int pointArray;
    private static PlayerBoard board;
    private static PlayerTarget personalTarget;


    //Constructor

    public Player(String playerID){

        this.playerID = playerID;
        pointSum = 0;

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



}
