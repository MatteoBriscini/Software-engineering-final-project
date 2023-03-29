package it.polimi.ingsw;

import it.polimi.ingsw.Cards.Card;
import it.polimi.ingsw.Exceptions.ConstructorException;
import it.polimi.ingsw.Exceptions.LenghtException;
import it.polimi.ingsw.Exceptions.NoSpaceException;
import it.polimi.ingsw.JsonSupportClasses.Position;
import it.polimi.ingsw.JsonSupportClasses.PositionWithColor;
import it.polimi.ingsw.PlayerClasses.*;
import it.polimi.ingsw.GroupTargets.*;

import java.io.FileNotFoundException;
import java.util.ArrayList;

public class GameMaster {
    private ArrayList<Player> players = new ArrayList<>();
    private CommonGoal[] commonGoals;
    private final MainBoard mainBoard = new MainBoard();


    /**
     * get method
     */
    public ArrayList<Player> getPlayerArray(){
        return players;
    }

    public Card[][] getMainBoard() {
        return mainBoard.getBoard();
    }

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
    public void setCommonGoal (int commonGoalID, int n) throws ConstructorException {
        switch (commonGoalID) {
            case 0:
                commonGoals[n] = new CouplesAndPokersGoals(2,6);
                break;
            case 1:
                commonGoals[n] = new CouplesAndPokersGoals(4,4);
                break;
            case 2:
                commonGoals[n] = new OneColorPatternGoals(2);
                break;
            case 3:
                commonGoals[n] = new SquaresGoal();
                break;
            case 4:
                commonGoals[n] = new RainbowRowsAndColumnsGoals(6, 1,3, 3);
                break;
            case 5:
                commonGoals[n] = new EightEqualTarget();
                break;
            case 6:
                commonGoals[n] = new OneColorPatternGoals(7);
                break;
            case 7:
                commonGoals[n] = new RainbowRowsAndColumnsGoals(5,1,3,3);
                break;
            case 8:
                commonGoals[n] = new RainbowRowsAndColumnsGoals(6,5,5,2);
                break;
            case 9:
                commonGoals[n]= new RainbowRowsAndColumnsGoals(5,6,6,2);
                break;
            case 10:
                commonGoals[n]= new OneColorPatternGoals(11);
                break;
            case 11:
                commonGoals[n]= new StairsPatternTarget();
                break;
        }
    }

    /**
     * @param privateGoalID is an array that contains identifier of the common goals
     * @throws FileNotFoundException exception if the private goals are not enough for the players
     * @throws LenghtException exception if the private goals are not enough for the players
     */
    public void setPrivateGoal (int[] privateGoalID) throws FileNotFoundException, LenghtException {

        // Check that the number of private goals matches the number of players
        if (privateGoalID.length != players.size()){
            throw new LenghtException("different length for arrays privateGaolId or players");
        }

        //Iterate over players and assign the private goal
         for (int i = 0; i < players.size(); i++) {
             players.get(i).setPlayerTarget(privateGoalID[i]);  //call the method in Player
         }
    }

    /**
     * @param validPosition are the correct positions for the player number
     * @return return the board filled
     */
    public boolean fillMainBoard(Position[] validPosition){
        return mainBoard.fillBoard(validPosition);
    }

    /**
     * this method calculate the points scored at the end of the game
     */
    public void endGameCalcPoint(int currentPlayer){ //return the total scored by the player
        players.get(currentPlayer).checkSpots();
        players.get(currentPlayer).checkPlayerTarget();

    }

    /** mathod to add points to the players
     * @param point points to add
     * @param currentPlayer player that need the points added
     */
    public void playerAddPoint(int point, int currentPlayer){
            players.get(currentPlayer).updatePointSum(point);
    }

    /**
     * @param currentPlayer the player from who I get the points
     * @return give back the player points
     */
    public int playerGetPoint(int currentPlayer){
        return players.get(currentPlayer).getPointSum();
    }

    /**
     * Card methods, add or remove a card from the board
     */
    public boolean addCard(int column, Card[] cards, int currentPlayer) throws NoSpaceException {  //call a try-catch on addCard in player
        return players.get(currentPlayer).addCard(column, cards);
    }

    /*
    public boolean removeCards(PositionWithColor[] cards){  //need a boolean in mainBoard
        return mainBoard.removeCard(cards);
    }
    */

    /**
     * commonGoals method, give info about the commonGoals, if it was already scored, if the common goal was scored in this turn and if it does, set that as already scored
     */
    public ArrayList<Player> getAlreadyScored (int commonGoalID){
        return commonGoals[commonGoalID].getAlreadyScored();
    }
    public void setAlreadyScored(ArrayList<Player> alreadyScored, int commonGoalID){
        commonGoals[commonGoalID].setAlreadyScored(alreadyScored);
    }
    public boolean checkCommonGoal(int commonGoalID, int currentPlayer){    //have to take current player board
        return commonGoals[commonGoalID].check(players.get(currentPlayer).getBoard());
    }

}