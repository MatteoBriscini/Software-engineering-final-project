package it.polimi.ingsw.Server.Connection;

import it.polimi.ingsw.client.ClientMain;
import it.polimi.ingsw.client.Player.PlayingPlayer;
import it.polimi.ingsw.Server.Controller;
import it.polimi.ingsw.Server.Exceptions.ConnectionControllerManagerException;
import it.polimi.ingsw.Server.Exceptions.addPlayerToGameException;
import it.polimi.ingsw.Server.Model.PlayerClasses.Player;
import it.polimi.ingsw.Shared.Connection.ConnectionType;
import it.polimi.ingsw.Shared.JsonSupportClasses.Position;
import junit.framework.TestCase;

import java.rmi.RemoteException;
import java.util.ArrayList;

public class ControllerSOCKETTest extends TestCase {
    Controller controller = new Controller();
    private ClientMain clientMain = new ClientMain();
    public void testServerSOCKETIn() throws RemoteException, InterruptedException, ConnectionControllerManagerException, addPlayerToGameException {
        System.out.println("START TEST testServerSOCKETIn\n");

        controller.addClient(8000, ConnectionType.SOCKET);


        ArrayList<Player> players = new ArrayList<>();      //gaming order array list for this test
        players.add(new it.polimi.ingsw.Server.Model.PlayerClasses.Player("antonio"));
        players.add(new it.polimi.ingsw.Server.Model.PlayerClasses.Player("marco"));
        players.add(new it.polimi.ingsw.Server.Model.PlayerClasses.Player("paolo"));

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


        System.out.println("\nEND TEST\n");
    }

}