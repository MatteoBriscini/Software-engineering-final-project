package it.polimi.ingsw.Server.Connection;

import com.google.gson.JsonObject;
import it.polimi.ingsw.Server.Connection.RMI.ControllerRMI;
import it.polimi.ingsw.Server.Controller;
import it.polimi.ingsw.Server.Exceptions.ConnectionControllerManagerException;
import it.polimi.ingsw.Shared.Cards.Card;
import it.polimi.ingsw.Shared.JsonSupportClasses.PositionWithColor;

import java.util.ArrayList;

public class ConnectionControllerManager {

    private final ArrayList<ConnectionController> interfaces = new ArrayList<>();
    boolean rmiActive = false;
    boolean socketActive = false;
    boolean debug = false;

    /**
     * create new connection class for controller when necessary
     * @param PORT available port
     * @param connectionType rmi or socket
     * @param controller game reference
     * @return true if the new port will be used false in all other case
     * @throws ConnectionControllerManagerException if connection type has an invalid parameters
     */
    public boolean addClient(int PORT, String connectionType, Controller controller) throws ConnectionControllerManagerException {
        switch (connectionType){
            case "RMI":
                if(!rmiActive) {
                    rmiActive = true;
                    interfaces.add(new ControllerRMI(controller, PORT));
                    return true;
                }
                break;
            case "SOCKET":
                if(!socketActive) {
                    socketActive = true;
                    interfaces.add(new ControllerSOCKET(controller, PORT));
                    return true;
                }
                break;
            default: throw new ConnectionControllerManagerException("invalid connectionType (use: RMI or SOCKET)");
        }
        return false;
    }

    /*************************************************************************
     ************************************************** OUT method ***********
     * ***********************************************************************
     */
    public void notifyActivePlayer(String activePlayerID){
        for (ConnectionController c: interfaces) {
            c.notifyActivePlayer(activePlayerID);
        }
    }
    public void sendPlayerList(String[] players){
        for (ConnectionController c: interfaces) {
            c.sendPlayerList(players);
        }
    }
    public void sendPlayersNUmber(int playersNumber){
        for (ConnectionController c: interfaces) {
            c.sendPlayersNUmber(playersNumber);
        }
    }
    public void sendMainBoard(Card[][] mainBoard){
        for (ConnectionController c: interfaces) {
            c.sendMainBoard(mainBoard);
        }
    }
    public void addCardToClientBoard(String playerID, int column, Card[] cards){
        for (ConnectionController c: interfaces) {
            c.addCardToClientBoard(playerID,column,cards);
        }
    }
    public void dellCardFromMainBoard(PositionWithColor[] cards){
        for (ConnectionController c: interfaces) {
            c.dellCardFromMainBoard(cards);
        }
    }
    public void sendAllPlayerBoard(ArrayList<Card[][]> playerBoards){
        for (ConnectionController c: interfaces) {
            c.sendAllPlayerBoard(playerBoards);
        }
    }
    public void sendAllCommonGoal(int[] commonGoalID){
        for (ConnectionController c: interfaces) {
            c.sendAllCommonGoal(commonGoalID);
        }
    }
    public void sendPrivateGoal(PositionWithColor[] cards,String playerID){
        for (ConnectionController c: interfaces) {
            c.sendPrivateGoal(cards,playerID);
        }
    }

    public void sendEndGamePoint(JsonObject points){
        for (ConnectionController c: interfaces) {
            c.sendEndGamePoint(points);
        }
    }

    public void sendWinner(JsonObject winner){
        for (ConnectionController c: interfaces) {
            c.sendWinner(winner);
        }
    }
}
