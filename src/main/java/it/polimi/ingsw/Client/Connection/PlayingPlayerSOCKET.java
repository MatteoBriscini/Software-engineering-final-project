package it.polimi.ingsw.Client.Connection;

import com.google.gson.Gson;
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

        //debug
        this.startGame("ciao");
    }

    /*************************************************************************
     ************************************************** OUT method ***********
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
                        System.out.println(e.toString());
                        //TODO
                    }
                    JsonObject data = jsonObject.get("data").getAsJsonObject();
                    if(getNameMethod != null) {
                        try {
                            getNameMethod.invoke(this, data);
                        } catch (IllegalAccessException | InvocationTargetException e) {
                            System.out.println(e.toString());
                            //TODO
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
        Boolean response = data.get("response").getAsBoolean();
        synchronized (this){
            this.validResponse=true;
            this.response = response;
            this.notify();
        }
    }
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
            if(!validResponse) System.out.println("server can't responde");//TODO server can't responde
        }
        return response;
    }
    @Override
    public boolean takeCard(int column, PositionWithColor[] cards) throws RemoteException {
        return false;
    }

    @Override
    public boolean startGame(String playerID) throws IOException {
        JsonObject data = new JsonObject();
        data.addProperty("playerID", playerID);

        JsonObject msg = new JsonObject();
        msg.addProperty("service", "startGame");
        msg.add("data", data);

        Boolean bool = this.sendMSG(msg);
        System.out.println(bool);
        return bool;
    }


    @Override
    public boolean quitGame(String playerID) throws RemoteException {
        return false;
    }
}
