package it.polimi.ingsw.server.Connection.RMI.SendCommand;

import it.polimi.ingsw.client.Connection.PlayingPlayerRemoteInterface;

public interface Command {
    /**
     * this is the actual call on the clients
     * @param client remote ref to clients
     * @return true if the command go in the correct way
     */
    boolean execute(PlayingPlayerRemoteInterface client);

}
