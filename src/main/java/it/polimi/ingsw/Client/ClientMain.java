package it.polimi.ingsw.Client;

import it.polimi.ingsw.Client.Connection.PlayingPlayerRMI;

import java.rmi.RemoteException;

public class ClientMain {
    static String serverIP = "127.0.0.1";



    public static void main(String[] args) {
        PlayingPlayerRMI tmp = null;
        try {
            tmp = new PlayingPlayerRMI(1233, serverIP, "carlo");
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
        try {
            tmp = new PlayingPlayerRMI(1234, serverIP, "antonio");
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

}
