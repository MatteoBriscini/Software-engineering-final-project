package it.polimi.ingsw.Server.Connection.RMI.SendCommand;

import it.polimi.ingsw.Client.Connection.PlayingPlayerRemoteInterface;
import it.polimi.ingsw.Shared.JsonSupportClasses.PositionWithColor;

import java.rmi.RemoteException;

public class DellCardFromMainBoard implements Command{
    PositionWithColor[] cards;
    public DellCardFromMainBoard(PositionWithColor[] cards){
        this.cards = cards;
    }


    @Override
    public boolean execute(PlayingPlayerRemoteInterface client) {
        try {
            client.dellCardFromMainBoard(cards);
        } catch (RemoteException e) {
            return false;
        }
        return true;
    }
}
