package it.polimi.ingsw.client.Connection;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import it.polimi.ingsw.client.Player.LobbyPlayer;
import it.polimi.ingsw.client.Player.Player;
import it.polimi.ingsw.client.Player.PlayingPlayer;
import it.polimi.ingsw.client.View.UserInterface;
import it.polimi.ingsw.shared.JsonSupportClasses.JsonUrl;
import it.polimi.ingsw.shared.PlayerMode;
import it.polimi.ingsw.shared.exceptions.addPlayerToGameException;
import it.polimi.ingsw.shared.Cards.Card;
import it.polimi.ingsw.shared.JsonSupportClasses.PositionWithColor;

import javax.security.auth.login.LoginException;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;

public abstract class ConnectionManager extends UnicastRemoteObject {
    protected Player player;
    protected String playerID;
    private boolean connected = false;

    protected boolean inGame = false;
    protected int pingPongTime;

    protected ConnectionManager() throws RemoteException {
        try {
            jsonCreate();
        } catch (FileNotFoundException e) {
            System.out.println("PlayingPlayerRMI: JSON FILE NOT FOUND");
            throw new RuntimeException(e);
        }
    }
    private void jsonCreate() throws FileNotFoundException {  //download json data
        InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream(JsonUrl.getUrl("netConfig"));
        if(inputStream == null) throw new FileNotFoundException();
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        JsonObject jsonObject = new Gson().fromJson(bufferedReader , JsonObject.class);
        this.pingPongTime = jsonObject.get("pingPongTime").getAsInt();
    }
    public boolean getConnected(){
        return connected;
    }
    public void setConnected(boolean connected){
        this.connected = connected;
    }
    public void setPlayer(Player player, String playerID){
        this.playerID = playerID;
        this.player = player;
    }
    public Player getPlayer(){
        return player;
    }
    public void setInGame(boolean inGame) {
        this.inGame = inGame;
    }
    public void setPlayerAsPlaying(){
        String playerID = player.getPlayerID();
        UserInterface ui = player.getUI();
        this.setInGame(true);
        String pwd = player.getPwd();
        try {
            this.player = new PlayingPlayer(playerID, pwd, this);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
        if(ui!=null){
            player.setUi(ui);
            player.setMode(PlayerMode.PLAYING);
        }
    }
    public void setPlayerAsLobby(){
        String playerID = player.getPlayerID();
        UserInterface ui = player.getUI();
        this.setInGame(false);
        String pwd = player.getPwd();
        this.player = new LobbyPlayer(playerID, pwd, this);
        if(ui!=null){
            player.setUi(ui);
            player.setMode(PlayerMode.LOBBY);
        }
    }
    /*************************************************************************
     ************************************************** OUT method ***********
     * ***********************************************************************
     */
    public abstract boolean takeCard(int column, PositionWithColor[] cards) throws Exception;
    public abstract boolean startGame(String playerID) throws Exception;
    public abstract boolean quitGame(String  playerID) throws Exception;
    public abstract void connection() throws Exception;
    public abstract void sendBroadcastMsg(String msg, String sender) throws Exception;
    public abstract void sendPrivateMSG(String userID, String msg, String sender) throws Exception;
    public abstract void login(String ID, String pwd) throws LoginException;
    public abstract void signUp(String ID, String pwd) throws LoginException;
    public abstract void joinGame(String ID) throws addPlayerToGameException;
    public abstract void joinGame(String ID, String searchID) throws addPlayerToGameException;
    public abstract void createGame(String ID) throws addPlayerToGameException;
    public abstract void createGame(String ID, int maxPlayerNumber) throws addPlayerToGameException;

    /************************************************************************
     ************************************************** IN method ***********
     * **********************************************************************
     * *
     * @param activePlayerID PLayerID of the player have to play now
     */
    public void notifyActivePlayer(String activePlayerID){
        ((PlayingPlayer)player).setActivePlayer(activePlayerID);
    }
    /**
     * @param playersID all players ID
     */
    public void receivePlayerList(String[] playersID){
        ((PlayingPlayer)player).setPlayersID(playersID);
    }
    /**
     * @param playersNumber total amount of players in the game
     */
    public void receivePlayersNumber(int playersNumber){
        ((PlayingPlayer)player).setPlayersNumber(playersNumber);
    }
    /**
     * @param mainBoard json array represent the main board
     */
    public void receiveMainBoard(String mainBoard){ //Card[][]
        Card[][] board = new Gson().fromJson(mainBoard, Card[][].class);
        ((PlayingPlayer)player).createMainBoard(board);
    }
    /**
     * @param playerBoards all players bord in the game (used in game start phase and when a player reconnects after a crash)
     */
    public void receiveAllPlayerBoard(String playerBoards){//Card[][]
        JsonArray jsonArray = new Gson().fromJson(playerBoards, JsonArray.class);

        ArrayList<Card[][]> boards = new ArrayList<>();
        for (int i = 0; i<jsonArray.size();i++){
            boards.add(new Gson().fromJson(jsonArray.get(0), Card[][].class));
        }
        ((PlayingPlayer)player).createAllClientBoard(boards);
    }
    /**
     * @param playerID name of the owner of the board we have to add cards
     * @param column on the board where we have to add cards
     * @param cards card array to add at the board
     */
    public void addCardToPlayerBoard(String playerID, int column,String cards){//Card[] (cards)
        Card[] cardArray = new Gson().fromJson(cards, Card[].class);
        ((PlayingPlayer)player).addCardToPlayerBoard(playerID, column, cardArray);
    }
    /**
     * @param cards position with color array, position have to remove from the mainBoard;
     */
    public void removeCardFromMainBoard(String cards){//PositionWithColor[]
        PositionWithColor[] position = new Gson().fromJson(cards, PositionWithColor[].class);
        ((PlayingPlayer)player).removeCardFromMainBoard(position);
    }
    /*
     * @param commonGoalID number unique identify the common goal
     */
    public void receiveAllCommonGoal(int[] commonGoalID){
        ((PlayingPlayer)player).setCommonGoalID(commonGoalID);
    }
    /**
     * @param cards PositionWithColor[] settings to recreate the image for private goal
     * @param playerID player's name for the private goal
     */
    public void receivePrivateGoal(String cards,String playerID){//PositionWithColor[] (cards)
        if(playerID.equals(((PlayingPlayer)player).getPlayerID())){
            PositionWithColor[] privateGoal = new Gson().fromJson(cards, PositionWithColor[].class);
            ((PlayingPlayer)player).setPrivateGoal(privateGoal);
        }

    }
    /**
     * receive points of all the players in the game
     * @param points map playerID + point for each game
     */
    public void endGameValue(String points){
        ((PlayingPlayer)player).endGameValue(points);
    }
    /**
     * @param winner json object with playerID and points of the winner
     */
    public void receiveWinner(String winner){
        ((PlayingPlayer)player).receiveWinner(winner);
    }
    /**
     * @param scored json object with point for each client
     */
    public void receiveLastCommonScored(String scored){
        JsonObject json= new Gson().fromJson(scored, JsonObject.class);
        ((PlayingPlayer)player).addCommonGoalScored(json);
    }
    /**
     * @param error json object with errorID && errorMSG
     * @param playerID name of the player the message is intended for
     */
    public void errorMSG(String error, String playerID){
        JsonObject json= new Gson().fromJson(error, JsonObject.class);

        if(playerID.equals(((PlayingPlayer)player).getPlayerID())){
            ((PlayingPlayer)player).errMsg(json);
        }

    }
    /**
     * the client is forced by the server to quit the game
     */
    public void forceDisconnection(){
        ((PlayingPlayer)player).disconnectError("the server has close the game for inactivity of the others players");
        this.setPlayerAsLobby();
    }

    /**************************************************************************
     ************************************************** chat ******************
     * ************************************************************************
     */

    public void receiveBroadcastMsg(String msg, String sender){
        ((PlayingPlayer)player).receiveBroadcastMsg(msg, sender);
    }
    public void receivePrivateMSG(String userID, String msg, String sender){
        ((PlayingPlayer)player).receivePrivateMSG(userID, msg, sender);
    }

}
