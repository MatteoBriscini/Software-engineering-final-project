package it.polimi.ingsw.Server.Connection;



import it.polimi.ingsw.Server.Controller;

import java.io.IOException;
import java.rmi.AlreadyBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class ControllerRMI implements ControllerRemoteInterface{
    protected final Controller controller;
    protected final int PORT;

    public ControllerRMI(Controller controller, int port){
        this.controller = controller;
        this.PORT = port;
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

        System.err.println("Server (rmi) for newGame ready on port: " + PORT);
    }
    @Override
    public boolean startGame(String playerID) throws RemoteException {
        System.out.println(playerID + " on port: "+ PORT);

        return false;
    }
}
