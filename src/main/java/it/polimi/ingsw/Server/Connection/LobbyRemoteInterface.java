package it.polimi.ingsw.Server.Connection;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface LobbyRemoteInterface extends Remote {

    public boolean login(String ID, String pwd) throws RemoteException;

}
