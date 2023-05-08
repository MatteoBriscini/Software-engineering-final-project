package it.polimi.ingsw.server.Connection;

import com.google.gson.Gson;
import it.polimi.ingsw.server.Controller;
import it.polimi.ingsw.shared.exceptions.addPlayerToGameException;
import it.polimi.ingsw.server.Lobby.Lobby;
import it.polimi.ingsw.shared.Connection.ConnectionType;
import it.polimi.ingsw.shared.JsonSupportClasses.PositionWithColor;

import javax.security.auth.login.LoginException;
import java.rmi.RemoteException;
import java.util.ArrayList;

public abstract class ConnectionController {

    protected int PORT;
    protected Lobby lobby;
    protected ConnectionType connectionType;

    public ConnectionController (Lobby lobby, int port, ConnectionType connectionType){
        this.lobby = lobby;
        this.PORT = port;
        this.connectionType = connectionType;
    }

    public abstract void connection();
    public Controller getActualController(String controllerID){
        ArrayList<Controller> controllers = lobby.getActiveGames();
        for ( Controller c : controllers){
            if(controllerID.equals(c.toString()))return c;
        }
        throw new RuntimeException("try to call method on not existing game");
    }
    public int getPORT(){
        return this.PORT;
    }
    /****************************************************************************
     ************************************************** IN playing methods ******
     * ***********************************************************************
     * method to start game, only the creator of the game can start the game in every moment
     * @param playerID ID of the player call the method
     * @return true if a player can start the game false in all other cases
     * @throws RemoteException if server is offline
     */
    public boolean startGame(String playerID, String connectionInterface){
        Controller controller = this.getActualController(connectionInterface);
        return controller.startGame(playerID);
    }
    /**
     * method to take a card from the mainBoard, only the current player can take a card
     * @param column in which column the player want to insert the cards
     * @param cards json string, contains the cards the player wants to pick
     * @param playerID ID of the player call the method
     * @return if the player who calls the method is the current and if the move is valid, false in all other cases
     */
    public boolean takeCard (int column, String cards , String playerID, String connectionInterface){
        PositionWithColor[] cardsArray = new Gson().fromJson(cards, PositionWithColor[].class);
        Controller controller = this.getActualController(connectionInterface);
        return controller.takeCard(column, cardsArray, playerID);
    }

    /**************************************************************************
     ************************************************** chat ******************
     * ************************************************************************
     * *
     * receive a message in broadcast to all clients
     * @param msg message to send
     * @param sender name of the player who sends the message
     */
    public void receiveBroadcastMsg(String msg, String sender, String connectionInterface){
        Controller controller = this.getActualController(connectionInterface);
        controller.broadcastMsg(msg,sender);
    }
    /**
     * receive a message in private to only one client
     * @param userID id of the player the message is for
     * @param msg message to send
     * @param sender name of the player who sends the message
     */
    public void receivePrivateMSG(String userID, String msg, String sender, String connectionInterface){
        Controller controller = this.getActualController(connectionInterface);
        controller.privateMSG(userID,msg,sender);
    }
/*************************************************************************
 ************************************************** IN lobby methods ********
 * ***********************************************************************
 * */

    public void joinLobby(String playerID){
        System.out.println("\u001B[36m"+"client: " + playerID + " join the lobby on port(RMI): " + PORT +"\u001B[0m");
    }
    public String login(String ID, String pwd) throws LoginException {
        return lobby.login(ID, pwd);
    }


    public boolean signUp(String ID, String pwd) throws LoginException {
        lobby.signUp(ID, pwd);
        return false;
    }

    public String joinGame(String ID) throws addPlayerToGameException {
        return lobby.joinGame(ID, this.connectionType);
    }
    public String joinGame(String ID, String searchID) throws addPlayerToGameException {
        return lobby.joinGame(ID, this.connectionType, searchID);
    }

    public String createGame(String ID) throws addPlayerToGameException {
        return lobby.createGame(ID);
    }

    public String createGame(String ID, int maxPlayerNumber) throws addPlayerToGameException {

        System.out.println(ID + " on port: "+ PORT + ", trying to create a game with " + maxPlayerNumber + " players");
        return lobby.createGame(ID);
    }
}
