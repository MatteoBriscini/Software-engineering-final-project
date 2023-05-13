package it.polimi.ingsw.server.Lobby;

import junit.framework.TestCase;

import javax.security.auth.login.LoginException;
import java.util.ArrayList;

public class LobbyTest extends TestCase {

    private Lobby testLobby;

    public void testLobby(){

        testLobby = new Lobby(2000, 1999);

        System.out.println("Test 1");
        String ID = "john";
        String pwd = "doe";

        try {
            testLobby.login(ID, pwd);
        } catch (LoginException e) {
            System.out.println(e);
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

        ArrayList<String> log = testLobby.getPlayersLoggedOn();
        System.out.println(log);

        System.out.println("Test 3");
        ID = "john";
        pwd = "doe";

        try {
            testLobby.login(ID, pwd);
        } catch (LoginException e) {
            System.out.println(e);
        }


        ID = "rat";
        pwd = "king";

        try {
            testLobby.login(ID, pwd);
        } catch (LoginException e) {
            System.out.println(e);
        }

        System.out.println("Test 3 end");



        log = testLobby.getPlayersLoggedOn();
        System.out.println(log);


/*
        testLobby = new Lobby(2000 , 1999);

        System.out.println("Test 1");
        String ID = "john";
        String pwd = "doe";

        try {
            testLobby.login(ID, pwd);
        } catch (LoginException e) {
            System.out.println(e);
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


        try {
            testLobby.createGame("riccardo", ConnectionType.RMI);
        } catch (addPlayerToGameException e) {
            System.out.println(e);
        }
        try {
            testLobby.createGame("matteo", ConnectionType.RMI);
        } catch (addPlayerToGameException e) {
            System.out.println(e);
        }


        Controller controller = test.get(0);
        Controller controller2 = test.get(1);

        try {
            testLobby.joinGame("davide", ConnectionType.RMI, "matteo");
        } catch (addPlayerToGameException e) {
            System.out.println(e);
        }

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
        try {
            testLobby.createGame("gianni", ConnectionType.RMI);
        } catch (addPlayerToGameException e) {
            System.out.println(e);
        }
        try {
            testLobby.joinGame("ing", ConnectionType.RMI);
        } catch (addPlayerToGameException e) {
            System.out.println(e);
        }

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
*/
    }
}