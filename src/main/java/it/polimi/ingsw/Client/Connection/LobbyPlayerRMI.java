package it.polimi.ingsw.Client.Connection;

import it.polimi.ingsw.Server.Connection.LobbyRemoteInterface;
import it.polimi.ingsw.Server.Lobby.PlayerLogin;
import it.polimi.ingsw.Shared.Connection.ConnectionType;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class LobbyPlayerRMI {

    private int PORT;
    private String serverIP;
    static LobbyRemoteInterface stub;

    //temp for testing

    private String ID = new String("Papa");
    private String pwd = new String("caspiterina");


    public LobbyPlayerRMI(int PORT, String serverIP){
        this.PORT = PORT;
        this.serverIP = serverIP;
        this.connection();
    }

    public void connection(){

        try{

            Registry registry = LocateRegistry.getRegistry(serverIP, PORT);
            stub = (LobbyRemoteInterface) registry.lookup("LobbyRemoteInterface");


        }catch(Exception e){
            System.err.println("Client exception: " + e.toString());
            e.printStackTrace();
        }

    }

    public static int login(String ID, String pwd){
        try {
            return stub.login(ID, pwd);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    public static boolean signUp(String ID, String pwd){
        try {
            return stub.signUp(ID, pwd);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    public static int joinGame(String ID, ConnectionType connectionType){
        try {
            return stub.joinGame(ID, connectionType); //TODO exception
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }
    public static int joinGame(String ID, ConnectionType connectionType, String searchID){
        try {
            return stub.joinGame(ID, connectionType, searchID); //TODO exception
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    public static int createGame(String ID, ConnectionType connectionType){
        try {
            return stub.createGame(ID, connectionType);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    public static int createGame(String ID, ConnectionType connectionType, int maxPlayerNumber){
        try {
            return stub.createGame(ID, connectionType, maxPlayerNumber);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }


}
