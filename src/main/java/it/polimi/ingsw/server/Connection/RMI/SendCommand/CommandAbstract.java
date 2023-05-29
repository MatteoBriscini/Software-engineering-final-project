package it.polimi.ingsw.server.Connection.RMI.SendCommand;

import it.polimi.ingsw.client.Connection.PlayingPlayerRemoteInterface;
import it.polimi.ingsw.server.Controller;

import java.util.Map;

public abstract class CommandAbstract implements Command{
    private Map<String, PlayingPlayerRemoteInterface> clients;
    private Controller connectionInterface;
    public CommandAbstract(Map<String, PlayingPlayerRemoteInterface> clients, Controller connectionInterface){
        this.clients = clients;
        this.connectionInterface = connectionInterface;
    }

    public Controller getConnectionInterface() {
        return connectionInterface;
    }

    public Map<String, PlayingPlayerRemoteInterface> getClients() {
        return clients;
    }
}
