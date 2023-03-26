package it.polimi.ingsw;

import it.polimi.ingsw.Cards.Card;
import it.polimi.ingsw.JsonSupportClasses.Position;
import it.polimi.ingsw.JsonSupportClasses.PositionWithColor;
import it.polimi.ingsw.PlayerClasses.*;
import it.polimi.ingsw.GroupTargets.*;
import java.util.ArrayList;

public class GameMaster {
    private ArrayList<String> players;
    private static PlayerBoard board;
    private CommonGoal[] commonGoals;

    private final MainBoard mainBoard = new MainBoard();

    private Player playerPoints;

    /**
     * get method
     */
    public ArrayList<String> getPlayerArray(){
        return players;
    }

    public MainBoard getMainBoard() {
        return mainBoard;
    }

    public void getPlayerGoal(int currentPlayer){

    }  //has to return player target

    public Card[][] getPlayerBoard(int currentPlayer){
        return board.getBoard();
    } //has to return player board

    /**
     * start game method
     */
    public int addNewPlayer (String playerID){
        this.players.add(playerID);
        return players.size();
    }
    public void setCommonGoal (int commonGoalID){

    }

    public void setPrivateGoal (int[] privateGoalID){


    }

    public boolean fillMainBoard(Position[] validPosition){
        MainBoard fullB = new MainBoard();
        boolean success = fullB.fillBoard(validPosition);   //need a boolean in MainBoard
        return success;
    }

    /**
     * point method
     */
    public int endGameCalcPoint(){
        return 0;
    }
    public void playerAddPoint(int point, int currentPlayer){

    }

    public int playerGetPoint(int currentPlayer){
        return playerPoints.getPointSum();
    }

    /**
     * MainBoard method
     */
    public boolean addCard(int column, Card[] cards, int currentPlayer){  //call a try-catch on addCard in player
        try {
            Player.addCard(column, cards, currentPlayer);
                return  true;     //return true if the cards were added
        }catch (Exception NoSpaceException ) {
            return false;         //return false if the cards weren't added
        }
    }
    public boolean delCard(PositionWithColor[] cards){ //need to call the MainBoard method delCard
        return false;
    }

    /**
     * commonGoals method
     */
    public void getAlreadyScored (){
        this.getAlreadyScored();
    }
    public void setAlreadyScored(){
        this.setAlreadyScored();
    }
    public boolean checkCommonGoal(int commonGoalID, int currentPlayer){   //have to take current player board
        return false;
    }

}