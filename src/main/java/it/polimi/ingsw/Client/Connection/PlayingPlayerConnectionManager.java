package it.polimi.ingsw.Client.Connection;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public abstract class PlayingPlayerConnectionManager extends UnicastRemoteObject {
    protected PlayingPlayerConnectionManager() throws RemoteException {
    }
}
