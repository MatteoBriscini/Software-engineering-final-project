package it.polimi.ingsw.Lobby;

import com.google.gson.Gson;
import it.polimi.ingsw.Controller;
import it.polimi.ingsw.PlayerClasses.PlayerTarget;

import javax.security.auth.login.LoginException;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Objects;

public class Lobby {

    //Attributes

    private ArrayList<Controller> activeGames = new ArrayList<>();

    private static String loginJSONURL;

    private ArrayList<String[]> playersInGames = new ArrayList<>();

    //Methods

    public synchronized void login(PlayerLogin loginInfo) throws LoginException {

        ArrayList<String[]> games;
        boolean f = false;

        String path = loginJSONURL;   //file path
        FileReader fileJson = null;      //file executable
        try {
            fileJson = new FileReader(path);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

        Gson gson = new Gson();
        PlayerLogin[] loginJSON = gson.fromJson(fileJson, PlayerLogin[].class);       //Call constructor on PlayerTarget array by passing the json file attributes

        for(int i = 0; i < loginJSON.length || !f; i++){

            if(loginJSON[i].getPlayerID().equals(loginInfo.getPlayerID())){
                if(!loginJSON[i].getPassword().equals(loginInfo.getPassword())){
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

        for(String[] players : games){

            for(int i = 0; i < players.length; i++){
                if(players[i].equals(loginInfo.getPlayerID())){
                    //connect to game with player already in
                }
            }

        }

    }

    public synchronized void registration(PlayerLogin loginInfo){



    }

    public void joinGame(){

    }


}

