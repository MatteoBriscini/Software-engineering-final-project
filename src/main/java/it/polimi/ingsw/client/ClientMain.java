package it.polimi.ingsw.client;

import it.polimi.ingsw.client.Connection.ClientRMI;
import it.polimi.ingsw.client.Connection.ClientSOCKET;
import it.polimi.ingsw.client.Connection.ConnectionManager;
import it.polimi.ingsw.client.Player.LobbyPlayer;
import it.polimi.ingsw.client.Player.Player;
import it.polimi.ingsw.client.Player.PlayingPlayer;
import it.polimi.ingsw.client.View.TUI;
import it.polimi.ingsw.shared.Connection.ConnectionType;

import java.rmi.RemoteException;

public class ClientMain {
    static String serverIP = "127.0.0.1";
    private static ConnectionType connectionType;

    public static void main(String[] args) {
        TUI tui = new TUI();
    }

}
