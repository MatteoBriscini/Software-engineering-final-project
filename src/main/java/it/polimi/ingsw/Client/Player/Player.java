package it.polimi.ingsw.Client.Player;

public abstract class Player {
    String playerID;
    String pwd;

    public void setPlayerID(String playerID) {
        this.playerID = playerID;
    }

    public String getPlayerID() {
        return playerID;
    }
}
