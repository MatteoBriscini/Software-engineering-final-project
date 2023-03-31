package it.polimi.ingsw.Lobby;

import it.polimi.ingsw.Controller;

import java.util.ArrayList;

public class Lobby {

    //Attributes

    private ArrayList<Controller> activeGames = new ArrayList<>();

    private String loginJSONURL;

    private ArrayList<String[]> playersInGames = new ArrayList<>();

    //Methods

    public synchronized void login(PlayerLogin loginInfo){

        ArrayList<String[]> games;

        synchronized (playersInGames){

            games = playersInGames;

        }

        for(String[] players : games){

            for(int i = 0; i < 4; i++){
                if(players[i].equals(loginInfo.getPlayerID())){

                }
            }

        }

    }

    public synchronized void registration(){

    }

    public void joinGame(){

    }


}

