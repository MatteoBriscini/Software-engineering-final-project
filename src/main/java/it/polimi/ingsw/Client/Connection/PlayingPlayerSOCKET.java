package it.polimi.ingsw.Client.Connection;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import it.polimi.ingsw.Client.Player.PlayingPlayer;
import it.polimi.ingsw.Shared.JsonSupportClasses.JsonUrl;
import it.polimi.ingsw.Shared.JsonSupportClasses.PositionWithColor;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.Socket;

public class PlayingPlayerSOCKET extends PlayingPlayerConnectionManager{
    private final String playerID;
    int PORT;
    private final String serverIP;
    private final Socket echoSocket;
    private final PrintWriter out;
    private final BufferedReader in;
    private final BufferedReader stdIn;
    private boolean validResponse;
    private boolean response;
    private JsonUrl jsonUrl;
    private int timeout;
    private Thread receiveMsgThread;
    private int pingPongTime;
    private Boolean pingPongResponse = true;
    Thread pingPongThread;

    public PlayingPlayerSOCKET(int PORT, String serverIP, String playerID, PlayingPlayer playingPlayer) throws Exception {
        super(playingPlayer);

        this.playerID = playerID;

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
        this.connection(PORT, serverIP);
    }

    private void jsonCreate() throws FileNotFoundException {  //download json data
        InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream(jsonUrl.getUrl("netConfig"));
        if(inputStream == null) throw new FileNotFoundException();
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        JsonObject jsonObject = new Gson().fromJson(bufferedReader , JsonObject.class);
        this.pingPongTime = jsonObject.get("pingPongTime").getAsInt();
        this.timeout = jsonObject.get("socketTimeout").getAsInt();
    }
    @Override
    public void connection(int PORT, String serverIP) throws IOException {
        JsonObject data = new JsonObject();
        data.addProperty("playerID", playerID);
        JsonObject msg = new JsonObject();
        msg.addProperty("service", "join");
        msg.add("data", data);
        this.sendMSG(msg);

        pingPongThread = new Thread(this::pingPong);       //start ping pong
        pingPongThread.start();
    }
    public void pingResponse(JsonObject data){
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
        out.println(msg.toString());  //send socket message
        synchronized (pingPongThread) {
            try {
                pingPongThread.wait(timeout);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            if(!pingPongResponse){
                playingPlayer.disconnectError("server can't respond");
                return;
            }
        }
        pingPongResponse = false;
        this.pingPong();
    }
    /*************************************************************************
     *                           IN method
     * ***********************************************************************
     */
    private void receiveMSG() throws IOException {
        receiveMsgThread = new Thread(() ->{
            String serverMsg;
            try {
                while ((serverMsg = in.readLine()) != null){//receive socket message

                    JsonObject jsonObject = new Gson().fromJson(serverMsg, JsonObject.class);
                    String methodName = jsonObject.get("service").getAsString();
                    if(methodName.equals("pingPong")){
                        JsonObject data = new JsonObject();

                        JsonObject msg = new JsonObject();
                        msg.addProperty("service", "pingResponse");
                        msg.add("data", data);

                        out.println(msg.toString());  //send socket message
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
                throw new RuntimeException(e); //TODO
            }
        });
        receiveMsgThread.start();
    }
    public void receiveResponse(JsonObject data){
        if (!data.get("existingMethod").getAsBoolean()) throw new RuntimeException("can't find method on server");
        synchronized (echoSocket){
            this.validResponse= true;
            this.response = data.get("response").getAsBoolean();
            echoSocket.notifyAll();
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
    public void forceDisconnection(JsonObject data){
        try {
            this.quitGame(this.playerID);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        playingPlayer.disconnectError("disconnection forced by the server");
    }
    /*************************************************************************
     *                          OUT method
     * ***********************************************************************
     */


    private boolean sendMSG (JsonObject msg) throws IOException {
        synchronized (this){
            this.validResponse=false;
        }
        out.println(msg.toString());  //send socket message
        synchronized (echoSocket) {
            try {
                echoSocket.wait(timeout);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            if(!validResponse) playingPlayer.disconnectError("server can't respond");
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

        JsonObject msg = new JsonObject();
        msg.addProperty("service", "takeCard");
        msg.add("data", data);

        boolean bool = this.sendMSG(msg);
        return bool;
    }
    @Override
    public boolean startGame(String playerID) throws IOException {
        JsonObject data = new JsonObject();
        data.addProperty("playerID", playerID);

        JsonObject msg = new JsonObject();
        msg.addProperty("service", "startGame");
        msg.add("data", data);

        boolean bool = this.sendMSG(msg);
        return bool;
    }
    @Override
    public boolean quitGame(String playerID) throws IOException {
        JsonObject data = new JsonObject();
        data.addProperty("playerID", playerID);
        JsonObject msg = new JsonObject();
        msg.addProperty("service", "quit");
        msg.add("data", data);

        //TODO make player to lobby player

        boolean bool = this.sendMSG(msg);
        in.close();
        out.close();

        return bool;
    }
    /**************************************************************************
     ************************************************** chat ******************
     * ************************************************************************
     */
    public void sendBroadcastMsg(String msg, String sender){}
    public void sendPrivateMSG(String userID, String msg, String sender){}

}
