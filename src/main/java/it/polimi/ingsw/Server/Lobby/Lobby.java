package it.polimi.ingsw.Server.Lobby;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.stream.JsonWriter;
import it.polimi.ingsw.Server.Controller;
import it.polimi.ingsw.Server.Connection.LobbyRMI;
import it.polimi.ingsw.Server.Exceptions.ConnectionControllerManagerException;
import it.polimi.ingsw.Server.Exceptions.addPlayerToGameException;
import it.polimi.ingsw.Shared.Connection.ConnectionType;
import it.polimi.ingsw.Shared.JsonSupportClasses.JsonUrl;

import javax.security.auth.login.LoginException;
import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Lobby {

    //Attributes


    private final LobbyRMI lobbyRMI;
    private int standardPORT;

    private int minPORT;
    private int maxPORT;


    private ArrayList<Controller> activeGames = new ArrayList<>();

    private static String loginJSONURL = "src/main/json/config/registeredPlayers.json";

    private JsonUrl jsonConfigUrl;

    private ArrayList<String[]> playersInGames = new ArrayList<>();

    private ArrayList<Integer> allocatedPORT;

    private ExecutorService executor;


    public Lobby(){
        try {
            jsonCreate();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        allocatedPORT = new ArrayList<>();
        this.lobbyRMI = new LobbyRMI(standardPORT, this);
    }
    public Lobby(int PORT){
        try {
            jsonCreate();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        allocatedPORT = new ArrayList<>();
        this.lobbyRMI = new LobbyRMI(PORT, this);
    }

    //Methods

    public synchronized int login(String ID, String pwd) throws LoginException {

        ArrayList<String[]> games;
        ArrayList<Controller> activeG;
        ArrayList<Integer> PORT;
        boolean f = false;

        String path = loginJSONURL;      //file path
        FileReader fileJson = null;      //file executable
        try {
            fileJson = new FileReader(path);
        } catch (FileNotFoundException e) {
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

        // create client


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
                    synchronized (allocatedPORT){

                        PORT = allocatedPORT;

                    }

                    if(activeG.get(j).isPlayerOffline(ID)){
                        return PORT.get(j);
                    }

                }
            }

        }

        return -1;
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

    public synchronized int joinGame(String ID, ConnectionType connectionType) throws addPlayerToGameException {

        ArrayList<String[]> tempPlayersInGames;
        ArrayList<Controller> tempActiveGames;
        int j = 0;


        synchronized (playersInGames){

            tempPlayersInGames = playersInGames;

        }

        synchronized (activeGames){

            tempActiveGames = activeGames;

        }

        while(j < tempActiveGames.size()){

            if(tempActiveGames.get(j).getMaxPlayerNumber() > tempPlayersInGames.get(j).length && tempActiveGames.get(j).getCurrentPlayer() == -1){

                return addPlayerToGame(ID, connectionType, j);

            }
            j++;
        }

        return -1;
    }

    public synchronized int joinGame(String ID, ConnectionType connectionType, String searchID) throws addPlayerToGameException {

        ArrayList<String[]> tempPlayersInGames;
        ArrayList<Controller> tempActiveGames;

        int j = 0;

        synchronized (playersInGames){

            tempPlayersInGames = playersInGames;

        }

        synchronized (activeGames){

            tempActiveGames = activeGames;

        }


        while(tempActiveGames.get(j) != null){

            for(String s : tempPlayersInGames.get(j)){

                if(s.equals(searchID)){

                    return addPlayerToGame(ID, connectionType, j);

                }
            }
            j++;
        }
        throw new addPlayerToGameException("ID not found");

    }

    public synchronized int addPlayerToGame(String ID, ConnectionType connectionType, int gameNumber) throws addPlayerToGameException {
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
        for(i = minPORT; i < maxPORT; i++){
            if(allocatedPORT == null || !allocatedPORT.contains(i)) break;
        }
        if(i == maxPORT){throw new addPlayerToGameException("All ports are full");}

        Collections.addAll(temp, tempPlayersInGames.get(j));
        temp.add(ID);
        playersInGames.set(j, temp.toArray(new String[0]));

        try {
            return tempActiveGames.get(j).addClient(i, connectionType);
        } catch (ConnectionControllerManagerException e) {
            throw new RuntimeException(e);
        }

    }

    public synchronized int createGame(String ID, ConnectionType connectionType) throws addPlayerToGameException {
        Controller controller = new Controller();
        return createGameSupport(ID, connectionType, controller);
    }

    public synchronized int createGame(String ID, ConnectionType connectionType, int maxPlayerNumber) throws addPlayerToGameException {
        Controller controller = new Controller(maxPlayerNumber);
        return createGameSupport(ID, connectionType, controller);
    }

    private synchronized int createGameSupport(String ID, ConnectionType connectionType, Controller controller) throws addPlayerToGameException {

        int i = 0;
        int port = -1;
        ArrayList<String> temp = new ArrayList<>();

        temp.add(ID);


        activeGames.add(controller);
        executor = Executors.newCachedThreadPool();

        executor.submit(controller);

        for(i = minPORT; i < maxPORT; i++){
            if(allocatedPORT == null || !allocatedPORT.contains(i)) break;
        }
        if(i == maxPORT){throw new addPlayerToGameException("All ports are full");}

        assert allocatedPORT != null;
        allocatedPORT.add(i);

        try {
            port = controller.addClient(i, connectionType);
        } catch (ConnectionControllerManagerException e) {
            throw new RuntimeException(e);
        }

        controller.addNewPlayer(ID);

        playersInGames.add(temp.toArray(new String[0]));

        return port;

    }

    private void jsonCreate() throws FileNotFoundException {  //download json data
        InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream(jsonConfigUrl.getUrl("netConfig"));
        if(inputStream == null) throw new FileNotFoundException();
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        JsonObject jsonObject = new Gson().fromJson(bufferedReader , JsonObject.class);
        this.maxPORT = jsonObject.get("maxUsablePort").getAsInt();
        int portRMI = jsonObject.get("defRmiPort").getAsInt();
        int portSOCKET = jsonObject.get("defSocketPort").getAsInt();
        if(portRMI > portSOCKET){
            this.minPORT = portRMI + 1;
        }else{
            this.minPORT = portSOCKET + 1;
        }
        this.standardPORT = portRMI;
    }


    public ArrayList<Controller> getActiveGames() {
        return activeGames;
    }

    public ArrayList<String[]> getPlayersInGames() {
        return playersInGames;
    }

    public void closeAllGames(){
        executor.shutdown();
    }
}

