package it.polimi.ingsw.Server.Connection.RMI.SendCommand;

import com.google.gson.JsonObject;
import it.polimi.ingsw.Client.Connection.PlayingPlayerRemoteInterface;

import java.rmi.RemoteException;

public class WinnerCommand implements Command{
    String winner;
    public  WinnerCommand(JsonObject winner){
        this.winner = winner.toString();
    }
    @Override
    public boolean execute(PlayingPlayerRemoteInterface client) {
        try {
            client.receiveWinner(winner);
        } catch (RemoteException e) {
            return false;
        }
        return true;
    }
}
