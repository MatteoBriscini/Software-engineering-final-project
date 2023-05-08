package it.polimi.ingsw.server.Connection;

import com.google.gson.JsonObject;
import it.polimi.ingsw.client.Connection.PlayingPlayerRemoteInterface;
import it.polimi.ingsw.server.Connection.RMI.RMI;
import it.polimi.ingsw.server.Controller;
import it.polimi.ingsw.shared.Cards.Card;
import it.polimi.ingsw.shared.JsonSupportClasses.PositionWithColor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ConnectionControllerManager {

    //private final ArrayList<ConnectionController> interfaces = new ArrayList<>();
    private boolean rmiActive = false;
    private boolean socketActive = false;
    private final Map<String, PlayingPlayerRemoteInterface> clientsRMImap = new HashMap<String, PlayingPlayerRemoteInterface>();  //PlayerID + remote interface
    private final ArrayList<SOCKET.MultiClientSocketGame> clientsSOCKET= new ArrayList<>();
    private RMI rmi;
    private Controller controller;
    private SOCKET socket;
    public ConnectionControllerManager(RMI rmi, SOCKET socket, Controller controller){
        this.rmi = rmi;
        this.socket = socket;
        this.controller=controller;
    }
    public ConnectionControllerManager(){}

    public boolean isRmiActive() {
        return rmiActive;
    }

    public boolean isSocketActive() {
        return socketActive;
    }

    public void addClientRMI(PlayingPlayerRemoteInterface client, String playerID){
        rmiActive = true;
        clientsRMImap.put(playerID, client);
    }
    public boolean removeClientRMI(PlayingPlayerRemoteInterface client, String playerID){
        if(clientsRMImap.get(playerID).equals(client)){
            clientsRMImap.remove(playerID);
            return true;
        }
        return false;
    }
    public Map<String, PlayingPlayerRemoteInterface> getClientsRMImap(){
        return clientsRMImap;
    }
    public void addClientSOCKET(SOCKET.MultiClientSocketGame client){
        socketActive = true;
        clientsSOCKET.add(client);
    }

    /*************************************************************************
     ************************************************** OUT method ***********
     * ***********************************************************************
     */
    public void notifyActivePlayer(String activePlayerID){
        if (clientsRMImap.size() > 0) {
            rmi.notifyActivePlayer(activePlayerID, clientsRMImap, controller);
        }
        if (clientsSOCKET.size() > 0){
            socket.notifyActivePlayer(activePlayerID, clientsSOCKET);
        }
    }
    public void sendPlayerList(String[] players){
        if (clientsRMImap.size() > 0) {
            rmi.sendPlayerList(players, clientsRMImap, controller);
        }
        if (clientsSOCKET.size() > 0){
            socket.sendPlayerList(players, clientsSOCKET);
        }
    }
    public void sendPlayersNUmber(int playersNumber){
        if (clientsRMImap.size() > 0) {
            rmi.sendPlayersNUmber(playersNumber, clientsRMImap, controller);
        }
        if (clientsSOCKET.size() > 0){
            socket.sendPlayersNUmber(playersNumber, clientsSOCKET);
        }
    }
    public void sendMainBoard(Card[][] mainBoard){
        if (clientsRMImap.size() > 0) {
            rmi.sendMainBoard(mainBoard, clientsRMImap, controller);
        }
        if (clientsSOCKET.size() > 0){
            socket.sendMainBoard(mainBoard, clientsSOCKET);
        }
    }
    public void addCardToClientBoard(String playerID, int column, Card[] cards){
        if (clientsRMImap.size() > 0) {
            rmi.addCardToClientBoard(playerID,column,cards, clientsRMImap, controller);
        }
        if (clientsSOCKET.size() > 0){
            socket.addCardToClientBoard(playerID,column,cards, clientsSOCKET);
        }
    }
    public void dellCardFromMainBoard(PositionWithColor[] cards){
        if (clientsRMImap.size() > 0) {
            rmi.dellCardFromMainBoard(cards, clientsRMImap, controller);
        }
        if (clientsSOCKET.size() > 0){
            socket.dellCardFromMainBoard(cards, clientsSOCKET);
        }
    }
    public void sendAllPlayerBoard(ArrayList<Card[][]> playerBoards){
        if (clientsRMImap.size() > 0) {
            rmi.sendAllPlayerBoard(playerBoards, clientsRMImap, controller);
        }
        if (clientsSOCKET.size() > 0){
            socket.sendAllPlayerBoard(playerBoards, clientsSOCKET);
        }
    }
    public void sendAllCommonGoal(int[] commonGoalID){
        if (clientsRMImap.size() > 0) {
            rmi.sendAllCommonGoal(commonGoalID, clientsRMImap, controller);
        }
        if (clientsSOCKET.size() > 0){
            socket.sendAllCommonGoal(commonGoalID, clientsSOCKET);
        }
    }
    public void sendPrivateGoal(PositionWithColor[] cards,String playerID){
        if (clientsRMImap.size() > 0) {
            rmi.sendPrivateGoal(cards,playerID, clientsRMImap, controller);
        }
        if (clientsSOCKET.size() > 0){
            socket.sendPrivateGoal(cards,playerID, clientsSOCKET);
        }
    }

    public void sendEndGamePoint(JsonObject points){
        if (clientsRMImap.size() > 0) {
            rmi.sendEndGamePoint(points, clientsRMImap, controller);
        }
        if (clientsSOCKET.size() > 0){
            socket.sendEndGamePoint(points, clientsSOCKET);
        }
    }

    public void sendWinner(JsonObject winner){
        if (clientsRMImap.size() > 0) {
            rmi.sendWinner(winner, clientsRMImap, controller);
        }
        if (clientsSOCKET.size() > 0){
            socket.sendWinner(winner, clientsSOCKET);
        }
    }

    public void sendLastCommonScored(JsonObject scored){
        if (clientsRMImap.size() > 0) {
            rmi.sendLastCommonScored(scored, clientsRMImap, controller);
        }
        if (clientsSOCKET.size() > 0){
            socket.sendLastCommonScored(scored, clientsSOCKET);
        }
    }

    public void sendError(JsonObject error, String playerID){
        if (clientsRMImap.size() > 0) {
            rmi.sendError(error, playerID, clientsRMImap, controller);
        }
        if (clientsSOCKET.size() > 0){
            socket.sendError(error, playerID, clientsSOCKET);
        }
    }

    public void forceClientDisconnection(){
        if (clientsRMImap.size() > 0) {
            rmi.forceClientDisconnection(clientsRMImap, controller);
        }
        if (clientsSOCKET.size() > 0){
            socket.forceClientDisconnection(clientsSOCKET);
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
    public void broadcastMsg(String msg, String sender){
        if (clientsRMImap.size() > 0) {
            rmi.sendBroadcastMsg(msg, sender, clientsRMImap, controller);
        }
        if (clientsSOCKET.size() > 0){
            socket.sendBroadcastMsg(msg, sender, clientsSOCKET);
        }
    }
    /**
     * send a message in private to only one client
     * @param userID id of the player the message is for
     * @param msg message to send
     * @param sender name of the player who sends the message
     */
    public void privateMSG(String userID, String msg, String sender){
        if (clientsRMImap.size() > 0) {
            rmi.sendPrivateMSG(userID, msg, sender, clientsRMImap, controller);
        }
        if (clientsSOCKET.size() > 0){
            socket.sendPrivateMSG(userID, msg, sender, clientsSOCKET);
        }
    }
}
