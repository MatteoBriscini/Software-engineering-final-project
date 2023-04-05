package it.polimi.ingsw.client.Connection;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface PlayingPlayerRemoteInterface extends Remote {
    void notifyActivePlayer(int activePlayerID) throws RemoteException;
    void receivePlayerList(String[] playersID) throws RemoteException;
}