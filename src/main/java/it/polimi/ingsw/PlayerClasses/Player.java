package it.polimi.ingsw.PlayerClasses;


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

    public void setPersonalTarget(PlayerTarget personalTarget) {
        this.personalTarget = personalTarget;
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
