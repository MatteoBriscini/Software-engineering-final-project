package it.polimi.ingsw.Client.Connection;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public abstract class PlayingPlayerController  extends UnicastRemoteObject {
    protected PlayingPlayerController() throws RemoteException {
    }
}
