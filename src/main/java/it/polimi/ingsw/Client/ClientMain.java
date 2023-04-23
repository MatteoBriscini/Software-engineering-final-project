package it.polimi.ingsw.Client;

import it.polimi.ingsw.Client.Player.Player;
import it.polimi.ingsw.Client.Player.PlayingPlayer;
import it.polimi.ingsw.Shared.Cards.Card;
import it.polimi.ingsw.Shared.Cards.CardColor;
import it.polimi.ingsw.Shared.Connection.ConnectionType;
import it.polimi.ingsw.Shared.JsonSupportClasses.Position;
import it.polimi.ingsw.Shared.JsonSupportClasses.PositionWithColor;

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
/*
        ClientMain clientMain= new ClientMain();
        try {
            Player player2 = new PlayingPlayer("marco", "addwa", clientMain, SOCKET, 1245, serverIP);

            ((PlayingPlayer)player2).startGame();

            Card[][] board = new Card[3][2];
            board[0][0] = new Card(CardColor.BLUE);
            board[1][0] = new Card(CardColor.BLUE);
            board[2][0] = new Card(CardColor.BLUE);
            board[0][1] = new Card(CardColor.BLUE);
            board[1][1] = new Card(CardColor.BLUE);
            board[2][1] = new Card(CardColor.BLUE);
            ((PlayingPlayer)player2).createMainBoard(board);
            Position[] pos = new Position[2];
            pos[0] = new Position(0,0);
            pos[1] = new Position(1,0);
            ((PlayingPlayer)player2).takeCard(0,pos);

            ((PlayingPlayer)player2).quitGame();
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
*/


    }

}
