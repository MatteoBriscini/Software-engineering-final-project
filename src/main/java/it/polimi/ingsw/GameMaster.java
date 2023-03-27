package it.polimi.ingsw;

import it.polimi.ingsw.Cards.Card;
import it.polimi.ingsw.Exceptions.NoSpaceException;
import it.polimi.ingsw.JsonSupportClasses.Position;
import it.polimi.ingsw.JsonSupportClasses.PositionWithColor;
import it.polimi.ingsw.PlayerClasses.*;
import it.polimi.ingsw.GroupTargets.*;
import java.util.ArrayList;

public class GameMaster {
    private ArrayList<Player> players;
    private CommonGoal[] commonGoals;
    private final MainBoard mainBoard = new MainBoard();


    /**
     * get method
     */
    public ArrayList<Player> getPlayerArray(){
        return players;
    }
/*
    public Card[][] getMainBoard() {
        return mainBoard.getBoard();
    }*/

    public PlayerTarget getPlayerGoal(int currentPlayer){
        return players.get(currentPlayer).getPersonalTarget();
    }  //has to return player target

    public Card[][] getPlayerBoard(int currentPlayer){
        return players.get(currentPlayer).getBoard();
    }

    /**
     * start game method
     */
    public int addNewPlayer (String playerID){
        this.players.add(new Player(playerID));
        return players.size();
    }
    public void setCommonGoal (int commonGoalID){//da fare **************************************************

    }

    public void setPrivateGoal (int[] privateGoalID){//da fare **************************************************
    }

    public boolean fillMainBoard(Position[] validPosition){
        return mainBoard.fillBoard(validPosition);
    }

    /**
     * point method
     */
    public int endGameCalcPoint(){ //da fare
        return 0;
    }
    public void playerAddPoint(int point, int currentPlayer){
            players.get(currentPlayer).updatePointSum(point);
    }

    public int playerGetPoint(int currentPlayer){
        return players.get(currentPlayer).getPointSum();
    }

    /**
     * Card method
     */
    public boolean addCard(int column, Card[] cards, int currentPlayer) throws NoSpaceException {  //call a try-catch on addCard in player
        return players.get(currentPlayer).addCard(column, cards);
    }
    public boolean removeCards(PositionWithColor[] cards){ //da fare **************************************************
        return false;
    }

    /**
     * commonGoals method
     */
    public ArrayList<Player> getAlreadyScored (int commonGoalID){
        return commonGoals[commonGoalID].getAlreadyScored();
    }
    public void setAlreadyScored(ArrayList<Player> alreadyScored, int commonGoalID){
        commonGoals[commonGoalID].setAlreadyScored(alreadyScored);
    }
    public boolean checkCommonGoal(int commonGoalID, int currentPlayer){   //have to take current player board
        return commonGoals[commonGoalID].check(players.get(currentPlayer).getBoard());
    }

}