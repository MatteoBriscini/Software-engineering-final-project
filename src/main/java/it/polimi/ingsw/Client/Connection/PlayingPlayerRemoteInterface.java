package it.polimi.ingsw.Client.Connection;



import it.polimi.ingsw.Shared.Cards.Card;
import it.polimi.ingsw.Shared.JsonSupportClasses.PositionWithColor;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface PlayingPlayerRemoteInterface extends Remote {
    void notifyActivePlayer(String activePlayerID) throws RemoteException;
    void receivePlayerList(String[] playersID) throws RemoteException;
    void receivePlayersNumber(int playersNumber) throws RemoteException;
    void receiveMainBoard(Card[][] mainBoard) throws RemoteException;
    void addCardToPlayerBoard(String playerID, int column, Card[] cards) throws RemoteException;
    void dellCardFromMainBoard(PositionWithColor[] cards) throws RemoteException;
}