package it.polimi.ingsw.playerClasses;


public class Player {

    //Attributes

    private String playerID;
    private int pointSum;
    //private int pointArray;
    private PlayerBoard board;
    private PlayerTarget personalTarget;


    //Constructor

    public Player(String playerID){

        this.playerID = playerID;
        pointSum = 0;

    }

}
