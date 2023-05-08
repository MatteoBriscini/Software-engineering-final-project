package it.polimi.ingsw.client;

import it.polimi.ingsw.client.Connection.ClientRMI;
import it.polimi.ingsw.client.Connection.ClientSOCKET;
import it.polimi.ingsw.client.Connection.ConnectionManager;
import it.polimi.ingsw.client.Player.LobbyPlayer;
import it.polimi.ingsw.client.Player.Player;
import it.polimi.ingsw.client.Player.PlayingPlayer;
import it.polimi.ingsw.shared.Connection.ConnectionType;

import java.rmi.RemoteException;

public class ClientMain {
    static String serverIP = "127.0.0.1";
    private static ConnectionType connectionType;

    public static void main(String[] args) {
        //TODO
        ConnectionManager connection;
        ConnectionManager connection1;
        try {
            connection = new ClientRMI(1234, "127.0.0.1");
            connection1 = new ClientSOCKET(1235, "127.0.0.1");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        Player player;
        player = new LobbyPlayer("paolo", "antonio", connection);
        Player player1;
        player1 = new LobbyPlayer("marco", "antonio", connection1);
        ((LobbyPlayer)player).login();
        ((LobbyPlayer)player).createGame();
        ((LobbyPlayer)player1).joinGame();
        player1 = connection1.getPlayer();
        player = connection.getPlayer();
        player.setDebugMode();
        player1.setDebugMode();
    }

}
