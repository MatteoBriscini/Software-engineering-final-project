package it.polimi.ingsw.client.Player;

import com.google.gson.JsonObject;
import it.polimi.ingsw.client.ClientMain;
import it.polimi.ingsw.client.Connection.ConnectionManager;
import it.polimi.ingsw.client.View.UserInterface;
import it.polimi.ingsw.shared.PlayerMode;

public abstract class Player {
    protected String playerID;
    protected String pwd;
    ConnectionManager connection;
    UserInterface ui;

    public Player(String playerID, String pwd, ConnectionManager connection){
        this.playerID = playerID;
        this.pwd = pwd;
        this.connection = connection;
        this.connection.setPlayer(this, this.playerID);

    }
    public void acceptingPlayingCommand(){
        if(ui!=null)ui.acceptingPlayingCommand();
    }
    public UserInterface getUI(){
        return ui;
    }
    public void setUi(UserInterface ui) {
        this.ui = ui;
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

    public void setMode(PlayerMode m){
        ui.setMode(m);
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
        if(ui != null) ui.printError(err.get("errorID").toString().toUpperCase() + ": " + err.get("errorMSG"));
        else System.err.println(err.get("errorID").toString().toUpperCase() + ": " + err.get("errorMSG"));
    }
}
