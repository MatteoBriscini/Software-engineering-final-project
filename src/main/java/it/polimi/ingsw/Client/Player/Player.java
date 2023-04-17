package it.polimi.ingsw.Client.Player;

import it.polimi.ingsw.Client.ClientMain;

public abstract class Player {
    protected String playerID;
    protected String pwd;
    protected ClientMain clientMain;

    public Player(String playerID, String pwd, ClientMain clientMain){
        this.playerID = playerID;
        this.pwd = pwd;
    }

    public String getPlayerID() {
        return playerID;
    }

    public String getPwd(){
        return pwd;
    }
}
