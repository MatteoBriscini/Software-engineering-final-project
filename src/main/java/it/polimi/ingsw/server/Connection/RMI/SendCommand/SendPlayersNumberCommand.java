package it.polimi.ingsw.server.Connection.RMI.SendCommand;

import it.polimi.ingsw.client.Connection.PlayingPlayerRemoteInterface;
import it.polimi.ingsw.server.Controller;

import java.rmi.RemoteException;
import java.util.Map;

public class SendPlayersNumberCommand extends CommandAbstract implements Command {

    private final int playersNumber;
    public SendPlayersNumberCommand(int playersNumber, Map<String, PlayingPlayerRemoteInterface> clients, Controller connectionInterface){
        super(clients, connectionInterface);
        this.playersNumber = playersNumber;
    }

    @Override
    public boolean execute(PlayingPlayerRemoteInterface client) {
        try {
            System.out.println("test");
            client.receivePlayersNumber(playersNumber);
        } catch (RemoteException e) {
            return false;
        }
        return true;
    }
}
