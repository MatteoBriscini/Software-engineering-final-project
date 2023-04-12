package it.polimi.ingsw.Server.Connection.RMI.SendCommand;

import it.polimi.ingsw.Client.Connection.PlayingPlayerRemoteInterface;

import java.rmi.RemoteException;

public class NotifyActivePlayerCommand implements Command {

    private final String activePlayerId;
    public NotifyActivePlayerCommand(String activePlayerId) {
        this.activePlayerId = activePlayerId;
    }

    public boolean execute(PlayingPlayerRemoteInterface client) {
        try {
            client.notifyActivePlayer(activePlayerId);
        } catch (RemoteException e) {
            return false;
        }
        return true;
    }
}
