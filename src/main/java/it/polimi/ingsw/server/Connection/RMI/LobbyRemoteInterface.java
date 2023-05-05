package it.polimi.ingsw.server.Connection.RMI;

import it.polimi.ingsw.client.Connection.PlayingPlayerRemoteInterface;
import it.polimi.ingsw.shared.exceptions.addPlayerToGameException;

import javax.security.auth.login.LoginException;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface LobbyRemoteInterface extends Remote {

    /**********************************************************************
     *                 lobby                                              *
     * ********************************************************************/
    void joinLobby(String playerID)throws RemoteException;
    String login(String ID, String pwd) throws LoginException, RemoteException;

    boolean signUp(String ID, String pwd) throws LoginException, RemoteException;

    String joinGame(String ID) throws addPlayerToGameException, RemoteException;

    String joinGame(String ID, String searchID) throws addPlayerToGameException, RemoteException;

    String createGame(String ID) throws addPlayerToGameException, RemoteException;

    String createGame(String ID, int maxPlayerNumber) throws addPlayerToGameException, RemoteException;

    /**********************************************************************
     *                 controller                                         *
     * ********************************************************************/
    void ping() throws RemoteException;
    /**
     * new client joins the party :)
     * @param client_ref ref to remote obj
     * @return true if the ref can be added false in all other case
     * @throws RemoteException if the server isn't available
     */
    boolean joinGameConnection(PlayingPlayerRemoteInterface client_ref, String playerID, String connectionInterface) throws RemoteException;
    /**
     * client left the party :(
     * @param client_ref  ref to remote obj
     * @return true if the ref can be removed false in all other case
     * @throws RemoteException if the server isn't available
     */
    boolean quitGameConnection(PlayingPlayerRemoteInterface client_ref,String playerID, String connectionInterface) throws RemoteException;

    /**
     * method to start game, only the creator of the game can start the game in every moment
     * @param playerID ID of the player call the method
     * @return true if player can start the game false in all other cases
     * @throws RemoteException if server is offline
     */
    boolean startGame(String playerID, String connectionInterface) throws RemoteException;

    /**
     * method to take a card from the mainBoard, only the current player can take a card
     * @param column in which column the player want to insert the cards
     * @param cards json string, contains the cards the player wants to pick
     * @param playerID ID of the player call the method
     * @return if the player who calls the method is the current and if the move is valid, false in all other cases
     * @throws RemoteException if server is offline
     */
    boolean takeCard(int column, String cards, String playerID, String connectionInterface) throws RemoteException; //PositionWithColor[]
    /**
     * send message in broadcast to all clients
     * @param msg message to send
     * @param sender name of the player who sends the message
     */

    void receiveBroadcastMsg(String msg, String sender, String connectionInterface) throws RemoteException;
    /**
     * send a message in private to only one client
     * @param userID id of the player the message is for
     * @param msg message to send
     * @param sender name of the player who sends the message
     */
    void receivePrivateMSG(String userID, String msg, String sender, String connectionInterface) throws RemoteException;
}
