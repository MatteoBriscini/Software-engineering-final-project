package it.polimi.ingsw.server.Connection.RMI;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import it.polimi.ingsw.client.Connection.PlayingPlayerRemoteInterface;
import it.polimi.ingsw.client.Player.PlayingPlayer;
import it.polimi.ingsw.server.Connection.ConnectionController;
import it.polimi.ingsw.server.Connection.RMI.SendCommand.*;
import it.polimi.ingsw.server.Controller;
import it.polimi.ingsw.server.Lobby.Lobby;
import it.polimi.ingsw.shared.Cards.Card;
import it.polimi.ingsw.shared.Connection.ConnectionType;
import it.polimi.ingsw.shared.JsonSupportClasses.JsonUrl;
import it.polimi.ingsw.shared.JsonSupportClasses.PositionWithColor;
import it.polimi.ingsw.shared.TextColor;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.rmi.AlreadyBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Map;

public class RMI extends ConnectionController implements LobbyRemoteInterface {
    private String playerID;
    private boolean inGame = false;
    protected final String cntType;
    private int pingPongTime;
    public RMI(int port, Lobby lobby , String IP) {
        super(lobby, port, ConnectionType.RMI);

        System.setProperty("java.rmi.server.hostname", IP);

        this.cntType = "RMI";
        this.connection();

        try {
            jsonCreate();
        } catch (FileNotFoundException e) {
            System.out.println("ControllerRMI: JSON FILE NOT FOUND");
            throw new RuntimeException(e);
        }
    }
    private void jsonCreate() throws FileNotFoundException {  //download json data
        InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream(JsonUrl.getUrl("netConfig"));
        if(inputStream == null) throw new FileNotFoundException();
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        JsonObject jsonObject = new Gson().fromJson(bufferedReader , JsonObject.class);
        this.pingPongTime = jsonObject.get("pingPongTime").getAsInt();
    }
    synchronized public void connection() {
        LobbyRemoteInterface stub = null;
        try {

            stub = (LobbyRemoteInterface) UnicastRemoteObject.exportObject((Remote) this, this.PORT);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }

        Registry registry = null;
        try {
            registry = LocateRegistry.createRegistry(this.PORT);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }

        try {
            registry.bind("LobbyRemoteInterface", stub);
        } catch (RemoteException | AlreadyBoundException e) {
            throw new RuntimeException(e);
        }

        System.out.println("\u001B[32m" + "Server (rmi) ready on port: " + PORT + TextColor.DEFAULT.get());
    }
    /**
     * method called from a client in ping pong
     */
    public void ping(){}
    private void pong(PlayingPlayerRemoteInterface client_ref, String playerID, Controller controller){

        synchronized (this) {
            try {
                this.wait(pingPongTime);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        try {
            if(controller.getClientsRMImap().containsValue(client_ref)) {
                client_ref.ping();
            }else {//if the player does a friendly, quit
                return;
            }
        } catch (RemoteException e) {
            this.quitGameConnection(client_ref, playerID, controller);
        }
        this.pong(client_ref, playerID, controller);
    }

    /************************************************************************
     ************************************************** IN method ***********
     * ***********************************************************************
     * *
     * new client join the party :)
     * @param client_ref ref to remote obj
     * @return true if the ref can be added false in all other cases
     */
    public synchronized boolean joinGameConnection(PlayingPlayerRemoteInterface client_ref,String playerID, String connectionInterface){
        Controller controller = this.getActualController(connectionInterface);
        if(!controller.getClientsRMImap().containsValue(client_ref)&&!controller.getClientsRMImap().containsKey(playerID) && (controller.getCurrentPlayer()==-1||controller.isPlayerOffline(playerID))&&!controller.isEndGame()) {
            System.out.println(TextColor.LIGHTBLUE.get() + "client: " + playerID + " join the game (RMI) " + TextColor.DEFAULT.get());

            controller.addClientRMI(client_ref, playerID);
            controller.setPlayerOnline(playerID);

            Thread thread = new Thread(() -> this.pong(client_ref, playerID, controller));       //start ping pong
            thread.start();

            return true;
        }
        return false;
    }
    public synchronized boolean quitGameConnection(PlayingPlayerRemoteInterface client_ref,String playerID, String connectionInterface){
        Controller controller = this.getActualController(connectionInterface);
        synchronized (this) {
            this.notifyAll(); //reset ping pong
        }
        return this.quitGameConnection(client_ref, playerID, controller);

    }
    /**
     * client left the party :(
     * @param client_ref  ref to remote obj
     * @return true if the ref can be removed false in all other cases
     */
    public synchronized boolean quitGameConnection(PlayingPlayerRemoteInterface client_ref,String playerID, Controller controller){
        if(controller.getClientsRMImap().containsValue(client_ref)){
            if(controller.removeClientRMI(client_ref, playerID)) {
                System.out.println(TextColor.YELLOW.get()+"client: " + playerID + " quit the game (RMI) "+TextColor.DEFAULT.get());
                controller.setPlayerOffline(playerID);
                return true;
            }
        }
        return false;
    }
    /*************************************************************************
     ************************************************** OUT method ***********
     * ***********************************************************************
     * *
     * send broadcast command to all the client (a command pattern)
     * @param command actual command
     */
    public void sendCommand(Command command, Map<String, PlayingPlayerRemoteInterface> clients, Controller connectionInterface){
        for(Map.Entry<String, PlayingPlayerRemoteInterface> client : clients.entrySet()){
            boolean bool= command.execute(client.getValue());
            if(!bool) {
                this.quitGameConnection(client.getValue(), client.getKey(), connectionInterface);
            }else {
                connectionInterface.setPlayerOnline(client.getKey());
            }
        }
    }

    public void notifyActivePlayer(String activePlayerID, Map<String, PlayingPlayerRemoteInterface> clients, Controller connectionInterface) {
        Command command = new NotifyActivePlayerCommand(activePlayerID);
        sendCommand(command, clients, connectionInterface);
    }

    public void sendPlayerList(String[] players, Map<String, PlayingPlayerRemoteInterface> clients, Controller connectionInterface) {
        Command command = new SendActivePlayerListCommand(players);
        sendCommand(command, clients, connectionInterface);
    }

    public void sendPlayersNUmber(int playersNumber, Map<String, PlayingPlayerRemoteInterface> clients, Controller connectionInterface) {
        Command command = new SendPlayersNumberCommand(playersNumber);
        sendCommand(command, clients, connectionInterface);
    }

    public void sendMainBoard(Card[][] mainBoard,Map<String, PlayingPlayerRemoteInterface> clients, Controller connectionInterface) {
        Command command = new SendMainBoardCommand(mainBoard);
        sendCommand(command, clients, connectionInterface);
    }

    public void addCardToClientBoard(String playerID, int column, Card[] cards, Map<String, PlayingPlayerRemoteInterface> clients, Controller connectionInterface) {
        Command command = new AddCardToClientBoard(playerID, column, cards);
        sendCommand(command, clients, connectionInterface);
    }


    public void dellCardFromMainBoard(PositionWithColor[] cards, Map<String, PlayingPlayerRemoteInterface>clients, Controller connectionInterface) {
        Command command = new DellCardFromMainBoard(cards);
        sendCommand(command, clients, connectionInterface);
    }


    public void sendAllPlayerBoard(ArrayList<Card[][]> playerBoards, Map<String, PlayingPlayerRemoteInterface> clients, Controller connectionInterface) {
        Command command = new SendAllPlayerBoardCommand(playerBoards);
        sendCommand(command, clients, connectionInterface);
    }


    public void sendAllCommonGoal(int[] commonGoalID, Map<String, PlayingPlayerRemoteInterface> clients, Controller connectionInterface) {
        Command command = new SendAllCommonGoalCommand(commonGoalID);
        sendCommand(command, clients, connectionInterface);
    }


    public void sendPrivateGoal(PositionWithColor[] cards, String playerID, Map<String, PlayingPlayerRemoteInterface> clients, Controller connectionInterface) {
        Command command = new SendPrivateGoalCommand(cards,playerID);
        sendCommand(command, clients, connectionInterface);
    }

    public void sendEndGamePoint(JsonObject points, Map<String, PlayingPlayerRemoteInterface> clients, Controller connectionInterface) {
        Command command = new EndGameCommand(points);
        sendCommand(command, clients, connectionInterface);
        for (String s: clients.keySet()) connectionInterface.removeClientRMI(clients.get(s), s);
    }

    public void sendWinner(JsonObject winner, Map<String, PlayingPlayerRemoteInterface> clients, Controller connectionInterface) {
        Command command = new WinnerCommand(winner);
        sendCommand(command, clients, connectionInterface);
    }

    public void sendLastCommonScored(JsonObject scored, Map<String, PlayingPlayerRemoteInterface> clients, Controller connectionInterface) {
        Command command = new SendLastCommonScored(scored);
        sendCommand(command, clients, connectionInterface);
    }

    public void sendError(JsonObject error, String playerID, Map<String, PlayingPlayerRemoteInterface> clients, Controller connectionInterface) {
        Command command = new ErrorCommand(error, playerID);
        sendCommand(command, clients, connectionInterface);
    }

    public void forceClientDisconnection(Map<String, PlayingPlayerRemoteInterface> clients, Controller connectionInterface) {
        Command command = new ClientDisconnectionCommand();
        sendCommand(command, clients, connectionInterface);
    }
    /**************************************************************************
     ************************************************** chat ******************
     * ************************************************************************/
    public void sendBroadcastMsg(String msg, String sender, Map<String, PlayingPlayerRemoteInterface> clients, Controller connectionInterface) {
        Command command = new BroadcastChatCommand(msg,sender);
        sendCommand(command, clients, connectionInterface);
    }

    public void sendPrivateMSG(String userID, String msg, String sender, Map<String, PlayingPlayerRemoteInterface> clients, Controller connectionInterface) {
        Command command = new PrivateChatCommand(userID, msg,sender);
        sendCommand(command, clients, connectionInterface);
    }


}
