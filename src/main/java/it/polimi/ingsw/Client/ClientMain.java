package it.polimi.ingsw.Client;

import it.polimi.ingsw.Client.Connection.LobbyPlayerRMI;

import javax.security.auth.login.LoginException;

public class ClientMain {
    static String serverIP = "127.0.0.1";



    public static void main(String[] args) {
        LobbyPlayerRMI tmp = null;
        tmp = new LobbyPlayerRMI(1234, serverIP);
        int test = 0;
        try {
            LobbyPlayerRMI.signUp("john", "doe");
        } catch (LoginException e) {
            System.out.println(e);
        }
        System.out.println(test);

    }

}
