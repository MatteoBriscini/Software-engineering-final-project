package it.polimi.ingsw;

import it.polimi.ingsw.Cards.Card;
import it.polimi.ingsw.Exceptions.*;
import it.polimi.ingsw.JsonSupportClasses.Position;
import it.polimi.ingsw.JsonSupportClasses.PositionWithColor;
import it.polimi.ingsw.PlayerClasses.*;
import it.polimi.ingsw.GroupTargets.*;

import java.io.FileNotFoundException;
import java.util.ArrayList;

public class GameMaster {
    private ArrayList<Player> players = new ArrayList<>();
    private final CommonGoal[] commonGoals = new CommonGoal[2];
    private final MainBoard mainBoard = new MainBoard();


    /**
     * @return return the players of the game
     */
    public ArrayList<Player> getPlayerArray(){
        return players;
    }

    public void setPlayersArray(ArrayList<Player> players){
        this.players = players;
    }

    /**
     * @return return the main board
     */
    public Card[][] getMainBoard() {
        return mainBoard.getBoard();
    }

    /**
     * @param currentPlayer is the reference to the player I need
     * @return return the personal target to the player
     */
    public PlayerTarget getPlayerGoal(int currentPlayer){
        return players.get(currentPlayer).getPersonalTarget();
    }  //has to return player target

    /**
     * @param currentPlayer is the reference to the player I need
     * @return return the bookshelf of the player
     */
    public Card[][] getPlayerBoard(int currentPlayer){
        return players.get(currentPlayer).getBoard();
    }


    /**
     * @param playerID is the reference to the player I need
     * @return the number of players in the game
     */
    public int addNewPlayer (String playerID){

        ArrayList<Player> tmp = new ArrayList<>();
        tmp.add(new Player(playerID));
        this.players.addAll(tmp);
        return players.size();
    }

    /**
     * @param commonGoalID is an int that identifies the common Goal
     * @param n index for array CommonGoals
     * @throws ConstructorException when constructor of goals receives invalid parameters
     */
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
                commonGoals[n] = new RainbowRowsAndColumnsGoals(6, 1,3, 4);
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
            default:
                throw new RuntimeException("invalid commonGoalID");
        }
    }

    /**
     * @param privateGoalID is an array that contains identifier of the common goals
     * @throws FileNotFoundException exception if the private goals are not enough for the players
     * @throws LengthException exception if the private goals are not enough for the players
     */
    public void setPrivateGoal (int[] privateGoalID) throws FileNotFoundException, LengthException {

        // Check that the number of private goals matches the number of players
        if (privateGoalID.length != players.size()){
            throw new LengthException("different length for arrays privateGaolId or players");
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
     * @param currentPlayer reference to the player I need
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
     * @param column is the index of the column
     * @param cards are the cards to add in the column
     * @param currentPlayer is the player that I refer to when I need to add a card
     * @return return the bookshelf with the cards added
     * @throws NoSpaceException
     */
    public boolean addCard(int column, Card[] cards, int currentPlayer) throws NoSpaceException {  //call a try-catch on addCard in player
        return players.get(currentPlayer).addCard(column, cards);
    }


   /**
     * @param cards is the reference to the cards I need to delete from the main board
     * @return the main board without the cards eleted
     */
    public boolean removeCards(PositionWithColor[] cards) throws InvalidPickException {  //need a boolean in mainBoard
        return mainBoard.removeCard(cards);
    }



    /**
     * @param commonGoalID is the identifier of the common goal
     * @return return the info about the "already scored" goals
     */
    public ArrayList<Player> getAlreadyScored (int commonGoalID){
        return commonGoals[commonGoalID].getAlreadyScored();
    }

    /**
     * @param alreadyScored indicate if the goal was already scored and how many times
     * @param commonGoalID is the identifier of the common goal
     */
    public void setAlreadyScored(ArrayList<Player> alreadyScored, int commonGoalID){
        commonGoals[commonGoalID].setAlreadyScored(alreadyScored);
    }

    /**
     * @param commonGoalID  is the identifier of the common goal
     * @param currentPlayer is the current player
     * @return return info about the correletion player-common goal
     */
    public boolean checkCommonGoal(int commonGoalID, int currentPlayer){    //have to take current player board
        return commonGoals[commonGoalID].check(players.get(currentPlayer).getBoard());
    }

    public void fixBoard(PositionWithColor[] position){
        mainBoard.fixBoard(position);
    }

}