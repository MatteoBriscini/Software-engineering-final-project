package it.polimi.ingsw.server.Connection;


import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import it.polimi.ingsw.server.Exceptions.addPlayerToGameException;
import it.polimi.ingsw.server.Lobby.Lobby;
import it.polimi.ingsw.shared.Cards.Card;
import it.polimi.ingsw.shared.Connection.ConnectionType;
import it.polimi.ingsw.shared.JsonSupportClasses.JsonUrl;
import it.polimi.ingsw.shared.JsonSupportClasses.PositionWithColor;

import javax.security.auth.login.LoginException;
import java.io.*;

import java.lang.reflect.*;
import java.net.ServerSocket;
import java.net.Socket;

import java.util.ArrayList;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SOCKET extends ConnectionController{
    private ServerSocket serverSocket;
    private ArrayList<MultiClientSocketGame> clients = new ArrayList<>();
    public SOCKET(Lobby lobby, int port){
        super(lobby, port, ConnectionType.SOCKET);
        this.connection();
    }
    private int pingPongTime = 5000;
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
                break;
            }
        }
        executor.shutdown();
    }
    /*************************************************************************
     ************************************************** OUT playing methods ***********
     * ***********************************************************************
     */

    private void sendCommand(JsonObject msg){
        for(MultiClientSocketGame client: clients){
            try {
                client.sendMSG(msg);
            } catch (IOException e) {
                throw new RuntimeException(e);

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
        msg.addProperty("service", "forceDisconnection");
        msg.add("data", data);
        this.sendCommand(msg);

        for(MultiClientSocketGame client: clients){
            try {
                client.forceClientDisconnection();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
    /**************************************************************************
     ************************************************** chat ******************
     * ************************************************************************
     */
    @Override
    public void sendBroadcastMsg(String MSG, String sender) {
        JsonObject data = new JsonObject();
        data.addProperty("msg", MSG);
        data.addProperty("sender", sender);

        JsonObject msg = new JsonObject();
        msg.addProperty("service", "receiveBroadcastMsg");
        msg.add("data", data);

        this.sendCommand(msg);
    }

    @Override
    public void sendPrivateMSG(String userID, String MSG, String sender) {
        JsonObject data = new JsonObject();
        data.addProperty("userID", userID);
        data.addProperty("msg", MSG);
        data.addProperty("sender", sender);

        JsonObject msg = new JsonObject();
        msg.addProperty("service", "receivePrivateMSG");
        msg.add("data", data);


        for(MultiClientSocketGame client: clients){
            try {
                client.privateChat(msg, userID, sender);
            } catch (IOException e) {
                throw new RuntimeException(e);

            }
        }
    }
    public boolean receiveBroadcastMsg(JsonObject data){
        this.receiveBroadcastMsg(data.get("msg").getAsString(), data.get("sender").getAsString());
        return true;
    }
    public boolean receivePrivateMSG(JsonObject data){
        this.receivePrivateMSG(data.get("userID").getAsString(), data.get("msg").getAsString(), data.get("sender").getAsString());
        return true;
    }
    /*************************************************************************
     ************************************************** IN playing methods ************
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

    /*************************************************************************
     ************************************************** IN lobby methods ********
     * ***********************************************************************
     * */

    public boolean login(JsonObject data){

        try {
            this.login(data.get("ID").getAsString(), data.get("pwd").getAsString());
        } catch (LoginException e) {
            return false;
        }
        return true;
    }


    public boolean signUp(JsonObject data){

        try {
            this.signUp(data.get("ID").getAsString(), data.get("pwd").getAsString());
        } catch (LoginException e) {
            return false;
        }
        return true;

    }

    public boolean joinGame(JsonObject data){
        if(!data.get("searchID").getAsString().equals("null")){
            try {
                this.joinGame(data.get("ID").getAsString(), data.get("searchID").getAsString());
            } catch (addPlayerToGameException e) {
                return false;
            }
        }else{
            try {
                this.joinGame(data.get("ID").getAsString());
            } catch (addPlayerToGameException e) {
                return false;
            }
        }

        return true;
    }

    public boolean createGame(JsonObject data){

        if(data.get("maxPlayerNumber").getAsInt() != 0){
            try {
                this.createGame(data.get("ID").getAsString(), data.get("maxPlayerNumber").getAsInt());
            } catch (addPlayerToGameException e) {
                return false;
            }
        }else{
            try {
                this.createGame(data.get("ID").getAsString());
            } catch (addPlayerToGameException e) {
                return false;
            }
        }
        return true;
    }


    /***********************************************************************************
     ************************************************** MultiClientSocketGame **********
     * *********************************************************************************
     */

    private class MultiClientSocketGame implements Runnable{
        private final Socket socket;
        private boolean cntOn = true;
        private JsonUrl jsonUrl;
        private String clientID;
        private Scanner in;
        private PrintWriter out;
        private int timeout;
        private int pingPongTime;
        private Boolean pingPongResponse = false;
        Thread pingPongThread;
        public MultiClientSocketGame(Socket socket){
            this.socket = socket;

            try {
                jsonCreate();
            } catch (FileNotFoundException e) {
                System.out.println("MultiClientSocketGame: JSON FILE NOT FOUND");
                throw new RuntimeException(e);
            }


            //pingPongThread = new Thread(this::pingPong);       //start ping pong
            //pingPongThread.start();
        }

        private void jsonCreate() throws FileNotFoundException {  //download json data
            InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream(jsonUrl.getUrl("netConfig"));
            if(inputStream == null) throw new FileNotFoundException();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            JsonObject jsonObject = new Gson().fromJson(bufferedReader , JsonObject.class);
            this.pingPongTime = jsonObject.get("pingPongTime").getAsInt();
            this.timeout = jsonObject.get("socketTimeout").getAsInt();
        }

        public void pingResponse(){
            synchronized (pingPongThread) {
                pingPongResponse = true;
                pingPongThread.notifyAll();
            }
        }
        private void pingPong(){
            try {
                Thread.sleep(pingPongTime);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            JsonObject data = new JsonObject();
            JsonObject msg = new JsonObject();
            msg.addProperty("service", "pingPong");
            msg.add("data", data);
            synchronized (pingPongThread) {
                try {
                    this.sendMSG(msg);  //send socket message
                    pingPongThread.wait(timeout);
                } catch (IOException | InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
            if(!cntOn) return;
            if(!pingPongResponse){

                setPlayerOffline(clientID);
                return;
            }
            pingPongResponse = false;
            this.pingPong();
        }
        public void forceClientDisconnection() throws IOException {
            if(cntOn)this.setPlayerOffline(this.clientID);

            cntOn = false;
            in.close();
            out.close();
            socket.close();
        }
        public void privateChat(JsonObject msg, String playerID, String sender) throws IOException {
            if (!sender.equals(this.clientID) &&playerID.equals(this.clientID))this.sendMSG(msg);
        }
        public void sendMSG (JsonObject msg) throws IOException {
            out.println(msg);
            out.flush();
        }
        public void setPlayerOffline(String playerID){
            System.out.println("\u001B[33m"+"client: " + playerID + " quit the game on port(SOCKET): " + PORT +"\u001B[0m");
            controller.setPlayerOffline(playerID);
        }
        public void setPlayerOnline(String playerID){
            System.out.println("\u001B[36m"+"client: " + playerID + " join the game on port(SOCKET): " + PORT  +"\u001B[0m");
            controller.setPlayerOnline(playerID);
        }
        private void receiveMSG()  throws IOException {
            boolean existingMethod;
            JsonObject response = new JsonObject();
            response.addProperty("service", "receiveResponse");
            JsonObject sendData = new JsonObject();
            while (cntOn){   //LOOP receive message
                String line = in.nextLine();
                //System.err.println(this.clientID + ":  "+ line);
                JsonObject jsonObject = new Gson().fromJson(line, JsonObject.class);
                String methodName = jsonObject.get("service").getAsString();
                JsonObject data = jsonObject.get("data").getAsJsonObject();

                if (methodName.equals("quit")) {
                    cntOn = false;
                    sendData.addProperty("existingMethod", true);
                    sendData.addProperty("response",true);
                    this.setPlayerOffline(data.get("playerID").getAsString());
                    response.add("data", sendData);
                    this.sendMSG(response);
                    break;
                } else if(methodName.equals("join")){
                    cntOn = true;
                    sendData.addProperty("existingMethod", true);
                    sendData.addProperty("response",true);
                    this.clientID = data.get("playerID").getAsString();
                    this.setPlayerOnline(data.get("playerID").getAsString());
                    response.add("data", sendData);
                    this.sendMSG(response);
                } else if(methodName.equals("pingPong")){

                    data = new JsonObject();
                    JsonObject msg = new JsonObject();
                    msg.addProperty("service", "pingResponse");
                    msg.add("data", data);
                    this.sendMSG(msg);
                } else if(methodName.equals("pingResponse")){
                    this.pingResponse();
                } else {
                    existingMethod = true;
                    Method getNameMethod = null;

                    try {
                        getNameMethod = SOCKET.this.getClass().getMethod(methodName, JsonObject.class);
                    } catch (NoSuchMethodException e) {
                        existingMethod = false;
                    }
                    System.out.println(existingMethod);
                    if(getNameMethod != null) {
                        try {
                            Boolean booleanResponse = (boolean) getNameMethod.invoke(SOCKET.this, data);
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
