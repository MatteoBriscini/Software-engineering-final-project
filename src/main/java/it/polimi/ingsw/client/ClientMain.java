package it.polimi.ingsw.client;

import it.polimi.ingsw.client.Player.Player;
import it.polimi.ingsw.client.Player.PlayingPlayer;
import it.polimi.ingsw.shared.Connection.ConnectionType;

import java.rmi.RemoteException;

public class ClientMain {
    static String serverIP = "127.0.0.1";
    private Player player;

    private static ConnectionType connectionType;

    public void setPlayerAsPlaying(int PORT){
        String playerID = player.getPlayerID();
        String pwd = player.getPwd();

        try {
            player = new PlayingPlayer(playerID, pwd, this, connectionType, PORT, serverIP);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }
    public static void main(String[] args) {


        try {
            PlayingPlayer player1 = new PlayingPlayer("marco", "adsdwd", new ClientMain(), ConnectionType.RMI, 1200, serverIP);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
        try {
            PlayingPlayer player2 = new PlayingPlayer("paolo", "adsdwd", new ClientMain(), ConnectionType.RMI, 1200, serverIP);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
        try {
            PlayingPlayer player3 = new PlayingPlayer("paolo", "adsdwd", new ClientMain(), ConnectionType.SOCKET, 1202, serverIP);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }

    }

}
