package it.polimi.ingsw.Server.Connection;

import it.polimi.ingsw.Server.Controller;
import it.polimi.ingsw.Server.Lobby.Lobby;

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

        System.out.println(ID + " on port: "+ PORT);
        try {
            lobby.login(ID, pwd);
        } catch (LoginException e) {
            throw new RuntimeException(e);
        }
        return PORT;
    }

    @Override
    public boolean signUp(String ID, String pwd) throws RemoteException {

        System.out.println(ID + " on port: "+ PORT + ", trying to sign up");
        lobby.signUp(ID, pwd);
        return false;
    }
}
