package it.polimi.ingsw.Server.Lobby;

import it.polimi.ingsw.Server.Controller;
import it.polimi.ingsw.Server.Exceptions.addPlayerToGameException;
import it.polimi.ingsw.Shared.Connection.ConnectionType;
import junit.framework.TestCase;

import javax.security.auth.login.LoginException;
import java.util.ArrayList;

public class LobbyTest extends TestCase {

    private final Lobby testLobby = new Lobby();

    public void testLobby(){

        System.out.println("Test 1");
        String ID = "john";
        String pwd = "doe";

        try {
            testLobby.login(ID, pwd);
        } catch (LoginException e) {
            throw new RuntimeException(e);
        }
        System.out.println("Test 1 end");



        System.out.println("Test 2");
        ID = "rat";
        pwd = "king";

        try {
            testLobby.signUp(ID, pwd);
        } catch (LoginException e) {
            System.out.println("User already signed up");
        }
        System.out.println("Test 2 end");



        System.out.println("Test 3");
        ArrayList<Controller> test= testLobby.getActiveGames();
        ArrayList<String[]> players = testLobby.getPlayersInGames();


        testLobby.createGame("riccardo", ConnectionType.RMI);
        testLobby.createGame("matteo", ConnectionType.RMI);


        Controller controller = test.get(0);
        Controller controller2 = test.get(1);

        testLobby.joinGame("davide", ConnectionType.RMI, "matteo");

        int i = 1;
        for(String[] s : players) {

            System.out.println("Game" + i);
            for (String value : s) {
                System.out.println(value);
            }
            System.out.println("\n");
            i++;
        }

        System.out.println(controller.getPlayerNumber());
        System.out.println(controller2.getPlayerNumber());

        assert(controller.getPlayerNumber() == 1);
        assert(controller2.getPlayerNumber() == 2);
        assert(test.size() == 2);

        System.out.println("Test 3 end");


        testLobby.closeAllGames();


        System.out.println("Test 4");
        testLobby.createGame("gianni", ConnectionType.RMI);
        testLobby.joinGame("ing", ConnectionType.RMI);

        players = testLobby.getPlayersInGames();
        i = 1;
        for(String[] s : players) {
            System.out.println("Game" + i);
            for (String value : s) {
                System.out.println(value);
            }
            System.out.println("\n");
            i++;
        }

        Controller controller3 = test.get(2);
        System.out.println(controller.getPlayerNumber());
        System.out.println(controller2.getPlayerNumber());
        System.out.println(controller3.getPlayerNumber());

        System.out.println("Test 4 end");

    }


    /*
    public void testLogin() {
        System.out.println("Test 1");
        String ID = "john";
        String pwd = "doe";

        try {
            testLobby.login(ID, pwd);
        } catch (LoginException e) {
            throw new RuntimeException(e);
        }
        System.out.println("Test 1 end");
    }

    public void testSignUp() {
        System.out.println("Test 2");
        String ID = "rat";
        String pwd = "king";

        try {
            testLobby.signUp(ID, pwd);
        } catch (LoginException e) {
            throw new RuntimeException(e);
        }
        System.out.println("Test 2 end");

    }


    public void testCreateGame(){
        System.out.println("Test 3");
        ArrayList<Controller> test= testLobby.getActiveGames();
        ArrayList<String[]> players = testLobby.getPlayersInGames();


        testLobby.createGame("riccardo", ConnectionType.RMI);
        testLobby.createGame("matteo", ConnectionType.RMI);


        Controller controller = test.get(0);
        Controller controller2 = test.get(1);

        testLobby.joinGame("davide", ConnectionType.RMI, "matteo");

        for(String[] s : players) {
            for(int i = 0; i < s.length; i++){
                System.out.println(s[i]);
            }
        }

        System.out.println(controller.getPlayerNumber());
        System.out.println(controller2.getPlayerNumber());

        assert(controller.getPlayerNumber() == 1);
        assert(controller2.getPlayerNumber() == 2);
        assert(test.size() == 2);

        System.out.println("Test 3 end");

    }

    public void testJoinGame(){
        System.out.println("Test 4");
        testLobby.createGame("gianni", ConnectionType.RMI);
        testLobby.joinGame("ing", ConnectionType.RMI);
        System.out.println("Test 4 end");
    }


     */
}