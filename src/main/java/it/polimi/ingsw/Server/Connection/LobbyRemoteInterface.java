package it.polimi.ingsw.Server.Connection;

import it.polimi.ingsw.Server.Exceptions.addPlayerToGameException;
import it.polimi.ingsw.Shared.Connection.ConnectionType;

import javax.security.auth.login.LoginException;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface LobbyRemoteInterface extends Remote {

    int login(String ID, String pwd) throws LoginException, RemoteException;

    boolean signUp(String ID, String pwd) throws LoginException, RemoteException;

    int joinGame(String ID, ConnectionType connectionType) throws addPlayerToGameException, RemoteException;

    int joinGame(String ID, ConnectionType connectionType, String searchID) throws addPlayerToGameException, RemoteException;

    int createGame(String ID, ConnectionType connectionType) throws addPlayerToGameException, RemoteException;

    int createGame(String ID, ConnectionType connectionType, int maxPlayerNumber) throws addPlayerToGameException, RemoteException;


}
