package it.polimi.ingsw.server.Connection;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import it.polimi.ingsw.client.ClientMain;
import it.polimi.ingsw.client.Connection.ClientRMI;
import it.polimi.ingsw.client.Connection.ConnectionManager;
import it.polimi.ingsw.client.Exceptions.PlayerNotFoundException;
import it.polimi.ingsw.client.Player.LobbyPlayer;
import it.polimi.ingsw.client.Player.Player;
import it.polimi.ingsw.client.Player.PlayingPlayer;
import it.polimi.ingsw.server.Connection.RMI.RMI;
import it.polimi.ingsw.server.Controller;
import it.polimi.ingsw.server.Exceptions.ConnectionControllerManagerException;
import it.polimi.ingsw.shared.exceptions.addPlayerToGameException;
import it.polimi.ingsw.server.Lobby.Lobby;
import it.polimi.ingsw.shared.Cards.Card;
import it.polimi.ingsw.shared.JsonSupportClasses.JsonUrl;
import it.polimi.ingsw.shared.JsonSupportClasses.Position;
import it.polimi.ingsw.shared.JsonSupportClasses.PositionWithColor;
import junit.framework.TestCase;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.ArrayList;

import static it.polimi.ingsw.shared.Cards.CardColor.EMPTY;

public class ConnectionControllerManagerTest extends TestCase {
    Lobby lobby;
    RMI rmi ;
    SOCKET socket;
    ConnectionControllerManager testServer;
    Controller controller = new Controller();
    private final int timeout = 2;
    private ClientMain clientMain;

    @Test
    private PositionWithColor[] jsonCreate(String name) throws FileNotFoundException {
        Gson gson = new Gson();
        InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream(JsonUrl.getUrl(name));
        if(inputStream == null) throw new FileNotFoundException();
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        return new Gson().fromJson(bufferedReader, PositionWithColor[].class);
    }

    @Test
    private  Card[][] setNotRandomPlayerBoard(String url) throws FileNotFoundException{
        Card[][] board = new Card[5][6];
        PositionWithColor[] pos = this.jsonCreate(url);
        for(int i=0; i<5;i++){
            for (int j=0; j<6;j++){
                board[i][j]= new Card(EMPTY);
            }
        }
        for(PositionWithColor p : pos){
            board[p.getX()][p.getY()]= new Card(p.getColor());
        }
        return board;
    }
    @Test
    public void testServerRMIGame() throws ConnectionControllerManagerException, RemoteException, FileNotFoundException, NotBoundException, InterruptedException {

        System.out.println("START TEST testServerRMIOut\n");

        lobby = new Lobby(7230, 7231);
        clientMain = new ClientMain();

        ArrayList<it.polimi.ingsw.server.Model.PlayerClasses.Player> players = new ArrayList<>();      //gaming order array list for this test
        players.add(new it.polimi.ingsw.server.Model.PlayerClasses.Player("antonio"));
        players.add(new it.polimi.ingsw.server.Model.PlayerClasses.Player("marco"));
        players.add(new it.polimi.ingsw.server.Model.PlayerClasses.Player("paolo"));

        ConnectionManager connection;
        ConnectionManager connection1;
        ConnectionManager connection2;

        try {
            connection = new ClientRMI(7230, "127.0.0.1");
            connection1 = new ClientRMI(7230, "127.0.0.1");
            connection2 = new ClientRMI(7230, "127.0.0.1");
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


        controller = lobby.getActiveGames().get(0);
        testServer = controller.getControllerManager();


        assertTrue(testServer.isRmiActive());
        assertFalse(testServer.isSocketActive());

        ArrayList<Card[][]> playersBoard = new ArrayList<>();
        playersBoard.add(this.setNotRandomPlayerBoard("personalBoard3"));
        playersBoard.add(this.setNotRandomPlayerBoard("personalBoard2"));
        playersBoard.add(this.setNotRandomPlayerBoard("personalBoard1"));

        System.out.println("TEST 1: send player board");
        testServer.sendAllPlayerBoard(playersBoard);


        System.out.println("TEST 2: common goal just scored");
        //send to a client the value of the common goal just scored
        JsonObject scored = new JsonObject();
        scored.addProperty("playerID", "marco");
        scored.addProperty("value", 8);
        testServer.sendLastCommonScored(scored);

        assertEquals(-1, controller.getCurrentPlayer());

        System.out.println("TEST 3: start game");

        assertFalse(((PlayingPlayer)testClient1).startGame());      //not authorized player try to start the game
        assertEquals(-1, controller.getCurrentPlayer());
        assertTrue(((PlayingPlayer)testClient).startGame());       // authorized player try to start the game
        System.out.println(controller.getCurrentPlayer());
        System.out.println(controller.getPlayerNumber());
        assertEquals(0, controller.getCurrentPlayer());

        Thread.sleep(500);

        assertEquals(controller.getCurrentPlayerID(), ((PlayingPlayer) testClient).getActivePlayer()); //antonio
        assertEquals(controller.getCurrentPlayerID(), ((PlayingPlayer) testClient1).getActivePlayer()); //marco
        assertEquals(controller.getCurrentPlayerID(), ((PlayingPlayer) testClient2).getActivePlayer()); //paolo


        controller.setNotRandomPlayerOrder(players);

        System.out.println("TEST 4: take card");
        Position[] pos = new Position[2];
        pos[0] = new Position(3,8);
        pos[1] = new Position(3,7);

        assertTrue(((PlayingPlayer)testClient).takeCard(0,pos)); // authorized player try to take card
        assertFalse(((PlayingPlayer) testClient2).takeCard(0, pos));//not authorized player try to take card
        assertEquals(1, controller.getCurrentPlayer());

        Thread.sleep(500);

        assertEquals(controller.getCurrentPlayerID(), ((PlayingPlayer) testClient).getActivePlayer()); //antonio
        assertEquals(controller.getCurrentPlayerID(), ((PlayingPlayer) testClient1).getActivePlayer()); //marco
        assertEquals(controller.getCurrentPlayerID(), ((PlayingPlayer) testClient2).getActivePlayer()); //paolo

        /*System.out.println("TEST 5: friendly quit game");
        assertTrue(((PlayingPlayer)testClient2).quitGame());
        assertTrue(controller.isPlayerOffline("paolo"));
        assertFalse(((PlayingPlayer) testClient2).quitGame());
        testClient2 = new LobbyPlayer("paolo", "antonio", connection2);  //paolo rejoins a game after the crash
        ((LobbyPlayer)testClient2).login();
        testClient2 = connection2.getPlayer();*/
        //assertFalse(controller.isPlayerOffline("paolo"));

        System.out.println("\nEND TEST\n");

        testServer.forceClientDisconnection();

    }
    @Test
    public void testRMIchat() throws ConnectionControllerManagerException, RemoteException, addPlayerToGameException, InterruptedException {
        /*
        System.out.println("START TEST testServerRMIOut\n");

        lobby = new Lobby(7233, 7232);
        clientMain = new ClientMain();

        ArrayList<it.polimi.ingsw.server.Model.PlayerClasses.Player> players = new ArrayList<>();      //gaming order array list for this test
        players.add(new it.polimi.ingsw.server.Model.PlayerClasses.Player("antonio"));
        players.add(new it.polimi.ingsw.server.Model.PlayerClasses.Player("marco"));
        players.add(new it.polimi.ingsw.server.Model.PlayerClasses.Player("paolo"));

        ConnectionManager connection;
        ConnectionManager connection1;
        ConnectionManager connection2;

        try {
            connection = new ClientRMI(7233, "127.0.0.1");
            connection1 = new ClientRMI(7233, "127.0.0.1");
            connection2 = new ClientRMI(7233, "127.0.0.1");
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


        ((PlayingPlayer)testClient2).sendBroadcastMsg("test broadcast message");

        boolean bool = false;
        try {
            ((PlayingPlayer)testClient1).sendPrivateMSG("paolo", "test private message");
        } catch (PlayerNotFoundException e) {
            System.out.println(e.toString());
            bool = true;
        }
        assertTrue(bool);

        assertTrue(((PlayingPlayer)testClient).startGame());       // authorized player try to start the game

        ((PlayingPlayer)testClient).sendBroadcastMsg("test broadcast message");
        try {
            ((PlayingPlayer)testClient1).sendPrivateMSG("paolo", "test private message");
        } catch (PlayerNotFoundException e) {
            throw new RuntimeException(e);
        }

        Thread.sleep(2000);
        System.out.println("\nEND TEST\n");
    }
    @Test
    public void testOnlyOnePlayerOnline() throws ConnectionControllerManagerException, addPlayerToGameException, RemoteException {
        System.out.println("START TEST testOnlyOnePlayerOnline\n");


        lobby = new Lobby(7235, 7234);
        clientMain = new ClientMain();

        ArrayList<it.polimi.ingsw.server.Model.PlayerClasses.Player> players = new ArrayList<>();      //gaming order array list for this test
        players.add(new it.polimi.ingsw.server.Model.PlayerClasses.Player("antonio"));
        players.add(new it.polimi.ingsw.server.Model.PlayerClasses.Player("marco"));
        players.add(new it.polimi.ingsw.server.Model.PlayerClasses.Player("paolo"));

        ConnectionManager connection;
        ConnectionManager connection1;
        ConnectionManager connection2;

        try {
            connection = new ClientRMI(7235, "127.0.0.1");
            connection1 = new ClientRMI(7235, "127.0.0.1");
            connection2 = new ClientRMI(7235, "127.0.0.1");
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

        assertTrue(((PlayingPlayer)testClient).startGame());

        ((PlayingPlayer)testClient).quitGame();
        ((PlayingPlayer)testClient2).quitGame();

        System.out.println("test1: take card after end of the game");
        Position[] pos = new Position[2];
        pos[0] = new Position(3,8);
        pos[1] = new Position(3,7);

        assertFalse(((PlayingPlayer)testClient2).takeCard(0, pos)); //not authorized take card (game is ended and server can't respond)

        System.out.println("\nEND TEST\n");

         */
    }
}