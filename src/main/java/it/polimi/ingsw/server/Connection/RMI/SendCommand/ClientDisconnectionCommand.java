package it.polimi.ingsw.server.Connection.RMI.SendCommand;

import it.polimi.ingsw.client.Connection.PlayingPlayerRemoteInterface;
import it.polimi.ingsw.server.Controller;

import java.rmi.RemoteException;
import java.util.Map;

public class ClientDisconnectionCommand extends CommandAbstract implements Command{
    public ClientDisconnectionCommand (Map<String, PlayingPlayerRemoteInterface> clients, Controller connectionInterface){
        super(clients, connectionInterface);
    }
    @Override
    public boolean execute(PlayingPlayerRemoteInterface client) {
        try {
            client.forceDisconnection();
        } catch (RemoteException e) {
            return false;
        }
        return true;
    }
}
