package it.polimi.ingsw.Server.Connection.RMI;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import it.polimi.ingsw.Server.Connection.ConnectionController;
import it.polimi.ingsw.Server.Connection.RMI.SendCommand.*;
import it.polimi.ingsw.Server.Controller;
import it.polimi.ingsw.Client.Connection.PlayingPlayerRemoteInterface;
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

    private ArrayList<PlayingPlayerRemoteInterface> clients = new ArrayList<>();
    private int pingPongTime;
    private ArrayList<String> clientsID = new ArrayList<>();
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
    private void jsonCreate() throws FileNotFoundException {  //download json data
        InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream(jsonUrl.getUrl("netConfig"));
        if(inputStream == null) throw new FileNotFoundException();
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        JsonObject jsonObject = new Gson().fromJson(bufferedReader , JsonObject.class);
        this.pingPongTime = jsonObject.get("pingPongTime").getAsInt();
    }
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
            registry.bind("ControllerRemoteInterface", stub);
        } catch (RemoteException | AlreadyBoundException e) {
            e.printStackTrace();
        }
        System.err.println("\u001B[32m" + "Server (rmi) for newGame ready on port: " + PORT + "\u001B[0m");
    }

    public void ping(){}
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
            }else {//if the player do a friendly quit
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
     * @return true if the ref can be added false in all other case
     * @throws RemoteException if the server isn't available
     */
    public synchronized boolean joinRMIControllerConnection(PlayingPlayerRemoteInterface client_ref,String playerID){
        if(!clients.contains(client_ref) && (controller.getCurrentPlayer()==-1 || controller.isPlayerOffline(playerID)) && !controller.isEndGame()){
            System.out.println("\u001B[36m"+"client: " + playerID + " join the game on port(RMI): " + PORT +"\u001B[0m");

            clients.add(client_ref);
            clientsID.add(playerID);
            controller.setPlayerOnline(playerID);

            Thread thread = new Thread(() -> {this.pong(client_ref, playerID);});       //start ping pong
            thread.start();

            return true;
        }
        return false;
    }

    /**
     * client left the party :(
     * @param client_ref  ref to remote obj
     * @return true if the ref can be removed false in all other case

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
     * send broadcast command to all the client (command pattern)
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

    public void notifyActivePlayer(String activePlayerID){
        Command command = new NotifyActivePlayerCommand(activePlayerID);
        sendCommand(command);
    }

    public void sendPlayerList(String[] players){
        Command command = new SendActivePlayerListCommand(players);
        sendCommand(command);
    }

    public void sendPlayersNUmber(int playersNumber){
        Command command = new SendPlayersNumberCommand(playersNumber);
        sendCommand(command);
    }

    public void sendMainBoard(Card[][] mainBoard){
        Command command = new SendMainBoardCommand(mainBoard);
        sendCommand(command);
    }

    public void addCardToClientBoard(String playerID, int column, Card[] cards){
        Command command = new AddCardToClientBoard(playerID, column, cards);
        sendCommand(command);
    }

    public void dellCardFromMainBoard(PositionWithColor[] cards){
        Command command = new DellCardFromMainBoard(cards);
        sendCommand(command);
    }

    public void sendAllPlayerBoard(ArrayList<Card[][]> playerBoards){
        Command command = new SendAllPlayerBoardCommand(playerBoards);
        sendCommand(command);
    }

    public void sendAllCommonGoal(int[] commonGoalID){
        Command command = new SendAllCommonGoalCommand(commonGoalID);
        sendCommand(command);
    }

    public void sendPrivateGoal(PositionWithColor[] cards,String playerID){
        Command command = new SendPrivateGoalCommand(cards,playerID);
        sendCommand(command);
    }

    public void sendEndGamePoint(JsonObject points){
        Command command = new EndGameCommand(points);
        sendCommand(command);
    }
    public void sendWinner(JsonObject winner){
        Command command = new WinnerCommand(winner);
        sendCommand(command);
    }

    public void sendLastCommonScored(JsonObject scored){
        Command command = new SendLastCommonScored(scored);
        sendCommand(command);
    }

    public void sendError(JsonObject error, String playerID){
        Command command = new ErrorCommand(error, playerID);
        sendCommand(command);
    }

    @Override
    public void forceClientDisconnection() {
        Command command = new ClientDisconnectionCommand();
        sendCommand(command);
        clients = new ArrayList<>();
    }
}
