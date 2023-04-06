package it.polimi.ingsw.Server.Connection.RMI;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import it.polimi.ingsw.Client.Connection.PlayingPlayerRemoteInterface;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ControllerRemoteInterface  extends Remote {
    /**
     * new client join the party :)
     * @param client_ref ref to remote obj
     * @return true if the ref can be added false in all other case
     * @throws RemoteException if the server isn't available
     */
    boolean joinRMIControllerConnection(PlayingPlayerRemoteInterface client_ref, String playerID) throws RemoteException;
    /**
     * client left the party :(
     * @param client_ref  ref to remote obj
     * @return true if the ref can be removed false in all other case
     * @throws RemoteException if the server isn't available
     */
    boolean quitRMIControllerConnection(PlayingPlayerRemoteInterface client_ref,String playerID) throws RemoteException;

    /**
     * method to start game, only the creator of the game can start the game in every moment
     * @param playerID ID of the player call the method
     * @return true if player can start the game false in all other cases
     * @throws RemoteException if server is offline
     */
    boolean startGame(String playerID) throws RemoteException;

    /**
     * method to take a card from mainBoard, only the current player can take a card
     * @param column in which column the player want to insert the cards
     * @param cards json string, contains the cards the player want to pick
     * @param playerID ID of the player call the method
     * @return if the player who call the method is the current and if the move is valid, false in all other cases
     * @throws RemoteException if server is offline
     */
    boolean takeCard(int column, String cards, String playerID) throws RemoteException; //PositionWithColor[]


}
