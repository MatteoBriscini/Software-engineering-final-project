package it.polimi.ingsw.server.Connection.RMI.SendCommand;

import com.google.gson.JsonObject;
import it.polimi.ingsw.client.Connection.PlayingPlayerRemoteInterface;
import it.polimi.ingsw.server.Controller;

import java.rmi.RemoteException;
import java.util.Map;

public class ErrorCommand extends CommandAbstract implements Command{
    String error;
    String playerID;
    public ErrorCommand(JsonObject error, String playerID, Map<String, PlayingPlayerRemoteInterface> clients, Controller connectionInterface){
        super(clients, connectionInterface);

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
