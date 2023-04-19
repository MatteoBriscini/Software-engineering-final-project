package it.polimi.ingsw.Client;

import it.polimi.ingsw.Client.Connection.PlayingPlayerSOCKET;
import it.polimi.ingsw.Client.Player.Player;
import it.polimi.ingsw.Client.Player.PlayingPlayer;
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

        ((PlayingPlayer)player).getMainBoard();

        try {
            player = new PlayingPlayer(playerID, pwd, this, connectionType, PORT, serverIP);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }
    public static void main(String[] args) {
        System.out.println("choose your connection type (RMI/socket):");
        Scanner scanner = new Scanner(System.in);
        String cT = scanner.nextLine();
        cT= cT.toUpperCase();
        if(cT.equals("SOCKET")){
            connectionType =  SOCKET;
        }else {
            connectionType = RMI;
        }



        //testing socket
        ClientMain clientMain= new ClientMain();
        try {
            Player player2 = new PlayingPlayer("marco", "addwa", clientMain, SOCKET, 1245, serverIP);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }

    }

}
