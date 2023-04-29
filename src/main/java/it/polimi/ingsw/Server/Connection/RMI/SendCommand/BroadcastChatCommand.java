package it.polimi.ingsw.Server.Connection.RMI.SendCommand;

import it.polimi.ingsw.client.Connection.PlayingPlayerRemoteInterface;

import java.rmi.RemoteException;

public class BroadcastChatCommand implements Command{
    private String msg;
    private String sender;

    public BroadcastChatCommand(String msg,String sender){
        this.msg = msg;
        this.sender = sender;
    }
    @Override
    public boolean execute(PlayingPlayerRemoteInterface client) {
        try {
            client.receiveBroadcastMsg(msg,sender);
        } catch (RemoteException e) {
            return false;
        }
        return true;
    }
}
