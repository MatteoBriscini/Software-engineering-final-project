package it.polimi.ingsw.server.Connection.RMI.SendCommand;

import com.google.gson.JsonObject;
import it.polimi.ingsw.client.Connection.PlayingPlayerRemoteInterface;
import it.polimi.ingsw.server.Controller;

import java.rmi.RemoteException;
import java.util.Map;

public class SendLastCommonScored extends CommandAbstract implements Command{
    private final String scored;
    public SendLastCommonScored(JsonObject scored, Map<String, PlayingPlayerRemoteInterface> clients, Controller connectionInterface){
        super(clients, connectionInterface);
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
