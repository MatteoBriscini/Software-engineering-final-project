package it.polimi.ingsw.Server;

import it.polimi.ingsw.Shared.Connection.ConnectionType;
import it.polimi.ingsw.Server.Exceptions.ConnectionControllerManagerException;

public class ServerMain {

    public static void main(String[] args) {

       Controller game1 =new Controller();
        try {
            game1.addClient(1233, ConnectionType.RMI);
        } catch (ConnectionControllerManagerException e) {
            throw new RuntimeException(e);
        }
        Controller game2 =new Controller();
        try {
            game2.addClient(1234, ConnectionType.RMI);
        } catch (ConnectionControllerManagerException e) {
            throw new RuntimeException(e);
        }

        try {
            game2.addClient(1245, ConnectionType.SOCKET);
        } catch (ConnectionControllerManagerException e) {
            throw new RuntimeException(e);
        }


    }


}


