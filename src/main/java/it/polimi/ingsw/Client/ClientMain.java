package it.polimi.ingsw.Client;

import it.polimi.ingsw.Client.Connection.PlayingPlayerRMI;
import it.polimi.ingsw.client.Connection.LobbyPlayerRMI;

import java.rmi.RemoteException;

public class ClientMain {
    static String serverIP = "127.0.0.1";



    public static void main(String[] args) {
        LobbyPlayerRMI tmp = null;
        tmp = new LobbyPlayerRMI(1234, serverIP);
    }

}
