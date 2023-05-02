package it.polimi.ingsw.server;

import it.polimi.ingsw.shared.Connection.ConnectionType;
import it.polimi.ingsw.server.Exceptions.ConnectionControllerManagerException;


public class ServerMain {

    public static void main(String[] args) {

        //Lobby lobby1 = new Lobby();



       Controller game1 =new Controller();
        try {
            game1.addClient(1200, ConnectionType.RMI);
        } catch (ConnectionControllerManagerException e) {
            throw new RuntimeException(e);
        }
        Controller game2 =new Controller();
        try {
            game2.addClient(1201, ConnectionType.RMI);
        } catch (ConnectionControllerManagerException e) {
            throw new RuntimeException(e);
        }

        try {
            game2.addClient(1202, ConnectionType.SOCKET);
        } catch (ConnectionControllerManagerException e) {
            throw new RuntimeException(e);
        }




    }


}


