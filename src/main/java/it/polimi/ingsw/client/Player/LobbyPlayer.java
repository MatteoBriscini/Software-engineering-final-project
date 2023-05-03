package it.polimi.ingsw.client.Player;

import com.google.gson.JsonObject;
import it.polimi.ingsw.client.ClientMain;
import it.polimi.ingsw.client.Connection.LobbyPlayerRMI;
import it.polimi.ingsw.server.Exceptions.addPlayerToGameException;
import it.polimi.ingsw.shared.Connection.ConnectionType;

import javax.security.auth.login.LoginException;

public class LobbyPlayer extends Player{

    private LobbyPlayerRMI lobbyPlayerRMI;
    private int PORT;


    public LobbyPlayer(String playerID, String pwd, ClientMain clientMain, ConnectionType connectionType, int PORT, String serverIP){
        super(playerID, pwd, clientMain);
        switch (connectionType){
            case RMI:

                try {
                    lobbyPlayerRMI = new LobbyPlayerRMI(PORT, serverIP);
                } catch (Exception e) {
                    this.disconnectError("Invalid connection config");
                }

                break;
            case SOCKET:
                break;
            case DEBUG:
                return;
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


    public void disconnectError(String msg){
        JsonObject err = new JsonObject();
        err.addProperty("errorID", "connection error");
        err.addProperty("errorMSG", msg);
        this.errMsg(err);
    }

    public void loginError(String msg){
        JsonObject err = new JsonObject();
        err.addProperty("errorID", "login error");
        err.addProperty("errorMSG", msg);
    }

    public void addPlayerToGameError(String msg){
        JsonObject err = new JsonObject();
        err.addProperty("errorID", "game error");
        err.addProperty("errorMSG", msg);
    }


    //Methods

    public void login(){
        try {
            this.PORT = lobbyPlayerRMI.login(playerID, pwd);
        } catch (LoginException e) {
            loginError(e.getMessage());
        }
        if(this.PORT != -1){
            int PORT = this.PORT;
            clientMain.setPlayerAsPlaying(PORT);
        }else{
            addPlayerToGameError("No port received");
        }
    }

    public void signUp(){
        try {
            lobbyPlayerRMI.signUp(playerID, pwd);
        } catch (LoginException e) {
            loginError(e.getMessage());
        }

    }

    public void joinGame(){
        try {
            this.PORT = lobbyPlayerRMI.joinGame(playerID);
        } catch (addPlayerToGameException e) {
            addPlayerToGameError(e.getMessage());
        }
        if(this.PORT != -1){
            int PORT = this.PORT;
            clientMain.setPlayerAsPlaying(PORT);
        }else{
            addPlayerToGameError("No port received");
        }
    }

    public void joinGame(String searchID){
        try {
            this.PORT = lobbyPlayerRMI.joinGame(playerID, searchID);
        } catch (addPlayerToGameException e) {
            addPlayerToGameError(e.getMessage());
        }
        if(this.PORT != -1){
            int PORT = this.PORT;
            clientMain.setPlayerAsPlaying(PORT);
        }else{
            addPlayerToGameError("No port received");
        }
    }

    public void createGame(){
        try {
            this.PORT = lobbyPlayerRMI.createGame(playerID);
        } catch (addPlayerToGameException e) {
            addPlayerToGameError(e.getMessage());
        }
        if(this.PORT != -1){
            int PORT = this.PORT;
            clientMain.setPlayerAsPlaying(PORT);
        }else{
            addPlayerToGameError("No port received");
        }
    }

    public void createGame(int maxPlayerNumber){
        try {
            this.PORT = lobbyPlayerRMI.createGame(playerID, maxPlayerNumber);
        } catch (addPlayerToGameException e) {
            addPlayerToGameError(e.getMessage());
        }
        if(this.PORT != -1){
            int PORT = this.PORT;
            clientMain.setPlayerAsPlaying(PORT);
        }else{
            addPlayerToGameError("No port received");
        }
    }

}
