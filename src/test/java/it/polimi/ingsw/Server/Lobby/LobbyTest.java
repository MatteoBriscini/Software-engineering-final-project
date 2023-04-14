package it.polimi.ingsw.Server.Lobby;

import it.polimi.ingsw.Server.Controller;
import it.polimi.ingsw.Server.Exceptions.addPlayerToGameException;
import it.polimi.ingsw.Shared.Connection.ConnectionType;
import junit.framework.TestCase;

import javax.security.auth.login.LoginException;
import java.util.ArrayList;

public class LobbyTest extends TestCase {


    Lobby testLobby = new Lobby();


    public void testLogin() {

        String ID = "john";
        String pwd = "doe";

        try {
            testLobby.login(ID, pwd);
        } catch (LoginException e) {
            throw new RuntimeException(e);
        }

    }

    public void testSignUp() {

        String ID = "rat";
        String pwd = "king";

        try {
            testLobby.signUp(ID, pwd);
        } catch (LoginException e) {
            throw new RuntimeException(e);
        }

    }


    public void testCreateGame(){
        ArrayList<Controller> test= testLobby.getActiveGames();

        System.out.println("Start test");

        testLobby.createGame("riccardo", ConnectionType.RMI);
        testLobby.createGame("matteo", ConnectionType.RMI);

        Controller controller = test.get(0);
        Controller controller2 = test.get(1);

        try {
            controller.addNewPlayer("riccardo");
            controller2.addNewPlayer("matteo");
            controller2.addNewPlayer("davide");
        } catch (addPlayerToGameException e) {
            throw new RuntimeException(e);
        }

        System.out.println(controller.getPlayerNumber());
        System.out.println(controller2.getPlayerNumber());

        assert(controller.getPlayerNumber() == 1);
        assert(controller2.getPlayerNumber() == 2);
        assert(test.size() == 2);

        System.out.println("End test");

    }

    public void testJoinGame(){

        testLobby.createGame("riccardo", ConnectionType.RMI);
        testLobby.joinGame("ing", ConnectionType.RMI);

    }

}