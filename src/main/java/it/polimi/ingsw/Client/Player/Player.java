package it.polimi.ingsw.Client.Player;

public abstract class Player {
    String playerID;
    String pwd;

    public Player(String playerID, String pwd){
        this.playerID = playerID;
        this.pwd = pwd;
    }

    public String getPlayerID() {
        return playerID;
    }
}
