package it.polimi.ingsw.Server;

import it.polimi.ingsw.Server.Connection.ControllerRMI;

public class ServerMain {

    public static void main(String[] args) {
       Controller game1 =new Controller();
       game1.createRMIConnection(1233);
       Controller game2 =new Controller();
       game2.createRMIConnection(1234);

       game2.createSOCKETConnection(1245);
    }
}
