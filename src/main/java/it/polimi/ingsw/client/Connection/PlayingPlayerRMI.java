package it.polimi.ingsw.client.Connection;

import it.polimi.ingsw.Server.Connection.ControllerRemoteInterface;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class PlayingPlayerRMI extends UnicastRemoteObject implements PlayingPlayerRemoteInterface{
    static ControllerRemoteInterface stub;

    private String playerID = "anthony";

    public PlayingPlayerRMI(int PORT, String serverIP, String playerID) throws RemoteException{
        this.playerID = playerID;
        this.connection(PORT, serverIP, playerID);
    }
    public void connection(int PORT, String serverIP, String name){
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
    }

    /*************************************************************************
     ************************************************** OUT method ***********
     * ***********************************************************************
     */

    public boolean quitGame() throws RemoteException{
        return stub.quitRMIControllerConnection(this, playerID);
    }
    public static boolean startGame(String name) throws RemoteException {
        return stub.startGame(name);
    }



    /************************************************************************
     ************************************************** IN method ***********
     * ***********************************************************************
    */

    @Override
    public void notifyActivePlayer(int activePlayerID) {
        System.out.println(activePlayerID);
    }

    @Override
    public void receivePlayerList(String[] playersID) throws RemoteException {

    }
}
