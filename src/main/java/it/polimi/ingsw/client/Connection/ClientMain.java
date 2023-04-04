package it.polimi.ingsw.client.Connection;

public class ClientMain {
    static String serverIP = "127.0.0.1";


    public static void main(String[] args) {
        /**
        PlayingPlayerRMI tmp = new PlayingPlayerRMI(1233, serverIP, "carlo");
        tmp = new PlayingPlayerRMI(1234, serverIP, "antonio");
         */

        LobbyPlayerRMI tmp = new LobbyPlayerRMI(1234, serverIP);

    }

}
