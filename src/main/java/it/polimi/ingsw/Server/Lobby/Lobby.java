package it.polimi.ingsw.Server.Lobby;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.stream.JsonWriter;
import it.polimi.ingsw.Server.Controller;
import it.polimi.ingsw.Server.Connection.LobbyRMI;
import it.polimi.ingsw.Server.Exceptions.addPlayerToGameException;

import javax.security.auth.login.LoginException;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class Lobby {

    //Attributes


    private LobbyRMI RMI = new LobbyRMI(1234, this);

    private ArrayList<Controller> activeGames = new ArrayList<>();

    private static String loginJSONURL = "src/main/json/config/registeredPlayers.json";

    private ArrayList<String[]> playersInGames = new ArrayList<>();

    private ArrayList<Integer> allocatedPORT;

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

    public synchronized void signUp(String ID, String pwd){

        String path = loginJSONURL;   //file path

        FileReader fileJsonRead = null;      //file executable
        try {
            fileJsonRead = new FileReader(path);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        JsonArray original = new Gson().fromJson(fileJsonRead, JsonArray.class);


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

    public void joinGame(String ID, String searchID){

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

            for(int i = 0; i < tempPlayersInGames.get(j).length; i++){
                if(tempPlayersInGames.get(j)[i].equals(searchID)){

                    if(tempPlayersInGames.get(j)[tempPlayersInGames.get(j).length].equals("null")){
                        //errore
                    }

                    try {
                        tempActiveGames.get(j).addNewPlayer(ID);
                    } catch (addPlayerToGameException e) {
                        throw new RuntimeException(e);
                    }

                }
            }

        }

    }


}

