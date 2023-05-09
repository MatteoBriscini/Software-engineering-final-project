package it.polimi.ingsw.client.Connection;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import it.polimi.ingsw.client.Player.PlayingPlayer;
import it.polimi.ingsw.shared.exceptions.addPlayerToGameException;
import it.polimi.ingsw.shared.JsonSupportClasses.JsonUrl;
import it.polimi.ingsw.shared.JsonSupportClasses.PositionWithColor;

import javax.security.auth.login.LoginException;
import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.Socket;

public class ClientSOCKET extends ConnectionManager {
    int PORT;
    private final String serverIP;
    private final Socket echoSocket;
    private final PrintWriter out;
    private final BufferedReader in;
    private final BufferedReader stdIn;
    private boolean validResponse;
    private boolean response;
    private int timeout;
    private Thread receiveMsgThread;
    private int pingPongTime;
    private Boolean pingPongResponse = true;
    private boolean quit = false;
    Thread pingPongThread;

    public ClientSOCKET(int PORT, String serverIP) throws Exception {
        super();

        try {
            jsonCreate();
        } catch (FileNotFoundException e) {
            System.out.println("PlayingPlayerSOCKET: JSON FILE NOT FOUND");
            throw new RuntimeException(e);
        }

        this.PORT = PORT;
        this.serverIP = serverIP;
        echoSocket = new Socket(this.serverIP, this.PORT);
        out = new PrintWriter(echoSocket.getOutputStream(), true);
        in  = new BufferedReader(new InputStreamReader(echoSocket.getInputStream()));
        stdIn = new BufferedReader(new InputStreamReader(System.in));

        this.receiveMSG();
    }

    private void jsonCreate() throws FileNotFoundException {  //download json data
        InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream(JsonUrl.getUrl("netConfig"));
        if(inputStream == null) throw new FileNotFoundException();
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        JsonObject jsonObject = new Gson().fromJson(bufferedReader , JsonObject.class);
        this.pingPongTime = jsonObject.get("pingPongTime").getAsInt();
        this.timeout = jsonObject.get("socketTimeout").getAsInt();
    }
    @Override
    public void connection() throws IOException {
        JsonObject data = new JsonObject();
        data.addProperty("playerID", playerID);
        JsonObject msg = new JsonObject();
        msg.addProperty("service", "joinLobby");
        msg.add("data", data);this.sendMSG(msg);
    }
    public void pingResponse(JsonObject data){
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
        if(quit){
            return;
        }
        JsonObject data = new JsonObject();
        JsonObject msg = new JsonObject();
        msg.addProperty("service", "pingPong");
        msg.add("data", data);
        out.println(msg.toString());
        out.flush();
        synchronized (pingPongThread) {
            try {
                pingPongThread.wait(timeout);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            if(!pingPongResponse){
                ((PlayingPlayer)player).disconnectError("server can't respond");
                return;
            }
        }
        pingPongResponse = false;
        this.pingPong();
    }
    private JsonObject prepareMSG(JsonObject data, String methodName){
        JsonObject msg = new JsonObject();
        msg.addProperty("service", methodName);
        msg.add("data", data);
        return msg;
    }
    /*************************************************************************
     *                           IN method
     * ***********************************************************************
     */
    synchronized private void receiveMSG() throws IOException {
        receiveMsgThread = new Thread(() ->{
            String serverMsg;
            try {
                while ((serverMsg = in.readLine()) != null ){//receive socket message
                    JsonObject jsonObject = new Gson().fromJson(serverMsg, JsonObject.class);
                    String methodName = jsonObject.get("service").getAsString();
                    if(methodName.equals("pingPong")){
                        JsonObject data = new JsonObject();
                        out.println(prepareMSG(data, "pingResponse"));
                        out.flush();
                    }else {
                        Method getNameMethod = null;
                        try {
                            getNameMethod = this.getClass().getMethod(methodName, JsonObject.class);
                        } catch (NoSuchMethodException e) {
                            throw new RuntimeException("server try to call not existing client's method");
                        }
                        JsonObject data = jsonObject.get("data").getAsJsonObject();

                        if(getNameMethod != null) {
                            try {
                                getNameMethod.invoke(this, data);
                            } catch (IllegalAccessException | InvocationTargetException e) {
                                throw new RuntimeException("can't find method on server");
                            }
                        }
                    }
                }
            } catch (IOException e) {
                if(!quit) player.disconnectError("can't find server");
            }
        });
        receiveMsgThread.start();
    }
    public void receiveResponse(JsonObject data){
        if (!data.get("existingMethod").getAsBoolean()) throw new RuntimeException("can't find method on server");
        synchronized (echoSocket){
            this.validResponse= true;
            this.response = data.get("response").getAsBoolean();
            echoSocket.notify();
        }
    }

    public void notifyActivePlayer(JsonObject data){
        this.notifyActivePlayer(data.get("playerID").getAsString());
    }

    public void receivePlayerList(JsonObject data){
        JsonArray jsonArray = data.get("players").getAsJsonArray();
        String[] players = new  Gson().fromJson(jsonArray, String[].class);
        this.receivePlayerList(players);
    }
    public void receivePlayersNumber(JsonObject data){
        this.receivePlayersNumber(data.get("playersNumber").getAsInt());
    }
    public void receiveMainBoard(JsonObject data){
        this.receiveMainBoard(data.get("mainBoard").toString());
    }
    public void errorMSG(JsonObject data){
        this.errorMSG(data.get("error").toString(), data.get("playerID").getAsString());

    }
    public void receiveAllPlayerBoard(JsonObject data){
        this.receiveAllPlayerBoard(data.get("playerBoards").toString());
    }
    public void addCardToPlayerBoard(JsonObject data){
        this.addCardToPlayerBoard(data.get("playerID").toString(), data.get("column").getAsInt(), data.get("cards").toString());
    }
    public void removeCardFromMainBoard(JsonObject data){
        this.removeCardFromMainBoard(data.get("cards").toString());
    }
    public void receiveAllCommonGoal(JsonObject data){
        JsonArray jsonArray = data.get("commonGoalID").getAsJsonArray();
        int[] commonGoalID = new int[jsonArray.size()];
        for (int i=0; i<commonGoalID.length;i++){
            commonGoalID[i] = jsonArray.get(i).getAsInt();
        }
        this.receiveAllCommonGoal(commonGoalID);
    }
    public void receivePrivateGoal(JsonObject data){
        this.receivePrivateGoal(data.get("cards").toString(), data.get("playerID").getAsString());
    }
    public void endGameValue(JsonObject data){
        this.endGameValue(data.get("points").toString());
    }
    public void receiveWinner(JsonObject data){
        this.receiveWinner(data.get("winner").toString());
    }
    public void receiveLastCommonScored(JsonObject data){
        this.receiveLastCommonScored(data.get("scored").toString());
    }
    public void forceDisconnection(JsonObject data) throws IOException {
        validResponse = true;
        quit= true;
        ((PlayingPlayer)player).disconnectError("disconnection forced by the server " + playerID);
    }
    public void setPlayerAsPlaying(JsonObject data){
        pingPongThread = new Thread(this::pingPong);       //start ping pong
        pingPongThread.start();
        this.setPlayerAsPlaying();
    }
    /*************************************************************************
     *                          OUT lobby method
     * ***********************************************************************
     */

    public void login(String ID, String pwd) throws LoginException {
        JsonObject data = new JsonObject();
        data.addProperty("ID", ID);
        data.addProperty("pwd", pwd);
        boolean bool = this.sendMSG(prepareMSG(data, "login"));
        if(!bool)throw new LoginException("fail to login");
    }

    @Override
    public void signUp(String ID, String pwd) throws LoginException {
        JsonObject data = new JsonObject();
        data.addProperty("ID", ID);
        data.addProperty("pwd", pwd);
        boolean bool = this.sendMSG(prepareMSG(data, "signUp"));
        if(!bool)throw new LoginException("fail to sing up");
    }


    public void joinGame(String ID, String searchID)throws addPlayerToGameException{
        JsonObject data = new JsonObject();
        data.addProperty("ID", ID);
        data.addProperty("searchID" ,searchID);
        boolean bool = this.sendMSG(prepareMSG(data, "joinGame"));
        if(bool) this.setPlayerAsPlaying(data);
        else throw new addPlayerToGameException("fail to joint the game");
    }

    @Override
    public void createGame(String ID) throws addPlayerToGameException {
        JsonObject data = new JsonObject();
        data.addProperty("ID", ID);
        data.addProperty("maxPlayerNumber", 0);
        boolean bool = this.sendMSG(prepareMSG(data, "createGame"));
        if(bool) this.setPlayerAsPlaying(data);
        else throw new addPlayerToGameException("fail to create the game");
    }

    @Override
    public void createGame(String ID, int maxPlayerNumber) throws addPlayerToGameException {
        JsonObject data = new JsonObject();
        data.addProperty("ID", ID);
        data.addProperty("maxPlayerNumber", maxPlayerNumber);
        boolean bool = this.sendMSG(prepareMSG(data, "createGame"));
        if(bool) this.setPlayerAsPlaying(data);
        else throw new addPlayerToGameException("fail to create the game");
    }

    public void joinGame(String ID)throws addPlayerToGameException{
        JsonObject data = new JsonObject();
        data.addProperty("ID", ID);
        data.addProperty("searchID" , "null");
        boolean bool = this.sendMSG(prepareMSG(data, "joinGame"));
        if(bool) this.setPlayerAsPlaying(data);
        else throw new addPlayerToGameException("fail to joint the game");
    }
    /*************************************************************************
     *                          OUT method
     * ***********************************************************************
     */


    private boolean sendMSG (JsonObject msg) {
        synchronized (this){
            this.validResponse=false;
        }
        out.println(msg.toString());  //send socket message
        out.flush();
        synchronized (echoSocket) {
            try {
                echoSocket.wait(timeout);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            if(!validResponse){
                ((PlayingPlayer)player).disconnectError("server can't respond");
            }
        }
        return response;
    }
    @Override
    public boolean takeCard(int column, PositionWithColor[] cards) throws IOException {
        JsonObject data = new JsonObject();
        JsonArray cardsArray = new  Gson().toJsonTree(cards).getAsJsonArray();
        data.addProperty("column", column);
        data.add("cards", cardsArray);
        data.addProperty("playerID", playerID);
        boolean bool = this.sendMSG(prepareMSG(data, "takeCard"));
        return bool;
    }
    @Override
    public boolean startGame(String playerID) throws IOException {
        JsonObject data = new JsonObject();
        data.addProperty("playerID", playerID);
        boolean bool = this.sendMSG(prepareMSG(data, "startGame"));
        return bool;
    }
    @Override
    public boolean quitGame(String playerID) throws IOException {
        quit = true;
        JsonObject data = new JsonObject();
        data.addProperty("playerID", playerID);
        boolean bool = this.sendMSG(prepareMSG(data, "quitGame"));
        this.setPlayerAsLobby();
        return bool;
    }
    /**************************************************************************
     ************************************************** chat ******************
     * ************************************************************************
     */
    public void sendBroadcastMsg(String MSG, String sender) throws IOException {
        JsonObject data = new JsonObject();
        data.addProperty("msg", MSG);
        data.addProperty("sender", sender);
        this.sendMSG(prepareMSG(data, "receiveBroadcastMsg"));
    }
    public void sendPrivateMSG(String userID, String MSG, String sender){
        JsonObject data = new JsonObject();
        data.addProperty("userID", userID);
        data.addProperty("msg", MSG);
        data.addProperty("sender", sender);
        this.sendMSG(prepareMSG(data, "receivePrivateMSG"));
    }



    public void receiveBroadcastMsg(JsonObject data){
        this.receiveBroadcastMsg(data.get("msg").getAsString(), data.get("sender").getAsString());
    }
    public void receivePrivateMSG(JsonObject data){
        this.receivePrivateMSG(data.get("userID").getAsString(), data.get("msg").getAsString(), data.get("sender").getAsString());
    }

}
