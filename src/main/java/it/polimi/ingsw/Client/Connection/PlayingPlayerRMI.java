package it.polimi.ingsw.Client.Connection;

import it.polimi.ingsw.Server.Connection.RMI.ControllerRemoteInterface;
import it.polimi.ingsw.Shared.Cards.Card;
import it.polimi.ingsw.Shared.JsonSupportClasses.PositionWithColor;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class PlayingPlayerRMI extends PlayingPlayerController implements PlayingPlayerRemoteInterface{
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
    public void notifyActivePlayer(String activePlayerID) {

    }

    @Override
    public void receivePlayerList(String[] playersID) throws RemoteException {

    }

    @Override
    public void receivePlayersNumber(int playersNumber) throws RemoteException {

    }

    @Override
    public void receiveMainBoard(Card[][] mainBoard) throws RemoteException {

    }

    @Override
    public void addCardToPlayerBoard(String playerID, int column, Card[] cards) {

    }

    @Override
    public void dellCardFromMainBoard(PositionWithColor[] cards) {

    }


}
