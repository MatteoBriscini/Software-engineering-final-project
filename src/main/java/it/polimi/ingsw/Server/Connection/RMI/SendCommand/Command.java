package it.polimi.ingsw.Server.Connection.RMI.SendCommand;

import it.polimi.ingsw.Client.Connection.PlayingPlayerRemoteInterface;

public interface Command {
    public boolean execute(PlayingPlayerRemoteInterface client);

}
