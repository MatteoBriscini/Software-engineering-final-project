package it.polimi.ingsw;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import it.polimi.ingsw.Cards.Card;
import it.polimi.ingsw.Exceptions.*;
import it.polimi.ingsw.JsonSupportClasses.Position;
import it.polimi.ingsw.JsonSupportClasses.PositionWithColor;
import it.polimi.ingsw.PlayerClasses.Player;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;


public class Controller {
    /**
     * parameters
     */
    GameMaster game = new GameMaster();
    int playerNum = 0;
    int currentPlayer = -1;  //start game put this to zero to enable the game
    final ArrayList<Boolean> activePlayers = new ArrayList<>();
    boolean alreadyStarted = false;
    boolean endGame = false; //stop playing phase in end game
    private Position[][] allowedPositionMatrix; //matrix of allowed position on main board (download from json file)
    private Position[] allowedPositionArray;    //array of allowed position fill based on main board and player number (in start game method)
    private Thread waitForPlayerResponse;
    private Thread endGameThread;
    //configuration value for controller
    private int timeout; //wait for player time (in seconds)
    private int numberOfPossibleCommonGoals;
    private int numberOfPossiblePrivateGoals;
    private int commonGoalNumber;
    private int maxPointCommonGoals;
    private int minPlayerNumber;
    private int maxPlayerNumber;

    public Controller(int maxPlayerNumber){
        try {
            jsonCreate();
        } catch (FileNotFoundException e) {
            System.out.println("Controller: JSON FILE NOT FOUND");
            throw new RuntimeException();
        }
        this.maxPlayerNumber = maxPlayerNumber;
    }
    public Controller(){ //default value null
        try {
            jsonCreate();
        } catch (FileNotFoundException e) {
            System.out.println("Controller: JSON FILE NOT FOUND");
            throw new RuntimeException();
        }
    }

    /**
     * download json file
     * @throws FileNotFoundException if method can't file json file
     */
    private void jsonCreate() throws FileNotFoundException {  //download json data
        Gson gson = new Gson();

        String urlController = "src/main/json/config/controllerConfig.json";
        FileReader fileJsonController = new FileReader(urlController);
        String urlPosition = "src/main/json/config/gameConfig.json";
        FileReader fileJsonPosition = new FileReader(urlPosition);

        //load default settings for the game
        JsonObject controller = new Gson().fromJson(fileJsonController, JsonObject.class);
        this.timeout = controller.get("timeout").getAsInt();
        this.numberOfPossibleCommonGoals =  controller.get("numberOfPossibleCommonGoals").getAsInt();
        this.numberOfPossiblePrivateGoals =  controller.get("numberOfPossiblePrivateGoal").getAsInt();
        this.commonGoalNumber = controller.get("commonGoalNumber").getAsInt();
        this.maxPointCommonGoals = controller.get("maxPointCommonGoals").getAsInt();
        this.minPlayerNumber = controller.get("minPlayerNumber").getAsInt();
        this.maxPlayerNumber =  controller.get("maxPlayerNumber").getAsInt();

        //allowed position for mainBoard
        allowedPositionMatrix = gson.fromJson(fileJsonPosition , Position[][].class);
    }

    /**
     * for debugging
     */
    public int getCurrentPlayer() {
        return currentPlayer;
    }

    public int getMaxPlayerNumber(){
        return maxPlayerNumber;
    }
    public int getPlayerNumber(){
        return playerNum;
    }
    public void setPlayerNum(int playerNum) {
        this.playerNum = playerNum;
    }

    public void setTimeout(int timeout){
        this.timeout = timeout;
    }
    /**************************************************************************
     ************************************************** disconnect management *
     *************************************************************************/

    public void setPlayerOffline(int i){
        synchronized (activePlayers) {
            activePlayers.set(i, false);
        }
    }
    public void setPlayerOnline(int i){
        synchronized (activePlayers) {
            activePlayers.set(i, true);
        }
    }
    
    /************************************************************************
    ************************************************** start game method ****
    *************************************************************************/
    synchronized public void addNewPlayer(String playerID) throws addPlayerToGameException {
        if(playerNum < maxPlayerNumber && !alreadyStarted){
            playerNum = game.addNewPlayer(playerID);
            activePlayers.add(true);
        } else {
            if(alreadyStarted) throw new addPlayerToGameException("try to add player in already started game");
            throw new addPlayerToGameException("try to add player in full game");
        }
        if (playerNum == maxPlayerNumber) this.startGame(game.getPlayerArray().get(0).getPlayerID()); //if the game is full start the game
    }

    synchronized public boolean startGame(String playerID){
        Random rand = new Random();
        int n = rand.nextInt(numberOfPossibleCommonGoals-commonGoalNumber);
        int m = rand.nextInt(numberOfPossiblePrivateGoals-maxPlayerNumber);
        ArrayList<Integer> numberList = new ArrayList<>();
        if(playerNum >= minPlayerNumber && !alreadyStarted && !endGame){

            if(!game.getPlayerArray().get(0).getPlayerID().equals(playerID)) return false; //if player hasn't privileges to start the game
            alreadyStarted = true;

            //shuffle gaming order
            ArrayList<Player> tmpPlayers = game.getPlayerArray();
            Collections.shuffle(tmpPlayers, new Random());
            game.setPlayersArray(tmpPlayers);

            //set commun goal
            for (int i=1; i<numberOfPossibleCommonGoals ; i++) numberList.add(i);
            Collections.shuffle(numberList);
            int[] commonGoalIDArray = this.setCommonGoals(numberList, n);

            //set private goal
            numberList.clear();
            for (int i=1; i<numberOfPossiblePrivateGoals ; i++) numberList.add(i);
            Collections.shuffle(numberList);
            int[] privateGoalIDArray = this.setPrivateGoals(numberList, m);

            //fill main board
            this.fillMainBoard();

            //send data to the player
            this.createClientData(commonGoalIDArray, privateGoalIDArray);
            this.turn();
            return true;
        } else {
            return false;   //finire else ***************************************************
        }
    }

    private int[] setCommonGoals(ArrayList<Integer> numberList,int n){
        int[] commonGoalArray = new int[commonGoalNumber];
        for(int i = 0; i<commonGoalNumber; i++){
            try {
                commonGoalArray[i] = numberList.get(n+1);
                game.setCommonGoal(commonGoalArray[i], 0);
            } catch (ConstructorException e) {
                System.out.println("invalid value for CommonGoals constructor");
                throw new RuntimeException(e);
            }
        }
        return commonGoalArray;
    }

    private int[] setPrivateGoals(ArrayList<Integer> numberList,int m){
        int[] privateGoalIDArray = new int[game.getPlayerArray().size()];
        for (int n = 0; n < game.getPlayerArray().size(); n++){
            privateGoalIDArray[n] = numberList.get(m+n);
        }
        try {
            game.setPrivateGoal(privateGoalIDArray);
        } catch (FileNotFoundException e) {
            System.out.println("json file for private goal not found");
            throw new RuntimeException(e);
        } catch (LengthException e) {
            System.out.println("privateGoalIDArray has wrong size");
            throw new RuntimeException(e);
        }
        return privateGoalIDArray;
    }

    private void fillMainBoard(){
        ArrayList<Position> tmp = new ArrayList<>();
        Collections.addAll(tmp, allowedPositionMatrix[0]);
        if (playerNum > minPlayerNumber){
            Collections.addAll(tmp, allowedPositionMatrix[1]);
        }
        if (playerNum == maxPlayerNumber){
            Collections.addAll(tmp, allowedPositionMatrix[2]);
        }
        allowedPositionArray = tmp.toArray(new Position[0]);

        if(!game.fillMainBoard(allowedPositionArray)){
            this.endGame();
        }
    }

    synchronized private void createClientData(int[] commonGoalIDArray, int[] CommonGoalIDArray){
        int i;

        //get main board
        Card[][] mainBoard = game.getMainBoard();

        //get all player board
        ArrayList<Card[][]> playersBoard = new ArrayList<>();
        for(i = 0; i< game.getPlayerArray().size(); i++){
            playersBoard.add(game.getPlayerBoard(i));
        }

        /*
        * for common and private goal i will send the goalID (int)
        * the method has to send the position of the client in the players array list
        *
        * has to send the matrix to client (via connection class)
        * */

    }

    /*************************************************************************
     ************************************************** end game method ******
     *************************************************************************/
    synchronized public void waitForEndGame() {
        endGameThread = new Thread(() -> {
            while (currentPlayer != 0 && !endGame) ;
            if (!endGame) endGame();
        });
        endGameThread.setName("endGameThread");
        endGameThread.start();
    }

    synchronized public void endGame(){
        endGame = true;
        endGameThread.interrupt();

        for(int i=0; i<playerNum; i++) {
            game.endGameCalcPoint(i);
        }

        //System.out.println("The Thread name is " + endGameThread.currentThread().getName());  //vorrei evitare sta cosa

        System.out.println("end game");
    }
    /*************************************************************************
     ************************************************** in game method *******
     *************************************************************************/
    synchronized public boolean takeCard(int column, PositionWithColor[] cards, String playerID){
        if(!endGame && alreadyStarted && game.getPlayerArray().get(currentPlayer).getPlayerID().equals(playerID)){


            //verify the numbers of cards
            if (cards.length > 0 && cards.length<=3){
                //devo comunucare al client che la mossa è errata ******************************************
                return false;
            }

            //remove the cards from main board
            try {
                if (game.removeCards(cards)){
                    if(!game.fillMainBoard(allowedPositionArray)) this.endGame();
                }
            } catch (InvalidPickException e) {
               //devo comunucare al client che la mossa è errata ******************************************
                return false;
            }

            //add card to player board
            ArrayList<Card> tmp = new ArrayList<>();
            for (PositionWithColor p : cards){
                tmp.add(new Card(p.getColor()));
            }
            try {
                if(game.addCard(column, tmp.toArray(new Card[0]), currentPlayer)){
                    game.playerAddPoint(1, currentPlayer);
                    this.waitForEndGame();
                }
            } catch (NoSpaceException e) {
                game.fixBoard(cards);
                //devo comunucare al client che la mossa è errata ******************************************
                return false;
            }

            //calc real time points and add it to current player
            this.updateAllCommonGoal();
            this.updateClientData(cards, tmp.toArray(new Card[0])); //update data in clients
            waitForPlayerResponse.interrupt();
            this.turn(); //skip to next player
            return true;
        } else {
            return true;
            //finire else ***************************************************
        }
    }

    private void updateAllCommonGoal(){
        for(int i = 0; i<commonGoalNumber ; i+=1){
            if (!game.getAlreadyScored(i).contains(game.getPlayerArray().get(currentPlayer)) && game.checkCommonGoal(i, currentPlayer)) {


                ArrayList<Player> alreadyScored = game.getAlreadyScored(i);//update the list of player has already reached the goal
                alreadyScored.add(game.getPlayerArray().get(currentPlayer));
                game.setAlreadyScored(alreadyScored, i);

                int point = maxPointCommonGoals - alreadyScored.size()*2;
                if(playerNum == 2 && point == 6) point = 4;
                game.playerAddPoint(point, currentPlayer);
            }
        }
    }
    synchronized public void turn(){
        if(endGame) return;         //if game is finish, da completare *******************************

        currentPlayer += 1;
        if(currentPlayer > playerNum) currentPlayer = 0;

        synchronized (activePlayers) {
            if (!activePlayers.get(currentPlayer)) {        //if a player is market as offline skip him
                turn();
                return;
            }
        }

        //time limit for player response and action
        waitForPlayerResponse = new Thread(() -> {
            int tmp = currentPlayer;
            try {
                Thread.sleep(timeout*1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            if (currentPlayer == tmp){turn();} //skip to next player
        });
        waitForPlayerResponse.start();
        waitForPlayerResponse.setName("waitForPlayerResponse");
    }

    synchronized private void updateClientData(PositionWithColor[] positions, Card[] cards){ //da finire********************************

        //get main board
        Card[][] mainBoard = game.getMainBoard();

        //get current player
        Card[][] currentPlayerBoard = game.getPlayerBoard(currentPlayer);

        //has to send the matrix to client (via connection class)
    }

}
