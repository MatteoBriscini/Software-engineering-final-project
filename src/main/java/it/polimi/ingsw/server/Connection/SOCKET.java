package it.polimi.ingsw.server.Connection;


import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import it.polimi.ingsw.server.Controller;
import it.polimi.ingsw.shared.exceptions.addPlayerToGameException;
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
import java.util.Objects;
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
            System.out.println(e.toString());
            return;
        }
        System.err.println("\u001B[32m" + "Server (socket) ready on port: " + PORT + "\u001B[0m");
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
    /**********************************************************************************
     ************************************************** OUT playing methods ***********
     * ********************************************************************************
     */

    private void sendCommand(JsonObject msg, ArrayList<MultiClientSocketGame> tmpClients){
        for(MultiClientSocketGame client: tmpClients){
            try {

                client.sendMSG(msg);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
    public void notifyActivePlayer(String activePlayerID, ArrayList<MultiClientSocketGame> clients) {
        JsonObject data = new JsonObject();
        data.addProperty("playerID", activePlayerID);

        JsonObject msg = new JsonObject();
        msg.addProperty("service", "notifyActivePlayer");
        msg.add("data", data);

        this.sendCommand(msg, clients);
    }
    public void sendPlayerList(String[] players, ArrayList<MultiClientSocketGame> clients) {
        JsonObject data = new JsonObject();
        JsonArray jsonArray = new Gson().toJsonTree(players).getAsJsonArray();
        data.add("players", jsonArray);

        JsonObject msg = new JsonObject();
        msg.addProperty("service", "receivePlayerList");
        msg.add("data", data);

        this.sendCommand(msg, clients);
    }
    public void sendPlayersNUmber(int playersNumber, ArrayList<MultiClientSocketGame> clients) {
        JsonObject data = new JsonObject();
        data.addProperty("playersNumber", playersNumber);

        JsonObject msg = new JsonObject();
        msg.addProperty("service", "receivePlayersNumber");
        msg.add("data", data);

        this.sendCommand(msg,clients);
    }
    public void sendMainBoard(Card[][] mainBoard, ArrayList<MultiClientSocketGame> clients) {
        JsonObject data = new JsonObject();
        JsonArray jsonArray = new Gson().toJsonTree(mainBoard).getAsJsonArray();
        data.add("mainBoard", jsonArray);

        JsonObject msg = new JsonObject();
        msg.addProperty("service", "receiveMainBoard");
        msg.add("data", data);

        this.sendCommand(msg, clients);
    }
    public void sendAllPlayerBoard(ArrayList<Card[][]> playerBoards, ArrayList<MultiClientSocketGame> clients) {
        JsonObject data = new JsonObject();
        JsonArray jsonArray = new Gson().toJsonTree(playerBoards).getAsJsonArray();
        data.add("playerBoards", jsonArray);

        JsonObject msg = new JsonObject();
        msg.addProperty("service", "receiveAllPlayerBoard");
        msg.add("data", data);

        this.sendCommand(msg, clients);
    }
    public void addCardToClientBoard(String playerID, int column, Card[] cards, ArrayList<MultiClientSocketGame> clients) {
        JsonObject data = new JsonObject();
        data.addProperty("playerID", playerID);
        data.addProperty("column", column);
        JsonArray jsonArray = new Gson().toJsonTree(cards).getAsJsonArray();
        data.add("cards", jsonArray);

        JsonObject msg = new JsonObject();
        msg.addProperty("service", "addCardToPlayerBoard");
        msg.add("data", data);

        this.sendCommand(msg, clients);
    }
    public void dellCardFromMainBoard(PositionWithColor[] cards, ArrayList<MultiClientSocketGame> clients) {
        JsonObject data = new JsonObject();
        JsonArray jsonArray = new Gson().toJsonTree(cards).getAsJsonArray();
        data.add("cards", jsonArray);

        JsonObject msg = new JsonObject();
        msg.addProperty("service", "removeCardFromMainBoard");
        msg.add("data", data);

        this.sendCommand(msg, clients);
    }
    public void sendAllCommonGoal(int[] commonGoalID, ArrayList<MultiClientSocketGame> clients) {
        JsonObject data = new JsonObject();
        JsonArray jsonArray = new Gson().toJsonTree(commonGoalID).getAsJsonArray();
        data.add("commonGoalID", jsonArray);

        JsonObject msg = new JsonObject();
        msg.addProperty("service", "receiveAllCommonGoal");
        msg.add("data", data);

        this.sendCommand(msg, clients);
    }
    public void sendPrivateGoal(PositionWithColor[] cards, String playerID, ArrayList<MultiClientSocketGame> clients) {
        JsonObject data = new JsonObject();
        JsonArray jsonArray = new Gson().toJsonTree(cards).getAsJsonArray();
        data.addProperty("playerID", playerID);
        data.add("cards", jsonArray);

        JsonObject msg = new JsonObject();
        msg.addProperty("service", "receivePrivateGoal");
        msg.add("data", data);

        this.sendCommand(msg, clients);
    }
    public void sendEndGamePoint(JsonObject points, ArrayList<MultiClientSocketGame> clients) {
        JsonObject data = new JsonObject();
        data.add("points", points);

        JsonObject msg = new JsonObject();
        msg.addProperty("service", "endGameValue");
        msg.add("data", data);

        this.sendCommand(msg, clients);
    }
    public void sendWinner(JsonObject winner, ArrayList<MultiClientSocketGame> clients) {
        JsonObject data = new JsonObject();
        data.add("winner", winner);

        JsonObject msg = new JsonObject();
        msg.addProperty("service", "receiveWinner");
        msg.add("data", data);

        this.sendCommand(msg, clients);
    }
    public void sendLastCommonScored(JsonObject scored, ArrayList<MultiClientSocketGame> clients) {
        JsonObject data = new JsonObject();
        data.add("scored", scored);

        JsonObject msg = new JsonObject();
        msg.addProperty("service", "receiveLastCommonScored");
        msg.add("data", data);

        this.sendCommand(msg, clients);
    }
    public void sendError(JsonObject error, String playerID, ArrayList<MultiClientSocketGame> clients){
        JsonObject data = new JsonObject();
        data.addProperty("playerID", playerID);
        data.add("error", error);

        JsonObject msg = new JsonObject();
        msg.addProperty("service", "errorMSG");
        msg.add("data", data);

        this.sendCommand(msg, clients);
    }
    public void forceClientDisconnection(ArrayList<MultiClientSocketGame> clients) {
        JsonObject data = new JsonObject();
        JsonObject msg = new JsonObject();
        msg.addProperty("service", "forceDisconnection");
        msg.add("data", data);
        this.sendCommand(msg, clients);

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
    public void sendBroadcastMsg(String MSG, String sender, ArrayList<MultiClientSocketGame> clients) {
        JsonObject data = new JsonObject();
        data.addProperty("msg", MSG);
        data.addProperty("sender", sender);

        JsonObject msg = new JsonObject();
        msg.addProperty("service", "receiveBroadcastMsg");
        msg.add("data", data);

        this.sendCommand(msg, clients);
    }
    public void sendPrivateMSG(String userID, String MSG, String sender, ArrayList<MultiClientSocketGame> clients) {
        JsonObject data = new JsonObject();
        data.addProperty("userID", userID);
        data.addProperty("msg", MSG);
        data.addProperty("sender", sender);

        JsonObject msg = new JsonObject();
        msg.addProperty("service", "receivePrivateMSG");
        msg.add("data", data);


        for(MultiClientSocketGame client: clients){
            try {
                client.privateChat(msg, userID, sender, clients);
            } catch (IOException e) {
                throw new RuntimeException(e);

            }
        }
    }
    public boolean receiveBroadcastMsg(JsonObject data, MultiClientSocketGame ref){
        this.receiveBroadcastMsg(data.get("msg").getAsString(), data.get("sender").getAsString(),ref.getController());
        return true;
    }
    public boolean receivePrivateMSG(JsonObject data, MultiClientSocketGame ref){
        this.receivePrivateMSG(data.get("userID").getAsString(), data.get("msg").getAsString(), data.get("sender").getAsString(),ref.getController());
        return true;
    }
    /*************************************************************************
     ************************************************** IN playing methods ************
     * ***********************************************************************
     */
    public boolean startGame(JsonObject data, MultiClientSocketGame ref){
        boolean bool = this.startGame(data.get("playerID").getAsString(),ref.getController());
        return bool;
    }
    public boolean takeCard(JsonObject data, MultiClientSocketGame ref){
        boolean bool = this.takeCard(data.get("column").getAsInt(),data.get("cards").toString(),data.get("playerID").getAsString(),ref.getController());
        return bool;
    }

    /*************************************************************************
     ************************************************** IN lobby methods ********
     * ***********************************************************************
     * */

    public boolean login(JsonObject data, MultiClientSocketGame ref){
        try {
            String controllerRef = this.login(data.get("ID").getAsString(), data.get("pwd").getAsString());
            if(!controllerRef.equals("null")){

                ref.setController(controllerRef);
                JsonObject data1 = new JsonObject();

                JsonObject msg = new JsonObject();
                msg.addProperty("service", "setPlayerAsPlaying");
                msg.add("data", data1);

                ArrayList<MultiClientSocketGame> tmp = new ArrayList<>();
                tmp.add(ref);

                this.sendCommand(msg, tmp);
                ref.setPlayerOnline();
            }
        } catch (LoginException e) {
            return false;
        }
        return true;
    }


    public boolean signUp(JsonObject data, MultiClientSocketGame ref){
        try {
            this.signUp(data.get("ID").getAsString(), data.get("pwd").getAsString());
        } catch (LoginException e) {
            return false;
        }
        return true;

    }

    public boolean joinGame(JsonObject data, MultiClientSocketGame ref){
        String controllerRef;
        if(!data.get("searchID").getAsString().equals("null")){
            try {
                controllerRef = this.joinGame(data.get("ID").getAsString(), data.get("searchID").getAsString());
            } catch (addPlayerToGameException e) {
                return false;
            }
        }else{
            try {
                controllerRef = this.joinGame(data.get("ID").getAsString());
            } catch (addPlayerToGameException e) {
                return false;
            }
        }
        ref.setController(controllerRef);
        ref.setPlayerOnline();
        return true;
    }

    public boolean createGame(JsonObject data, MultiClientSocketGame ref){
        String controllerRef;
        if(data.get("maxPlayerNumber").getAsInt() != 0){
            try {
                controllerRef = this.createGame(data.get("ID").getAsString(), data.get("maxPlayerNumber").getAsInt());

            } catch (addPlayerToGameException e) {
                return false;
            }
        }else{
            try {
               controllerRef = this.createGame(data.get("ID").getAsString());
            } catch (addPlayerToGameException e) {
                return false;
            }
        }
        ref.setController(controllerRef);
        ref.setPlayerOnline();
        return true;
    }


    /***********************************************************************************
     ************************************************** MultiClientSocketGame **********
     * *********************************************************************************
     */

    public class MultiClientSocketGame implements Runnable{
        private final Socket socket;
        private String controllerRef;
        private boolean cntOn = true, quit;
        private String clientID;
        private Scanner in;
        private PrintWriter out;
        private int timeout;
        private int pingPongTime;
        private Boolean pingPongResponse = false;
        Thread pingPongThread;
        public void setController (String controllerRef){
            this.controllerRef=controllerRef;
        }
        public String getController(){
            return controllerRef;
        }
        public MultiClientSocketGame(Socket socket){
            this.socket = socket;

            try {
                jsonCreate();
            } catch (FileNotFoundException e) {
                System.out.println("MultiClientSocketGame: JSON FILE NOT FOUND");
                throw new RuntimeException(e);
            }
        }

        private void jsonCreate() throws FileNotFoundException {  //download json data
            InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream(JsonUrl.getUrl("netConfig"));
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
            if(!cntOn || quit) return;
            if(!pingPongResponse){
                setPlayerOffline(clientID);
                return;
            }
            pingPongResponse = false;
            this.pingPong();
        }
        public void forceClientDisconnection() throws IOException {
            quit = true;
        }
        public void privateChat(JsonObject msg, String playerID, String sender, ArrayList<MultiClientSocketGame> clients) throws IOException {
            if (!sender.equals(this.clientID) && playerID.equals(this.clientID) && clients.contains(this))this.sendMSG(msg);
        }
        public void sendMSG (JsonObject msg) throws IOException {
            out.println(msg);
            out.flush();
        }
        public void setPlayerOffline(String playerID){
            System.out.println("\u001B[33m"+"client: " + playerID + " quit the game(SOCKET)" +"\u001B[0m");
            Controller controller = SOCKET.this.getActualController(controllerRef);
            controller.setPlayerOffline(playerID);
            controller.removeClientSOCKET(this);
        }
        public void setPlayerOnline(){
            pingPongThread = new Thread(this::pingPong);       //start ping pong
            pingPongThread.start();

            this.setPlayerOnline(this.clientID);
        }
        public void setPlayerOnline(String playerID){
            System.out.println("\u001B[36m"+"client: " + playerID + " join the game (SOCKET)" +"\u001B[0m");
            Controller controller = SOCKET.this.getActualController(controllerRef);
            controller.setPlayerOnline(playerID);
            controller.addClientSOCKET(this);
        }
        private void receiveMSG()  throws IOException {  //TODO quit cnt sia qui che in rmi
            boolean existingMethod;
            JsonObject response = new JsonObject();
            response.addProperty("service", "receiveResponse");
            JsonObject sendData = new JsonObject();
            while (cntOn){   //LOOP receive message
                String line = in.nextLine();
                //System.out.println(line + clientID);
                JsonObject jsonObject = new Gson().fromJson(line, JsonObject.class);
                String methodName = jsonObject.get("service").getAsString();
                JsonObject data = jsonObject.get("data").getAsJsonObject();
                if (methodName.equals("quitGame")) {
                    quit = true;
                    sendData.addProperty("existingMethod", true);
                    sendData.addProperty("response",true);
                    this.setPlayerOffline(data.get("playerID").getAsString());
                    response.add("data", sendData);
                    this.sendMSG(response);
                } else if(methodName.equals("joinLobby")){
                    quit = false;
                    cntOn = true;
                    sendData.addProperty("existingMethod", true);
                    sendData.addProperty("response",true);
                    this.clientID = data.get("playerID").getAsString();
                    System.out.println("\u001B[36m"+"client: " + this.clientID + " join the lobby (SOCKET)" +"\u001B[0m");
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
                        getNameMethod = SOCKET.this.getClass().getMethod(methodName, JsonObject.class, MultiClientSocketGame.class);
                    } catch (NoSuchMethodException e) {
                        existingMethod = false;
                    }
                    if(getNameMethod != null) {
                        try {
                            Boolean booleanResponse = (boolean) getNameMethod.invoke(SOCKET.this, data, this);
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
