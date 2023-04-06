package it.polimi.ingsw.Server.Connection.RMI.SendCommand;

import it.polimi.ingsw.Client.Connection.PlayingPlayerRemoteInterface;
import it.polimi.ingsw.Shared.Cards.Card;

import java.rmi.Remote;
import java.rmi.RemoteException;

public class AddCardToClientBoard  implements Command{
    private final String playerID;
    private final int column;
    private Card[] cards;
    public AddCardToClientBoard(String playerID, int column, Card[] cards){
        this.playerID = playerID;
        this.column = column;
        this.cards = cards;
    }

    public boolean execute(PlayingPlayerRemoteInterface client) {
        try {
            client.addCardToPlayerBoard(playerID, column, cards);
        } catch (RemoteException e) {
            return false;
        }
        return true;
    }
}
