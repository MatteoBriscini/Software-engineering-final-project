package it.polimi.ingsw.Server.Connection.RMI.SendCommand;

import com.google.gson.JsonObject;
import it.polimi.ingsw.Client.Connection.PlayingPlayerRemoteInterface;

import java.rmi.RemoteException;

public class EndGameCommand implements Command{
    String points;
    public EndGameCommand(JsonObject points){
        this.points = points.toString();
    }
    @Override
    public boolean execute(PlayingPlayerRemoteInterface client) {
        try {
            client.endGameValue(points);
        } catch (RemoteException e) {
            return false;
        }
        return true;
    }
}
