package it.polimi.ingsw.server.Connection.RMI.SendCommand;

import com.google.gson.JsonObject;
import it.polimi.ingsw.client.Connection.PlayingPlayerRemoteInterface;

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