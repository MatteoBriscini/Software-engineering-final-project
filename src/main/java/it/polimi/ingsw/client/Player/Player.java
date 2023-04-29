package it.polimi.ingsw.client.Player;

import com.google.gson.JsonObject;
import it.polimi.ingsw.client.ClientMain;

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

    public void errMsg(JsonObject err){
        System.err.println(err.get("errorID").toString().toUpperCase() + ": " + err.get("errorMSG")); //TODO necessita metodo lato grafico
    }
}
