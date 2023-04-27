package it.polimi.ingsw.Server.Connection;

import com.google.gson.Gson;
import com.google.gson.*;
import it.polimi.ingsw.Server.Controller;
import it.polimi.ingsw.Shared.Cards.Card;
import it.polimi.ingsw.Shared.JsonSupportClasses.PositionWithColor;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Arrays;

public abstract class ConnectionController {
    protected Controller controller;
    protected int PORT;

    public ConnectionController (Controller controller, int port){
        this.controller = controller;
        this.PORT = port;
    }

    public abstract void connection();

    public int getPORT(){
        return this.PORT;
    }

    /*************************************************************************
     ************************************************** OUT method ***********
     * ***********************************************************************
     */

    public abstract void notifyActivePlayer(String activePlayerID);
    public abstract void sendPlayerList(String[] players);
    public abstract void sendPlayersNUmber(int playersNumber);
    public abstract void sendMainBoard(Card[][] mainBoard);
    public abstract void addCardToClientBoard(String playerID, int column, Card[] cards);
    public abstract void dellCardFromMainBoard(PositionWithColor[] cards);
    public abstract void sendAllPlayerBoard(ArrayList<Card[][]> playerBoards);
    public abstract void sendAllCommonGoal(int[] commonGoalID);
    public abstract void sendPrivateGoal(PositionWithColor[] cards,String playerID);
    public abstract void sendEndGamePoint(JsonObject points);
    public abstract void sendWinner(JsonObject winner);
    public abstract void sendLastCommonScored(JsonObject scored);
    public abstract void sendError(JsonObject error, String playerID);
    public abstract void forceClientDisconnection();
    public abstract void sendBroadcastMsg(String msg, String sender);
    public abstract void sendPrivateMSG(String userID, String msg, String sender);

    /************************************************************************
     ************************************************** IN method ***********
     * ***********************************************************************
     * method to start game, only the creator of the game can start the game in every moment
     * @param playerID ID of the player call the method
     * @return true if player can start the game false in all other cases
     * @throws RemoteException if server is offline
     */
    public boolean startGame(String playerID){
        return controller.startGame(playerID);
    }
    /**
     * method to take a card from mainBoard, only the current player can take a card
     * @param column in which column the player want to insert the cards
     * @param cards json string, contains the cards the player want to pick
     * @param playerID ID of the player call the method
     * @return if the player who call the method is the current and if the move is valid, false in all other cases
     * @throws RemoteException if server is offline
     */
    public boolean takeCard (int column, String cards , String playerID){
        PositionWithColor[] cardsArray = new Gson().fromJson(cards, PositionWithColor[].class);
        boolean bool = controller.takeCard(column, cardsArray, playerID);
        return bool;
    }

    /**************************************************************************
     ************************************************** chat ******************
     * ************************************************************************
     */
    public void receiveBroadcastMsg(String msg, String sender){
        controller.broadcastMsg(msg,sender);
    }
    public void receivePrivateMSG(String userID, String msg, String sender){
        controller.privateMSG(userID, msg, sender);
    }
}
