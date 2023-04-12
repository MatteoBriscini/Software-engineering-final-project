package it.polimi.ingsw.Server.Connection.RMI.SendCommand;

import com.google.gson.JsonObject;
import it.polimi.ingsw.Client.Connection.PlayingPlayerRemoteInterface;

import java.rmi.RemoteException;

public class ErrorCommand implements Command{
    String error;
    String playerID;
    public ErrorCommand(JsonObject error, String playerID){
        this.playerID = playerID;
        this.error = error.toString();
    }
    @Override
    public boolean execute(PlayingPlayerRemoteInterface client) {
            try {
                client.errorMSG(error, playerID);
            } catch (RemoteException e) {
                return false;
            }
            return true;
    }
}
