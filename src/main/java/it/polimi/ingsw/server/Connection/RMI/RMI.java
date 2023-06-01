package it.polimi.ingsw.server.Connection.RMI;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import it.polimi.ingsw.client.Connection.PlayingPlayerRemoteInterface;
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
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class RMI extends ConnectionController implements LobbyRemoteInterface {
    private String playerID;
    private boolean inGame = false;

    private BlockingQueue<Command> commands = new LinkedBlockingQueue<>();
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

    /**
     * setup rmi connection
     */
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
    /**
     * detect the clint crash
     * @param client_ref remote client interface
     * @param playerID name of the client
     * @param controller game in with the client play
     */
    private void pong(PlayingPlayerRemoteInterface client_ref, String playerID, Controller controller){
        Timer timer = new Timer();
        timer.schedule(new PingPong(client_ref, playerID, controller, timer), 15, pingPongTime);
    }

    private class PingPong extends TimerTask {
        private final PlayingPlayerRemoteInterface client_ref;
        private final String playerID;
        private final Controller controller;
        private final Timer timer;
        /**
         * detect the clint crash
         *
         * @param client_ref remote client interface
         * @param playerID   name of the client
         * @param controller game in with the client play
         */
        public PingPong(PlayingPlayerRemoteInterface client_ref, String playerID, Controller controller, Timer timer){
            this.client_ref = client_ref;
            this.playerID = playerID;
            this.controller = controller;
            this.timer = timer;
        }
        /**
         * The action to be performed by this timer task.
         */
        @Override
        public void run() {
            try {
                if (controller.getClientsRMImap().containsValue(client_ref)) {
                    client_ref.ping();
                } else {//if the player does a friendly, quit
                    timer.cancel();
                }
            } catch (RemoteException e) {
                RMI.this.quitGameConnection(client_ref, playerID, controller);
            }
        }
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
        if(!controller.getClientsRMImap().containsValue(client_ref)&&!controller.getClientsRMImap().containsKey(playerID) && (controller.getCurrentPlayer()==-1||controller.isPlayerOffline(playerID))) {
            System.out.println(TextColor.LIGHTBLUE.get() + "client: " + playerID + " join the game (RMI) " + TextColor.DEFAULT.get());

            controller.addClientRMI(client_ref, playerID);
            controller.setPlayerOnline(playerID);

            this.pong(client_ref, playerID, controller);       //start ping pong

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
     * add command to blocking que and start a new thread to execute it and remove it from the blocking que
     * @param command actual command
     */
    public void sendCommand(Command command){
        commands.add(command);
        new Thread(new blockingQueueHandler(this)).start();
    }

    /**
     * private class run on a thread take first command claas in the blocking que and execute it on all the client saved in the actual command
     */
    private class blockingQueueHandler implements Runnable{

        private Command command;
        private Map<String, PlayingPlayerRemoteInterface> clients;
        private Controller connectionInterface;
        private RMI rmi;

        public blockingQueueHandler(RMI rmi){
            this.rmi = rmi;
        }
        @Override
        public void run() {
            CommandAbstract call;
            synchronized (rmi) {
                try {
                    call = (CommandAbstract) commands.take();
                    //System.out.println(call);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                clients = call.getClients();
                connectionInterface = call.getConnectionInterface();
                for (Map.Entry<String, PlayingPlayerRemoteInterface> client : clients.entrySet()) {

                    boolean bool = call.execute(client.getValue());
                    if (!bool) {
                        rmi.quitGameConnection(client.getValue(), client.getKey(), connectionInterface);
                    } else {
                        if(connectionInterface.isPlayerOffline(client.getKey()))connectionInterface.setPlayerOnline(client.getKey());
                    }
                }
            }
        }
    }
    /**
     * @param activePlayerID the id of the player have to play now
     * @param clients the clients list of the game
     * @param connectionInterface ref to the controller
     */
    public void notifyActivePlayer(String activePlayerID, Map<String, PlayingPlayerRemoteInterface> clients, Controller connectionInterface) {
        Command command = new NotifyActivePlayerCommand(activePlayerID,clients,connectionInterface);
        sendCommand(command);
    }

    /**
     * used when create or recreate client data
     * @param players the list of players in the game when it starts
     * @param clients the clients list of the game
     * @param connectionInterface ref to the controller
     */
    public void sendPlayerList(String[] players, Map<String, PlayingPlayerRemoteInterface> clients, Controller connectionInterface) {
        Command command = new SendActivePlayerListCommand(players,clients,connectionInterface);
        sendCommand(command);
    }
    /**
     * used in waiting room to communicate to the game creator the number of player actual in the game
     * @param playersNumber number of player actual in the game
     * @param clients the clients list of the game
     * @param connectionInterface ref to the controller
     */
    public void sendPlayersNUmber(int playersNumber, Map<String, PlayingPlayerRemoteInterface> clients, Controller connectionInterface) {
        Command command = new SendPlayersNumberCommand(playersNumber,clients,connectionInterface);
        sendCommand(command);
    }
    /**
     * used in create or recreate data clients, it will be sent in broadcast
     * @param mainBoard cardMatrix represent the mainBoard
     * @param clients the clients list of the game
     * @param connectionInterface ref to the controller
     */
    public void sendMainBoard(Card[][] mainBoard,Map<String, PlayingPlayerRemoteInterface> clients, Controller connectionInterface) {
        Command command = new SendMainBoardCommand(mainBoard,clients,connectionInterface);
        sendCommand(command);
    }
    /**
     * update playerBoard by delta, it will be sent in broadcast
     * @param playerID identify witch playerBoard
     * @param column position on the playerBoard
     * @param cards the delta
     * @param clients the clients list of the game
     * @param connectionInterface ref to the controller
     */
    public void addCardToClientBoard(String playerID, int column, Card[] cards, Map<String, PlayingPlayerRemoteInterface> clients, Controller connectionInterface) {
        Command command = new AddCardToClientBoard(playerID, column, cards, clients, connectionInterface);
        sendCommand(command);
    }
    /**
     * update mainBoard by delta, it will be sent in broadcast
     * @param cards the delta
     * @param clients the clients list of the game
     * @param connectionInterface ref to the controller
     */
    public void dellCardFromMainBoard(PositionWithColor[] cards, Map<String, PlayingPlayerRemoteInterface>clients, Controller connectionInterface) {
        Command command = new DellCardFromMainBoard(cards, clients, connectionInterface);
        sendCommand(command);
    }
    /**
     * used in create or recreate data clients, it will be sent in broadcast
     * @param playerBoards arrayList with card matrix who represent all players board of thi specific game
     * @param clients the clients list of the game
     * @param connectionInterface ref to the controller
     */
    public void sendAllPlayerBoard(ArrayList<Card[][]> playerBoards, Map<String, PlayingPlayerRemoteInterface> clients, Controller connectionInterface) {
        Command command = new SendAllPlayerBoardCommand(playerBoards,clients,connectionInterface);
        sendCommand(command);
    }
    /**
     * it will be sent in broadcast
     * @param commonGoalID array with id of all common goals
     * @param clients the clients list of the game
     * @param connectionInterface ref to the controller
     */
    public void sendAllCommonGoal(int[] commonGoalID, Map<String, PlayingPlayerRemoteInterface> clients, Controller connectionInterface) {
        Command command = new SendAllCommonGoalCommand(commonGoalID,clients,connectionInterface);
        sendCommand(command);
    }
    /**
     * @param cards position on payer board and color need to respect to achieve private goal
     * @param playerID recipient of th message
     * @param clients the clients list of the game
     * @param connectionInterface ref to the controller
     */
    public void sendPrivateGoal(PositionWithColor[] cards, String playerID, Map<String, PlayingPlayerRemoteInterface> clients, Controller connectionInterface) {
        Command command = new SendPrivateGoalCommand(cards,playerID,clients,connectionInterface);
        sendCommand(command);
    }

    /**
     * @param points jsonObject with all points for all the player in the game, it will be sent in broadcast
     * @param clients the clients list of the game
     * @param connectionInterface ref to the controller
     */
    public void sendEndGamePoint(JsonObject points, Map<String, PlayingPlayerRemoteInterface> clients, Controller connectionInterface) {
        Command command = new EndGameCommand(points, clients, connectionInterface);
        sendCommand(command);
    }
    /**
     * @param winner jsonObject with point and name of the winner it will be sent in broadcast
     * @param clients the clients list of the game
     * @param connectionInterface ref to the controller
     */
    public void sendWinner(JsonObject winner, Map<String, PlayingPlayerRemoteInterface> clients, Controller connectionInterface) {
        Command command = new WinnerCommand(winner, clients, connectionInterface);
        sendCommand(command);
    }
    /**
     * @param scored jsonObject with all point scored by all the player in the game by common goal, it will be sent in broadcast
     * @param clients the clients list of the game
     * @param connectionInterface ref to the controller
     */
    public void sendLastCommonScored(JsonObject scored, Map<String, PlayingPlayerRemoteInterface> clients, Controller connectionInterface) {
        Command command = new SendLastCommonScored(scored, clients, connectionInterface);
        sendCommand(command);
    }
    /**
     * @param error jsonObject with error id and error code
     * @param playerID recipient of the error msg
     * @param clients the clients list of the game
     * @param connectionInterface ref to the controller
     */
    public void sendError(JsonObject error, String playerID, Map<String, PlayingPlayerRemoteInterface> clients, Controller connectionInterface) {
        Command command = new ErrorCommand(error, playerID, clients, connectionInterface);
        sendCommand(command);
    }
    /**
     * @param clients the clients list of the game
     * @param connectionInterface ref to the controller
     */
    public void forceClientDisconnection(Map<String, PlayingPlayerRemoteInterface> clients, Controller connectionInterface) {
        Command command = new ClientDisconnectionCommand(clients, connectionInterface);
        sendCommand(command);
    }
    /**************************************************************************
     ************************************************** chat ******************
     * ************************************************************************
     *
     * send game chat private message
     * @param msg actual message
     * @param sender who send the message
     * @param clients the clients list of the game
     * @param connectionInterface ref to the controller
     */
    public void sendBroadcastMsg(String msg, String sender, Map<String, PlayingPlayerRemoteInterface> clients, Controller connectionInterface) {
        Command command = new BroadcastChatCommand(msg,sender,clients,connectionInterface);
        sendCommand(command);
    }
    /**
     * send game chat private message
     * @param userID recipient of the message
     * @param msg actual message
     * @param sender who send the message
     * @param clients the clients list of the game
     * @param connectionInterface ref to the controller
     */
    public void sendPrivateMSG(String userID, String msg, String sender, Map<String, PlayingPlayerRemoteInterface> clients, Controller connectionInterface) {
        Command command = new PrivateChatCommand(userID, msg,sender,clients,connectionInterface);
        sendCommand(command);
    }


}
