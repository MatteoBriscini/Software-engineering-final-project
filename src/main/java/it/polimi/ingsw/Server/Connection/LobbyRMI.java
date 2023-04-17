package it.polimi.ingsw.Server.Connection;

import it.polimi.ingsw.Server.Lobby.Lobby;
import it.polimi.ingsw.Shared.Connection.ConnectionType;

import javax.security.auth.login.LoginException;
import java.rmi.AlreadyBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class LobbyRMI implements LobbyRemoteInterface{

    protected final Lobby lobby;
    protected final int PORT;

    protected final String cntType;

    public LobbyRMI(int port, Lobby lobby){
        this.lobby = lobby;
        this.PORT = port;
        this.cntType = "RMI";
        this.connection();

    }

    synchronized public void connection(){
        LobbyRemoteInterface stub = null;

        try {
            stub = (LobbyRemoteInterface) UnicastRemoteObject.exportObject((Remote) this, PORT);
        }catch (RemoteException e){
            System.out.println(e.toString());
        }

        Registry registry = null;
        try {
            registry = LocateRegistry.createRegistry(PORT);
        } catch (RemoteException e) {
            System.out.println(e.toString());
        }

        try {
            registry.bind("LobbyRemoteInterface", stub);
        } catch (RemoteException | AlreadyBoundException e) {
            // finire ********************************************************
            System.out.println(e.toString());
        }

        System.err.println("Server (rmi) for lobby ready on port: " + PORT);
    }

    @Override
    public int login(String ID, String pwd) throws RemoteException {

        int msg;
        System.out.println(ID + " on port: "+ PORT + ", logging in");
        try {
            msg = lobby.login(ID, pwd);
        } catch (LoginException e) {
            throw new RuntimeException(e);
        }
        return msg;
    }

    @Override
    public boolean signUp(String ID, String pwd) throws RemoteException {

        System.out.println(ID + " on port: "+ PORT + ", trying to sign up");
        try {
            lobby.signUp(ID, pwd);
        } catch (LoginException e) {
            throw new RuntimeException(e);
        }
        return false;
    }

    public int joinGame(String ID, ConnectionType connectionType){

        int msg;

        System.out.println(ID + " on port: "+ PORT + ", trying to join a random game");

        msg = lobby.joinGame(ID, connectionType);

        return msg;
    }
    public int joinGame(String ID, ConnectionType connectionType, String searchID){

        int msg;

        System.out.println(ID + " on port: "+ PORT + ", trying to join a game with player " + searchID);

        msg = lobby.joinGame(ID, connectionType, searchID);

        return msg;
    }

    public int createGame(String ID, ConnectionType connectionType){

        int msg;

        System.out.println(ID + " on port: "+ PORT + ", trying to create a game");

        msg = lobby.createGame(ID, connectionType);

        return msg;
    }

    public int createGame(String ID, ConnectionType connectionType, int maxPlayerNumber)
    {

        int msg;

        System.out.println(ID + " on port: "+ PORT + ", trying to create a game with " + maxPlayerNumber + " players");

        msg = lobby.createGame(ID, connectionType, maxPlayerNumber);

        return msg;
    }

    /*

    RMI PORT 1234
    SOCKET PORT 1235

     */

}