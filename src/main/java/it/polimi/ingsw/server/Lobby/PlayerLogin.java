package it.polimi.ingsw.server.Lobby;

public class PlayerLogin {

    //Attributes

    private String playerID;

    private String password;


    public PlayerLogin(String ID, String pwd){
        this.playerID = ID;
        this.password = pwd;
    }


    //Method


    public String getPlayerID() {
        return playerID;
    }

    public String getPassword() {
        return password;
    }
}
/*
* JsonArray asdasdas = new Gson()
* JsonObject asddasd = asdasdada.get().getAsJsonObject
*
* asdad.get("parametro").getAsString()
*
*
* */