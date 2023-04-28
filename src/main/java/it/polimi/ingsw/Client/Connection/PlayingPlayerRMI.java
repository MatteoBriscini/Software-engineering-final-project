package it.polimi.ingsw.Client.Connection;

import com.google.gson.Gson;
import com.google.gson.*;
import it.polimi.ingsw.Client.Player.PlayingPlayer;
import it.polimi.ingsw.Server.Connection.RMI.ControllerRemoteInterface;
import it.polimi.ingsw.Shared.JsonSupportClasses.JsonUrl;
import it.polimi.ingsw.Shared.JsonSupportClasses.PositionWithColor;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class PlayingPlayerRMI extends PlayingPlayerConnectionManager implements PlayingPlayerRemoteInterface{
    /**
     * parameters
     */
    private static ControllerRemoteInterface stub;
    private boolean inGame = true;
    private int pingPongTime;
    private final String playerID;
    private JsonUrl jsonUrl;

    //constructor for testing
    public PlayingPlayerRMI(int PORT, String serverIP, String playerID, PlayingPlayer playingPlayer) throws Exception {
        super(playingPlayer);
        this.playerID = playerID;
        this.connection(PORT, serverIP);

        try {
            jsonCreate();
        } catch (FileNotFoundException e) {
            System.out.println("PlayingPlayerRMI: JSON FILE NOT FOUND");
            throw new RuntimeException(e);
        }
    }
    /**
     * download json file
     * @throws FileNotFoundException if method can't file json file
     */
    private void jsonCreate() throws FileNotFoundException {  //download json data
        InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream(jsonUrl.getUrl("netConfig"));
        if(inputStream == null) throw new FileNotFoundException();
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        JsonObject jsonObject = new Gson().fromJson(bufferedReader , JsonObject.class);
        this.pingPongTime = jsonObject.get("pingPongTime").getAsInt();
    }
    /**
     * start RMi connection
     */
    public void connection(int PORT, String serverIP) throws RemoteException, NotBoundException {
            Registry registry = LocateRegistry.getRegistry(serverIP , PORT);
            stub = (ControllerRemoteInterface) registry.lookup("ControllerRemoteInterface");
            stub.joinRMIControllerConnection(this, playerID);

        Thread thread = new Thread(this::pong);       //start ping pong
        thread.start();
    }
    /**
     * method called from the server in ping pong
     */
    public void ping(){}
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
            playingPlayer.disconnectError("server can't respond");
            return;
        }
        this.pong();
    }

    /*************************************************************************
     ************************************************** OUT method ***********
     * ***********************************************************************
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
        return stub.quitRMIControllerConnection(this, playerID);
    }

    /**
     * start the game only the creator can
     * @param playerID this client player id
     * @return true if the method goes in the correct way
     * @throws RemoteException if server is offline
     */
    public boolean startGame(String  playerID) throws RemoteException {
        return stub.startGame( playerID);
    }

    /**
     * @param column column on the current player board
     * @param cards array of taken cards
     * @return true if the method goes in the correct way
     * @throws RemoteException if server is offline
     */
    @Override
    public boolean takeCard(int column, PositionWithColor[] cards) throws RemoteException {
        JsonArray jsonArray = new Gson().toJsonTree(cards).getAsJsonArray();
        return stub.takeCard(column,jsonArray.toString(), playerID);
    }

    /**
     * @return the player id of this client (called by server when necessary)
     */
    public String getPlayerID(){
        return playingPlayer.getPlayerID();
    }

    /**
     * the server can force the disconnection to the clients
     */
    @Override
    public void forceDisconnection(){
        playingPlayer.disconnectError("disconnection forced by the server");
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
        stub.receiveBroadcastMsg(msg, sender);
    }
    /**
     * send a message in private to only one client
     * @param userID id of the player the message is for
     * @param msg message to send
     * @param sender name of the player who send the message
     */
    public void sendPrivateMSG(String userID, String msg, String sender) throws RemoteException {
        stub.receivePrivateMSG(userID, msg, sender);
    }
}
