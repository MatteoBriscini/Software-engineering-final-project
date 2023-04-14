package it.polimi.ingsw.Client.Connection;

import com.google.gson.Gson;
import com.google.gson.*;
import it.polimi.ingsw.Client.Player.PlayingPlayer;
import it.polimi.ingsw.Server.Connection.RMI.ControllerRemoteInterface;
import it.polimi.ingsw.Shared.JsonSupportClasses.PositionWithColor;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;


public class PlayingPlayerRMI extends PlayingPlayerConnectionManager implements PlayingPlayerRemoteInterface{
    static ControllerRemoteInterface stub;

    private final String playerID;

    public PlayingPlayerRMI(int PORT, String serverIP, String playerID, PlayingPlayer playingPlayer) throws RemoteException{
        super(playingPlayer);
        this.playerID = playerID;
        this.connection(PORT, serverIP);
    }
    public void connection(int PORT, String serverIP){
        try {
            Registry registry = LocateRegistry.getRegistry(serverIP , PORT);
            stub = (ControllerRemoteInterface) registry.lookup("ControllerRemoteInterface");


        } catch (Exception e) {
            System.err.println("Client exception: " + e.toString());
            e.printStackTrace();
        }


        //send remote ref to server
        try {
            stub.joinRMIControllerConnection(this, playerID);
        } catch (RemoteException e) {

            throw new RuntimeException(e);
        }

        try {
            startGame(playerID);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    /*************************************************************************
     ************************************************** OUT method ***********
     * ***********************************************************************
     */
    public boolean quitGame(String  playerID) throws RemoteException{
        return stub.quitRMIControllerConnection(this, playerID);
    }
    public static boolean startGame(String  playerID) throws RemoteException {
        return stub.startGame( playerID);
    }
    public boolean takeCard(int column, PositionWithColor[] cards) throws RemoteException {
        Gson gson = new Gson();
        JsonArray jsonArray = new Gson().toJsonTree(cards).getAsJsonArray();
        return stub.takeCard(column,jsonArray.toString(), playerID);
    }



}
