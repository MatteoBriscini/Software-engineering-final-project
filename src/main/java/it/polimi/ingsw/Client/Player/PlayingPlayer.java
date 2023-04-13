package it.polimi.ingsw.Client.Player;

import com.google.gson.JsonObject;
import it.polimi.ingsw.Client.Connection.PlayingPlayerConnectionManager;
import it.polimi.ingsw.Client.Connection.PlayingPlayerRMI;
import it.polimi.ingsw.Client.Game.MainBoard;
import it.polimi.ingsw.Client.Game.PlayerBoard;
import it.polimi.ingsw.Shared.Cards.Card;
import it.polimi.ingsw.Shared.Connection.ConnectionType;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collections;

public class PlayingPlayer extends Player{
    PlayingPlayerConnectionManager connectionManager;
    MainBoard mainBoard;
    PlayerBoard[] playerBoards;
    String[] playersID;
    String activePlayer;
    boolean myTurn;
    int playersNumber;
    int[]CommonGoalID;
    JsonObject[] commonGoalScored;
    /**
     * @param connectionType rmi or socket
     * @param port for the specific game on the server
     * @param serverIP to reach the server
     * @throws RemoteException if the server do not response (wrong port or ip)
     */
    public PlayingPlayer(ConnectionType connectionType, int port, String serverIP) throws RemoteException {
        switch (connectionType){
            case RMI:
                connectionManager = new PlayingPlayerRMI(port, serverIP, playerID, this);
                break;
            case SOCKET: //TODO
                break;
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
    public void addCommonGoalScored(JsonObject scored){
        ArrayList<JsonObject> tmpScored = new ArrayList<>();
        Collections.addAll(tmpScored, commonGoalScored);
        tmpScored.add(scored);
        commonGoalScored = tmpScored.toArray(new JsonObject[0]);
    }
}
