package it.polimi.ingsw.playerClasses;

public class Player {

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

}
