package it.polimi.ingsw.client.Connection;

import it.polimi.ingsw.Server.Connection.ControllerRemoteInterface;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class PlayingPlayerRMI {

    static ControllerRemoteInterface stub;

    public PlayingPlayerRMI(int PORT, String serverIP, String name){
        this.connection(PORT, serverIP, name);
    }
    public void connection(int PORT, String serverIP, String name){
        try {
            Registry registry = LocateRegistry.getRegistry(serverIP , PORT);
            stub = (ControllerRemoteInterface) registry.lookup("ControllerRemoteInterface");

            PlayingPlayerRMI.startGame(stub, name);
        } catch (Exception e) {
            System.err.println("Client exception: " + e.toString());
            e.printStackTrace();
        }
    }

    public static boolean startGame( ControllerRemoteInterface  stub, String name) throws RemoteException {
        return stub.startGame(name);
    }
}
