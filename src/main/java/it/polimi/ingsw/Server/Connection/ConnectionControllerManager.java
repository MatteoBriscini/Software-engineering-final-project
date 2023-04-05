package it.polimi.ingsw.Server.Connection;

import it.polimi.ingsw.Server.Controller;
import it.polimi.ingsw.Server.Exceptions.ConnectionControllerManagerException;

import java.util.ArrayList;

public class ConnectionControllerManager {

    private final ArrayList<ConnectionController> interfaces = new ArrayList<>();
    boolean rmiActive = false;
    boolean socketActive = false;


    /**
     * create new connection class for controller when necessary
     * @param PORT available port
     * @param connectionType rmi or socket
     * @param controller game reference
     * @return true if the new port will be used false in all other case
     * @throws ConnectionControllerManagerException if connection type has an invalid parameters
     */
    public boolean addClient(int PORT, String connectionType, Controller controller) throws ConnectionControllerManagerException {
        switch (connectionType){
            case "RMI":
                if(!rmiActive) {
                    rmiActive = true;
                    interfaces.add(new ControllerRMI(controller, PORT));
                    return true;
                }
                break;
            case "SOCKET":
                if(!socketActive) {
                    socketActive = true;
                    interfaces.add(new ControllerSOCKET(controller, PORT));
                    return true;
                }
                break;
            default: throw new ConnectionControllerManagerException("invalid connectionType (use: RMI or SOCKET)");
        }
        return false;
    }
}
