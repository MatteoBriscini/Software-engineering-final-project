package it.polimi.ingsw.server.Connection;

import it.polimi.ingsw.client.ClientMain;
import it.polimi.ingsw.client.Connection.ClientSOCKET;
import it.polimi.ingsw.client.Connection.ConnectionManager;
import it.polimi.ingsw.client.Player.LobbyPlayer;
import it.polimi.ingsw.client.Player.Player;
import it.polimi.ingsw.client.Player.PlayingPlayer;
import it.polimi.ingsw.server.Controller;
import it.polimi.ingsw.server.Exceptions.ConnectionControllerManagerException;
import it.polimi.ingsw.server.Lobby.Lobby;
import it.polimi.ingsw.shared.exceptions.addPlayerToGameException;
import junit.framework.TestCase;

import java.rmi.RemoteException;

public class ControllerSOCKETTest extends TestCase {
    Controller controller = new Controller();
    ConnectionControllerManager testServer;
    Lobby lobby;
    private ClientMain clientMain = new ClientMain();
    public void testServerSOCKETIn() throws RemoteException, InterruptedException, ConnectionControllerManagerException, addPlayerToGameException {
        lobby = new Lobby(8001, 8000);
        ConnectionManager connection;
        Player testClient;
        ConnectionManager connection1;
        Player testClient1;
        try {
            connection = new ClientSOCKET(8000, "127.0.0.1");
            testClient  = new LobbyPlayer("antonio", "antonio", connection);
            connection1 = new ClientSOCKET(8000, "127.0.0.1");
            testClient1  = new LobbyPlayer("mauro", "antonio", connection1);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        ((LobbyPlayer)testClient).login();
        ((LobbyPlayer)testClient).createGame();
        testClient = connection.getPlayer();
        System.out.println(testClient);

        controller = lobby.getActiveGames().get(0);
        testServer = controller.getControllerManager();

        assertFalse(testServer.isRmiActive());
        assertTrue(testServer.isSocketActive());

        Thread.sleep(2000);
       /* System.out.println("START TEST testServerSOCKETIn\n");

        //controller.addClient(8000, ConnectionType.SOCKET);


        ArrayList<Player> players = new ArrayList<>();      //gaming order array list for this test
        players.add(new it.polimi.ingsw.server.Model.PlayerClasses.Player("antonio"));
        players.add(new it.polimi.ingsw.server.Model.PlayerClasses.Player("marco"));
        players.add(new it.polimi.ingsw.server.Model.PlayerClasses.Player("paolo"));

        assert(controller.getCurrentPlayer()==-1);

        PlayingPlayer testClient2  = new PlayingPlayer("marco", "antonio", clientMain, ConnectionType.SOCKET, 8000, "127.0.0.1");
        assert (!controller.isPlayerOffline("marco"));
        controller.addNewPlayer("antonio");
        PlayingPlayer testClient1  = new PlayingPlayer("antonio", "antonio", clientMain, ConnectionType.SOCKET, 8000, "127.0.0.1");
        assert (!controller.isPlayerOffline("antonio"));
        controller.addNewPlayer("marco");
        PlayingPlayer testClient3  = new PlayingPlayer("paolo", "antonio", clientMain, ConnectionType.SOCKET, 8000, "127.0.0.1");
        assert (!controller.isPlayerOffline("paolo"));
        controller.addNewPlayer("paolo");

        assert(controller.getCurrentPlayer()==-1);

        System.out.println("test1: start game");

        boolean bool = testClient2.startGame();
        //System.out.println(bool);
        assert (!bool);      //not authorized player try to start the game
        assert(controller.getCurrentPlayer()==-1);
        assert (testClient1.startGame());       // authorized player try to start the game
        assert(controller.getCurrentPlayer()==0);
        Thread.sleep(50);
        assert (testClient1.getActivePlayer().equals(testClient2.getActivePlayer()));
        assert (testClient3.getActivePlayer().equals(testClient2.getActivePlayer()));
        assert (testClient3.getActivePlayer().equals(controller.getCurrentPlayerID()));

        controller.setNotRandomPlayerOrder(players);

        System.out.println("test2: take card");
        Position[] pos = new Position[2];
        pos[0] = new Position(3,8);
        pos[1] = new Position(3,7);

        assert (testClient1.takeCard(0,pos)); // authorized player try to take card
        assert(controller.getCurrentPlayer()==1);
        assert (!testClient3.takeCard(0,pos));//not authorized player try to take card
        assert(controller.getCurrentPlayer()==1);
        assert (testClient1.getActivePlayer().equals(testClient2.getActivePlayer()));
        assert (testClient3.getActivePlayer().equals(testClient2.getActivePlayer()));
        assert (testClient3.getActivePlayer().equals(controller.getCurrentPlayerID()));

        System.out.println("test3: friendly quit game");
        assert (testClient2.quitGame());
        assert (controller.isPlayerOffline("marco"));
        testClient2  = new PlayingPlayer("marco", "antonio", clientMain, ConnectionType.SOCKET, 8000, "127.0.0.1");  //marco rejoin a game after the crash
        assert (!controller.isPlayerOffline("marco"));
        testClient1.quitGame();
        testClient2.quitGame();

        Thread.sleep(2000);
        System.out.println("\nEND TEST\n");
    }
    public void testSOCKETchat() throws ConnectionControllerManagerException, RemoteException, addPlayerToGameException, InterruptedException {
        controller = new Controller();

        System.out.println("START TEST testServerRMIIn\n");

        //controller.addClient(8003, ConnectionType.SOCKET);

        controller.addNewPlayer("antonio");
        controller.addNewPlayer("marco");
        controller.addNewPlayer("paolo");

        PlayingPlayer testClient1  = new PlayingPlayer("antonio", "antonio", clientMain, ConnectionType.SOCKET, 8003, "127.0.0.1");
        assert (!controller.isPlayerOffline("antonio"));
        PlayingPlayer testClient2  = new PlayingPlayer("marco", "antonio", clientMain, ConnectionType.SOCKET, 8003, "127.0.0.1");
        assert (!controller.isPlayerOffline("marco"));
        PlayingPlayer testClient3  = new PlayingPlayer("paolo", "antonio", clientMain, ConnectionType.SOCKET, 8003, "127.0.0.1");
        assert (!controller.isPlayerOffline("paolo"));

        testClient3.sendBroadcastMsg("test broadcast message");

        boolean bool = false;
        try {
            testClient2.sendPrivateMSG("paolo", "test private message");
        } catch (PlayerNotFoundException e) {
            System.out.println(e.toString());
            bool = true;
        }
        assert (bool);


        assert (testClient1.startGame());       // authorized player try to start the game
        Thread.sleep(1000);

        testClient1.sendBroadcastMsg("test broadcast message");
        try {
            testClient2.sendPrivateMSG("paolo", "test private message");
        } catch (PlayerNotFoundException e) {
            throw new RuntimeException(e);
        }

        Thread.sleep(2000);
        System.out.println("\nEND TEST\n");*/
    }
}