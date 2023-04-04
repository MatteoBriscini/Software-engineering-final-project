package it.polimi.ingsw.Server.Lobby;

import com.google.gson.Gson;
import it.polimi.ingsw.Server.Controller;
import it.polimi.ingsw.Server.Connection.LobbyRMI;
import it.polimi.ingsw.Server.Exceptions.addPlayerToGameException;

import javax.security.auth.login.LoginException;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;

public class Lobby {

    //Attributes


    private LobbyRMI RMI = new LobbyRMI(1234);

    private ArrayList<Controller> activeGames = new ArrayList<>();

    private static String loginJSONURL;

    private ArrayList<String[]> playersInGames = new ArrayList<>();

    //Methods

    public synchronized void login(String ID, String pwd) throws LoginException {

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

            if(loginJSON[i].getPlayerID().equals(ID)){
                if(!loginJSON[i].getPassword().equals(pwd)){
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

        for(String[] players : games){

            for(int i = 0; i < players.length; i++){
                if(players[i].equals(ID)){
                    //connect to game with player already in
                }
            }

        }

    }

    public synchronized void signUp(PlayerLogin loginInfo){



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

