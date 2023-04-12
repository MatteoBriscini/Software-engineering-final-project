package it.polimi.ingsw.Server;

import it.polimi.ingsw.Server.Exceptions.ConnectionControllerManagerException;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
public class ServerMain {

    public static void main(String[] args) {

       Controller game1 =new Controller();
        try {
            game1.addClient(1233, "RMI");
        } catch (ConnectionControllerManagerException e) {
            throw new RuntimeException(e);
        }
        Controller game2 =new Controller();
        try {
            game2.addClient(1234, "RMI");
        } catch (ConnectionControllerManagerException e) {
            throw new RuntimeException(e);
        }

        try {
            game2.addClient(1245, "SOCKET");
        } catch (ConnectionControllerManagerException e) {
            throw new RuntimeException(e);
        }


    }


}


