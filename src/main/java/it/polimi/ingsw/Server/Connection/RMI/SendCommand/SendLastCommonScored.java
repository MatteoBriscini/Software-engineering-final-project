package it.polimi.ingsw.Server.Connection.RMI.SendCommand;

import com.google.gson.JsonObject;
import it.polimi.ingsw.Client.Connection.PlayingPlayerRemoteInterface;

import java.rmi.RemoteException;

public class SendLastCommonScored implements Command{
    private final String scored;
    public SendLastCommonScored(JsonObject scored){
        this.scored = scored.toString();
    }
    @Override
    public boolean execute(PlayingPlayerRemoteInterface client) {
        try {
            client.receiveLastCommonScored(scored);
        } catch (RemoteException e) {
            return false;
        }
        return true;
    }
}
