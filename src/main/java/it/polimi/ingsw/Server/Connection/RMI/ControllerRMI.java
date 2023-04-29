package it.polimi.ingsw.Server.Connection.RMI;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import it.polimi.ingsw.Server.Connection.ConnectionController;
import it.polimi.ingsw.Server.Connection.RMI.SendCommand.*;
import it.polimi.ingsw.Server.Controller;
import it.polimi.ingsw.client.Connection.PlayingPlayerRemoteInterface;
import it.polimi.ingsw.Shared.Cards.Card;
import it.polimi.ingsw.Shared.JsonSupportClasses.JsonUrl;
import it.polimi.ingsw.Shared.JsonSupportClasses.PositionWithColor;

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


public class ControllerRMI extends ConnectionController implements ControllerRemoteInterface {
    /**
     * parameters
     */
    private ArrayList<PlayingPlayerRemoteInterface> clients = new ArrayList<>();
    private int pingPongTime;
    private final ArrayList<String> clientsID = new ArrayList<>();
    private JsonUrl jsonUrl;
    public ControllerRMI(Controller controller, int port){
        super(controller, port);
        this.connection();

        try {
            jsonCreate();
        } catch (FileNotFoundException e) {
            System.out.println("ControllerRMI: JSON FILE NOT FOUND");
            throw new RuntimeException(e);
        }
    }
    /**
     * download json file
     * @throws FileNotFoundException if method can't file json file
     */
    private void jsonCreate() throws FileNotFoundException {  //download json data
        InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream(jsonUrl.getUrl("netConfig"));
        if(inputStream == null) throw new FileNotFoundException();
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        JsonObject jsonObject = new Gson().fromJson(bufferedReader , JsonObject.class);
        this.pingPongTime = jsonObject.get("pingPongTime").getAsInt();
    }

    /**
     * start RMi connection
     */
    @Override
    synchronized public void connection(){
        ControllerRemoteInterface stub = null;
        try {
            stub = (ControllerRemoteInterface) UnicastRemoteObject.exportObject((Remote) this, PORT);
        }catch (RemoteException e){
            e.printStackTrace();
        }

        Registry registry = null;
        try {
            registry = LocateRegistry.createRegistry(PORT);
        } catch (RemoteException e) {
            e.printStackTrace();
        }

        try {
            assert registry != null;
            registry.bind("ControllerRemoteInterface", stub);
        } catch (RemoteException | AlreadyBoundException e) {
            e.printStackTrace();
        }
        System.err.println("\u001B[32m" + "Server (rmi) for newGame ready on port: " + PORT + "\u001B[0m");
    }

    /**
     * method called from a client in ping pong
     */
    public void ping(){}

    /**
     * recursive method implements ping pong with clients
     * @param client_ref remote ref to clients
     * @param playerID id of the player to mark him as offline when needed
     */
    private void pong(PlayingPlayerRemoteInterface client_ref, String playerID){
        synchronized (this) {
            try {
                this.wait(pingPongTime);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        try {
            if(clients.contains(client_ref)) {
                client_ref.ping();
            }else {//if the player does a friendly, quit
                return;
            }
        } catch (RemoteException e) {
            this.quitRMIControllerConnection(client_ref, playerID);
        }
        this.pong(client_ref, playerID);
    }

    /************************************************************************
     ************************************************** IN method ***********
     * ***********************************************************************
     * *
     * new client join the party :)
     * @param client_ref ref to remote obj
     * @return true if the ref can be added false in all other cases
     */
    public synchronized boolean joinRMIControllerConnection(PlayingPlayerRemoteInterface client_ref,String playerID){
        if(!clients.contains(client_ref) && (controller.getCurrentPlayer()==-1 || controller.isPlayerOffline(playerID)) && !controller.isEndGame()){
            System.out.println("\u001B[36m"+"client: " + playerID + " join the game on port(RMI): " + PORT +"\u001B[0m");

            clients.add(client_ref);
            clientsID.add(playerID);
            controller.setPlayerOnline(playerID);

            Thread thread = new Thread(() -> this.pong(client_ref, playerID));       //start ping pong
            thread.start();

            return true;
        }
        return false;
    }
    /**
     * client left the party :(
     * @param client_ref  ref to remote obj
     * @return true if the ref can be removed false in all other cases
     */
    public synchronized boolean quitRMIControllerConnection(PlayingPlayerRemoteInterface client_ref,String playerID){
        if(clients.contains(client_ref)){
            System.out.println("\u001B[33m"+"client: " + playerID + " quit the game on port(RMI): " + PORT +"\u001B[0m");
            clients.remove(client_ref);
            clientsID.remove(playerID);
            controller.setPlayerOffline(playerID);
            synchronized (this) {
                this.notifyAll(); //reset ping pong
            }
            return true;
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
    public void sendCommand(Command command){
        for(int i = 0; i<clients.size(); i ++){
            boolean bool= command.execute(clients.get(i));
            if(!bool) {
                this.quitRMIControllerConnection(clients.get(i), clientsID.get(i));
            }else {
                if(clientsID.size()>0)controller.setPlayerOnline(clientsID.get(i));
            }
        }
    }
    /**
     * @param activePlayerID player's id of the client have to play in this turn
     */
    public void notifyActivePlayer(String activePlayerID){
        Command command = new NotifyActivePlayerCommand(activePlayerID);
        sendCommand(command);
    }
    /**
     * @param players array list with al the name of the players in game
     */
    public void sendPlayerList(String[] players){
        Command command = new SendActivePlayerListCommand(players);
        sendCommand(command);
    }
    /**
     * this method is called in pre-game phase each time a player logs in the game
     * @param playersNumber number of players actually in the game
     */
    public void sendPlayersNUmber(int playersNumber){
        Command command = new SendPlayersNumberCommand(playersNumber);
        sendCommand(command);
    }
    /**
     * @param mainBoard send the main board to clients
     */
    public void sendMainBoard(Card[][] mainBoard){
        Command command = new SendMainBoardCommand(mainBoard);
        sendCommand(command);
    }
    /**
     * add card on player's board on all clients
     * @param playerID id of the player who has the board
     * @param column column on the board
     * @param cards array of Cards
     */
    public void addCardToClientBoard(String playerID, int column, Card[] cards){
        Command command = new AddCardToClientBoard(playerID, column, cards);
        sendCommand(command);
    }
    /**
     * dell card form main board
     * @param cards array of Cards
     */
    public void dellCardFromMainBoard(PositionWithColor[] cards){
        Command command = new DellCardFromMainBoard(cards);
        sendCommand(command);
    }
    /**
     * this method is called during the start of the game or after the reconnection of a clients
     * @param playerBoards array list of player board
     */
    public void sendAllPlayerBoard(ArrayList<Card[][]> playerBoards){
        Command command = new SendAllPlayerBoardCommand(playerBoards);
        sendCommand(command);
    }
    /**
     * @param commonGoalID array of id of the common goal id
     */
    public void sendAllCommonGoal(int[] commonGoalID){
        Command command = new SendAllCommonGoalCommand(commonGoalID);
        sendCommand(command);
    }
    /**
     * @param cards position with color to create on UI the images of common goals
     * @param playerID name of the player
     */
    public void sendPrivateGoal(PositionWithColor[] cards,String playerID){
        Command command = new SendPrivateGoalCommand(cards,playerID);
        sendCommand(command);
    }
    /**
     * @param points contains name and points far all the players
     */
    public void sendEndGamePoint(JsonObject points){
        Command command = new EndGameCommand(points);
        sendCommand(command);
    }
    /**
     * @param winner contains name and points of the winner
     */
    public void sendWinner(JsonObject winner){
        Command command = new WinnerCommand(winner);
        sendCommand(command);
    }
    /**
     * @param scored contains name and points of the last common goal scored
     */
    public void sendLastCommonScored(JsonObject scored){
        Command command = new SendLastCommonScored(scored);
        sendCommand(command);
    }
    /**
     * @param error contains errorId and errorMsg
     * @param playerID id of the clients the error is for
     */
    public void sendError(JsonObject error, String playerID){
        Command command = new ErrorCommand(error, playerID);
        sendCommand(command);
    }

    @Override
    public void forceClientDisconnection() {
        Command command = new ClientDisconnectionCommand();
        sendCommand(command);
        if(clients.size()>0)clients = new ArrayList<>();
    }
    /**************************************************************************
     ************************************************** chat ******************
     * ************************************************************************
     * *
     * send message in broadcast to all clients
     * @param msg message to send
     * @param sender name of the player who send the message
     */
    public void sendBroadcastMsg(String msg, String sender){
        Command command = new BroadcastChatCommand(msg,sender);
        sendCommand(command);
    }
    /**
     * send a message in private to only one client
     * @param userID id of the player the message is for
     * @param msg message to send
     * @param sender name of the player who send the message
     */
    public void sendPrivateMSG(String userID, String msg, String sender){
        Command command = new PrivateChatCommand(userID, msg,sender);
        sendCommand(command);
    }
}
