package it.polimi.ingsw.client.Player;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import it.polimi.ingsw.client.Connection.ConnectionManager;
import it.polimi.ingsw.client.Exceptions.InvalidPickException;
import it.polimi.ingsw.client.Exceptions.PlayerNotFoundException;
import it.polimi.ingsw.client.Game.MainBoard;
import it.polimi.ingsw.client.Game.PlayerBoard;
import it.polimi.ingsw.shared.Cards.Card;

import it.polimi.ingsw.shared.JsonSupportClasses.Position;
import it.polimi.ingsw.shared.JsonSupportClasses.PositionWithColor;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;

public class PlayingPlayer extends Player{
    private MainBoard mainBoard;
    private PlayerBoard[] playerBoards;
    private String[] playersID;
    private int myTurnNumber;
    private String activePlayer;
    private boolean myTurn;
    private int playersNumber;
    private int[]CommonGoalID;
    private PositionWithColor[] privateGoal;
    private JsonObject[] commonGoalScored;

    /**
     * @param playerID id of the client will be unique
     * @param pwd access password
     * @param connection class who will manage connection to the server
     * @throws RemoteException if the server does not response (wrong port or ip)
     */
    public PlayingPlayer(String playerID, String pwd, ConnectionManager connection) throws RemoteException {
        super(playerID, pwd, connection);
    }

    /*************************************************************************
     ************************************************** GET ******************
     * ***********************************************************************
    */
    public ConnectionManager getConnetcionManager(){
        return connection;
    }

    public MainBoard getMainBoard() {
        return new MainBoard(mainBoard.getBoard());
    }
    public int getPlayersNumber() {
        return playersNumber;
    }

    /**
     * @param playerID need to know witch playerBoard want to get
     * @return player board
     */
    public PlayerBoard getPlayerBoard(String playerID) {
        int i;
        for (i= 0; i<playersID.length; i++){
            if (playersID[i].equals(playerID)) break;
        }
        return new PlayerBoard(playerBoards[i].getBoard());
    }
    public String[] getPlayersID() {
        return playersID;
    }
    public String getActivePlayer(){
        return activePlayer;
    }
    public int[] getCommonGoalID() {
        return CommonGoalID;
    }

    public PositionWithColor[] getPrivateGoal() {
        PositionWithColor[] tmpPrivateGoal = new PositionWithColor[privateGoal.length];
        for(int i=0; i<privateGoal.length; i++){
            tmpPrivateGoal[i] = new PositionWithColor(privateGoal[i].getX(), privateGoal[i].getY(), privateGoal[i].getSketch(), privateGoal[i].getColor());
        }
        return privateGoal;
    }

    public JsonObject[] getCommonGoalScored() {
        return commonGoalScored;
    }
    /*************************************************************************
     ************************************************** SET ******************
     * ***********************************************************************
     */
    public void setActivePlayer(String activePlayer) {
        this.activePlayer = activePlayer;
        myTurn = activePlayer.equals(playerID);
        if(ui!=null) ui.notifyNewActivePlayer();
    }
    public void setCommonGoalID(int[] commonGoalID) {
        CommonGoalID = commonGoalID;
    }
    public void setPlayersID(String[] playersID) {
        this.playersID = playersID;
        for(int i=0; i<playersID.length; i++){
            if(this.playerID.equals(playersID[i])){
                this.myTurnNumber = i;
                break;
            }
        }
    }
    public void setPlayersNumber(int playersNumber) {
        this.playersNumber = playersNumber;
        if(ui!=null)ui.receiveNumPlayers(playersNumber);
    }
    public void createMainBoard(Card[][] board){
        this.mainBoard = new MainBoard(board);

    }
    public void createAllClientBoard(ArrayList<Card[][]> boards){
        ArrayList<PlayerBoard> tmpBoards = new ArrayList<>();
        for(Card[][] c : boards){
            tmpBoards.add(new PlayerBoard(c));
        }
        playerBoards = tmpBoards.toArray(new PlayerBoard[0]);
        if(ui!=null) ui.updateAll();
    }

    public void setPrivateGoal(PositionWithColor[] privateGoal) {
        this.privateGoal = privateGoal;
    }

    /*************************************************************************
     ************************************************** others ******************
     * ***********************************************************************
     * *
     * called from the server when a player scored a common goal
     * @param scored
     */
    public void addCommonGoalScored(JsonObject scored){
        ArrayList<JsonObject> tmpScored = new ArrayList<>();
        if(commonGoalScored != null) Collections.addAll(tmpScored, commonGoalScored);
        tmpScored.add(scored);
        commonGoalScored = tmpScored.toArray(new JsonObject[0]);
    }
    /**
     * try to start the game
     * @return false if there is a connection problem (it will print spontaneous an error message), or the player cant start the game
     */
    public boolean startGame(){
        try {
            return connection.startGame(this.playerID);
        } catch (Exception e) {
            this.disconnectError("server can't respond");
            return false;
        }
    }

    /**
     * try to quit the game
     * @return false if there is a connection problem (it will print spontaneous an error message), or the player cant start the game
     */
    public boolean quitGame(){
        try {
            boolean bool = connection.quitGame(this.playerID);
            if(bool)connection.setPlayerAsLobby();
            return bool;
        } catch (Exception e) {
            this.disconnectError("server can't respond");
            return false;
        }
    }

    /**
     * @param column on the player board where
     * @param cards sorted array of cards the player want to take
     * @return false if the move is not valid (it will print a spontaneous error message)
     */
    public boolean takeCard(int column, Position[] cards){
        Position[] tmpCards = new Position[cards.length];
        for (int i = 0; i<cards.length;i++){
            tmpCards[i] = new Position(cards[i].getX(), cards[i].getY());
        }

        if(!checkMainBoardMove(tmpCards)) return false;

        if(!checkPlayerBoardMove(column, cards.length)) return false;

        PositionWithColor[] pos = new  PositionWithColor[cards.length];
        for(int i = 0; i<cards.length; i++){
            pos[i] = new PositionWithColor(cards[i].getX(), cards[i].getY(), mainBoard.getSketch(cards[i].getX(), cards[i].getY()) , mainBoard.getColor(cards[i].getX(), cards[i].getY()));
        }

        try {
            return connection.takeCard(column, pos);
        } catch (Exception e) {
            this.disconnectError("server can't respond");
            return false;
        }
    }

    /**
     * @param pos where want to take the card
     * @return false if the move is not valid (it will print a spontaneous error message)
     */
    public boolean checkMainBoardMove(Position[] pos){
        try {
            mainBoard.validPick(pos);
        } catch (InvalidPickException e) {
            JsonObject err = new JsonObject();
            err.addProperty("errorID", "invalid move");
            err.addProperty("errorMSG", e.getMessage());
            this.errMsg(err);
            return false;
        }
        return true;
    }

    /**
     * @param column where put the card
     * @param nCards how many cards have to insert in the column
     * @return false if the move is not valid (it will print a spontaneous error message)
     */
    private boolean checkPlayerBoardMove(int column,int nCards){
        if(!playerBoards[myTurnNumber].checkSpace(column, nCards)){
            JsonObject err = new JsonObject();
            err.addProperty("errorID", "invalid move");
            err.addProperty("errorMSG", "not enough space on your shelf in that column");
            this.errMsg(err);
            return false;
        }
        return true;
    }
    /**
     * called from server
     * @param playerID on which playerBoard method has to add the cards
     * @param column where have to add cards
     * @param cards element added to the player board
     */
    synchronized public void addCardToPlayerBoard(String playerID, int column,Card[] cards){
        for(int i=0; i<playersID.length; i++){
            if(playersID[i].equals(playerID)){
                playerBoards[i].addCard(column, cards);
            }
        }
        if(ui!=null)ui.updatePlayerBoard(playerID, column, cards);
    }

    /**
     * called from server
     * @param position where have to remove cards
     */
    synchronized public void removeCardFromMainBoard(PositionWithColor[] position){
        mainBoard.removeCard(position);
        if(ui!=null)ui.updateMainBoard(position);
    }

    /**
     * called from server
     * @param points JsonObject {"playerID": pointsValue} for each player in the game
     */
    public void endGameValue(String points){
        JsonObject jsonObject = new Gson().fromJson(points, JsonObject.class);
        if(ui!=null)ui.finalResults(jsonObject);
    }

    /**
     * called from server
     * @param winner  JsonObject {"winnerName": playerID, "points": pointsValue}
     */
    public void receiveWinner(String winner){
        //TODO necessita metodo lato grafico
    }

    /**************************************************************************
     ************************************************** chat ******************
     * ************************************************************************
     * *
     * called from tui, it wil be sending msg as private or as broadcast (if contains --playerID : it will be private)
     * @param msg the String of message
     * @throws PlayerNotFoundException if can't find the playerID or the game is not started yet (only in vase of private msg)
     */
    public void sendMessage(String msg)throws PlayerNotFoundException{
        int index = msg.indexOf(':');
        String marker = msg.substring(0,2);
        if(index == -1 || !marker.equals("--")){
            sendBroadcastMsg(msg);
            return;
        }
        String sender = msg.substring(2, index).replaceAll("\\s+","");
        msg = msg.substring(index+1, msg.length());
        if(msg.charAt(0) == ' ')msg = msg.substring(1);
        sendPrivateMSG(sender, msg);
    }

    /**
     * @param msg String with the message
     */
    public void sendBroadcastMsg(String msg){
        try{
            connection.sendBroadcastMsg(msg, this.playerID);
        } catch (Exception e) {
            this.disconnectError("server can't respond");
        }
    }

    /**
     * called from server where there is a new broadcast message
     * @param msg the message
     * @param sender the player who wrote the message
     */
    public void receiveBroadcastMsg(String msg, String sender){
        if(!sender.equals(this.playerID)) {
            String sout = sender + ": " + msg;
            if(ui!=null) ui.receiveMsg(sout);
            else System.out.println(sout);
        }
    }

    /**
     * @param userID at which player have to send the message
     * @param msg the message
     * @throws PlayerNotFoundException if can't find the playerID or the game is not started yet
     */
    public void sendPrivateMSG(String userID, String msg) throws PlayerNotFoundException {
        ArrayList<String> tmpPlayers = new ArrayList<>();
        if (playersID == null) throw new PlayerNotFoundException("game not started yet");
        Collections.addAll(tmpPlayers, playersID);
        if (!tmpPlayers.contains(userID)) throw new PlayerNotFoundException("player not found");
        try{
            connection.sendPrivateMSG(userID, msg, this.playerID);
        } catch (Exception e) {
            this.disconnectError("server can't respond");
        }
    }

    /**
     * @param userID at which player have to send the message
     * @param msg the message
     * @param sender the player who wrote the message
     */
    public void receivePrivateMSG(String userID, String msg, String sender){
        if(!sender.equals(this.playerID) && Objects.equals(userID, this.playerID)){
            String sout = sender + ": [PRIVATE] " + msg;
            if(ui!=null) ui.receiveMsg(sout);
            else System.out.println(sout);
        }
    }
}
