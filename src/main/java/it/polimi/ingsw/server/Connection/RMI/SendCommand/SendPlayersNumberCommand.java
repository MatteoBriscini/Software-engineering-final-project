package it.polimi.ingsw.server.Connection.RMI.SendCommand;

import it.polimi.ingsw.client.Connection.PlayingPlayerRemoteInterface;

import java.rmi.RemoteException;

public class SendPlayersNumberCommand implements Command {

    private final int playersNumber;
    public SendPlayersNumberCommand(int playersNumber){
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
