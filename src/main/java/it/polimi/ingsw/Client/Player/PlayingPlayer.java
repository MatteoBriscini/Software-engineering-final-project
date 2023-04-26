package it.polimi.ingsw.Client.Player;

import com.google.gson.JsonObject;
import it.polimi.ingsw.Client.ClientMain;
import it.polimi.ingsw.Client.Connection.PlayingPlayerConnectionManager;
import it.polimi.ingsw.Client.Connection.PlayingPlayerRMI;
import it.polimi.ingsw.Client.Connection.PlayingPlayerSOCKET;
import it.polimi.ingsw.Client.Exceptions.InvalidPickException;
import it.polimi.ingsw.Client.Game.MainBoard;
import it.polimi.ingsw.Client.Game.PlayerBoard;
import it.polimi.ingsw.Shared.Cards.Card;
import it.polimi.ingsw.Shared.Connection.ConnectionType;
import it.polimi.ingsw.Shared.JsonSupportClasses.Position;
import it.polimi.ingsw.Shared.JsonSupportClasses.PositionWithColor;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class PlayingPlayer extends Player{
    private PlayingPlayerConnectionManager connectionManager;
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
     * @param connectionType rmi or socket
     * @param port for the specific game on the server
     * @param serverIP to reach the server
     * @throws RemoteException if the server do not response (wrong port or ip)
     */
    public PlayingPlayer(String playerID, String pwd, ClientMain clientMain, ConnectionType connectionType, int port, String serverIP) throws RemoteException {
        super(playerID, pwd, clientMain);
        switch (connectionType){
            case RMI:
                try {
                    connectionManager = new PlayingPlayerRMI(port, serverIP, playerID, this);
                } catch (Exception e) {
                    this.disconnectError("invalid connection config received from server");
                    return;
                }
                break;
            case SOCKET:
                try {
                    connectionManager = new PlayingPlayerSOCKET(port, serverIP, playerID, this);
                } catch (Exception e) {
                    System.out.println(e.toString());
                    this.disconnectError("invalid connection config received from server");
                    return;
                }
                break;
            case DEBUG: return;
        }
    }

    /*************************************************************************
     ************************************************** GET ******************
     * ***********************************************************************
    */
    public MainBoard getMainBoard() {
        return mainBoard;
    }
    public int getPlayersNumber() {
        return playersNumber;
    }
    public PlayerBoard getPlayerBoard(String playerID) {
        int i;
        for (i= 0; i<playersID.length; i++){
            if (playersID[i].equals(playerID)) break;
        }
        return playerBoards[i];
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
    }

    public void setPrivateGoal(PositionWithColor[] privateGoal) {
        this.privateGoal = privateGoal;
    }

    /*************************************************************************
     ************************************************** others ******************
     * ***********************************************************************
     */
    public void addCommonGoalScored(JsonObject scored){
        ArrayList<JsonObject> tmpScored = new ArrayList<>();
        if(commonGoalScored != null) Collections.addAll(tmpScored, commonGoalScored);
        tmpScored.add(scored);
        commonGoalScored = tmpScored.toArray(new JsonObject[0]);
    }
    public boolean startGame(){
        try {
            return connectionManager.startGame(this.playerID);
        } catch (Exception e) {
            this.disconnectError("server don't respond");
            return false;
        }
    }

    public boolean quitGame(){
        try {
            return connectionManager.quitGame(this.playerID);
            //TODO il player deve tornare allo stato di lobby
        } catch (Exception e) {
            System.out.println(e.toString());
            this.disconnectError("server don't respond");
            return false;
        }
    }

    public boolean takeCard(int column, Position[] cards){

        Position[] tmpCards = new Position[cards.length];
        for (int i = 0; i<cards.length;i++){
            tmpCards[i] = new Position(cards[i].getX(), cards[i].getY());
        }
        try {
            mainBoard.validPick(tmpCards);
        } catch (InvalidPickException e) {
            JsonObject err = new JsonObject();
            err.addProperty("errorID", "invalid move");
            err.addProperty("errorMSG", e.toString());
            this.errMsg(err);
            return false;
        }

        if(!playerBoards[myTurnNumber].checkSpace(column, cards.length)){
            JsonObject err = new JsonObject();
            err.addProperty("errorID", "invalid move");
            err.addProperty("errorMSG", "not enough space on your shelf");
            this.errMsg(err);
            return false;
        }

        PositionWithColor[] pos = new  PositionWithColor[cards.length];
        for(int i = 0; i<cards.length; i++){
            pos[i] = new PositionWithColor(cards[i].getX(), cards[i].getY(), mainBoard.getSketch(cards[i].getX(), cards[i].getY()) , mainBoard.getColor(cards[i].getX(), cards[i].getY()));
        }

        try {
            return connectionManager.takeCard(column, pos);
        } catch (Exception e) {
            this.disconnectError("server don't respond");
            return false;
        }
    }

    public void addCardToPlayerBoard(String playerID, int column,Card[] cards){
        for(int i=0; i<playersID.length; i++){
            if(playersID[i].equals(playerID)){
                //TODO addCardToPlayerBoard
            }
        }
    }

    public void removeCardFromMainBoard(PositionWithColor[] position){
        mainBoard.removeCard(position);
    }

    public void endGameValue(String points){
        //TODO necessita metodo lato grafico
    }
    public void receiveWinner(String winner){
        //TODO necessita metodo lato grafico
    }
    public void disconnectError(String msg){
        JsonObject err = new JsonObject();
        err.addProperty("errorID", "connection error");
        err.addProperty("errorMSG", msg);
        //TODO il player deve tornare allo stato di lobby
        this.errMsg(err);
    }


}
