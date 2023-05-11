package it.polimi.ingsw.server;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import it.polimi.ingsw.server.Lobby.Lobby;
import it.polimi.ingsw.server.Lobby.LobbySupport;
import it.polimi.ingsw.shared.JsonSupportClasses.JsonUrl;

import javax.security.auth.login.LoginException;
import java.io.*;
import java.util.Scanner;


public class ServerMain {

    public static void main(String[] args) {
        System.out.println("server is starting...");
        LobbySupport lobbySupport = new LobbySupport();
        String serverIP = null;
        try {
            serverIP = lobbySupport.jsonCreate();
        } catch (FileNotFoundException e) {
            System.err.println("LOBBY: json file not found");
        }
        System.out.println("try to start server on default ip [" + serverIP + "] want to load default settings [y/n]");
        char command=Character.toUpperCase(new Scanner(System.in).next().charAt(0));
        if(command != 'Y') {
            System.out.println("enter server ip:");
            serverIP=new Scanner(System.in).next();
        }
        System.out.println("\n\n apply settings...\n\n");
        Lobby lobby = new Lobby(serverIP);
    }



}


