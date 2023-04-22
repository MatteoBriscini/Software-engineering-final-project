package it.polimi.ingsw.Client.Connection;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import it.polimi.ingsw.Client.Player.PlayingPlayer;
import it.polimi.ingsw.Shared.JsonSupportClasses.PositionWithColor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.Socket;
import java.rmi.RemoteException;

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
    private int timeout;
    private Thread receiveMsgThread;


    public PlayingPlayerSOCKET(int PORT, String serverIP, String playerID, PlayingPlayer playingPlayer) throws Exception {
        super(playingPlayer);

        this.playerID = playerID;

        this.PORT = PORT;
        this.serverIP = serverIP;
        echoSocket = new Socket(this.serverIP, this.PORT);
        out = new PrintWriter(echoSocket.getOutputStream(), true);
        in  = new BufferedReader(new InputStreamReader(echoSocket.getInputStream()));
        stdIn = new BufferedReader(new InputStreamReader(System.in));

        this.receiveMSG();
        this.connection(PORT, serverIP);
    }
    @Override
    public void connection(int PORT, String serverIP) throws IOException {
        JsonObject data = new JsonObject();
        data.addProperty("playerID", playerID);
        JsonObject msg = new JsonObject();
        msg.addProperty("service", "join");
        msg.add("data", data);
        this.sendMSG(msg);
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
            } catch (IOException e) {
                throw new RuntimeException(e); //TODO
            }
        });
        receiveMsgThread.start();
    }
    public void receiveResponse(JsonObject data){
        if (!data.get("existingMethod").getAsBoolean()) throw new RuntimeException("can't find method on server");
        synchronized (this){
            this.validResponse= true;
            this.response = data.get("response").getAsBoolean();
            this.notify();
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
            throw new RuntimeException(e);//TODO
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
        synchronized (this) {
            try {
                    this.wait(timeout * 1000L);
            } catch (InterruptedException e) {

                    throw new RuntimeException(e);
            }
            if(!validResponse) System.out.println("server can't response");//TODO server can't response
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


}
