package it.polimi.ingsw.client.Player;

import it.polimi.ingsw.client.ClientMain;
import it.polimi.ingsw.client.Connection.ClientRMI;
import it.polimi.ingsw.client.Connection.ConnectionManager;
import it.polimi.ingsw.server.Lobby.Lobby;
import junit.framework.TestCase;
import org.junit.Test;

import java.rmi.RemoteException;
import java.sql.Connection;
import java.util.ArrayList;

public class PlayingPlayerTest extends TestCase {
    Lobby lobby;
    private ClientMain clientMain;
    @Test
    public void testSendMessage() throws Exception {
        System.out.println("START TEST testServerRMIOut\n");

        lobby = new Lobby(7275, 7276);
        clientMain = new ClientMain();

        ArrayList<it.polimi.ingsw.server.Model.PlayerClasses.Player> players = new ArrayList<>();      //gaming order array list for this test
        players.add(new it.polimi.ingsw.server.Model.PlayerClasses.Player("antonio"));
        players.add(new it.polimi.ingsw.server.Model.PlayerClasses.Player("marco"));
        players.add(new it.polimi.ingsw.server.Model.PlayerClasses.Player("paolo"));

        ConnectionManager connection;
        ConnectionManager connection1;
        ConnectionManager connection2;

        try {
            connection = new ClientRMI(7275, "127.0.0.1");
            connection1 = new ClientRMI(7275, "127.0.0.1");
            connection2 = new ClientRMI(7275, "127.0.0.1");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }


        Player testClient  = new LobbyPlayer("antonio", "antonio", connection);
        ((LobbyPlayer)testClient).createGame();
        testClient = connection.getPlayer();
        Player testClient1  = new LobbyPlayer("marco", "antonio", connection1);
        ((LobbyPlayer)testClient1).joinGame("antonio");
        testClient1 = connection1.getPlayer();
        Player testClient2 = new LobbyPlayer("paolo", "antonio", connection2);
        ((LobbyPlayer)testClient2).joinGame("antonio");
        testClient2 = connection2.getPlayer();
        ((PlayingPlayer)testClient).startGame();
        ((PlayingPlayer)testClient).sendMessage("ciao a tutti");
        ((PlayingPlayer)testClient).sendMessage("--ma rco : ciao a marco");
    }
}