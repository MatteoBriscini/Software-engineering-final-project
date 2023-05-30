package it.polimi.ingsw.server.Connection.RMI.SendCommand;

import it.polimi.ingsw.client.Connection.PlayingPlayerRemoteInterface;
import it.polimi.ingsw.server.Controller;

import java.rmi.RemoteException;
import java.util.Map;

public class BroadcastChatCommand extends CommandAbstract implements Command{
    private String msg;
    private String sender;

    public BroadcastChatCommand(String msg, String sender, Map<String, PlayingPlayerRemoteInterface> clients, Controller connectionInterface){
        super(clients, connectionInterface);

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
