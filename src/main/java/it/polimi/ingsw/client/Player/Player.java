package it.polimi.ingsw.client.Player;

import com.google.gson.JsonObject;
import it.polimi.ingsw.client.ClientMain;
import it.polimi.ingsw.client.Connection.ConnectionManager;

public abstract class Player {
    protected String playerID;
    protected String pwd;
    ConnectionManager connection;

    public Player(String playerID, String pwd, ConnectionManager connection){
        this.playerID = playerID;
        this.pwd = pwd;
        this.connection = connection;
        this.connection.setPlayer(this, this.playerID);

    }

    /**
     * in test connection error don't have to kill process
     */
    public String getPlayerID() {
        return playerID;
    }
    public String getPwd(){
        return pwd;
    }

    public ConnectionManager getConnection() {
        return connection;
    }
    public void disconnectError(String msg){
        JsonObject err = new JsonObject();
        err.addProperty("errorID", "connection error");
        err.addProperty("errorMSG", msg);
        this.errMsg(err);
    }
    public void errMsg(JsonObject err){
        System.err.println(err.get("errorID").toString().toUpperCase() + ": " + err.get("errorMSG")); //TODO necessita metodo lato grafico
    }
}