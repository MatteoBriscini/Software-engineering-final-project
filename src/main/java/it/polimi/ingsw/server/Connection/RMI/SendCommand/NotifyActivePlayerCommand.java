package it.polimi.ingsw.server.Connection.RMI.SendCommand;

import it.polimi.ingsw.client.Connection.PlayingPlayerRemoteInterface;
import it.polimi.ingsw.server.Controller;

import java.rmi.RemoteException;
import java.util.Map;

public class NotifyActivePlayerCommand extends CommandAbstract implements Command {

    private final String activePlayerId;
    public NotifyActivePlayerCommand(String activePlayerId, Map<String, PlayingPlayerRemoteInterface> clients, Controller connectionInterface) {
        super(clients,connectionInterface);
        this.activePlayerId = activePlayerId;
    }

    public boolean execute(PlayingPlayerRemoteInterface client) {
        try {
            System.out.println("test");
            client.notifyActivePlayer(activePlayerId);
        } catch (RemoteException e) {
            return false;
        }
        return true;
    }
}
