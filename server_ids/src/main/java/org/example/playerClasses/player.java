package org.example.playerClasses;

public class player {


    //Attributes

    private String playerID;
    private int pointSum;
    //private int pointArray;
    private playerBoard board;
    private playerTarget personalTarget;


    //Constructor

    public player(String playerID, playerBoard board, playerTarget personalTarget){

        this.playerID = playerID;
        this.board = board;
        this.personalTarget = personalTarget;
        pointSum = 0;

    }


    //Methods
    //Get methods

    public String getPlayerID() {
        return playerID;
    }

    public int getPointSum() {
        return pointSum;
    }

    /*

    public int getPointArray() {
        return pointArray;
    }

    */


    //Other methods

    public static void takeCard(int column, card selection[]){

    }


    public static void addPoint(int points){

    }


    public static void calcPointPlayerTarget(playerTarget personalTarget){

    }

}
