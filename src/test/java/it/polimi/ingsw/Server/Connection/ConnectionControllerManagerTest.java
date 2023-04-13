package it.polimi.ingsw.Server.Connection;

import it.polimi.ingsw.Server.Controller;
import it.polimi.ingsw.Server.Exceptions.ConnectionControllerManagerException;
import it.polimi.ingsw.Shared.Connection.ConnectionType;
import junit.framework.TestCase;

public class ConnectionControllerManagerTest extends TestCase {
    ConnectionControllerManager test = new ConnectionControllerManager();
    Controller controller = new Controller();
    public void testAddClient() throws ConnectionControllerManagerException {
        test.addClient(1234, ConnectionType.RMI, controller);
        assert(test.isRmiActive());
        assert(test.getInterfaces().size()==1);
        test.addClient(1235, ConnectionType.RMI, controller);
        assert(test.isRmiActive());
        assert(test.getInterfaces().size()==1);
        //test.addClient(1235, ConnectionType.SOCKET, controller);
    }
}