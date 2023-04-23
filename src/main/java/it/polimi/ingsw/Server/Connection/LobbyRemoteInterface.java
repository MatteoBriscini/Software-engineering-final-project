package it.polimi.ingsw.Server.Connection;

import it.polimi.ingsw.Shared.Connection.ConnectionType;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface LobbyRemoteInterface extends Remote {

    int login(String ID, String pwd) throws RemoteException;

    boolean signUp(String ID, String pwd) throws RemoteException;

    int joinGame(String ID, ConnectionType connectionType) throws RemoteException;

    int joinGame(String ID, ConnectionType connectionType, String searchID) throws RemoteException;

    int createGame(String ID, ConnectionType connectionType) throws RemoteException;

    int createGame(String ID, ConnectionType connectionType, int maxPlayerNumber) throws RemoteException;


}
