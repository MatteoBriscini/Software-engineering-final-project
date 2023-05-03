package it.polimi.ingsw.server.Connection;

import it.polimi.ingsw.server.Exceptions.addPlayerToGameException;
import it.polimi.ingsw.shared.Connection.ConnectionType;

import javax.security.auth.login.LoginException;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface LobbyRemoteInterface extends Remote {

    int login(String ID, String pwd) throws LoginException, RemoteException;

    boolean signUp(String ID, String pwd) throws LoginException, RemoteException;

    int joinGame(String ID) throws addPlayerToGameException, RemoteException;

    int joinGame(String ID, String searchID) throws addPlayerToGameException, RemoteException;

    int createGame(String ID) throws addPlayerToGameException, RemoteException;

    int createGame(String ID, int maxPlayerNumber) throws addPlayerToGameException, RemoteException;


}
