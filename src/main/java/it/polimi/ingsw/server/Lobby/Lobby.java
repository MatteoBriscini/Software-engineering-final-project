package it.polimi.ingsw.server.Lobby;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.stream.JsonWriter;
import it.polimi.ingsw.server.Connection.SOCKET;
import it.polimi.ingsw.server.Controller;
import it.polimi.ingsw.server.Connection.RMI.RMI;
import it.polimi.ingsw.shared.exceptions.addPlayerToGameException;
import it.polimi.ingsw.shared.Connection.ConnectionType;
import it.polimi.ingsw.shared.JsonSupportClasses.JsonUrl;

import javax.security.auth.login.LoginException;
import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Lobby {

    //Attributes


    private final RMI rmi;
    private final SOCKET socket;
    private int standardPORTrmi;
    private int getStandardPORTsocket;


    private ArrayList<Controller> activeGames = new ArrayList<>();

    private static String loginJSONURL = "/home/matteo/Desktop/IDS/AM19/src/main/json/config/registeredPlayers.json";

    private JsonUrl jsonConfigUrl;

    private ArrayList<String[]> playersInGames = new ArrayList<>();

    private ExecutorService executor;


    public Lobby(String ID){
        try {
            jsonCreate();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        this.rmi = new RMI(standardPORTrmi, this, ID);
        this.socket = new SOCKET(this, getStandardPORTsocket);
        Thread thread = new Thread(socket::acceptConnection);
        thread.start();
    }

    public Lobby(int rmiPort, int socketPort){
        try {
            jsonCreate();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        this.rmi = new RMI(rmiPort, this, "127.0.0.1");
        this.socket = new SOCKET(this, socketPort);
        Thread thread = new Thread(socket::acceptConnection);
        thread.start();
    }
    private void jsonCreate() throws FileNotFoundException {  //download json data
        InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream(jsonConfigUrl.getUrl("netConfig"));
        if(inputStream == null) throw new FileNotFoundException();
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        JsonObject jsonObject = new Gson().fromJson(bufferedReader , JsonObject.class);
        int portRMI = jsonObject.get("defRmiPort").getAsInt();
        int portSOCKET = jsonObject.get("defSocketPort").getAsInt();
        this.standardPORTrmi = portRMI;
        this.getStandardPORTsocket = portSOCKET;
    }

    //Methods

    public synchronized String login(String ID, String pwd) throws LoginException {
        ArrayList<String[]> games;
        ArrayList<Controller> activeG;
        ArrayList<Integer> PORT;
        boolean f = false;

        String path = loginJSONURL;      //file path
        FileReader fileJson = null;      //file executable
        try {
            fileJson = new FileReader(path);
        } catch (FileNotFoundException e) {
            System.err.println(4);
            throw new RuntimeException(e);
        }

        Gson gson = new Gson();
        JsonArray loginJSON = gson.fromJson(fileJson, JsonArray.class);
        JsonObject temp;

        for(int i = 0; i < loginJSON.size() && !f; i++){

            temp = loginJSON.get(i).getAsJsonObject();

            if(temp.get("playerID").getAsString().equals(ID)){
                if(!temp.get("password").getAsString().equals(pwd)){
                    throw new LoginException("Wrong password");
                }else{
                    f = true;
                }
            }

        }

        if(!f){
            throw new LoginException("Wrong login credentials");
        }



        synchronized (playersInGames){

            games = playersInGames;

        }


        for(int j = 0; j < games.size(); j++){

            String[] players = games.get(j);

            for(int i = 0; i < players.length; i++){
                if(players[i].equals(ID)){

                    synchronized (activeGames){

                        activeG = activeGames;

                    }
                    if(activeG.get(j).isPlayerOffline(ID)){
                        return activeG.get(j).toString();
                    }

                }
            }

        }

        return "null";
    }

    public synchronized void signUp(String ID, String pwd) throws LoginException {

        String path = loginJSONURL;   //file path

        if(ID.charAt(0) < 65 || (ID.charAt(0) > 90 && ID.charAt(0) < 97) || ID.charAt(0) > 122){
            throw new LoginException("Illegal characters in ID");
        }

        FileReader fileJsonRead = null;      //file executable
        try {
            fileJsonRead = new FileReader(path);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        JsonArray original = new Gson().fromJson(fileJsonRead, JsonArray.class);

        for(int i = 0; i < original.size(); i++){
            if( original.get(i).getAsJsonObject().get("playerID").getAsString().equals(ID)){
                throw new LoginException("ID already taken");
            }
        }


        FileWriter fileJson = null;      //file executable
        try {
            fileJson = new FileWriter(path);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        Gson gson = new Gson();

        JsonWriter writer = new JsonWriter(fileJson);


        JsonObject update = new JsonObject();
        update.addProperty("playerID", ID);
        update.addProperty("password", pwd);

        original.add(update);

        gson.toJson(original, JsonObject.class, writer);

        try {
            writer.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


    }

    public synchronized String joinGame(String ID, ConnectionType connectionType) throws addPlayerToGameException {

        ArrayList<String[]> tempPlayersInGames;
        ArrayList<Controller> tempActiveGames;
        int j = 0;

        synchronized (activeGames){
            if(activeGames.size()==0) throw new addPlayerToGameException("ID not found");
            tempActiveGames = activeGames;
        }

        synchronized (playersInGames){
            tempPlayersInGames = playersInGames;
        }

        while(j < tempActiveGames.size()){
            if(tempActiveGames.get(j).getMaxPlayerNumber() > tempPlayersInGames.get(j).length && tempActiveGames.get(j).getCurrentPlayer() == -1){
                addPlayerToGame(ID, connectionType, j);
                return activeGames.get(j).toString();
            }
            j++;
        }
        throw new addPlayerToGameException("not free game at the moment");
    }

    public synchronized String joinGame(String ID, ConnectionType connectionType, String searchID) throws addPlayerToGameException {

        ArrayList<String[]> tempPlayersInGames;
        ArrayList<Controller> tempActiveGames;

        int j = 0;

        synchronized (activeGames){
            if(activeGames.size()==0) throw new addPlayerToGameException("ID not found");
            tempActiveGames = activeGames;
        }

        synchronized (playersInGames){
            tempPlayersInGames = playersInGames;
        }


        while(tempActiveGames.get(j) != null){
            for(String s : tempPlayersInGames.get(j)){
                if(s.equals(searchID)){
                    if(activeGames.get(j).getCurrentPlayer()==-1) {
                        addPlayerToGame(ID, connectionType, j);
                        return activeGames.get(j).toString();
                    }else {
                        throw new addPlayerToGameException("game just started");
                    }
                }
            }
            j++;
        }
        throw new addPlayerToGameException("ID not found");
    }

    public synchronized void addPlayerToGame(String ID, ConnectionType connectionType, int gameNumber) throws addPlayerToGameException {
        ArrayList<String[]> tempPlayersInGames;
        ArrayList<Controller> tempActiveGames;
        ArrayList<String> temp = new ArrayList<>();
        int j = gameNumber, i = 0;



        synchronized (playersInGames){
            tempPlayersInGames = playersInGames;
        }

        synchronized (activeGames){
            tempActiveGames = activeGames;
        }

        tempActiveGames.get(j).addNewPlayer(ID);

        Collections.addAll(temp, tempPlayersInGames.get(j));
        temp.add(ID);
        playersInGames.set(j, temp.toArray(new String[0]));

    }

    public synchronized String createGame(String ID) throws addPlayerToGameException {
        Controller controller = new Controller();
        createGameSupport(ID, controller);
        return controller.toString();
    }

    public synchronized String createGame(String ID, int maxPlayerNumber) throws addPlayerToGameException {
        Controller controller = new Controller(maxPlayerNumber);
        createGameSupport(ID, controller);
        return controller.toString();
    }

    private synchronized void createGameSupport(String ID, Controller controller) throws addPlayerToGameException {

        int i = 0;
        int port = -1;
        ArrayList<String> temp = new ArrayList<>();

        temp.add(ID);

        controller.turnOnCnt(socket, rmi);

        activeGames.add(controller);
        executor = Executors.newCachedThreadPool();

        executor.submit(controller);

        playersInGames.add(temp.toArray(new String[0]));
        controller.addNewPlayer(ID);

    }



    public synchronized ArrayList<Controller> getActiveGames() {
        return activeGames;
    }

    public ArrayList<String[]> getPlayersInGames() {
        return playersInGames;
    }

    public void closeAllGames(){
        executor.shutdown();
    }
}

