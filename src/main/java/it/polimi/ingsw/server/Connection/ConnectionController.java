package it.polimi.ingsw.server.Connection;

import com.google.gson.Gson;
import com.google.gson.*;
import it.polimi.ingsw.server.Controller;
import it.polimi.ingsw.server.Exceptions.addPlayerToGameException;
import it.polimi.ingsw.server.Lobby.Lobby;
import it.polimi.ingsw.shared.Cards.Card;
import it.polimi.ingsw.shared.Connection.ConnectionType;
import it.polimi.ingsw.shared.JsonSupportClasses.PositionWithColor;

import javax.security.auth.login.LoginException;
import java.rmi.RemoteException;
import java.util.ArrayList;

public abstract class ConnectionController {
    protected Controller controller = new Controller();
    protected int PORT;
    protected Lobby lobby;
    protected ConnectionType connectionType;

    public ConnectionController (Lobby lobby, int port, ConnectionType connectionType){
        this.lobby = lobby;
        this.PORT = port;
        this.connectionType = connectionType;
    }

    public abstract void connection();

    public int getPORT(){
        return this.PORT;
    }

    /*************************************************************************
     ************************************************** OUT playing methods ***********
     * ***********************************************************************
     * *
     * @param activePlayerID player's id of the client have to play in this turn
     */
    public abstract void notifyActivePlayer(String activePlayerID);
    /**
     * @param players array list with al the name of the players in game
     */
    public abstract void sendPlayerList(String[] players);
    /**
     * this method is called in pre-game phase each time a player logs in the game
     * @param playersNumber number of players actually in the game
     */
    public abstract void sendPlayersNUmber(int playersNumber);
    /**
     * @param mainBoard send the main board to clients
     */
    public abstract void sendMainBoard(Card[][] mainBoard);
    /**
     * add card on player's board on all clients
     * @param playerID id of the player who has the board
     * @param column column on the board
     * @param cards array of Cards
     */
    public abstract void addCardToClientBoard(String playerID, int column, Card[] cards);
    /**
     * dell card form main board
     * @param cards array of Cards
     */
    public abstract void dellCardFromMainBoard(PositionWithColor[] cards);
    /**
     * this method is called during the start of the game or after the reconnection of a clients
     * @param playerBoards array list of player board
     */
    public abstract void sendAllPlayerBoard(ArrayList<Card[][]> playerBoards);
    /**
     * @param commonGoalID array of id of the common goal id
     */
    public abstract void sendAllCommonGoal(int[] commonGoalID);
    /**
     * @param cards position with color to create on UI the images of common goals
     * @param playerID name of the player
     */
    public abstract void sendPrivateGoal(PositionWithColor[] cards,String playerID);
    /**
     * @param points contains name and points far all the players
     */
    public abstract void sendEndGamePoint(JsonObject points);
    /**
     * @param winner contains name and points of the winner
     */
    public abstract void sendWinner(JsonObject winner);
    /**
     * @param scored contains name and points of the last common goal scored
     */
    public abstract void sendLastCommonScored(JsonObject scored);
    /**
     * @param error contains errorId and errorMsg
     * @param playerID id of the clients the error is for
     */
    public abstract void sendError(JsonObject error, String playerID);
    public abstract void forceClientDisconnection();
    /**
     * send a message in broadcast to all clients
     * @param msg message to send
     * @param sender name of the player who sends the message
     */
    public abstract void sendBroadcastMsg(String msg, String sender);
    /**
     * send a message in private to only one client
     * @param userID id of the player the message is for
     * @param msg message to send
     * @param sender name of the player who send the message
     */
    public abstract void sendPrivateMSG(String userID, String msg, String sender);

    /*************************************************************************
     ************************************************** IN playing methods ******
     * ***********************************************************************
     * method to start game, only the creator of the game can start the game in every moment
     * @param playerID ID of the player call the method
     * @return true if player can start the game false in all other cases
     * @throws RemoteException if server is offline
     */
    public boolean startGame(String playerID){
        return controller.startGame(playerID);
    }
    /**
     * method to take a card from the mainBoard, only the current player can take a card
     * @param column in which column the player want to insert the cards
     * @param cards json string, contains the cards the player wants to pick
     * @param playerID ID of the player call the method
     * @return if the player who calls the method is the current and if the move is valid, false in all other cases
     */
    public boolean takeCard (int column, String cards , String playerID){
        PositionWithColor[] cardsArray = new Gson().fromJson(cards, PositionWithColor[].class);
        boolean bool = controller.takeCard(column, cardsArray, playerID);
        return bool;
    }

    /**************************************************************************
     ************************************************** chat ******************
     * ************************************************************************
     * *
     * receive a message in broadcast to all clients
     * @param msg message to send
     * @param sender name of the player who sends the message
     */
    public void receiveBroadcastMsg(String msg, String sender){
        controller.broadcastMsg(msg,sender);
    }
    /**
     * receive a message in private to only one client
     * @param userID id of the player the message is for
     * @param msg message to send
     * @param sender name of the player who sends the message
     */
    public void receivePrivateMSG(String userID, String msg, String sender){
        controller.privateMSG(userID, msg, sender);
    }
/*************************************************************************
 ************************************************** IN lobby methods ********
 * ***********************************************************************
 * */

    public int login(String ID, String pwd) throws LoginException {

        int msg;
        System.out.println(ID + " on port: "+ PORT + ", logging in");
        msg = lobby.login(ID, pwd);
        return msg;
    }


    public boolean signUp(String ID, String pwd) throws LoginException {

        System.out.println(ID + " on port: "+ PORT + ", trying to sign up");
        lobby.signUp(ID, pwd);
        return false;
    }

    public int joinGame(String ID) throws addPlayerToGameException {

        int msg;

        System.out.println(ID + " on port: "+ PORT + ", trying to join a random game");

        msg = lobby.joinGame(ID, this.connectionType);

        return msg;
    }
    public int joinGame(String ID, String searchID) throws addPlayerToGameException {

        int msg;

        System.out.println(ID + " on port: "+ PORT + ", trying to join a game with player " + searchID);

        msg = lobby.joinGame(ID, this.connectionType, searchID);

        return msg;
    }

    public int createGame(String ID) throws addPlayerToGameException {

        int msg;

        System.out.println(ID + " on port: "+ PORT + ", trying to create a game");

        msg = lobby.createGame(ID, this.connectionType);

        return msg;
    }

    public int createGame(String ID, int maxPlayerNumber) throws addPlayerToGameException {

        int msg;

        System.out.println(ID + " on port: "+ PORT + ", trying to create a game with " + maxPlayerNumber + " players");

        msg = lobby.createGame(ID, this.connectionType, maxPlayerNumber);

        return msg;
    }
}
