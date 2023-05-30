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
    public void removeClientSOCKET(SOCKET.MultiClientSocketGame client){
        clientsSOCKET.remove(client);
    }

    /*************************************************************************
     ************************************************** OUT method ***********
     * ***********************************************************************
     */
    public void notifyActivePlayer(String activePlayerID){
        if (clientsRMImap.size() > 0){
            /*
            try {
                method.put("notifyActivePlayer");
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

             */
            rmi.notifyActivePlayer(activePlayerID, clientsRMImap, controller);
        }
        if (clientsSOCKET.size() > 0){
            socket.notifyActivePlayer(activePlayerID, clientsSOCKET);
        }
    }
    public void sendPlayerList(String[] players){
        if (clientsRMImap.size() > 0){
            /*
            try {
                method.put("sendPlayerList");throw new RuntimeException(e)
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

             */
            rmi.sendPlayerList(players, clientsRMImap, controller);
        }
        if (clientsSOCKET.size() > 0){
            socket.sendPlayerList(players, clientsSOCKET);
        }
    }
    public void sendPlayersNUmber(int playersNumber){
        if (clientsRMImap.size() > 0){
            /*
            try {
                method.put("sendPlayersNUmber");
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

             */
            rmi.sendPlayersNUmber(playersNumber, clientsRMImap, controller);
        }
        if (clientsSOCKET.size() > 0){
            socket.sendPlayersNUmber(playersNumber, clientsSOCKET);
        }
    }
    public void sendMainBoard(Card[][] mainBoard){
        if (clientsRMImap.size() > 0){
            /*
            try {
                method.put("sendMainBoard");
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

             */
            rmi.sendMainBoard(mainBoard, clientsRMImap, controller);
        }
        if (clientsSOCKET.size() > 0){
            socket.sendMainBoard(mainBoard, clientsSOCKET);
        }
    }
    public void addCardToClientBoard(String playerID, int column, Card[] cards){
        if (clientsRMImap.size() > 0) {
            /*
            try {
                method.put("addCardToClientBoard");
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

             */
            rmi.addCardToClientBoard(playerID,column,cards, clientsRMImap, controller);
        }
        if (clientsSOCKET.size() > 0){
            socket.addCardToClientBoard(playerID,column,cards, clientsSOCKET);
        }
    }
    public void dellCardFromMainBoard(PositionWithColor[] cards){
        if (clientsRMImap.size() > 0) {
            /*
            try {
                method.put("dellCardFromMainBoard");
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

             */
            rmi.dellCardFromMainBoard(cards, clientsRMImap, controller);
        }
        if (clientsSOCKET.size() > 0){
            socket.dellCardFromMainBoard(cards, clientsSOCKET);
        }
    }
    public void sendAllPlayerBoard(ArrayList<Card[][]> playerBoards){
        if (clientsRMImap.size() > 0) {
            /*
            try {
                method.put("dellCardFromMainBoard");
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

             */
            rmi.sendAllPlayerBoard(playerBoards, clientsRMImap, controller);
        }
        if (clientsSOCKET.size() > 0){
            socket.sendAllPlayerBoard(playerBoards, clientsSOCKET);
        }
    }
    public void sendAllCommonGoal(int[] commonGoalID){
        if (clientsRMImap.size() > 0) {
            /*
            try {
                method.put("sendAllCommonGoal");
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

             */
            rmi.sendAllCommonGoal(commonGoalID, clientsRMImap, controller);
        }
        if (clientsSOCKET.size() > 0){
            socket.sendAllCommonGoal(commonGoalID, clientsSOCKET);
        }
    }
    public void sendPrivateGoal(PositionWithColor[] cards,String playerID){
        if (clientsRMImap.size() > 0) {
            /*
            try {
                method.put("sendPrivateGoal");
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

             */
            rmi.sendPrivateGoal(cards,playerID, clientsRMImap, controller);
        }
        if (clientsSOCKET.size() > 0){
            socket.sendPrivateGoal(cards,playerID, clientsSOCKET);
        }
    }

    public void sendEndGamePoint(JsonObject points){
        if (clientsRMImap.size() > 0) {
            /*
            try {
                method.put("sendEndGamePoint");
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

             */
            rmi.sendEndGamePoint(points, clientsRMImap, controller);
        }
        if (clientsSOCKET.size() > 0){
            socket.sendEndGamePoint(points, clientsSOCKET);
        }
    }

    public void sendWinner(JsonObject winner){
        if (clientsRMImap.size() > 0) {
            /*
            try {
                method.put("sendWinner");
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

             */
            rmi.sendWinner(winner, clientsRMImap, controller);
        }
        if (clientsSOCKET.size() > 0){
            socket.sendWinner(winner, clientsSOCKET);
        }
    }

    public void sendLastCommonScored(JsonObject scored){
        if (clientsRMImap.size() > 0) {
            /*
            try {
                method.put("sendLastCommonScored");
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

             */
            rmi.sendLastCommonScored(scored, clientsRMImap, controller);
        }
        if (clientsSOCKET.size() > 0){
            socket.sendLastCommonScored(scored, clientsSOCKET);
        }
    }

    public void sendError(JsonObject error, String playerID){
        if (clientsRMImap.size() > 0) {
            /*
            try {
                method.put("sendError");
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

             */
            rmi.sendError(error, playerID, clientsRMImap, controller);
        }
        if (clientsSOCKET.size() > 0){
            socket.sendError(error, playerID, clientsSOCKET);
        }
    }

    public void forceClientDisconnection(){
        if (clientsRMImap.size() > 0) {
            /*
            try {
                method.put("forceClientDisconnection");
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

             */
            rmi.forceClientDisconnection(clientsRMImap, controller);
        }
        if (clientsSOCKET.size() > 0){
            socket.forceClientDisconnection(clientsSOCKET);
        }
    }

    /*
    private class blockedQueueHandler implements Runnable{


        public void run(){
            String methodToCall;
            while(true){
                try {
                    methodToCall = method.take();
                } catch (InterruptedException e) {
                    return;
                }
                switch(methodToCall){
                    case "":


                }
            }
        }

    }

     */

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
