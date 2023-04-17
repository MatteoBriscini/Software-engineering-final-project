package it.polimi.ingsw.Client;

import it.polimi.ingsw.Client.Connection.LobbyPlayerRMI;

public class ClientMain {
    static String serverIP = "127.0.0.1";



    public static void main(String[] args) {
        LobbyPlayerRMI tmp = null;
        tmp = new LobbyPlayerRMI(1234, serverIP);
        int test = 0;
        LobbyPlayerRMI.signUp("john", "doe");
        System.out.println(test);

    }

}
