package it.polimi.ingsw.client.Player;

import com.google.gson.JsonObject;
import it.polimi.ingsw.client.Connection.ConnectionManager;
import it.polimi.ingsw.shared.exceptions.addPlayerToGameException;

import javax.security.auth.login.LoginException;

public class LobbyPlayer extends Player{


    private int PORT;


    public LobbyPlayer(String playerID, String pwd, ConnectionManager connection){
        super(playerID, pwd, connection);
        this.connection.setInGame(false);
    }

    //Set and get methods

    public void setID(String ID){
        this.playerID = ID;
    }

    public void setPwd(String pwd){
        this.pwd = pwd;
    }

    public String getPlayerID() {
        String playerID = this.playerID;
        return playerID;
    }

    public String getPwd() {
        String pwd = this.pwd;
        return pwd;
    }


    //Errors




    public void loginError(String msg){
        JsonObject err = new JsonObject();
        err.addProperty("errorID", "login error");
        err.addProperty("errorMSG", msg);
    }

    public void addPlayerToGameError(String msg){
        JsonObject err = new JsonObject();
        err.addProperty("errorID", "game error");
        err.addProperty("errorMSG", msg);
        this.errMsg(err);
    }


    //Methods

    public boolean login(){
        try {
            connection.login(playerID, pwd);
        } catch (LoginException e) {
            loginError(e.getMessage());
            return false;
        }
        if(this.PORT != -1){
            int PORT = this.PORT;
            //clientMain.setPlayerAsPlaying(this);
        }else{
            addPlayerToGameError("No port received");
        }
        return true;
    }

    public boolean signUp(){
        try {
            connection.signUp(playerID, pwd);
        } catch (LoginException e) {
            loginError(e.getMessage());
            return false;
        }
        return true;
    }

    public boolean joinGame(){
        try {
            connection.joinGame(playerID);
        }
        catch (addPlayerToGameException e) {
            addPlayerToGameError(e.getMessage());
            return false;
        }
        return true;

    }

    public boolean joinGame(String searchID){
        try {
            connection.joinGame(playerID, searchID);
        } catch (addPlayerToGameException e) {
            addPlayerToGameError(e.getMessage());
            return false;
        }
        return true;
    }

    public boolean createGame(){
        try {
            connection.createGame(playerID);
        } catch (addPlayerToGameException e) {
            addPlayerToGameError(e.getMessage());
        }
        //clientMain.setPlayerAsPlaying(this);
        return true;
    }

    public boolean createGame(int maxPlayerNumber){
        try {
            connection.createGame(playerID, maxPlayerNumber);
        } catch (addPlayerToGameException e) {
            addPlayerToGameError(e.getMessage());
            return false;
        }
        return true;
    }


}
