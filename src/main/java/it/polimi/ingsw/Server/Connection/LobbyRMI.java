package it.polimi.ingsw.Server.Connection;

import it.polimi.ingsw.Server.Exceptions.addPlayerToGameException;
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
    protected int PORT;

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

            stub = (LobbyRemoteInterface) UnicastRemoteObject.exportObject((Remote) this, this.PORT);
        }catch (RemoteException e){
            throw new RuntimeException(e);
        }

        Registry registry = null;
        try {
            registry = LocateRegistry.createRegistry(this.PORT);
        } catch (RemoteException e) {
            System.out.println(e);
            throw new RuntimeException(e);
        }

        try {
            registry.bind("LobbyRemoteInterface", stub);
        } catch (RemoteException | AlreadyBoundException e) {
            // finire ********************************************************
            System.out.println(e.toString());
            throw new RuntimeException(e);
        }

        System.err.println("Server (rmi) for lobby ready on port: " + PORT);
    }

    @Override
    public int login(String ID, String pwd) throws LoginException {

        int msg;
        System.out.println(ID + " on port: "+ PORT + ", logging in");
        msg = lobby.login(ID, pwd);
        return msg;
    }

    @Override
    public boolean signUp(String ID, String pwd) throws LoginException {

        System.out.println(ID + " on port: "+ PORT + ", trying to sign up");
        lobby.signUp(ID, pwd);
        return false;
    }

    public int joinGame(String ID, ConnectionType connectionType) throws addPlayerToGameException {

        int msg;

        System.out.println(ID + " on port: "+ PORT + ", trying to join a random game");

        msg = lobby.joinGame(ID, connectionType);

        return msg;
    }
    public int joinGame(String ID, ConnectionType connectionType, String searchID) throws addPlayerToGameException {

        int msg;

        System.out.println(ID + " on port: "+ PORT + ", trying to join a game with player " + searchID);

        msg = lobby.joinGame(ID, connectionType, searchID);

        return msg;
    }

    public int createGame(String ID, ConnectionType connectionType) throws addPlayerToGameException {

        int msg;

        System.out.println(ID + " on port: "+ PORT + ", trying to create a game");

        msg = lobby.createGame(ID, connectionType);

        return msg;
    }

    public int createGame(String ID, ConnectionType connectionType, int maxPlayerNumber) throws addPlayerToGameException {

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
