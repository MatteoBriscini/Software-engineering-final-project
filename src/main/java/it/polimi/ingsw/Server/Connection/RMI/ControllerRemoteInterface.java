package it.polimi.ingsw.Server.Connection.RMI;

import com.google.gson.JsonArray;
import it.polimi.ingsw.Client.Connection.PlayingPlayerRemoteInterface;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ControllerRemoteInterface  extends Remote {
    boolean joinRMIControllerConnection(PlayingPlayerRemoteInterface client_ref, String playerID) throws RemoteException;

    boolean quitRMIControllerConnection(PlayingPlayerRemoteInterface client_ref,String playerID) throws RemoteException;
    boolean startGame(String playerID) throws RemoteException;
    boolean takeCard(int column, JsonArray cards,String playerID) throws RemoteException;


}
