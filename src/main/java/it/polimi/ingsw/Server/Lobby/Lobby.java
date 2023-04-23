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

import javax.security.auth.login.LoginException;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Lobby {

    //Attributes


    private final LobbyRMI lobbyRMI;

    private ArrayList<Controller> activeGames = new ArrayList<>();

    private static String loginJSONURL = "src/main/json/config/registeredPlayers.json";

    private ArrayList<String[]> playersInGames = new ArrayList<>();

    private ArrayList<Integer> allocatedPORT;

    private ExecutorService executor;


    public Lobby(){

        System.out.println("test");
        allocatedPORT = new ArrayList<>();
        this.lobbyRMI = new LobbyRMI(9000, this);

    }
    public Lobby(int PORT){
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
            throw new LoginException("ID already taken");
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

    public int joinGame(String ID, ConnectionType connectionType){

        ArrayList<String[]> tempPlayersInGames;
        ArrayList<Controller> tempActiveGames;
        ArrayList<String> temp = new ArrayList<>();
        int j = 0, i = 0, port = -1;


        synchronized (playersInGames){

            tempPlayersInGames = playersInGames;

        }

        synchronized (activeGames){

            tempActiveGames = activeGames;

        }

        while(j < tempActiveGames.size()){

            if(tempActiveGames.get(j).getMaxPlayerNumber() > tempPlayersInGames.get(j).length && tempActiveGames.get(j).getCurrentPlayer() == -1){
                try {
                    tempActiveGames.get(j).addNewPlayer(ID); //TODO exception
                    for(i = 1236; i < 1256; i++){
                        if(allocatedPORT == null || !allocatedPORT.contains(i)) break;
                    }
                    if(i == 1256){} //TODO aggiungere eccezione massimo porte

                    Collections.addAll(temp, tempPlayersInGames.get(j));
                    temp.add(ID);
                    playersInGames.set(j, temp.toArray(new String[0]));

                    try {
                        return tempActiveGames.get(j).addClient(i, connectionType);
                    } catch (ConnectionControllerManagerException e) {
                        throw new RuntimeException(e);
                    }

                } catch (addPlayerToGameException e) {
                }
                break;
            }
            j++;
        }

        return port;
    }

    public int joinGame(String ID, ConnectionType connectionType, String searchID){

        ArrayList<String[]> tempPlayersInGames;
        ArrayList<Controller> tempActiveGames;
        ArrayList<String> temp = new ArrayList<>();

        int j = 0, i = 0;

        synchronized (playersInGames){

            tempPlayersInGames = playersInGames;

        }

        synchronized (activeGames){

            tempActiveGames = activeGames;

        }


        while(tempActiveGames.get(j) != null){

            for(String s : tempPlayersInGames.get(j)){
                //TODO ID not found exception
                if(s.equals(searchID)){

                    try {
                        tempActiveGames.get(j).addNewPlayer(ID); //TODO full game || already started exception
                        for(i = 1236; i < 1256; i++){
                            if(allocatedPORT == null || !allocatedPORT.contains(i)) break;
                        }
                        if(i == 1256){} //TODO aggiungere eccezione massimo porte

                        Collections.addAll(temp, tempPlayersInGames.get(j));
                        temp.add(ID);
                        playersInGames.set(j, temp.toArray(new String[0]));

                        try {
                            return tempActiveGames.get(j).addClient(i, connectionType);
                        } catch (ConnectionControllerManagerException e) {
                            throw new RuntimeException(e);
                        }


                    } catch (addPlayerToGameException e) {
                        throw new RuntimeException(e);
                    }


                }
            }
            j++;
        }

        return -1;
    }

    public int createGame(String ID, ConnectionType connectionType){
        Controller controller = new Controller();
        return createGameSupport(ID, connectionType, controller);
    }

    public int createGame(String ID, ConnectionType connectionType, int maxPlayerNumber){
        Controller controller = new Controller(maxPlayerNumber);
        return createGameSupport(ID, connectionType, controller);
    }

    private int createGameSupport(String ID, ConnectionType connectionType, Controller controller){

        int i = 0;
        int port = -1;
        ArrayList<String> temp = new ArrayList<>();

        temp.add(ID);


        activeGames.add(controller);
        executor = Executors.newCachedThreadPool();

        executor.submit(controller);

        for(i = 1236; i < 1256; i++){
            if(allocatedPORT == null || !allocatedPORT.contains(i)) break;
        }
        if(i == 1256){} //TODO aggiungere eccezione massimo porte

        allocatedPORT.add(i);

        try {
            port = controller.addClient(i, connectionType);
        } catch (ConnectionControllerManagerException e) {
            throw new RuntimeException(e);
        }

        try {
            controller.addNewPlayer(ID);
        } catch (addPlayerToGameException e) {
            throw new RuntimeException(e);
        }

        playersInGames.add(temp.toArray(new String[0]));

        return port;

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

