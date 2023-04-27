package it.polimi.ingsw.Server.Connection.RMI.SendCommand;

import it.polimi.ingsw.Client.Connection.PlayingPlayerRemoteInterface;

import java.rmi.RemoteException;

public class PrivateChatCommand implements Command{

    private String userID;
    private String msg;
    private String sender;
    public PrivateChatCommand (String userID, String msg, String sender){
        this.userID = userID;
        this.msg = msg;
        this.sender = sender;
    }
    @Override
    public boolean execute(PlayingPlayerRemoteInterface client) {
        try {
            if(client.getPlayerID().equals(userID)) client.receivePrivateMSG(userID, msg ,sender);
        } catch (RemoteException e) {
            return false;
        }
        return true;
    }
}
