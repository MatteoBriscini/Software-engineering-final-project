package it.polimi.ingsw.Server.Connection;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import it.polimi.ingsw.Server.Controller;
import it.polimi.ingsw.Shared.Cards.Card;
import it.polimi.ingsw.Shared.JsonSupportClasses.PositionWithColor;

import java.io.IOException;
import java.io.PrintWriter;

import java.lang.reflect.*;
import java.net.ServerSocket;
import java.net.Socket;

import java.util.ArrayList;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ControllerSOCKET extends ConnectionController {
    private ServerSocket serverSocket;
    private ArrayList<MultiClientSocketGame> clients = new ArrayList<>();
    public ControllerSOCKET(Controller controller, int port){
        super(controller, port);
        this.connection();
    }
    synchronized public void connection(){
        try {
            serverSocket = new ServerSocket(PORT);  //throw exception if unavailable port
        } catch (IOException e) {
            System.out.println(e.toString());           //da finire*********************************
            return;
        }
        System.err.println("\u001B[32m" + "Server (socket) for newGame ready on port: " + PORT + "\u001B[0m");
    }
    public void acceptConnection(){
        ExecutorService executor = Executors.newCachedThreadPool();
        while (true) {
            try {
                Socket socket = serverSocket.accept();
                MultiClientSocketGame client = new MultiClientSocketGame(socket);
                clients.add(client);
                executor.submit(client);
            } catch (IOException e) {
                System.out.println(e.toString()); //go here if socket is closed
                break;//TODO
            }
        }
        executor.shutdown();
    }
    /*************************************************************************
     ************************************************** OUT method ***********
     * ***********************************************************************
     */

    private void sendCommand(JsonObject msg){
        for(MultiClientSocketGame client: clients){
            try {
                client.sendMSG(msg);
            } catch (IOException e) {
                throw new RuntimeException(e);
                //TODO
            }
        }
    }
    @Override
    public void notifyActivePlayer(String activePlayerID) {
        JsonObject data = new JsonObject();
        data.addProperty("playerID", activePlayerID);

        JsonObject msg = new JsonObject();
        msg.addProperty("service", "notifyActivePlayer");
        msg.add("data", data);

        this.sendCommand(msg);
    }
    @Override
    public void sendPlayerList(String[] players) {
        JsonObject data = new JsonObject();
        JsonArray jsonArray = new Gson().toJsonTree(players).getAsJsonArray();
        data.add("players", jsonArray);

        JsonObject msg = new JsonObject();
        msg.addProperty("service", "receivePlayerList");
        msg.add("data", data);

        this.sendCommand(msg);
    }
    @Override
    public void sendPlayersNUmber(int playersNumber) {
        JsonObject data = new JsonObject();
        data.addProperty("playersNumber", playersNumber);

        JsonObject msg = new JsonObject();
        msg.addProperty("service", "receivePlayersNumber");
        msg.add("data", data);

        this.sendCommand(msg);
    }
    @Override
    public void sendMainBoard(Card[][] mainBoard) {
        JsonObject data = new JsonObject();
        JsonArray jsonArray = new Gson().toJsonTree(mainBoard).getAsJsonArray();
        data.add("mainBoard", jsonArray);

        JsonObject msg = new JsonObject();
        msg.addProperty("service", "receiveMainBoard");
        msg.add("data", data);

        this.sendCommand(msg);
    }
    @Override
    public void sendAllPlayerBoard(ArrayList<Card[][]> playerBoards) {
        JsonObject data = new JsonObject();
        JsonArray jsonArray = new Gson().toJsonTree(playerBoards).getAsJsonArray();
        data.add("playerBoards", jsonArray);

        JsonObject msg = new JsonObject();
        msg.addProperty("service", "receiveAllPlayerBoard");
        msg.add("data", data);

        this.sendCommand(msg);
    }
    @Override
    public void addCardToClientBoard(String playerID, int column, Card[] cards) {
        JsonObject data = new JsonObject();
        data.addProperty("playerID", playerID);
        data.addProperty("column", column);
        JsonArray jsonArray = new Gson().toJsonTree(cards).getAsJsonArray();
        data.add("cards", jsonArray);

        JsonObject msg = new JsonObject();
        msg.addProperty("service", "addCardToPlayerBoard");
        msg.add("data", data);

        this.sendCommand(msg);
    }
    @Override
    public void dellCardFromMainBoard(PositionWithColor[] cards) {
        JsonObject data = new JsonObject();
        JsonArray jsonArray = new Gson().toJsonTree(cards).getAsJsonArray();
        data.add("cards", jsonArray);

        JsonObject msg = new JsonObject();
        msg.addProperty("service", "removeCardFromMainBoard");
        msg.add("data", data);

        this.sendCommand(msg);
    }
    @Override
    public void sendAllCommonGoal(int[] commonGoalID) {
        JsonObject data = new JsonObject();
        JsonArray jsonArray = new Gson().toJsonTree(commonGoalID).getAsJsonArray();
        data.add("commonGoalID", jsonArray);

        JsonObject msg = new JsonObject();
        msg.addProperty("service", "receiveAllCommonGoal");
        msg.add("data", data);

        this.sendCommand(msg);
    }
    @Override
    public void sendPrivateGoal(PositionWithColor[] cards, String playerID) {
        JsonObject data = new JsonObject();
        JsonArray jsonArray = new Gson().toJsonTree(cards).getAsJsonArray();
        data.addProperty("playerID", playerID);
        data.add("cards", jsonArray);

        JsonObject msg = new JsonObject();
        msg.addProperty("service", "receivePrivateGoal");
        msg.add("data", data);

        this.sendCommand(msg);
    }
    @Override
    public void sendEndGamePoint(JsonObject points) {
        JsonObject data = new JsonObject();
        data.add("points", points);

        JsonObject msg = new JsonObject();
        msg.addProperty("service", "endGameValue");
        msg.add("data", data);

        this.sendCommand(msg);
    }
    @Override
    public void sendWinner(JsonObject winner) {
        JsonObject data = new JsonObject();
        data.add("winner", winner);

        JsonObject msg = new JsonObject();
        msg.addProperty("service", "receiveWinner");
        msg.add("data", data);

        this.sendCommand(msg);
    }
    @Override
    public void sendLastCommonScored(JsonObject scored) {
        JsonObject data = new JsonObject();
        data.add("scored", scored);

        JsonObject msg = new JsonObject();
        msg.addProperty("service", "receiveWinner");
        msg.add("data", data);

        this.sendCommand(msg);
    }
    @Override
    public void sendError(JsonObject error, String playerID){
        JsonObject data = new JsonObject();
        data.addProperty("playerID", playerID);
        data.add("error", error);

        JsonObject msg = new JsonObject();
        msg.addProperty("service", "errorMSG");
        msg.add("data", data);

        this.sendCommand(msg);
    }
    @Override
    public void forceClientDisconnection() {
        JsonObject data = new JsonObject();

        JsonObject msg = new JsonObject();
        msg.addProperty("service", "errorMSG");
        msg.add("data", data);

        this.sendCommand(msg);
    }
    /*************************************************************************
     ************************************************** IN method ************
     * ***********************************************************************
     */
    public boolean startGame(JsonObject data){
        boolean bool = this.startGame(data.get("playerID").getAsString());
        return bool;
    }
    public boolean takeCard(JsonObject data){
        boolean bool = this.takeCard(data.get("column").getAsInt(),data.get("cards").toString(),data.get("playerID").getAsString());
        return bool;

    }

    /***********************************************************************************
     ************************************************** MultiClientSocketGame **********
     * *********************************************************************************
     */

    private class MultiClientSocketGame implements Runnable{
        private final Socket socket;
        private Scanner in;
        private PrintWriter out;
        public MultiClientSocketGame(Socket socket){
            this.socket = socket;
        }

        public void sendMSG (JsonObject msg) throws IOException {
            out.println(msg);
            out.flush();
        }

        public void setPlayerOffline(String playerID){
            System.out.println("\u001B[36m"+"client: " + playerID + " quit the game on port(SOCKET): " + PORT +"\u001B[0m");
            controller.setPlayerOffline(playerID);
        }
        public void setPlayerOnline(String playerID){
            System.out.println("\u001B[36m"+"client: " + playerID + " join the game on port(SOCKET): " + PORT +"\u001B[0m");
            controller.setPlayerOnline(playerID);
        }
        private void receiveMSG()  throws IOException {
            boolean existingMethod;
            JsonObject response = new JsonObject();
            response.addProperty("service", "receiveResponse");
            JsonObject sendData = new JsonObject();
            while (true){   //LOOP receive message
                String line = in.nextLine();
                JsonObject jsonObject = new Gson().fromJson(line, JsonObject.class);
                String methodName = jsonObject.get("service").getAsString();
                JsonObject data = jsonObject.get("data").getAsJsonObject();
                if (methodName.equals("quit")) {
                    //TODO friendly quit
                    sendData.addProperty("existingMethod", true);
                    sendData.addProperty("response",true);
                    this.setPlayerOffline(data.get("playerID").getAsString());
                    response.add("data", sendData);
                    this.sendMSG(response);
                    break;
                } else if(methodName.equals("join")){
                    sendData.addProperty("existingMethod", true);
                    sendData.addProperty("response",true);
                    this.setPlayerOnline(data.get("playerID").getAsString());
                    response.add("data", sendData);
                    this.sendMSG(response);
                } else {
                    existingMethod = true;
                    Method getNameMethod = null;

                    try {
                        getNameMethod = ControllerSOCKET.this.getClass().getMethod(methodName, JsonObject.class);
                    } catch (NoSuchMethodException e) {
                        existingMethod = false;
                    }

                    if(getNameMethod != null) {
                        try {
                            Boolean booleanResponse = (boolean) getNameMethod.invoke(ControllerSOCKET.this, data);
                            sendData.addProperty("response", booleanResponse);
                        } catch (IllegalAccessException | InvocationTargetException e) {
                            existingMethod = false;
                        }
                    }

                    sendData.addProperty("existingMethod", existingMethod);
                    response.add("data", sendData);
                    this.sendMSG(response);
                }
            }
            in.close();
            out.close();
            socket.close();
        }
        public void run(){
            //go here when players connect to the server
            try {
                in = new Scanner(socket.getInputStream());
                out = new PrintWriter(socket.getOutputStream());
                this.receiveMSG();
            } catch (IOException e) {
                System.err.println(e.getMessage());
            }
        }

    }
}
