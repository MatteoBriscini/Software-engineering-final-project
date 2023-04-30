package it.polimi.ingsw.server.Connection.RMI.SendCommand;

import it.polimi.ingsw.client.Connection.PlayingPlayerRemoteInterface;

import java.rmi.RemoteException;

public class SendActivePlayerListCommand implements Command {

    private final String[] players;
    public SendActivePlayerListCommand(String[] players){
        this.players = players;
    }
    @Override
    public boolean execute(PlayingPlayerRemoteInterface client) {
        try {
            client.receivePlayerList(players);
        } catch (RemoteException e) {
            return false;
        }
        return true;
    }
}
