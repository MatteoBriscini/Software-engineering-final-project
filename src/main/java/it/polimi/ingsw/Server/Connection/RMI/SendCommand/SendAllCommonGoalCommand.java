package it.polimi.ingsw.Server.Connection.RMI.SendCommand;

import it.polimi.ingsw.Client.Connection.PlayingPlayerRemoteInterface;

import java.rmi.RemoteException;

public class SendAllCommonGoalCommand implements Command{
    private final int[] commonGoalID;
    public SendAllCommonGoalCommand(int[] commonGoalID){
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
