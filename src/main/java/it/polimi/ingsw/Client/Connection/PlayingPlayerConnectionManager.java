package it.polimi.ingsw.Client.Connection;

import com.google.gson.Gson;
import it.polimi.ingsw.Client.Player.PlayingPlayer;
import it.polimi.ingsw.Shared.JsonSupportClasses.PositionWithColor;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public abstract class PlayingPlayerConnectionManager extends UnicastRemoteObject {
    PlayingPlayer playingPlayer;

    protected PlayingPlayerConnectionManager(PlayingPlayer playingPlayer) throws RemoteException {
        this.playingPlayer=playingPlayer;
    }
    /*************************************************************************
     ************************************************** OUT method ***********
     * ***********************************************************************
     */
    public abstract void connection(int PORT, String serverIP);
    public abstract boolean takeCard(int column, PositionWithColor[] cards) throws RemoteException;

    /************************************************************************
     ************************************************** IN method ***********
     * ***********************************************************************
     * *
     * @param activePlayerID PLayerID of the player have to play now
     */
    public void notifyActivePlayer(String activePlayerID){
        playingPlayer.setActivePlayer(activePlayerID);
    }
    /**
     * @param playersID all players ID
     */
    public void receivePlayerList(String[] playersID){
        playingPlayer.setPlayersID(playersID);
    }
    /**
     * @param playersNumber total amount of players in the game
     */
    public void receivePlayersNumber(int playersNumber){
        playingPlayer.setPlayersNumber(playersNumber);
    }
    /**
     * @param mainBoard json array represent the main board
     */
    public void receiveMainBoard(String mainBoard){ //Card[][]
        //TODO
    }
    /**
     * @param playerID name of the owner of the board we have to add cards
     * @param column on the board where we have to add cards
     * @param cards card array to add at the board
     */
    public void addCardToPlayerBoard(String playerID, int column,String cards){//Card[] (cards)
        //TODO
    }
    /**
     * @param cards position with color array, position have to remove from the mainBoard;
     */
    public void dellCardFromMainBoard(String cards){//PositionWithColor[]
        //TODO
    }
    /**
     * @param playerBoards all players bord in the game (used in game start phase and when a player reconnect after a crash)
     */
    public void receiveAllPlayerBoard(String playerBoards){//Card[][]
        //TODO
    }
    /**
     * @param commonGoalID number unique identify the common goal
     */
    public void receiveAllCommonGoal(int[] commonGoalID){
        playingPlayer.setCommonGoalID(commonGoalID);
    }
    /**
     * @param cards PositionWithColor[] settings to recreate the image for private goal
     * @param playerID player's name for the private goal
     */
    public void receivePrivateGoal(String cards,String playerID){//PositionWithColor[] (cards)
        if(playerID.equals(playingPlayer.getPlayerID())){
            //TODO
        }

    }
    /**
     * receive points of all the players in the game
     * @param points map playerID + point for each game
     */
    public void endGameValue(String points){
        //TODO
    }
    /**
     * @param winner json object with playerID and points of the winner
     */
    public void receiveWinner(String winner){
        //TODO
    }
    /**
     * @param scored json object with point for each client
     */
    public void receiveLastCommonScored(String scored){
        //TODO
    }
    /**
     * @param error json object with errorID && errorMSG
     * @param playerID name of the player the message is intended for
     */
    public void errorMSG(String error, String playerID){
        //TODO
    }

}
