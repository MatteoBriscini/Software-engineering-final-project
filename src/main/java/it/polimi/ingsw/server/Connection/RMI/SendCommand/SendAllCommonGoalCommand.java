package it.polimi.ingsw.server.Connection.RMI.SendCommand;

import it.polimi.ingsw.client.Connection.PlayingPlayerRemoteInterface;
import it.polimi.ingsw.server.Controller;

import java.rmi.RemoteException;
import java.util.Map;

public class SendAllCommonGoalCommand extends CommandAbstract implements Command{
    private final int[] commonGoalID;
    public SendAllCommonGoalCommand(int[] commonGoalID, Map<String, PlayingPlayerRemoteInterface> clients, Controller connectionInterface){
        super(clients, connectionInterface);
        this.commonGoalID = commonGoalID;
    }
    @Override
    public boolean execute(PlayingPlayerRemoteInterface client) {
        try {
            client.receiveAllCommonGoal(commonGoalID);
        } catch (RemoteException e) {
            return false;
        }
        return true;
    }
}
