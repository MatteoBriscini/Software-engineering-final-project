package it.polimi.ingsw.Server.Connection;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ControllerRemoteInterface  extends Remote {
    boolean startGame(String playerID) throws RemoteException;
}
