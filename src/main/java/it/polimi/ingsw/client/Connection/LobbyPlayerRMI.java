package it.polimi.ingsw.client.Connection;

import it.polimi.ingsw.server.Connection.LobbyRemoteInterface;
import it.polimi.ingsw.server.Exceptions.addPlayerToGameException;
import it.polimi.ingsw.shared.Connection.ConnectionType;

import javax.security.auth.login.LoginException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class LobbyPlayerRMI {

    private int PORT;
    private String serverIP;
    static LobbyRemoteInterface stub;



    public LobbyPlayerRMI(int PORT, String serverIP) throws Exception {
        this.PORT = PORT;
        this.serverIP = serverIP;
        this.connection();
    }

    public void connection() throws Exception {

        try{

            Registry registry = LocateRegistry.getRegistry(serverIP, PORT);
            stub = (LobbyRemoteInterface) registry.lookup("LobbyRemoteInterface");


        }catch(Exception e){
            throw new Exception();
        }

    }

    public static int login(String ID, String pwd) throws LoginException{
        try {
            return stub.login(ID, pwd);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    public static boolean signUp(String ID, String pwd) throws LoginException{
        try {
            return stub.signUp(ID, pwd);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    public static int joinGame(String ID, ConnectionType connectionType) throws addPlayerToGameException {

        try {
            return stub.joinGame(ID, connectionType);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }

    }
    public static int joinGame(String ID, ConnectionType connectionType, String searchID) throws addPlayerToGameException {

        try {
            return stub.joinGame(ID, connectionType, searchID);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }

    }

    public static int createGame(String ID, ConnectionType connectionType) throws addPlayerToGameException {

        try {
            return stub.createGame(ID, connectionType);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }

    }

    public static int createGame(String ID, ConnectionType connectionType, int maxPlayerNumber) throws addPlayerToGameException {

        try {
            return stub.createGame(ID, connectionType, maxPlayerNumber);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }

    }


}
