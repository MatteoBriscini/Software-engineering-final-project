package it.polimi.ingsw.server.Connection.RMI.SendCommand;

import it.polimi.ingsw.client.Connection.PlayingPlayerRemoteInterface;

import java.rmi.RemoteException;

public class ClientDisconnectionCommand implements Command{
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