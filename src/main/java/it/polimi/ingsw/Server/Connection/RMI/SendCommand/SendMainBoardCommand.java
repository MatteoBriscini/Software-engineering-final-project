package it.polimi.ingsw.Server.Connection.RMI.SendCommand;

import it.polimi.ingsw.Client.Connection.PlayingPlayerRemoteInterface;

import it.polimi.ingsw.Shared.Cards.Card;

import java.rmi.RemoteException;

public class SendMainBoardCommand implements Command {
    Card[][] mainBoard;
    public SendMainBoardCommand(Card[][] mainBoard){
        this.mainBoard = mainBoard;
    }

    @Override
    public boolean execute(PlayingPlayerRemoteInterface client) {
        try {
            client.receiveMainBoard(mainBoard);
        } catch (RemoteException e) {
            return false;
        }
        return true;
    }
}
