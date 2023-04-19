package it.polimi.ingsw.Client.Connection;



import it.polimi.ingsw.Shared.Cards.Card;
import it.polimi.ingsw.Shared.JsonSupportClasses.PositionWithColor;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

public interface PlayingPlayerRemoteInterface extends Remote {
    /**
     * @param activePlayerID PLayerID of the player have to play now
     * @throws RemoteException if the client is offline
     */
    void notifyActivePlayer(String activePlayerID) throws RemoteException;
    /**
     * @param playersID all players ID
     * @throws RemoteException if the client is offline
     */
    void receivePlayerList(String[] playersID) throws RemoteException;
    /**
     * @param playersNumber total amount of players in the game
     * @throws RemoteException if the client is offline
     */
    void receivePlayersNumber(int playersNumber) throws RemoteException;
    /**
     * @param mainBoard json array represent the main board
     * @throws RemoteException if the client is offline
     */
    void receiveMainBoard(String mainBoard) throws RemoteException; //Card[][]
    /**
     * @param playerID name of the owner of the board we have to add cards
     * @param column on the board where we have to add cards
     * @param cards card array to add at the board
     * @throws RemoteException if the client is offline
     */
    void addCardToPlayerBoard(String playerID, int column,String  cards) throws RemoteException;//Card[] (cards)
    /**
     * @param cards position with color array, position have to remove from the mainBoard (used in game start phase and when a player reconnect after a crash)
     * @throws RemoteException if the client is offline
     */
    void removeCardFromMainBoard(String cards) throws RemoteException;//PositionWithColor[]
    /**
     * @param playerBoards all players bord in the game (used in game start phase and when a player reconnect after a crash)
     * @throws RemoteException if the client is offline
     */
    void receiveAllPlayerBoard(String playerBoards) throws RemoteException; //ArrayList<Card[][]>
    /**
     * @param commonGoalID number unique identify the common goal
     * @throws RemoteException if the client is offline
     */
    void receiveAllCommonGoal(int[] commonGoalID) throws RemoteException;
    /**
     * @param cards PositionWithColor[] settings to recreate the image for private goal
     * @param playerID player's name for the private goal
     * @throws RemoteException if the client is offline
     */
    void receivePrivateGoal(String cards,String playerID) throws RemoteException;//PositionWithColor[] (cards)
    /**
     * receive points of all the players in the game
     * @param points map playerID + point for each game
     * @throws RemoteException if the players is offline
     */
    void endGameValue(String points) throws RemoteException;
    /**
     * @param winner json object with playerID and points of the winner
     * @throws RemoteException if the players is offline
     */
    void receiveWinner(String winner) throws RemoteException;

    /**
     * @param scored json object with point for each client
     * @throws RemoteException if the players is offline
     */
    void receiveLastCommonScored(String scored) throws RemoteException;
    /**
     * @param error json object with errorID && errorMSG
     * @param playerID name of the player the message is intended for
     * @throws RemoteException if the players is offline
     */
    void errorMSG(String error, String playerID) throws RemoteException;

    /**
     * the client is forced by the server to quit the game
     * @throws RemoteException if the players is offline
     */
    void forceDisconnection()throws RemoteException;
}