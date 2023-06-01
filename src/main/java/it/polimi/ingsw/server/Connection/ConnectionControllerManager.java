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
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class ConnectionControllerManager {

    //private final ArrayList<ConnectionController> interfaces = new ArrayList<>();
    private BlockingQueue<String> method = new LinkedBlockingQueue<>();
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

    /**
     * @return true if there is rmi client in this game
     */
    public boolean isRmiActive() {
        return rmiActive;
    }

    /**
     * @return true if there is socket client in this game
     */
    public boolean isSocketActive() {
        return socketActive;
    }
    /**
     * to add a client ref to this controller (rmi)
     * @param client remote interface of the client
     * @param playerID name of the player
     */
    public void addClientRMI(PlayingPlayerRemoteInterface client, String playerID){
        rmiActive = true;
        clientsRMImap.put(playerID, client);
    }
    /**
     * to remove a client ref to this controller (rmi)
     * @param client remote interface of the client
     * @param playerID name of the player
     * @return false if the client isn't in the game
     */
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
    /**
     * to add a client ref to this controller (socket)
     * @param client thread ref who is a manager the client request
     */
    public void addClientSOCKET(SOCKET.MultiClientSocketGame client){
        socketActive = true;
        clientsSOCKET.add(client);
    }
    /**
     * @param client thread ref who is a manager the client request
     */
    public void removeClientSOCKET(SOCKET.MultiClientSocketGame client){
        clientsSOCKET.remove(client);
    }

    /*************************************************************************
     ************************************************** OUT method ***********
     * ***********************************************************************
     *
     * @param activePlayerID the id of the player have to play now
     */
    public void notifyActivePlayer(String activePlayerID){
        if (clientsRMImap.size() > 0){
            rmi.notifyActivePlayer(activePlayerID, clientsRMImap, controller);
        }
        if (clientsSOCKET.size() > 0){
            socket.notifyActivePlayer(activePlayerID, clientsSOCKET);
        }
    }

    /**
     * used when create or recreate client data
     * @param players the list of players in the game when it starts
     */
    public void sendPlayerList(String[] players){
        if (clientsRMImap.size() > 0){
            rmi.sendPlayerList(players, clientsRMImap, controller);
        }
        if (clientsSOCKET.size() > 0){
            socket.sendPlayerList(players, clientsSOCKET);
        }
    }

    /**
     * used in waiting room to communicate to the game creator the number of player actual in the game
     * @param playersNumber number of player actual in the game
     */
    public void sendPlayersNUmber(int playersNumber){
        if (clientsRMImap.size() > 0){
            rmi.sendPlayersNUmber(playersNumber, clientsRMImap, controller);
        }
        if (clientsSOCKET.size() > 0){
            socket.sendPlayersNUmber(playersNumber, clientsSOCKET);
        }
    }

    /**
     * used in create or recreate data clients, it will be sent in broadcast
     * @param mainBoard cardMatrix represent the mainBoard
     */
    public void sendMainBoard(Card[][] mainBoard){
        if (clientsRMImap.size() > 0){
            rmi.sendMainBoard(mainBoard, clientsRMImap, controller);
        }
        if (clientsSOCKET.size() > 0){
            socket.sendMainBoard(mainBoard, clientsSOCKET);
        }
    }

    /**
     * update playerBoard by delta, it will be sent in broadcast
     * @param playerID identify witch playerBoard
     * @param column position on the playerBoard
     * @param cards the delta
     */
    public void addCardToClientBoard(String playerID, int column, Card[] cards){
        if (clientsRMImap.size() > 0) {
            rmi.addCardToClientBoard(playerID,column,cards, clientsRMImap, controller);
        }
        if (clientsSOCKET.size() > 0){
            socket.addCardToClientBoard(playerID,column,cards, clientsSOCKET);
        }
    }

    /**
     * update mainBoard by delta, it will be sent in broadcast
     * @param cards the delta
     */
    public void dellCardFromMainBoard(PositionWithColor[] cards){
        if (clientsRMImap.size() > 0) {
            rmi.dellCardFromMainBoard(cards, clientsRMImap, controller);
        }
        if (clientsSOCKET.size() > 0){
            socket.dellCardFromMainBoard(cards, clientsSOCKET);
        }
    }
    /**
     * used in create or recreate data clients, it will be sent in broadcast
     * @param playerBoards arrayList with card matrix who represent all players board of thi specific game
     */
    public void sendAllPlayerBoard(ArrayList<Card[][]> playerBoards){
        if (clientsRMImap.size() > 0) {
            rmi.sendAllPlayerBoard(playerBoards, clientsRMImap, controller);
        }
        if (clientsSOCKET.size() > 0){
            socket.sendAllPlayerBoard(playerBoards, clientsSOCKET);
        }
    }

    /**
     * it will be sent in broadcast
     * @param commonGoalID array with id of all common goals
     */
    public void sendAllCommonGoal(int[] commonGoalID){
        if (clientsRMImap.size() > 0) {
            rmi.sendAllCommonGoal(commonGoalID, clientsRMImap, controller);
        }
        if (clientsSOCKET.size() > 0){
            socket.sendAllCommonGoal(commonGoalID, clientsSOCKET);
        }
    }

    /**
     * @param cards position on payer board and color need to respect to achieve private goal
     * @param playerID recipient of th message
     */
    public void sendPrivateGoal(PositionWithColor[] cards,String playerID){
        if (clientsRMImap.size() > 0) {
            rmi.sendPrivateGoal(cards,playerID, clientsRMImap, controller);
        }
        if (clientsSOCKET.size() > 0){
            socket.sendPrivateGoal(cards,playerID, clientsSOCKET);
        }
    }

    /**
     * @param points jsonObject with all points for all the player in the game, it will be sent in broadcast
     */
    public void sendEndGamePoint(JsonObject points){
        if (clientsRMImap.size() > 0) {
            rmi.sendEndGamePoint(points, clientsRMImap, controller);
        }
        if (clientsSOCKET.size() > 0){
            socket.sendEndGamePoint(points, clientsSOCKET);
        }
    }

    /**
     * @param winner jsonObject with point and name of the winner it will be sent in broadcast
     */
    public void sendWinner(JsonObject winner){
        if (clientsRMImap.size() > 0) {
            rmi.sendWinner(winner, clientsRMImap, controller);
        }
        if (clientsSOCKET.size() > 0){
            socket.sendWinner(winner, clientsSOCKET);
        }
    }

    /**
     * @param scored jsonObject with all point scored by all the player in the game by common goal, it will be sent in broadcast
     */
    public void sendLastCommonScored(JsonObject scored){
        if (clientsRMImap.size() > 0) {
            rmi.sendLastCommonScored(scored, clientsRMImap, controller);
        }
        if (clientsSOCKET.size() > 0){
            socket.sendLastCommonScored(scored, clientsSOCKET);
        }
    }

    /**
     * @param error jsonObject with error id and error code
     * @param playerID recipient of the error msg
     */
    public void sendError(JsonObject error, String playerID){
        if (clientsRMImap.size() > 0) {
            rmi.sendError(error, playerID, clientsRMImap, controller);
        }
        if (clientsSOCKET.size() > 0){
            socket.sendError(error, playerID, clientsSOCKET);
        }
    }

    /**
     * server can force the return to the lobby on all client in one game, ending the game
     */
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
