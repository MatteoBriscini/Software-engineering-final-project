package it.polimi.ingsw.Client.Connection;

import com.google.gson.Gson;
import com.google.gson.*;
import it.polimi.ingsw.Server.Connection.RMI.ControllerRemoteInterface;
import it.polimi.ingsw.Shared.Cards.Card;
import it.polimi.ingsw.Shared.JsonSupportClasses.PositionWithColor;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.Arrays;

public class PlayingPlayerRMI extends PlayingPlayerConnectionManager implements PlayingPlayerRemoteInterface{
    static ControllerRemoteInterface stub;

    private final String playerID;

    public PlayingPlayerRMI(int PORT, String serverIP, String playerID) throws RemoteException{
        this.playerID = playerID;
        this.connection(PORT, serverIP, playerID);
    }
    public void connection(int PORT, String serverIP, String name){
        try {
            Registry registry = LocateRegistry.getRegistry(serverIP , PORT);
            stub = (ControllerRemoteInterface) registry.lookup("ControllerRemoteInterface");


        } catch (Exception e) {
            System.err.println("Client exception: " + e.toString());
            e.printStackTrace();
        }


        //send remote ref to server
        try {
            stub.joinRMIControllerConnection(this, playerID);
        } catch (RemoteException e) {

            throw new RuntimeException(e);
        }

        try {
            startGame(playerID);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    /*************************************************************************
     ************************************************** OUT method ***********
     * ***********************************************************************
     */
    public boolean quitGame(String  playerID) throws RemoteException{
        return stub.quitRMIControllerConnection(this, playerID);
    }
    public static boolean startGame(String  playerID) throws RemoteException {
        return stub.startGame( playerID);
    }
    public boolean takeCard(int column, PositionWithColor[] cards) throws RemoteException {
        Gson gson = new Gson();
        JsonArray jsonArray = new Gson().toJsonTree(cards).getAsJsonArray();
        return stub.takeCard(column,jsonArray.toString(), playerID);
    }
    /************************************************************************
     ************************************************** IN method ***********
     * ***********************************************************************
     * *
     * @param activePlayerID PLayerID of the player have to play now
     * @throws RemoteException if the client is offline
     */
    @Override
    public void notifyActivePlayer(String activePlayerID) {

    }
    /**
     * @param playersID all players ID
     * @throws RemoteException if the client is offline
     */
    @Override
    public void receivePlayerList(String[] playersID) throws RemoteException {

    }
    /**
     * @param playersNumber total amount of players in the game
     * @throws RemoteException if the client is offline
     */
    @Override
    public void receivePlayersNumber(int playersNumber) throws RemoteException {

    }
    /**
     * @param mainBoard json array represent the main board
     * @throws RemoteException if the client is offline
     */
    @Override
    public void receiveMainBoard(String mainBoard) throws RemoteException { //Card[][]

    }
    /**
     * @param playerID name of the owner of the board we have to add cards
     * @param column on the board where we have to add cards
     * @param cards card array to add at the board
     * @throws RemoteException if the client is offline
     */
    @Override
    public void addCardToPlayerBoard(String playerID, int column,String cards) throws RemoteException {//Card[] (cards)

    }
    /**
     * @param cards position with color array, position have to remove from the mainBoard;
     * @throws RemoteException if the client is offline
     */
    @Override
    public void dellCardFromMainBoard(String cards) throws RemoteException {//PositionWithColor[]

    }
    /**
     * @param playerBoards all players bord in the game (used in game start phase and when a player reconnect after a crash)
     * @throws RemoteException if the client is offline
     */
    @Override
    public void receiveAllPlayerBoard(String playerBoards) throws RemoteException {

    }
    /**
     * @param commonGoalID number unique identify the common goal
     * @throws RemoteException if the client is offline
     */
    @Override
    public void receiveAllCommonGoal(int[] commonGoalID)  throws RemoteException {

    }
    /**
     * @param cards PositionWithColor[] settings to recreate the image for private goal
     * @param playerID player's name for the private goal
     * @throws RemoteException if the client is offline
     */
    @Override
    public void receivePrivateGoal(String cards,String playerID) throws RemoteException {//PositionWithColor[] (cards)

    }
    /**
     * receive points of all the players in the game
     * @param points map playerID + point for each game
     * @throws RemoteException if the players is offline
     */
    @Override
    public void endGameValue(String points) throws RemoteException {

    }

    @Override
    public void receiveWinner(String winner) throws RemoteException {

    }


}
