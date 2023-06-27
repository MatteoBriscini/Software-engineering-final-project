package it.polimi.ingsw.client.Player;

import com.google.gson.JsonObject;
import it.polimi.ingsw.client.Connection.ConnectionManager;
import it.polimi.ingsw.shared.exceptions.addPlayerToGameException;

import javax.security.auth.login.LoginException;

public class LobbyPlayer extends Player{
    /**
     * @param playerID id of the player
     * @param pwd password of the player
     * @param connection type of connection selected by the player
     */
    public LobbyPlayer(String playerID, String pwd, ConnectionManager connection){
        super(playerID, pwd, connection);
        this.connection.setInGame(false);
        try {
            if(!connection.getConnected()){
                connection.connection();
                connection.setConnected(true);
            }
        } catch (Exception e) {
            this.disconnectError("server is offline close the app");
            connection.setConnected(false);
        }
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


    /**
     * @param msg error message
     *            sends an error message in case of an error in the login
     */
    public void loginError(String msg){
        JsonObject err = new JsonObject();
        err.addProperty("errorID", "login error");
        err.addProperty("errorMSG", msg);
        this.errMsg(err);

    }

    /**
     * @param msg error message
     *            sends an error message in case of an error while adding the player to a game
     */
    public void addPlayerToGameError(String msg){
        JsonObject err = new JsonObject();
        err.addProperty("errorID", "game error");
        err.addProperty("errorMSG", msg);
        this.errMsg(err);
    }


    //Methods

    /**
     * @return true if the login is successful, false if and error occurred
     */
    public boolean login(){
        try {
            connection.login(playerID, pwd);
        } catch (LoginException e) {
            loginError(e.getMessage());
            return false;
        }
        return true;
    }

    /**
     * @return true if the signup is successful, false if and error occurred
     */
    public boolean signUp(){
        try {
            connection.signUp(playerID, pwd);
        } catch (LoginException e) {
            loginError(e.getMessage());
            return false;
        }
        return true;
    }

    /**
     * @return true if the join game is successful, false if and error occurred
     */
    public boolean joinGame(){
        try {
            connection.joinGame(playerID);
        }
        catch (addPlayerToGameException e) {
            connection.setPlayerAsLobby();
            addPlayerToGameError(e.getMessage());
            return false;
        }
        return true;

    }

    /**
     * @return true if the join game is successful, false if and error occurred
     */
    public boolean joinGame(String searchID){
        try {
            connection.joinGame(playerID, searchID);
        } catch (addPlayerToGameException e) {
            addPlayerToGameError(e.getMessage());
            connection.setPlayerAsLobby();
            return false;
        }
        return true;
    }

    /**
     * @return true if the create game is successful, false if and error occurred
     */
    public boolean createGame(){
        try {
            connection.createGame(playerID);
        } catch (addPlayerToGameException e) {
            connection.setPlayerAsLobby();
            addPlayerToGameError(e.getMessage());
        }
        //clientMain.setPlayerAsPlaying(this);
        return true;
    }

    /**
     * @return true if the create game is successful, false if and error occurred
     */
    public boolean createGame(int maxPlayerNumber){
        try {
            connection.createGame(playerID, maxPlayerNumber);
        } catch (addPlayerToGameException e) {
            connection.setPlayerAsLobby();
            addPlayerToGameError(e.getMessage());
            return false;
        }
        return true;
    }
}