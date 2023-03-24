package it.polimi.ingsw;

import it.polimi.ingsw.Cards.Card;
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

    public MainBoard getMainBoard() {
        return mainBoard;
    }

    public void getPlayerGoal(int currentPlayer){}  //has to return player target

    public void getPlayerBoard(int currentPlayer){} //has to return player board

    /**
     * start game method
     */
    public int addNewPlayer (String playerID){
        //add player costrucor
        return players.size();
    }
    public void setCommonGoal (int commonGoalID){

    }

    public void setPrivateGoal (int[] privateGoalID){

    }

    public boolean fullMainBoard(Position[] validPosition){
        return false;
    }

    /**
     * point method
     */
    public int endGameCalcPoint(){
        return 0;
    }
   public void playerAddPoint(int point, int currentPlayer){

   }

   public int playerGetPoin(int currentPlayer){
        return 0;
   }

    /**
     * MainBoard method
     */
    public boolean addCard(int column, Card[] cards){
        return false;
    }
    public boolean delCard(PositionWithColor[] cards){
        return false;
    }

    /**
     * commonGoals method
     */
    public int getNPlayerCommonGoal(int commonGoalID){
        return 0;
    }
    public boolean checkCommonGoal(int commonGoalID, int currentPlayer){ //have to take current player board
        return false;
    }

}
