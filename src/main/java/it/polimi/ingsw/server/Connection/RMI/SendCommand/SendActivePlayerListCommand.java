package it.polimi.ingsw.server.Connection.RMI.SendCommand;

import it.polimi.ingsw.client.Connection.PlayingPlayerRemoteInterface;
import it.polimi.ingsw.server.Controller;

import java.rmi.RemoteException;
import java.util.Map;

public class SendActivePlayerListCommand extends CommandAbstract implements Command {

    private final String[] players;
    public SendActivePlayerListCommand(String[] players, Map<String, PlayingPlayerRemoteInterface> clients, Controller connectionInterface){
        super(clients, connectionInterface);
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
