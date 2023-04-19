package it.polimi.ingsw.Server.Connection;

import com.google.gson.Gson;
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
    public ControllerSOCKET(Controller controller, int port){
        super(controller, port);
        this.connection();
    }

    public void notifyActivePlayer(int activePlayerID){

    }

    synchronized public void connection(){

        ExecutorService executor = Executors.newCachedThreadPool();
        ServerSocket serverSocket;

        try {
            serverSocket = new ServerSocket(PORT);  //throw exception if unavailable port
        } catch (IOException e) {
            System.out.println(e.toString());           //da finire*********************************
            return;
        }
        System.err.println("\u001B[32m" + "Server (socket) for newGame ready on port: " + PORT + "\u001B[0m");

        while (true){
            try {
                Socket socket = serverSocket.accept();
                executor.submit(new MultiClientSocketGame(socket));
            } catch (IOException e) {

                System.out.println("test");
                System.out.println(e.toString()); //go here if socket is closed
                break;//da finire*********************************
            }
        }
        executor.shutdown();
    }

    /*************************************************************************
     ************************************************** OUT method ***********
     * ***********************************************************************
     */
    @Override
    public void notifyActivePlayer(String activePlayerID) {

    }

    @Override
    public void sendPlayerList(String[] players) {

    }

    @Override
    public void sendPlayersNUmber(int playersNumber) {

    }

    @Override
    public void sendMainBoard(Card[][] mainBoard) {

    }

    @Override
    public void addCardToClientBoard(String playerID, int column, Card[] cards) {

    }

    @Override
    public void dellCardFromMainBoard(PositionWithColor[] cards) {

    }

    @Override
    public void sendAllPlayerBoard(ArrayList<Card[][]> playerBoards) {

    }

    @Override
    public void sendAllCommonGoal(int[] commonGoalID) {

    }

    @Override
    public void sendPrivateGoal(PositionWithColor[] cards, String playerID) {

    }

    @Override
    public void sendEndGamePoint(JsonObject points) {

    }

    @Override
    public void sendWinner(JsonObject winner) {

    }

    @Override
    public void sendLastCommonScored(JsonObject scored) {

    }

    @Override
    public void sendError(JsonObject error, String playerID){

    }

    @Override
    public void forceClientDisconnection() {

    }
    /*************************************************************************
     ************************************************** IN method ************
     * ***********************************************************************
     */
    public boolean startGame(JsonObject data){
        this.startGame(data.get("playerID").getAsString());
        return true;
    }

    /***********************************************************************************
     ************************************************** MultiClientSocketGame **********
     * *********************************************************************************
     */

    private class MultiClientSocketGame implements Runnable{
        private final Socket socket;

        public MultiClientSocketGame(Socket socket){
            this.socket = socket;
        }

        public void run(){
            //go here when players connect to the server
            try {
                Boolean bool;
                JsonObject response = new JsonObject();
                response.addProperty("service", "receiveResponse");
                JsonObject sendData = new JsonObject();

                Scanner in = new Scanner(socket.getInputStream());
                PrintWriter out = new PrintWriter(socket.getOutputStream());
                while (true){
                    String line = in.nextLine();
                    if (line.equals("quit")) {
                        bool = true; //TODO friendly quit
                        out.flush();
                        break;
                    } else {
                        bool = true;


                        JsonObject jsonObject = new Gson().fromJson(line, JsonObject.class);
                        String methodName = jsonObject.get("service").getAsString();
                        Method getNameMethod = null;
                        try {
                            getNameMethod = ControllerSOCKET.this.getClass().getMethod(methodName, JsonObject.class);
                        } catch (NoSuchMethodException e) {
                            bool = false;
                        }
                        JsonObject data = jsonObject.get("data").getAsJsonObject();
                        if(getNameMethod != null) {
                            try {
                                boolean name = (boolean) getNameMethod.invoke(ControllerSOCKET.this, data);
                            } catch (IllegalAccessException | InvocationTargetException e) {
                                bool = false;
                                System.out.println(bool);
                            }
                        }
                        sendData.addProperty("response", bool);
                        response.add("data", sendData);
                        out.println(response);
                        out.flush();
                    }
                }
                in.close();
                out.close();
                socket.close();
            } catch (IOException e) {
                System.err.println(e.getMessage());
            }
        }

    }
}
