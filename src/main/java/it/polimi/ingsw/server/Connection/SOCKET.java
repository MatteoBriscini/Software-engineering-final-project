package it.polimi.ingsw.server.Connection;


import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import it.polimi.ingsw.server.Controller;
import it.polimi.ingsw.shared.TextColor;
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

import java.util.*;
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

    /**
     * setup rmi connection
     */
    synchronized public void connection(){
        try {
            serverSocket = new ServerSocket(PORT);  //throw exception if unavailable port
        } catch (IOException e) {
            System.out.println(e.toString());
            return;
        }
        System.err.println("\u001B[32m" + "Server (socket) ready on port: " + PORT + TextColor.DEFAULT.get());
    }

    /**
     * loop to accept new client connection
     */
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

    /**
     * @param activePlayerID the id of the player have to play now
     * @param clients list of runnable class managed the client, server have to send command only to this clients
     */
    public void notifyActivePlayer(String activePlayerID, ArrayList<MultiClientSocketGame> clients) {
        JsonObject data = new JsonObject();
        data.addProperty("playerID", activePlayerID);

        this.sendCommand(clients.get(clients.size()-1).prepareMSG(data, "notifyActivePlayer"), clients);
    }

    /**
     * used when create or recreate client data
     * @param players the list of players in the game when it starts
     * @param clients list of runnable class managed the client, server have to send command only to this clients
     */
    public void sendPlayerList(String[] players, ArrayList<MultiClientSocketGame> clients) {
        JsonObject data = new JsonObject();
        JsonArray jsonArray = new Gson().toJsonTree(players).getAsJsonArray();
        data.add("players", jsonArray);

        this.sendCommand(clients.get(clients.size()-1).prepareMSG(data, "receivePlayerList"), clients);
    }

    /**
     * used in waiting room to communicate to the game creator the number of player actual in the game
     * @param playersNumber number of player actual in the game
     * @param clients list of runnable class managed the client, server have to send command only to this clients
     */
    public void sendPlayersNUmber(int playersNumber, ArrayList<MultiClientSocketGame> clients) {
        JsonObject data = new JsonObject();
        data.addProperty("playersNumber", playersNumber);

        this.sendCommand(clients.get(clients.size()-1).prepareMSG(data, "receivePlayersNumber"),clients);
    }

    /**
     * used in create or recreate data clients, it will be sent in broadcast
     * @param mainBoard cardMatrix represent the mainBoard
     * @param clients list of runnable class managed the client, server have to send command only to this clients
     */
    public void sendMainBoard(Card[][] mainBoard, ArrayList<MultiClientSocketGame> clients) {
        JsonObject data = new JsonObject();
        JsonArray jsonArray = new Gson().toJsonTree(mainBoard).getAsJsonArray();
        data.add("mainBoard", jsonArray);

        this.sendCommand(clients.get(clients.size()-1).prepareMSG(data, "receiveMainBoard"), clients);
    }

    /**
     * used in create or recreate data clients, it will be sent in broadcast
     * @param playerBoards arrayList with card matrix who represent all players board of thi specific game
     * @param clients list of runnable class managed the client, server have to send command only to this clients
     */
    public void sendAllPlayerBoard(ArrayList<Card[][]> playerBoards, ArrayList<MultiClientSocketGame> clients) {
        JsonObject data = new JsonObject();
        JsonArray jsonArray = new Gson().toJsonTree(playerBoards).getAsJsonArray();
        data.add("playerBoards", jsonArray);

        this.sendCommand(clients.get(clients.size()-1).prepareMSG(data, "receiveAllPlayerBoard"), clients);
    }

    /**
     * update playerBoard by delta, it will be sent in broadcast
     * @param playerID identify witch playerBoard
     * @param column position on the playerBoard
     * @param cards the delta
     * @param clients list of runnable class managed the client, server have to send command only to this clients
     */
    public void addCardToClientBoard(String playerID, int column, Card[] cards, ArrayList<MultiClientSocketGame> clients) {
        JsonObject data = new JsonObject();
        data.addProperty("playerID", playerID);
        data.addProperty("column", column);
        JsonArray jsonArray = new Gson().toJsonTree(cards).getAsJsonArray();
        data.add("cards", jsonArray);

        this.sendCommand(clients.get(clients.size()-1).prepareMSG(data, "addCardToPlayerBoard"), clients);
    }

    /**
     * update mainBoard by delta, it will be sent in broadcast
     * @param cards the delta
     * @param clients list of runnable class managed the client, server have to send command only to this clients
     */
    public void dellCardFromMainBoard(PositionWithColor[] cards, ArrayList<MultiClientSocketGame> clients) {
        JsonObject data = new JsonObject();
        JsonArray jsonArray = new Gson().toJsonTree(cards).getAsJsonArray();
        data.add("cards", jsonArray);

        this.sendCommand(clients.get(clients.size()-1).prepareMSG(data, "removeCardFromMainBoard"), clients);
    }

    /**
     * it will be sent in broadcast
     * @param commonGoalID array with id of all common goals
     * @param clients list of runnable class managed the client, server have to send command only to this clients
     */
    public void sendAllCommonGoal(int[] commonGoalID, ArrayList<MultiClientSocketGame> clients) {
        JsonObject data = new JsonObject();
        JsonArray jsonArray = new Gson().toJsonTree(commonGoalID).getAsJsonArray();
        data.add("commonGoalID", jsonArray);

        this.sendCommand(clients.get(clients.size()-1).prepareMSG(data, "receiveAllCommonGoal"), clients);
    }

    /**
     * @param cards position on payer board and color need to respect to achieve private goal
     * @param playerID recipient of th message
     * @param clients list of runnable class managed the client, server have to send command only to this clients
     */
    public void sendPrivateGoal(PositionWithColor[] cards, String playerID, ArrayList<MultiClientSocketGame> clients) {
        JsonObject data = new JsonObject();
        JsonArray jsonArray = new Gson().toJsonTree(cards).getAsJsonArray();
        data.addProperty("playerID", playerID);
        data.add("cards", jsonArray);

        this.sendCommand(clients.get(clients.size()-1).prepareMSG(data, "receivePrivateGoal"), clients);
    }

    /**
     * @param points jsonObject with all points for all the player in the game, it will be sent in broadcast
     * @param clients list of runnable class managed the client, server have to send command only to this clients
     */
    public void sendEndGamePoint(JsonObject points, ArrayList<MultiClientSocketGame> clients) {
        JsonObject data = new JsonObject();
        data.add("points", points);

        this.sendCommand(clients.get(clients.size()-1).prepareMSG(data, "endGameValue"), clients);
        for (MultiClientSocketGame client : clients) {
            try {
                client.forceClientDisconnection();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * @param winner jsonObject with point and name of the winner it will be sent in broadcast
     * @param clients list of runnable class managed the client, server have to send command only to this clients
     */
    public void sendWinner(JsonObject winner, ArrayList<MultiClientSocketGame> clients) {
        JsonObject data = new JsonObject();
        data.add("winner", winner);

        this.sendCommand(clients.get(clients.size()-1).prepareMSG(data,"receiveWinner"), clients);
    }

    /**
     * @param scored jsonObject with all point scored by all the player in the game by common goal, it will be sent in broadcast
     * @param clients list of runnable class managed the client, server have to send command only to this clients
     */
    public void sendLastCommonScored(JsonObject scored, ArrayList<MultiClientSocketGame> clients) {
        JsonObject data = new JsonObject();
        data.add("scored", scored);

        this.sendCommand(clients.get(clients.size()-1).prepareMSG(data, "receiveLastCommonScored"), clients);
    }

    /**
     * @param error jsonObject with error id and error code
     * @param playerID recipient of the error msg
     * @param clients list of runnable class managed the client, server have to send command only to this clients
     */
    public void sendError(JsonObject error, String playerID, ArrayList<MultiClientSocketGame> clients){
        JsonObject data = new JsonObject();
        data.addProperty("playerID", playerID);
        data.add("error", error);

        this.sendCommand(clients.get(clients.size()-1).prepareMSG(data, "errorMSG"), clients);
    }
    /**
     * server can force the return to the lobby on all client in one game, ending the game
     * @param clients list of runnable class managed the client, server have to send command only to this clients
     */
    public void forceClientDisconnection(ArrayList<MultiClientSocketGame> clients) {
        for(MultiClientSocketGame client: clients){
            try {
                client.forceClientDisconnection();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        JsonObject data = new JsonObject();
        this.sendCommand(clients.get(clients.size()-1).prepareMSG(data,"forceDisconnection"), clients);
    }

    /**************************************************************************
     ************************************************** chat ******************
     * ************************************************************************
     * *
     * send message in broadcast to all clients
     * @param MSG message to send
     * @param sender name of the player who sends the message
     * @param clients list of clients have to send the message
     */
    public void sendBroadcastMsg(String MSG, String sender, ArrayList<MultiClientSocketGame> clients) {
        JsonObject data = new JsonObject();
        data.addProperty("msg", MSG);
        data.addProperty("sender", sender);

        this.sendCommand(clients.get(clients.size()-1).prepareMSG(data,"receiveBroadcastMsg"), clients);
    }

    /**
     * send a message in private to only one client
     * @param userID id of the player the message is for
     * @param MSG message to send
     * @param sender name of the player who sends the message
     * @param clients list of clients have to send the message
     */
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
    /**
     * server receive public message, have to redirect it
     * @param data all the data need from the method package in a json file
     * @param ref ref to the runnable class mange the client
     * @return true if the command goes in the right way
     */
    public boolean receiveBroadcastMsg(JsonObject data, MultiClientSocketGame ref){
        this.receiveBroadcastMsg(data.get("msg").getAsString(), data.get("sender").getAsString(),ref.getController());
        return true;
    }
    /**
     * server receive private message, have to redirect it
     * @param data all the data need from the method package in a json file
     * @param ref ref to the runnable class mange the client
     * @return true if the command goes in the right way
     */
    public boolean receivePrivateMSG(JsonObject data, MultiClientSocketGame ref){
        this.receivePrivateMSG(data.get("userID").getAsString(), data.get("msg").getAsString(), data.get("sender").getAsString(),ref.getController());
        return true;
    }
    /*************************************************************************
     ************************************************** IN playing methods ************
     * ***********************************************************************
     /**
     * to start the game
     * @param data all the data need from the method package in a json file
     * @param ref ref to the runnable class mange the client
     * @return true if the command goes in the right way
     */
    public boolean startGame(JsonObject data, MultiClientSocketGame ref){
        boolean bool = this.startGame(data.get("playerID").getAsString(),ref.getController());
        return bool;
    }
    /**
     * to take cards
     * @param data all the data need from the method package in a json file
     * @param ref ref to the runnable class mange the client
     * @return true if the command goes in the right way
     */
    public boolean takeCard(JsonObject data, MultiClientSocketGame ref){
        boolean bool = this.takeCard(data.get("column").getAsInt(),data.get("cards").toString(),data.get("playerID").getAsString(),ref.getController());
        return bool;
    }

    /*************************************************************************
     ************************************************** IN lobby methods ********
     * ***********************************************************************
     *
     * to login to the server
     * @param data all the data need from the method package in a json file
     * @param ref ref to the runnable class mange the client
     * @return true if the command goes in the right way
     */

    public boolean login(JsonObject data, MultiClientSocketGame ref){
        try {
            String controllerRef = this.login(data.get("ID").getAsString(), data.get("pwd").getAsString());
            if(!controllerRef.equals("null")){
                ref.setController(controllerRef);
                JsonObject data1 = new JsonObject();

                ArrayList<MultiClientSocketGame> tmp = new ArrayList<>();
                tmp.add(ref);

                this.sendCommand(ref.prepareMSG(data1, "setPlayerAsPlaying"), tmp);
                ref.setPlayerOnline();
            }
        } catch (LoginException e) {
            return false;
        }
        return true;
    }
    /**
     * to sign up to the server
     * @param data all the data need from the method package in a json file
     * @param ref ref to the runnable class mange the client
     * @return true if the command goes in the right way
     */
    public boolean signUp(JsonObject data, MultiClientSocketGame ref){
        try {
            this.signUp(data.get("ID").getAsString(), data.get("pwd").getAsString());
        } catch (LoginException e) {
            return false;
        }
        return true;

    }
    /**
     * to join a nwe game
     * @param data all the data need from the method package in a json file
     * @param ref ref to the runnable class mange the client
     * @return true if the command goes in the right way
     */
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
    /**
     * to create a nwe game
     * @param data all the data need from the method package in a json file
     * @param ref ref to the runnable class mange the client
     * @return true if the command goes in the right way
     */
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
     *
     * runnable class, al client are managed on server on a private thread by this class
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
                pingPongThread.notify();
            }
        }
        private void pingPong(){
            try {
                Thread.sleep(pingPongTime);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            JsonObject data = new JsonObject();
            synchronized (pingPongThread) {
                try {
                    this.sendMSG(prepareMSG(data,"pingPong"));  //send a socket message
                    pingPongThread.wait(timeout);
                } catch (IOException | InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
            if(!pingPongResponse){
                if(cntOn && !quit)setPlayerOffline(clientID);
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
        private void sendResponse(boolean existigMethod, boolean methodResult) throws IOException {
            JsonObject sendData = new JsonObject();
            sendData.addProperty("existingMethod", existigMethod);
            sendData.addProperty("response", methodResult);
            sendMSG(prepareMSG(sendData,"receiveResponse"));
        }
        private JsonObject prepareMSG(JsonObject data, String methodName){
            JsonObject msg = new JsonObject();
            msg.addProperty("service", methodName);
            msg.add("data", data);
            return msg;
        }
        public void sendMSG (JsonObject msg) throws IOException {
            out.println(msg);
            out.flush();
        }
        public void setPlayerOffline(String playerID){
            System.out.println(TextColor.YELLOW.get()+"client: " + playerID + " quit the game(SOCKET)" +TextColor.DEFAULT.get());
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
            System.out.println(TextColor.LIGHTBLUE.get()+"client: " + playerID + " join the game (SOCKET)" +TextColor.DEFAULT.get());
            Controller controller = SOCKET.this.getActualController(controllerRef);
            controller.setPlayerOnline(playerID);
            controller.addClientSOCKET(this);
        }
        private void receiveMSG()  throws IOException {
            boolean existingMethod;
            JsonObject response = new JsonObject();
            response.addProperty("service", "receiveResponse");
            JsonObject sendData = new JsonObject();
            while (cntOn){   //LOOP receive message
                String line = in.nextLine();
                JsonObject jsonObject = new Gson().fromJson(line, JsonObject.class);
                String methodName = jsonObject.get("service").getAsString();
                JsonObject data = jsonObject.get("data").getAsJsonObject();
                switch (methodName) {
                    case ("quitGame"):
                        this.setPlayerOffline(data.get("playerID").getAsString());
                        quit = true;
                        sendResponse(true,true);
                        break;
                    case ("joinLobby"):
                        this.clientID = data.get("playerID").getAsString();
                        quit = false;
                        cntOn = true;
                        sendResponse(true,true);
                        System.out.println(TextColor.LIGHTBLUE.get() + "client: " + this.clientID + " join the lobby (SOCKET)" + TextColor.DEFAULT.get());
                        break;
                    case ("pingPong"):
                        sendMSG(prepareMSG(new JsonObject(), "pingResponse"));
                        break;
                    case ("pingResponse"):
                        this.pingResponse();
                        break;
                    default:
                        Map<Boolean, Method> mapG = this.getMethod(methodName);
                        if (mapG.containsKey(false)){
                            sendResponse(false,false);
                            return;
                        }
                        Map<Boolean, Boolean> mapC = callMethod(mapG.get(true), data);
                        if (mapC.containsKey(false)) {
                            sendResponse(false, false);
                        }
                        else {
                            sendResponse(true, mapC.get(true));
                        }
                        break;
                }
            }
            in.close();
            out.close();
            socket.close();
        }

        private Map<Boolean, Method> getMethod(String methodName){
            Map<Boolean, Method> map = new HashMap<>();
            Method getNameMethod = null;
            try {
                getNameMethod = SOCKET.this.getClass().getMethod(methodName, JsonObject.class, MultiClientSocketGame.class);
            } catch (NoSuchMethodException e){
                map.put(false, getNameMethod);
            }
            map.put(true, getNameMethod);
            return map;
        }
        private Map<Boolean, Boolean>  callMethod(Method getNameMethod, JsonObject data){
            Map<Boolean, Boolean> map = new HashMap<>();
            Boolean booleanResponse = false;
            if(getNameMethod != null) {
                try {
                    booleanResponse = (boolean) getNameMethod.invoke(SOCKET.this, data, this);
                    map.put(true, booleanResponse);
                } catch (IllegalAccessException | InvocationTargetException e) {
                    map.put(false, booleanResponse);
                }
            }
            return map;
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
