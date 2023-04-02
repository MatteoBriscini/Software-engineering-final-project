package it.polimi.ingsw.Server.Model;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import it.polimi.ingsw.Server.Exceptions.ConstructorException;
import it.polimi.ingsw.Server.Exceptions.InvalidPickException;
import it.polimi.ingsw.Server.Exceptions.LengthException;
import it.polimi.ingsw.Server.Exceptions.NoSpaceException;
import it.polimi.ingsw.Server.Model.Cards.Card;
import it.polimi.ingsw.Server.JsonSupportClasses.Position;
import it.polimi.ingsw.Server.JsonSupportClasses.PositionWithColor;
import it.polimi.ingsw.Server.Model.GroupGoals.*;
import it.polimi.ingsw.Server.Model.PlayerClasses.Player;
import it.polimi.ingsw.Server.Model.PlayerClasses.PlayerTarget;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Collections;

public class GameMaster {
    private ArrayList<Player> players = new ArrayList<>();
    private CommonGoal[] commonGoals;
    private final MainBoard mainBoard = new MainBoard();
    private Integer[] couplesAndPokerGoalsConfig;
    private Integer[] oneColourPatternGoalsConfig;
    private Integer[] rainbowRowsAndColumnsGoalsConfig;


    public GameMaster(){
        try {
            this.jsonCreate();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    private void jsonCreate() throws FileNotFoundException{
        Gson gson = new Gson();

        String gameMasterConfigURL = "src/main/json/config/gameMasterConfig.json";
        String controllerConfigURL = "src/main/json/config/controllerConfig.json";
        FileReader fileJson = new FileReader(controllerConfigURL);
        JsonObject jsonObject = new Gson().fromJson(fileJson, JsonObject.class);
        commonGoals = new CommonGoal[jsonObject.get("commonGoalNumber").getAsInt()];


        fileJson = new FileReader(gameMasterConfigURL);
        jsonObject = new  Gson().fromJson(fileJson, JsonObject.class);
        this.couplesAndPokerGoalsConfig = gson.fromJson(jsonObject.get("couplesAndPokersGoals").getAsJsonArray(), Integer[].class);
        this.oneColourPatternGoalsConfig = gson.fromJson(jsonObject.get("oneColorPatternGoals").getAsJsonArray(), Integer[].class);
        this.rainbowRowsAndColumnsGoalsConfig = gson.fromJson(jsonObject.get("rainbowRowsAndColumnsGoals").getAsJsonArray(), Integer[].class);

    }




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

        int minValue;

        if(commonGoalID >= 0 && commonGoalID <= 1){
            commonGoals[n] = new CouplesAndPokersGoals(couplesAndPokerGoalsConfig[3-(commonGoalID*2)],couplesAndPokerGoalsConfig[2-(commonGoalID*2)]);
        }else if(commonGoalID >= 2 && commonGoalID <= 4){
            minValue = commonGoalID-1;

            commonGoals[n] = new OneColorPatternGoals(oneColourPatternGoalsConfig[3-minValue]);
        }else if(commonGoalID >= 5 && commonGoalID < 8){
            minValue = commonGoalID-4;
            commonGoals[n] = new RainbowRowsAndColumnsGoals(rainbowRowsAndColumnsGoalsConfig[12-(minValue*4)],rainbowRowsAndColumnsGoalsConfig[13-(minValue*4)],rainbowRowsAndColumnsGoalsConfig[14-(minValue*4)],rainbowRowsAndColumnsGoalsConfig[15-(minValue*4)]);
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
    public int endGameCalcPoint(int currentPlayer){ //return the total scored by the player
        int sum;

        players.get(currentPlayer).checkSpots();
        players.get(currentPlayer).checkPlayerTarget();
        sum = players.get(currentPlayer).getPointSum();

        return sum;
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
     * @return return info about the correlation player-common goal
     */
    public boolean checkCommonGoal(int commonGoalID, int currentPlayer){    //have to take current player board
        return commonGoals[commonGoalID].check(players.get(currentPlayer).getBoard());
    }

    public void fixBoard(PositionWithColor[] position){
        mainBoard.fixBoard(position);
    }

}