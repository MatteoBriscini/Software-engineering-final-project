package it.polimi.ingsw.server;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import it.polimi.ingsw.client.Connection.PlayingPlayerRemoteInterface;
import it.polimi.ingsw.server.Connection.ConnectionControllerManager;
import it.polimi.ingsw.server.Connection.RMI.RMI;
import it.polimi.ingsw.server.Connection.SOCKET;
import it.polimi.ingsw.server.Exceptions.*;
import it.polimi.ingsw.shared.Cards.Card;
import it.polimi.ingsw.server.Model.GameMaster;
import it.polimi.ingsw.shared.Cards.CardColor;
import it.polimi.ingsw.shared.JsonSupportClasses.JsonUrl;
import it.polimi.ingsw.shared.JsonSupportClasses.Position;
import it.polimi.ingsw.shared.JsonSupportClasses.PositionWithColor;
import it.polimi.ingsw.server.Model.PlayerClasses.Player;
import it.polimi.ingsw.shared.exceptions.addPlayerToGameException;

import java.io.*;
import java.util.*;


public class Controller implements Runnable {
    /**
     * parameters
     */
    GameMaster game = new GameMaster();
    int playerNum = 0;
    int currentPlayer = -1;  //start game put this to zero to enable the game
    boolean alreadyStarted = false;
    boolean endGame = false; //stop playing phase in end game
    JsonObject mainBoardConfig = new JsonObject();
    private Position[] allowedPositionArray;    //array of allowed position fill based on main board and player number (in start game method)
    private Thread waitForPlayerResponse;
    private Thread endGameThread = new Thread();
    boolean firstPlayer;
    //connection
    private ConnectionControllerManager controllerManager = new ConnectionControllerManager();
    private final ArrayList<Boolean> activePlayers = new ArrayList<>();

    //configuration value for controller
    private JsonUrl jsonUrl;
    private int timeout; //wait for player time (in seconds)
    private int numberOfPossibleCommonGoals;
    private int numberOfPossiblePrivateGoals;
    private int commonGoalNumber;
    private int maxPointCommonGoals;
    private int minPlayerNumber;
    private int maxPlayerNumber;
    private int fullBoardPoint;
    private int minTakeCard;
    private int maxTakeCard;
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
            throw new RuntimeException(e);
        }
    }

    public void run() {}


    /**
     * download json file
     * @throws FileNotFoundException if method can't file json file
     */
    private void jsonCreate() throws FileNotFoundException {  //download json data
        InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream(jsonUrl.getUrl("controllerConfig"));
        if(inputStream == null) throw new FileNotFoundException();
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        InputStream inputStream1 = this.getClass().getClassLoader().getResourceAsStream(jsonUrl.getUrl("gameConfig"));
        if(inputStream1 == null) throw new FileNotFoundException();
        BufferedReader bufferedReader1 = new BufferedReader(new InputStreamReader(inputStream1));

        //load default settings for the game
        JsonObject controller = new Gson().fromJson(bufferedReader , JsonObject.class);
        this.timeout = controller.get("timeout").getAsInt();
        this.numberOfPossibleCommonGoals =  controller.get("numberOfPossibleCommonGoals").getAsInt();
        this.numberOfPossiblePrivateGoals =  controller.get("numberOfPossiblePrivateGoal").getAsInt();
        this.commonGoalNumber = controller.get("commonGoalNumber").getAsInt();
        this.maxPointCommonGoals = controller.get("maxPointCommonGoals").getAsInt();
        this.minPlayerNumber = controller.get("minPlayerNumber").getAsInt();
        this.maxPlayerNumber =  controller.get("maxPlayerNumber").getAsInt();
        this.fullBoardPoint = controller.get("fullBoardPoint").getAsInt();
        this.minTakeCard = controller.get("minTakeCard").getAsInt();
        this.maxTakeCard = controller.get("maxTakeCard").getAsInt();

        //allowed position for mainBoard
        mainBoardConfig = new Gson().fromJson( bufferedReader1, JsonObject.class).getAsJsonObject();
    }

    /**
     * for debugging
     */
    public synchronized int getCurrentPlayer() {
        return currentPlayer;
    }
    public boolean isEndGame(){
        return endGame;
    }
    public String getCurrentPlayerID() {
        return game.getPlayerArray().get(currentPlayer).getPlayerID();
    }
    public String getNotCurrentPlayerID() {
        int i =currentPlayer +1;
        if(i>playerNum) i=0;
        return game.getPlayerArray().get(i).getPlayerID();
    }

    public int getMaxPlayerNumber(){
        return maxPlayerNumber;
    }
    public void setNotRandomPlayerOrder(ArrayList<Player> players){
        game.setPlayersArray(players);
    }
    public void setNotRandomPrivateGaol(int[] privateGoalID) throws FileNotFoundException, LengthException {
        game.setPrivateGoal(privateGoalID);
    }
    public int getPlayerNumber(){
        return playerNum;
    }
    public void setTimeout(int timeout){
        this.timeout = timeout;
    }
    public void fixMainBoard(PositionWithColor[] cards){
        game.fixBoard(cards);
    }
    public void setBoardNonRandomBoard(Card[][] board,String playerID){game.setBoardNonRandomBoard(board, playerID);}
    public int getPlayerPoint(String playerID){
        for(Player p : game.getPlayerArray()){
            if (p.getPlayerID().equals(playerID))return p.getPointSum();
        }
        return 0;
    }
    public void setNonRandomCommonGoal (int commonGoalID, int n) throws ConstructorException{
        game.setCommonGoal(commonGoalID, n);
    }
    public Card[][] getMainBoard(){return game.getMainBoard();}
    /**************************************************************************
     ************************************************** connection management *
     * ************************************************************************
     */

    public void turnOnCnt(SOCKET socket, RMI rmi){
        controllerManager = new ConnectionControllerManager(rmi, socket, this);
    }
    public ConnectionControllerManager getControllerManager(){
        return controllerManager;
    }
    public Map<String, PlayingPlayerRemoteInterface> getClientsRMImap(){return controllerManager.getClientsRMImap();}


    public void addClientRMI(PlayingPlayerRemoteInterface client, String playerID){
        controllerManager.addClientRMI(client,playerID);
    }
    public boolean removeClientRMI(PlayingPlayerRemoteInterface client, String playerID){
        return controllerManager.removeClientRMI(client, playerID);
    }
    public void addClientSOCKET(SOCKET.MultiClientSocketGame client){

        controllerManager.addClientSOCKET(client);
    }

    /**
     * @param playerID the name of the player wants to check the status
     * @return true if the player is the game and his status is offline false in all other cases
     */
    public boolean isPlayerOffline(String playerID){
        for(int i=0; i<activePlayers.size();i++){
            if(game.getPlayerArray().get(i).getPlayerID().equals(playerID) && activePlayers.get(i).equals(false))return true;
        }
        return false;
    }
    /**
     * mark a player as offline
     * @param playerID id of the player
     */
    synchronized public void setPlayerOffline(String playerID){
        ArrayList<Player> players = game.getPlayerArray();
        for (int i = 0; i<players.size(); i++){
            if(players.get(i).getPlayerID().equals(playerID)){
                if(activePlayers.get(i)) {
                    System.err.println(players.get(i).getPlayerID() + " lost connection");
                    activePlayers.set(i, false);
                    if(!endGame)this.checkConnectedPlayerNumbers();
                    return;
                }
            }
        }

    }

    /**
     * if there is only one player online, the game can't continue and it will be ended
     */
    synchronized private void checkConnectedPlayerNumbers(){
        int i=0;
        for (boolean b: activePlayers){
            if (b)i++;
        }
        if(i<=1){
            this.currentPlayer = -1;
            this.endGame = true;
        }
        if(i==1){
            controllerManager.forceClientDisconnection();
        }
    }

    /**
     * mark a player as online
     * @param playerID id of the player
     */
    synchronized public void setPlayerOnline(String playerID){
        ArrayList<Player> players = game.getPlayerArray();
        for (int i = 0; i<players.size(); i++){
            if(players.get(i).getPlayerID().equals(playerID)){
                if(!activePlayers.get(i)) {
                    System.out.println("\u001B[33m" + players.get(i).getPlayerID() + " reconnected the game" + "\u001B[0m");
                    activePlayers.set(i, true);

                    //update all playerBoards in all clients
                    ArrayList<Card[][]> playersBoard = new ArrayList<>();
                    for(int j = 0; j< game.getPlayerArray().size(); j++){
                        playersBoard.add(game.getPlayerBoard(j));
                    }
                    controllerManager.sendAllPlayerBoard(playersBoard);

                    //update the main board to all clients
                    controllerManager.sendMainBoard(game.getMainBoard());

                    System.out.println("\u001B[36m" + "recreate client data after "+ playerID+ " reconnection after a crash" + "\u001B[0m");
                    return;
                }
            }
        }
    }
    /**************************************************************************
     ************************************************** chat ******************
     * ************************************************************************
     * *
     * send message in broadcast to all clients
     * @param msg message to send
     * @param sender name of the player who sends the message
     */
    synchronized public void broadcastMsg(String msg, String sender){
        controllerManager.broadcastMsg(msg, sender);
    }

    /**
     * send a message in private to only one client
     * @param userID id of the player the message is for
     * @param msg message to send
     * @param sender name of the player who send the message
     */
    synchronized public void privateMSG(String userID, String msg, String sender){
        controllerManager.privateMSG(userID, msg, sender);
    }
    /************************************************************************
     ************************************************** start game method ****
     * ***********************************************************************
     * *
     * add new player to the game (called by lobby)
     * @param playerID id of the new player of the game
     * @throws addPlayerToGameException if the game is full or already started
     */
    synchronized public void addNewPlayer(String playerID) throws addPlayerToGameException {
        if(playerNum < maxPlayerNumber && !alreadyStarted){
            playerNum = game.addNewPlayer(playerID);
            controllerManager.sendPlayersNUmber(playerNum);
            activePlayers.add(true); //there is new client online
        } else {
            if(alreadyStarted) throw new addPlayerToGameException("try to add player in already started game");
            throw new addPlayerToGameException("try to add player in full game");
        }
        if (playerNum == maxPlayerNumber) this.startGame(game.getPlayerArray().get(0).getPlayerID()); //if the game is full start the game
    }

    /**
     * this method starts the game
     * @param playerID playerID of the client call the method
     * @return true if the players can start the game false in all other case
     */
    synchronized public boolean startGame(String playerID){
        Random rand = new Random();
        int n = rand.nextInt(numberOfPossibleCommonGoals-commonGoalNumber);
        int m = rand.nextInt(numberOfPossiblePrivateGoals-maxPlayerNumber);
        ArrayList<Integer> numberList = new ArrayList<>();
        JsonObject error = new JsonObject();
        if(playerNum >= minPlayerNumber && !alreadyStarted && !endGame){

            if(!game.getPlayerArray().get(0).getPlayerID().equals(playerID)) {//if player hasn't privileges to start the game
                error.addProperty("errorID", "can't start the game");
                error.addProperty("errorMSG", "the player hasn't privileges to start the game");
                controllerManager.sendError(error, playerID);
                return false;
            }
            alreadyStarted = true;

            //shuffle gaming order
            ArrayList<Player> tmpPlayers = game.getPlayerArray();
            Collections.shuffle(tmpPlayers, new Random());
            game.setPlayersArray(tmpPlayers);

            //set common goal
            for (int i=0; i<numberOfPossibleCommonGoals; i++) numberList.add(i);
            Collections.shuffle(numberList);
            int[] commonGoalIDArray = this.setCommonGoals(numberList, n);

            //set private goal
            numberList.clear();
            for (int i=0; i<numberOfPossiblePrivateGoals; i++) numberList.add(i);
            Collections.shuffle(numberList);
            this.setPrivateGoals(numberList, m);

            //fill main board
            this.fillMainBoard();

            //send data to the player
            this.createClientData(commonGoalIDArray);
            this.turn();
            return true;
        } else {
            error.addProperty("errorID", "can't start the game");
            if(alreadyStarted) error.addProperty("errorMSG", "the game is already started");
            if(playerNum < minPlayerNumber ) error.addProperty("errorMSG", "there isn't enough player in the game");
            controllerManager.sendError(error, playerID);

            return false;
        }
    }

    /**
     * @param numberList shuffle array list of int
     * @param n random start index in the numberList
     * @return the array containing id for all common goals
     */
    private int[] setCommonGoals(ArrayList<Integer> numberList,int n){
        int[] commonGoalArray = new int[commonGoalNumber];

        for(int i = 0; i<commonGoalNumber; i++){
            try {
                commonGoalArray[i] = numberList.get(n+i);
                game.setCommonGoal(commonGoalArray[i], i);
            } catch (ConstructorException e) {
                System.out.println("invalid value for CommonGoals constructor");
                throw new RuntimeException(e);
            }
        }
        return commonGoalArray;
    }

    /**
     * this method creates a random private goal for all players
     * @param numberList shuffle array list of int
     * @param m random start index in the numberList
     * @return the array containing id for all private goals
     */
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

    /**
     * random fill the player board for the first time based on player number
     */
    private void fillMainBoard(){
        ArrayList<Position> tmp = new ArrayList<>();
        Gson gson = new Gson();
        Collections.addAll(tmp, gson.fromJson(mainBoardConfig.get("player2").getAsJsonArray(), Position[].class));
        if (playerNum > 2){
            Collections.addAll(tmp, gson.fromJson(mainBoardConfig.get("player3").getAsJsonArray(), Position[].class));
        }
        if (playerNum == 4){
            Collections.addAll(tmp, gson.fromJson(mainBoardConfig.get("player4").getAsJsonArray(), Position[].class));
        }
        allowedPositionArray = new Position[tmp.size()];
        allowedPositionArray = tmp.toArray(new Position[0]);

        if(!game.fillMainBoard(allowedPositionArray)){
            this.endGame();
        }
    }

    /**
     * send all data to clients in start game or after a client reconnection
     * @param commonGoalIDArray ids for common goal
     */
    synchronized private void createClientData(int[] commonGoalIDArray){
        System.out.println("\u001B[36m" + "create client data" + "\u001B[0m");

        //send players id list, and game order (broadcast to each client)
        ArrayList<String> playersID = new ArrayList<>();
        for(Player p: game.getPlayerArray()){
            playersID.add(p.getPlayerID());
        }
        controllerManager.sendPlayerList(playersID.toArray(new String[0]));

        //get main board
        Card[][] mainBoard = game.getMainBoard();

        //send all main board (broadcast to each client)
        controllerManager.sendMainBoard(mainBoard);

        //get all player board
        ArrayList<Card[][]> playersBoard = new ArrayList<>();
        for(int i = 0; i< game.getPlayerArray().size(); i++){
            playersBoard.add(game.getPlayerBoard(i));
        }

        //send all player board (broadcast to each client)
        controllerManager.sendAllPlayerBoard(playersBoard);

        //send all commonGoalID (broadcast to each client)
        controllerManager.sendAllCommonGoal(commonGoalIDArray);

        //send all private goal (broadcast to each client)
        ArrayList<Player> players = game.getPlayerArray();
        for (Player p: players) {
            ArrayList<PositionWithColor> privateGoal = new ArrayList<>();
            for(int j=0; j<p.getPersonalTarget().getColor().length; j++){
                privateGoal.add(new PositionWithColor(p.getPersonalTarget().getX()[j],
                        p.getPersonalTarget().getY()[j],0,Enum.valueOf(CardColor.class, p.getPersonalTarget().getColor()[j])));
            }
            controllerManager.sendPrivateGoal(privateGoal.toArray(new PositionWithColor[0]), p.getPlayerID());
        }
    }

    /*************************************************************************
     ************************************************** end game method ******
     * ***********************************************************************
     * *
     * called when player have full board this method wait the end of the turn ad call end game
     */
    synchronized public void waitForEndGame() {
        endGameThread = new Thread(() -> {
            synchronized (endGameThread) {
                while (!firstPlayer  && !endGame){  // && !endGame
                    try {
                        endGameThread.wait();
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
            if (!endGame) endGame();
        });
        endGameThread.setName("endGameThread");
        endGameThread.start();

    }

    /**
     * this method is called when the game is ended, it's calc points
     */
    synchronized public void endGame(){
        endGame = true;
        System.out.println("\u001B[36m" + "the game is ended" + "\u001B[0m");

        synchronized (waitForPlayerResponse) {
            waitForPlayerResponse.notify();
        }


        if(endGameThread!=null){
            synchronized (endGameThread){
                endGameThread.notify();
            }
        }

        for(int i=0; i<playerNum; i++) {
            game.endGameCalcPoint(i);
        }

        JsonObject points = new JsonObject();
        JsonObject winner = new JsonObject();
        for(Player p : game.getPlayerArray()){
            if(winner.get("winnerName")==null || winner.get("winnerPoints").getAsInt()<p.getPointSum()){
                winner.addProperty("winnerName",p.getPlayerID());
                winner.addProperty("winnerPoints",p.getPointSum());
            }
            points.addProperty(p.getPlayerID(),p.getPointSum());
        }
        controllerManager.sendWinner(winner);
        controllerManager.sendEndGamePoint(points);
    }
    /*************************************************************************
     ************************************************** in game method *******
     * ***********************************************************************
     *  *
     * called from client when player want to take cards from mainBoard to add it to him board
     * @param column number of columns in the playerBoard
     * @param cards array of card the player wants to take, contains color and position on the mainBoard
     * @param playerID playerID of the client call the method
     * @return true if the move is validly false in all other cases
     */
    synchronized public boolean takeCard(int column, PositionWithColor[] cards, String playerID){
        JsonObject error = new JsonObject();
        if(!endGame && alreadyStarted && game.getPlayerArray().get(currentPlayer).getPlayerID().equals(playerID)){
            //verify the numbers of cards
            if (cards.length < minTakeCard || cards.length > maxTakeCard){
                error.addProperty("errorID", "invalid move");
                error.addProperty("errorMSG", "taken none ore to many cards");
                controllerManager.sendError(error, playerID);

                return false;
            }



            //remove the cards from main board
            PositionWithColor[] tmpCards = new PositionWithColor[cards.length];
            for (int i = 0; i<cards.length;i++){
                tmpCards[i] = new PositionWithColor(cards[i].getX(), cards[i].getY(), cards[i].getSketch(),cards[i].getColor());
            }
            try {
                if (game.removeCards(tmpCards)){
                    if(!game.fillMainBoard(allowedPositionArray)) this.endGame();
                }
            } catch (InvalidPickException e) {
                System.out.println(e.toString());
                //send error msg to the client
                error.addProperty("errorID", "invalid move");
                error.addProperty("errorMSG", e.toString());
                controllerManager.sendError(error, playerID);

                //update main board to all clients
                controllerManager.sendMainBoard(game.getMainBoard());
                return false;
            }


            //add card to player board
            ArrayList<Card> tmp = new ArrayList<>();

            for (PositionWithColor p: cards){
                tmp.add(new Card(p.getColor()));
            }
            try {
                if(game.addCard(column, tmp.toArray(new Card[0]), currentPlayer)){
                    game.playerAddPoint(fullBoardPoint, currentPlayer);
                    this.waitForEndGame();
                }
            } catch (NoSpaceException e) {

                //send error msg to the client
                error.addProperty("errorID", "invalid move");
                error.addProperty("errorMSG", "not enough space on the player board");
                controllerManager.sendError(error, playerID);
                game.fixBoard(cards);

                //update all playerBoards in all clients
                ArrayList<Card[][]> playersBoard = new ArrayList<>();
                for(int i = 0; i< game.getPlayerArray().size(); i++){
                    playersBoard.add(game.getPlayerBoard(i));
                }
                controllerManager.sendAllPlayerBoard(playersBoard);
                return false;
            }

            //calc real time points and add it to current player
            this.updateAllCommonGoal();
            this.updateClientData(cards, tmp.toArray(new Card[0]), column); //update data in clients
            synchronized (waitForPlayerResponse) {
                waitForPlayerResponse.notify();
            }
            this.turn(); //skip to next player
            return true;
        } else {
            error.addProperty("errorID", "invalid move");
            error.addProperty("errorMSG", game.getPlayerArray().get(currentPlayer).getPlayerID() + "'s turn, you can't take cards");
            controllerManager.sendError(error, playerID);
            return false;
        }
    }

    /**
     * verify if a player scores a commonGoal and saves it
     */
    private void updateAllCommonGoal(){

        for(int i = 0; i<commonGoalNumber ; i+=1){
            ArrayList<String> alreadyScored = game.getAlreadyScored(i);
            if (!alreadyScored.contains(game.getPlayerArray().get(currentPlayer).getPlayerID()) && game.checkCommonGoal(i, currentPlayer)){

                //update the list of player has already reached the goal
                alreadyScored.add(game.getPlayerArray().get(currentPlayer).getPlayerID());
                game.setAlreadyScored(alreadyScored, i);

                //calculate point && add to the player points
                int point = maxPointCommonGoals - (alreadyScored.size()-1) * 2;
                if (playerNum == 2 && point == 6) point = 4;
                game.playerAddPoint(point, currentPlayer);

                //send to client the value of the common goal just scored
                JsonObject scored = new JsonObject();
                scored.addProperty("playerID", game.getPlayerArray().get(currentPlayer).getPlayerID());
                scored.addProperty("value", point);
                controllerManager.sendLastCommonScored(scored);
                System.out.println("\u001B[36m" + "send new CommonGoal scorer" + "\u001B[0m");
            }

        }
    }

    /**
     * this method increments the currentPlayer and verifies the player doesn't exceed the time limit to make a move
     */
    synchronized public void turn(){
        if(endGame) return;         //if game is finish
        firstPlayer = false;

        synchronized (endGameThread){
            endGameThread.notify();

            currentPlayer += 1;
            if(currentPlayer >= playerNum) {
                currentPlayer = 0;
                firstPlayer = true;
            }
        }
        //if a player is market as offline skip him
        synchronized (activePlayers) {
            while (!activePlayers.get(currentPlayer)) {
                currentPlayer+=1;
                if(currentPlayer >= playerNum) currentPlayer = 0;
            }
        }

        //send current player to client
        controllerManager.notifyActivePlayer(game.getPlayerArray().get(currentPlayer).getPlayerID());

        //time limit for player response and action
        waitForPlayerResponse = new Thread(() -> {
            int tmp = currentPlayer;
            synchronized (waitForPlayerResponse) {
                try {
                    waitForPlayerResponse.wait(timeout* 1000L);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
            if (currentPlayer == tmp){
                turn();
            } //skip to next player
        });
        waitForPlayerResponse.start();
        waitForPlayerResponse.setName("waitForPlayerResponse");
    }

    /**
     * update data in all clients after each turn
     * @param positions have to dell card from main boards
     * @param cards have to add card on player board
     * @param column card on player boards needs to insert in a specific column
     */
    synchronized private void updateClientData(PositionWithColor[] positions, Card[] cards, int column){
        System.out.println("\u001B[36m" + "update client data" + "\u001B[0m");

        //update main board (broadcast to each client)
        controllerManager.dellCardFromMainBoard(positions);

        //update player board off current player (broadcast to each client)
        controllerManager.addCardToClientBoard(game.getPlayerArray().get(getCurrentPlayer()).getPlayerID(), column, cards);
    }

}