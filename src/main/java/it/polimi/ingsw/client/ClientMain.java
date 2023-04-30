package it.polimi.ingsw.client;

import it.polimi.ingsw.client.Player.Player;
import it.polimi.ingsw.client.Player.PlayingPlayer;
import it.polimi.ingsw.Shared.Connection.ConnectionType;

import java.rmi.RemoteException;
import java.util.Scanner;

import static it.polimi.ingsw.Shared.Connection.ConnectionType.*;

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
        System.out.println("test");





    }

}
