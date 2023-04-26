package it.polimi.ingsw.Client.Connection;

import com.google.gson.Gson;
import com.google.gson.*;
import it.polimi.ingsw.Client.Player.PlayingPlayer;
import it.polimi.ingsw.Server.Connection.RMI.ControllerRemoteInterface;
import it.polimi.ingsw.Shared.JsonSupportClasses.PositionWithColor;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;


public class PlayingPlayerRMI extends PlayingPlayerConnectionManager implements PlayingPlayerRemoteInterface{
    private static ControllerRemoteInterface stub;
    private boolean inGame = true;
    private int pingPongTime = 5000;
    private final String playerID;

    //constructor for testing
    public PlayingPlayerRMI(int PORT, String serverIP, String playerID, PlayingPlayer playingPlayer) throws Exception {
        super(playingPlayer);
        this.playerID = playerID;
        this.connection(PORT, serverIP);
    }
    public void connection(int PORT, String serverIP) throws RemoteException, NotBoundException {
            Registry registry = LocateRegistry.getRegistry(serverIP , PORT);
            stub = (ControllerRemoteInterface) registry.lookup("ControllerRemoteInterface");
            stub.joinRMIControllerConnection(this, playerID);

        Thread thread = new Thread(this::pong);       //start ping pong
        thread.start();
    }
    public void ping(){}

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
            }else {//if the player do a friendly quit
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
     */
    public boolean quitGame(String  playerID) throws RemoteException{
        synchronized (this) {
            this.notifyAll();
        }
        inGame = false;
        return stub.quitRMIControllerConnection(this, playerID);
    }
    public boolean startGame(String  playerID) throws RemoteException {
        return stub.startGame( playerID);
    }

    @Override

    public boolean takeCard(int column, PositionWithColor[] cards) throws RemoteException {
        JsonArray jsonArray = new Gson().toJsonTree(cards).getAsJsonArray();
        return stub.takeCard(column,jsonArray.toString(), playerID);
    }

    @Override
    public void forceDisconnection(){
        playingPlayer.disconnectError("disconnection forced by the server");
    }


}
