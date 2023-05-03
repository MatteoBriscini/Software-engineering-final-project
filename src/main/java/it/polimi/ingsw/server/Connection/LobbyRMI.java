package it.polimi.ingsw.server.Connection;

import com.google.gson.JsonObject;
import it.polimi.ingsw.server.Exceptions.addPlayerToGameException;
import it.polimi.ingsw.server.Lobby.Lobby;
import it.polimi.ingsw.shared.Cards.Card;
import it.polimi.ingsw.shared.Connection.ConnectionType;
import it.polimi.ingsw.shared.JsonSupportClasses.PositionWithColor;

import javax.security.auth.login.LoginException;
import java.rmi.AlreadyBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;

public class LobbyRMI extends ConnectionController  implements LobbyRemoteInterface {

    protected final String cntType;

    public LobbyRMI(int port, Lobby lobby) {
        super(lobby, port, ConnectionType.RMI);
        this.cntType = "RMI";
        this.connection();

    }

    synchronized public void connection() {
        LobbyRemoteInterface stub = null;
        try {

            stub = (LobbyRemoteInterface) UnicastRemoteObject.exportObject((Remote) this, this.PORT);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }

        Registry registry = null;
        try {
            registry = LocateRegistry.createRegistry(this.PORT);
        } catch (RemoteException e) {
            System.out.println(e);
            throw new RuntimeException(e);
        }

        try {
            registry.bind("LobbyRemoteInterface", stub);
        } catch (RemoteException | AlreadyBoundException e) {
            // finire ********************************************************
            System.out.println(e.toString());
            throw new RuntimeException(e);
        }

        System.err.println("Server (rmi) for lobby ready on port: " + PORT);
    }

    @Override
    public void notifyActivePlayer(String activePlayerID) {

    }

    @Override
    public void sendPlayerList(String[] players) {

    }

    @Override
    public void sendPlayersNUmber(int playersNumber) {

    }

    @Override
    public void sendMainBoard(Card[][] mainBoard) {

    }

    @Override
    public void addCardToClientBoard(String playerID, int column, Card[] cards) {

    }

    @Override
    public void dellCardFromMainBoard(PositionWithColor[] cards) {

    }

    @Override
    public void sendAllPlayerBoard(ArrayList<Card[][]> playerBoards) {

    }

    @Override
    public void sendAllCommonGoal(int[] commonGoalID) {

    }

    @Override
    public void sendPrivateGoal(PositionWithColor[] cards, String playerID) {

    }

    @Override
    public void sendEndGamePoint(JsonObject points) {

    }

    @Override
    public void sendWinner(JsonObject winner) {

    }

    @Override
    public void sendLastCommonScored(JsonObject scored) {

    }

    @Override
    public void sendError(JsonObject error, String playerID) {

    }

    @Override
    public void forceClientDisconnection() {

    }

    @Override
    public void sendBroadcastMsg(String msg, String sender) {

    }

    @Override
    public void sendPrivateMSG(String userID, String msg, String sender) {

    }




    /*

    RMI PORT 1234
    SOCKET PORT 1235

     */

}
