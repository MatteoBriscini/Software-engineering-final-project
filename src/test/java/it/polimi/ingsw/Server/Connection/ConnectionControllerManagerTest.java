package it.polimi.ingsw.Server.Connection;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import it.polimi.ingsw.Client.ClientMain;
import it.polimi.ingsw.Client.Player.Player;
import it.polimi.ingsw.Client.Player.PlayingPlayer;
import it.polimi.ingsw.Server.Controller;
import it.polimi.ingsw.Server.Exceptions.ConnectionControllerManagerException;
import it.polimi.ingsw.Server.Exceptions.addPlayerToGameException;
import it.polimi.ingsw.Shared.Cards.Card;
import it.polimi.ingsw.Shared.Connection.ConnectionType;
import it.polimi.ingsw.Shared.JsonSupportClasses.JsonUrl;
import it.polimi.ingsw.Shared.JsonSupportClasses.Position;
import it.polimi.ingsw.Shared.JsonSupportClasses.PositionWithColor;
import junit.framework.TestCase;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.rmi.RemoteException;
import java.util.ArrayList;

import static it.polimi.ingsw.Shared.Cards.CardColor.EMPTY;

public class ConnectionControllerManagerTest extends TestCase {
    ConnectionControllerManager testServer = new ConnectionControllerManager();
    Controller controller = new Controller();
    private JsonUrl jsonUrl;
    private final int timeout = 2;
    private ClientMain clientMain = new ClientMain();

    private PositionWithColor[] jsonCreate(String name) throws FileNotFoundException {
        Gson gson = new Gson();
        InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream(jsonUrl.getUrl(name));
        if(inputStream == null) throw new FileNotFoundException();
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        return new Gson().fromJson(bufferedReader, PositionWithColor[].class);
    }
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

    public void testServerRMIOut() throws ConnectionControllerManagerException, RemoteException, FileNotFoundException {
        controller = new Controller();

        System.out.println("START TEST testServerRMIOut\n");

        testServer.addClient(7234, ConnectionType.RMI, controller);
        assert(testServer.isRmiActive());
        assert(!testServer.isSocketActive());
        assert(testServer.getInterfaces().size()==1);

        Player testClient  = new PlayingPlayer("antonio", "antonio", clientMain,ConnectionType.RMI, 7234, "127.0.0.1");

        ArrayList<Card[][]> playersBoard = new ArrayList<>();
        playersBoard.add(this.setNotRandomPlayerBoard("personalBoard3"));
        playersBoard.add(this.setNotRandomPlayerBoard("personalBoard2"));
        playersBoard.add(this.setNotRandomPlayerBoard("personalBoard1"));

        System.out.println("TEST 1: send player board");
        testServer.sendAllPlayerBoard(playersBoard);


        System.out.println("TEST 2: common goal just scored");
        //send to client the value of the common goal just scored
        JsonObject scored = new JsonObject();
        scored.addProperty("playerID", "marco");
        scored.addProperty("value", 8);
        testServer.sendLastCommonScored(scored);

        assert(((PlayingPlayer) testClient).getCommonGoalScored().length == 1);

        testServer.forceClientDisconnection();

        System.out.println("\nEND TEST\n");
    }

    public void testServerRMIIn() throws ConnectionControllerManagerException, RemoteException, addPlayerToGameException, InterruptedException {
        controller = new Controller();

        System.out.println("START TEST testServerRMIIn\n");

        controller.addClient(7236, ConnectionType.RMI);

        ArrayList<it.polimi.ingsw.Server.Model.PlayerClasses.Player> players = new ArrayList<>();      //gaming order array list for this test
        players.add(new it.polimi.ingsw.Server.Model.PlayerClasses.Player("antonio"));
        players.add(new it.polimi.ingsw.Server.Model.PlayerClasses.Player("marco"));
        players.add(new it.polimi.ingsw.Server.Model.PlayerClasses.Player("paolo"));

        controller.addNewPlayer("antonio");
        controller.addNewPlayer("marco");
        controller.addNewPlayer("paolo");

        PlayingPlayer testClient2  = new PlayingPlayer("marco", "antonio", clientMain, ConnectionType.RMI, 7236, "127.0.0.1");
        assert (!controller.isPlayerOffline("marco"));
        PlayingPlayer testClient1  = new PlayingPlayer("antonio", "antonio", clientMain, ConnectionType.RMI, 7236, "127.0.0.1");
        assert (!controller.isPlayerOffline("antonio"));
        PlayingPlayer testClient3  = new PlayingPlayer("paolo", "antonio", clientMain, ConnectionType.RMI, 7236, "127.0.0.1");
        assert (!controller.isPlayerOffline("paolo"));

        assert(controller.getCurrentPlayer()==-1);

        System.out.println("test1: start game");
        assert (!testClient2.startGame());      //not authorized player try to start the game
        assert(controller.getCurrentPlayer()==-1);
        assert (testClient1.startGame());       // authorized player try to start the game
        assert(controller.getCurrentPlayer()==0);
        assert (testClient1.getActivePlayer().equals(testClient2.getActivePlayer()));
        assert (testClient3.getActivePlayer().equals(testClient2.getActivePlayer()));
        assert (testClient3.getActivePlayer().equals(controller.getCurrentPlayerID()));

        controller.setNotRandomPlayerOrder(players);

        System.out.println("test1: take card");
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

        System.out.println("test1: friendly quit game");
        assert (testClient2.quitGame());
        assert (controller.isPlayerOffline("marco"));
        assert (!testClient2.quitGame());
        testClient2  = new PlayingPlayer("marco", "antonio", clientMain, ConnectionType.RMI, 7236, "127.0.0.1");  //marco rejoin a game after the crash
        assert (!controller.isPlayerOffline("marco"));

        System.out.println("\nEND TEST\n");
    }

    public void testRMIchat() throws ConnectionControllerManagerException, RemoteException {
        controller = new Controller();

        System.out.println("START TEST testServerRMIIn\n");

        controller.addClient(7237, ConnectionType.RMI);

        PlayingPlayer testClient2  = new PlayingPlayer("marco", "antonio", clientMain, ConnectionType.RMI, 7237, "127.0.0.1");
        assert (!controller.isPlayerOffline("marco"));
        PlayingPlayer testClient1  = new PlayingPlayer("antonio", "antonio", clientMain, ConnectionType.RMI, 7237, "127.0.0.1");
        assert (!controller.isPlayerOffline("antonio"));
        PlayingPlayer testClient3  = new PlayingPlayer("paolo", "antonio", clientMain, ConnectionType.RMI, 7237, "127.0.0.1");
        assert (!controller.isPlayerOffline("paolo"));

        testClient1.sendBroadcastMsg("test broadcast message");
        testClient2.sendPrivateMSG("paolo", "test private message");

        System.out.println("\nEND TEST\n");
    }
}