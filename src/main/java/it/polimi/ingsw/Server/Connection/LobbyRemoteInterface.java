package it.polimi.ingsw.Server.Connection;

import it.polimi.ingsw.Shared.Connection.ConnectionType;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface LobbyRemoteInterface extends Remote {

    public int login(String ID, String pwd) throws RemoteException;

    public boolean signUp(String ID, String pwd) throws RemoteException;

    public int joinGame(String ID, ConnectionType connectionType);

    public int joinGame(String ID, ConnectionType connectionType, String searchID);

    public int createGame(String ID, ConnectionType connectionType);

    public int createGame(String ID, ConnectionType connectionType, int maxPlayerNumber);


}
