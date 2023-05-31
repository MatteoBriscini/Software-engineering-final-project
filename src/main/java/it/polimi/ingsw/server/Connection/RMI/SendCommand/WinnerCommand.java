package it.polimi.ingsw.server.Connection.RMI.SendCommand;

import com.google.gson.JsonObject;
import it.polimi.ingsw.client.Connection.PlayingPlayerRemoteInterface;
import it.polimi.ingsw.server.Controller;

import java.rmi.RemoteException;
import java.util.Map;

public class WinnerCommand extends CommandAbstract implements Command{
    String winner;
    public  WinnerCommand(JsonObject winner, Map<String, PlayingPlayerRemoteInterface> clients, Controller connectionInterface){
        super(clients, connectionInterface);
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
