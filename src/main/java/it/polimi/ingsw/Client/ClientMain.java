package it.polimi.ingsw.Client;

import it.polimi.ingsw.Client.Player.Player;
import it.polimi.ingsw.Client.Player.PlayingPlayer;
import it.polimi.ingsw.Shared.Connection.ConnectionType;

import java.rmi.RemoteException;
import java.util.Scanner;

public class ClientMain {
    static String serverIP = "127.0.0.1";
    private Player player;
    private ConnectionType connectionType;

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
        System.out.println("choose your connection type (RMI/socket):");
        Scanner scanner = new Scanner(System.in);
        String name = scanner.nextLine();
        System.out.println(name);
    }

}
