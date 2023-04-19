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
    static ControllerRemoteInterface stub;

    private final String playerID;

    public PlayingPlayerRMI(int PORT, String serverIP, String playerID, PlayingPlayer playingPlayer) throws Exception {
        super(playingPlayer);
        this.playerID = playerID;
        this.connection(PORT, serverIP);
    }
    public void connection(int PORT, String serverIP) throws RemoteException, NotBoundException {
            Registry registry = LocateRegistry.getRegistry(serverIP , PORT);
            stub = (ControllerRemoteInterface) registry.lookup("ControllerRemoteInterface");
            stub.joinRMIControllerConnection(this, playerID);
    }

    /*************************************************************************
     ************************************************** OUT method ***********
     * ***********************************************************************
     */
    public boolean quitGame(String  playerID) throws RemoteException{
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
        try {
            this.quitGame(playingPlayer.getPlayerID());
        } catch (RemoteException e) {
            playingPlayer.disconnectError("server don't respond");
            return;
        }
        playingPlayer.disconnectError("disconnection forced by the server");
    }


}
