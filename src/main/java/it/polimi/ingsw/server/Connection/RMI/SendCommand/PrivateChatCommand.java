package it.polimi.ingsw.server.Connection.RMI.SendCommand;

import it.polimi.ingsw.client.Connection.PlayingPlayerRemoteInterface;
import it.polimi.ingsw.server.Controller;

import java.rmi.RemoteException;
import java.util.Map;

public class PrivateChatCommand extends CommandAbstract implements Command{

    private String userID;
    private String msg;
    private String sender;
    public PrivateChatCommand (String userID, String msg, String sender, Map<String, PlayingPlayerRemoteInterface> clients, Controller connectionInterface){
        super(clients, connectionInterface);
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
