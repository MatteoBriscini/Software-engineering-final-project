package it.polimi.ingsw.Server.Connection.RMI;

import it.polimi.ingsw.Server.Connection.ConnectionController;
import it.polimi.ingsw.Server.Connection.RMI.SendCommand.*;
import it.polimi.ingsw.Server.Controller;
import it.polimi.ingsw.Client.Connection.PlayingPlayerRemoteInterface;
import it.polimi.ingsw.Shared.Cards.Card;
import it.polimi.ingsw.Shared.JsonSupportClasses.PositionWithColor;

import java.rmi.AlreadyBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;

public class ControllerRMI extends ConnectionController implements ControllerRemoteInterface {

    ArrayList<PlayingPlayerRemoteInterface> clients = new ArrayList<>();
    ArrayList<String> clientsID = new ArrayList<>();

    public ControllerRMI(Controller controller, int port){
        super(controller, port);
        this.connection();
    }
    synchronized public void connection(){
        ControllerRemoteInterface stub = null;
        try {
            stub = (ControllerRemoteInterface) UnicastRemoteObject.exportObject((Remote) this, PORT);
        }catch (RemoteException e){
            e.printStackTrace();
        }

        Registry registry = null;
        try {
            registry = LocateRegistry.createRegistry(PORT);
        } catch (RemoteException e) {
            // finire ********************************************************
            e.printStackTrace();
        }

        try {
            registry.bind("ControllerRemoteInterface", stub);
        } catch (RemoteException e) {
            // finire ********************************************************
            e.printStackTrace();
        } catch (AlreadyBoundException e) {
            // finire ********************************************************
            e.printStackTrace();
        }
        System.err.println("\u001B[32m" + "Server (rmi) for newGame ready on port: " + PORT + "\u001B[0m");
    }


    /************************************************************************
     ************************************************** IN method ***********
     * ***********************************************************************
     * *
     * client RMI join the game
     * @param client_ref ref to remote obj
     * @return true if the ref can be added false in all other case
     * @throws RemoteException if the server isn't available
     */
    public synchronized boolean joinRMIControllerConnection(PlayingPlayerRemoteInterface client_ref,String playerID) throws RemoteException{
        if(!clients.contains(client_ref)){
            System.out.println("\u001B[36m"+"client: " + playerID + " join the game on port(RMI): " + PORT +"\u001B[0m");

            clients.add(client_ref);
            clientsID.add(playerID);
            controller.setPlayerOnline(playerID);
            return true;
        }
        return false;
    }

    /**
     * client RMI quit friendly connection
     * @param client_ref  ref to remote obj
     * @return true if the ref can be removed false in all other case
     * @throws RemoteException if the server isn't available
     */
    public synchronized boolean quitRMIControllerConnection(PlayingPlayerRemoteInterface client_ref,String playerID) throws RemoteException{
        if(clients.contains(client_ref)){
            clients.remove(client_ref);
            clientsID.remove(playerID);
            controller.setPlayerOffline(playerID);
            return true;
        }
        return false;
    }



    /*************************************************************************
     ************************************************** OUT method ***********
     * ***********************************************************************
     */


    public void sendCommand(Command command){
        for(int i = 0; i<clients.size(); i ++){
            boolean bool= command.execute(clients.get(i));
            if(!bool) {
                try {
                    this.quitRMIControllerConnection(clients.get(i), clientsID.get(i));
                } catch (RemoteException ex) {
                    throw new RuntimeException(ex);
                }
            }else {
                controller.setPlayerOnline(clientsID.get(i));
            }
        }
    }

    public void notifyActivePlayer(String activePlayerID){
        Command command = new NotifyActivePlayerCommand(activePlayerID);
        sendCommand(command);
    }

    public void sendPlayerList(String[] players){
        Command command = new SendActivePlayerListCommand(players);
        sendCommand(command);
    }

    public void sendPlayersNUmber(int playersNumber){
        Command command = new SendPlayersNumberCommand(playersNumber);
        sendCommand(command);
    }

    public void sendMainBoard(Card[][] mainBoard){
        Command command = new SendMainBoardCommand(mainBoard);
        sendCommand(command);
    }

    public void addCardToClientBoard(String playerID, int column, Card[] cards){
        Command command = new AddCardToClientBoard(playerID, column, cards);
        sendCommand(command);
    }

    public void dellCardFromMainBoard(PositionWithColor[] cards){
        Command command = new DellCardFromMainBoard(cards);
        sendCommand(command);
    }


}
