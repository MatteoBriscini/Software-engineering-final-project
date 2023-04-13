package it.polimi.ingsw.Client;

import it.polimi.ingsw.Client.Connection.PlayingPlayerRMI;
import it.polimi.ingsw.Client.Player.Player;
import it.polimi.ingsw.Client.Player.PlayingPlayer;
import it.polimi.ingsw.Shared.Connection.ConnectionType;
import it.polimi.ingsw.Shared.JsonSupportClasses.PositionWithColor;

import java.rmi.RemoteException;
import java.util.Arrays;

import static it.polimi.ingsw.Shared.Cards.CardColor.*;

public class ClientMain {
    static String serverIP = "127.0.0.1";



    public static void main(String[] args) {
        Player tmp;
        Player tmp2;
        PositionWithColor[] pos = new PositionWithColor[2];
        pos[0] = new PositionWithColor(2,3,0,WHITE);
        pos[1] = new PositionWithColor(3,6,1,YELLOW);

        try {
            tmp = new PlayingPlayer(ConnectionType.RMI, 1234, serverIP);
            tmp.setPlayerID("paolo");
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }

        try {
            tmp2 = new PlayingPlayer(ConnectionType.RMI, 1233, serverIP);
            tmp2.setPlayerID("emma");
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

}
