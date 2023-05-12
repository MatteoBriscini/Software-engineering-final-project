package it.polimi.ingsw.client.Connection;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import it.polimi.ingsw.client.Player.PlayingPlayer;
import it.polimi.ingsw.server.Connection.RMI.LobbyRemoteInterface;
import it.polimi.ingsw.shared.exceptions.addPlayerToGameException;
import it.polimi.ingsw.shared.JsonSupportClasses.PositionWithColor;

import javax.security.auth.login.LoginException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class ClientRMI extends ConnectionManager implements PlayingPlayerRemoteInterface{

    private final int PORT;
    private final String serverIP;
    private LobbyRemoteInterface stub;
    private String remoteControllerRef;


    public ClientRMI(int PORT, String serverIP) throws Exception {
        super();
        this.PORT = PORT;
        this.serverIP = serverIP;
    }



    public void connection() throws Exception {
        try{
            Registry registry = LocateRegistry.getRegistry(serverIP, PORT);
            stub = (LobbyRemoteInterface) registry.lookup("LobbyRemoteInterface");
        }catch(Exception e){
            throw new Exception();
        }
        Thread thread = new Thread(this::pong);       //start ping pong
        thread.start();

    }

    public void login(String ID, String pwd) throws LoginException{
        String tmp = "null";
        try {
           tmp = stub.login(ID, pwd);
        } catch (RemoteException e) {
            player.disconnectError("server can't respond");
        }
        if(!tmp.equals("null")){
            this.remoteControllerRef = tmp;
            try {
                this.setPlayerAsPlaying();
                stub.joinGameConnection(this, this.playerID, remoteControllerRef);
            } catch (RemoteException e){
                this.setPlayerAsLobby();
                player.disconnectError("server can't respond");
            }
        }
        if(!inGame) {
            try {
                stub.joinLobby(this.playerID);
            } catch (RemoteException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void signUp(String ID, String pwd) throws LoginException{
        try {
            stub.signUp(ID, pwd);
        } catch (RemoteException e) {
            player.disconnectError("server can't respond");
        }
        if(!inGame) {
            try {
                stub.joinLobby(this.playerID);
            } catch (RemoteException e) {
                throw new RuntimeException(e);
            }
        }
    }
    public void joinGame(String ID) throws addPlayerToGameException{
        try {
            this.remoteControllerRef = stub.joinGame(ID);
            stub.joinGameConnection(this, this.playerID, remoteControllerRef);
        } catch (RemoteException e){
            player.disconnectError("server can't respond");
            return;
        }
        this.setPlayerAsPlaying();
    }
    public void joinGame(String ID, String searchID) throws addPlayerToGameException{
        try {
            this.remoteControllerRef = stub.joinGame(ID, searchID);
            stub.joinGameConnection(this, this.playerID, remoteControllerRef);
        } catch (RemoteException e) {
            player.disconnectError("server can't respond");
            return;
        }
        this.setPlayerAsPlaying();
    }

    public void createGame(String ID) throws addPlayerToGameException{
        try {
            this.remoteControllerRef = stub.createGame(ID);
            stub.joinGameConnection(this, this.playerID, remoteControllerRef);
        } catch (RemoteException e) {
            player.disconnectError("server can't respond");
            return;
        }
        this.setPlayerAsPlaying();
    }

    public void createGame(String ID, int maxPlayerNumber) throws addPlayerToGameException{
        try {
            this.remoteControllerRef = stub.createGame(ID, maxPlayerNumber);
            stub.joinGameConnection(this, this.playerID, remoteControllerRef);
        } catch (RemoteException e) {
            player.disconnectError("server can't respond");
            return;
        }
        this.setPlayerAsPlaying();
    }

    /*******************************************************************************
     ************************************************** OUT game methods ***********
     * *****************************************************************************
     * *
     * to quit the game friendly
     * @param playerID this client player id
     * @return true if the quit go in the correct way
     * @throws RemoteException if server is offline
     */
    public boolean quitGame(String  playerID) throws RemoteException{
        synchronized (this) {
            this.notifyAll();
        }
        inGame = false;
        return stub.quitGameConnection(this, playerID, remoteControllerRef);
    }

    /**
     * start the game only the creator can
     * @param playerID this client player id
     * @return true if the method goes in the correct way
     * @throws RemoteException if server are offline
     */
    public boolean startGame(String  playerID) throws RemoteException {
        return stub.startGame( playerID, remoteControllerRef);
    }

    /**
     * @param column column on the current player board
     * @param cards array of taken cards
     * @return true if the method goes in the correct way
     * @throws RemoteException if server are offline
     */
    @Override
    public boolean takeCard(int column, PositionWithColor[] cards) throws RemoteException {
        JsonArray jsonArray = new Gson().toJsonTree(cards).getAsJsonArray();
        return stub.takeCard(column,jsonArray.toString(), playerID, remoteControllerRef);
    }

    /**
     * @return the player id of this client (called by server when necessary)
     */
    public String getPlayerID(){
        return ((PlayingPlayer)player).getPlayerID();
    }

    @Override
    public void ping() throws RemoteException {}
    /**
     * recursive method implements ping pong with server
     */
    private void pong(){
        synchronized (this) {
            try {
                this.wait(pingPongTime);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        try {
            if(inGame) {
                stub.ping();
            }else {//if the player does a friendly quit
                return;
            }
        } catch (RemoteException e) {
            player.disconnectError("server can't respond");
            this.inGame = false;
            return;
        }
        this.pong();
    }
    /**
     * the server can force the disconnection to the clients
     */
    @Override
    public void forceDisconnection(){
        ((PlayingPlayer)player).disconnectError("disconnection forced by the server");
    }

    /**************************************************************************
     ************************************************** chat ******************
     * ************************************************************************
     * *
     * send message in broadcast to all clients
     * @param msg message to send
     * @param sender name of the player who send the message
     */
    public void sendBroadcastMsg(String msg, String sender) throws RemoteException {
        stub.receiveBroadcastMsg(msg, sender, remoteControllerRef);
    }
    /**
     * send a message in private to only one client
     * @param userID id of the player the message is for
     * @param msg message to send
     * @param sender name of the player who send the message
     */
    public void sendPrivateMSG(String userID, String msg, String sender) throws RemoteException {
        stub.receivePrivateMSG(userID, msg, sender, remoteControllerRef);
    }
}
