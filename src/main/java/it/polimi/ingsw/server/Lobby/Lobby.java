package it.polimi.ingsw.server.Lobby;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.stream.JsonWriter;
import it.polimi.ingsw.server.Connection.SOCKET;
import it.polimi.ingsw.server.Controller;
import it.polimi.ingsw.server.Connection.RMI.RMI;
import it.polimi.ingsw.shared.TextColor;
import it.polimi.ingsw.shared.exceptions.addPlayerToGameException;
import it.polimi.ingsw.shared.Connection.ConnectionType;
import it.polimi.ingsw.shared.JsonSupportClasses.JsonUrl;

import javax.security.auth.login.LoginException;
import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Lobby {

    //Attributes


    private final RMI rmi;
    private final SOCKET socket;
    private int standardPORTrmi;
    private int getStandardPORTsocket;


    private ArrayList<Controller> activeGames = new ArrayList<>();

    private static String loginJSONURL;
    private static String directoryJson;

    private JsonUrl jsonConfigUrl;

    private ArrayList<String[]> playersInGames = new ArrayList<>();


    private ExecutorService executor;



    public Lobby(String ID){
        try {
            jsonCreate();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        this.rmi = new RMI(standardPORTrmi, this, ID);
        this.socket = new SOCKET(this, getStandardPORTsocket);
        Thread thread = new Thread(socket::acceptConnection);
        thread.start();
    }

    /**
     * @param rmiPort rmi port for creating RMI class
     * @param socketPort socket port for creating SOCKET class
     */
    public Lobby(int rmiPort, int socketPort){
        try {
            jsonCreate();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        this.rmi = new RMI(rmiPort, this, "127.0.0.1");
        this.socket = new SOCKET(this, socketPort);
        Thread thread = new Thread(socket::acceptConnection);
        thread.start();
    }

    /**
     * @throws FileNotFoundException configuration file not found
     */
    private void jsonCreate() throws FileNotFoundException {  //download json data
        InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream(jsonConfigUrl.getUrl("netConfig"));
        if(inputStream == null) throw new FileNotFoundException();
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        JsonObject jsonObject = new Gson().fromJson(bufferedReader , JsonObject.class);
        int portRMI = jsonObject.get("defRmiPort").getAsInt();
        int portSOCKET = jsonObject.get("defSocketPort").getAsInt();
        String json = jsonObject.get("loginJsonUrl").getAsString();
        String directory = jsonObject.get("directory").getAsString();
        this.standardPORTrmi = portRMI;
        this.getStandardPORTsocket = portSOCKET;
        loginJSONURL = json;
        directoryJson = directory;
    }

    //Methods

    /**
     * @param ID player ID for logging into the game
     * @param pwd player password for logging into the game
     * @return is either null or a string indicating the controller handling a specific game, in this case the game the player is already in, in case of disconnection
     * @throws LoginException different errors that may occur during login
     */
    public synchronized String login(String ID, String pwd) throws LoginException {
        ArrayList<String[]> games;
        ArrayList<Controller> activeG;
        ArrayList<Integer> PORT;
        boolean f = false;

        String path = System.getProperty("user.home") + loginJSONURL;      //file path
        FileReader fileJson = null;      //file executable
        try {
            fileJson = new FileReader(path);
        } catch (FileNotFoundException e) {
            signUp(ID, pwd);
            return "null";
        }


        Gson gson = new Gson();
        JsonArray loginJSON = gson.fromJson(fileJson, JsonArray.class);
        JsonObject temp;

        for(int i = 0; i < loginJSON.size() && !f; i++){

            temp = loginJSON.get(i).getAsJsonObject();

            if(temp.get("playerID").getAsString().equals(ID)){
                if(!temp.get("password").getAsString().equals(pwd)){
                    throw new LoginException("Wrong password");
                }else{
                    f = true;
                }
            }

        }

        if(!f){
            throw new LoginException("Wrong login credentials");
        }



        synchronized (playersInGames){

            games = playersInGames;

        }


        for(int j = 0; j < games.size(); j++){

            String[] players = games.get(j);

            for(int i = 0; i < players.length; i++){
                if(players[i].equals(ID)){

                    synchronized (activeGames){

                        activeG = activeGames;

                    }
                    if(activeG.get(j).isPlayerOffline(ID)){
                        return activeG.get(j).toString();
                    }else {
                        throw new LoginException("Player already online");
                    }

                }
            }

        }

        checkPlayerLog(ID);

        return "null";
    }

    /**
     * @param ID player ID the player wishes to have
     * @param pwd setting password for the login
     * @throws LoginException different errors that may occur during login
     */
    /*
    the method adds the ID and password to a JSON file stored in a config directory in the user's home directory
    this just memorizes different logins that the player may have
     */
    public synchronized void signUp(String ID, String pwd) throws LoginException {

        String path = System.getProperty("user.home") + loginJSONURL;   //file path

        if(ID.charAt(0) < 65 || (ID.charAt(0) > 90 && ID.charAt(0) < 97) || ID.charAt(0) > 122){
            throw new LoginException("Illegal characters in ID");
        }

        FileReader fileJsonRead = null;      //file executable
        try {
            fileJsonRead = new FileReader(path);
        } catch (IOException e) {
            fileJsonRead = null;
            new File(System.getProperty("user.home") + directoryJson ).mkdirs();
        }

        JsonArray original = new JsonArray();

        if(fileJsonRead != null) {
            original = new Gson().fromJson(fileJsonRead, JsonArray.class);

            for (int i = 0; i < original.size(); i++) {
                if (original.get(i).getAsJsonObject().get("playerID").getAsString().equals(ID)) {
                    throw new LoginException("ID already taken");
                }
            }
        }


        FileWriter fileJson = null;      //file executable
        try {
            fileJson = new FileWriter(path);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        Gson gson = new Gson();

        JsonWriter writer = new JsonWriter(fileJson);


        JsonObject update = new JsonObject();
        update.addProperty("playerID", ID);
        update.addProperty("password", pwd);



        original.add(update);
        gson.toJson(original, JsonObject.class, writer);



        try {
            writer.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        checkPlayerLog(ID);

    }

    /**
     * @param ID ID of the player wishing to join a game
     * @param connectionType type of protocol the player is currently using
     * @return returns a string indicating the controller of the game to be joined
     * @throws addPlayerToGameException different errors that may occur while searching for a game to join
     */
    /*
    the method searches for the first game in the list of games that hasn't already started with space in the waiting room and adds the player to it
     */
    public synchronized String joinGame(String ID, ConnectionType connectionType) throws addPlayerToGameException {
        ArrayList<String[]> tempPlayersInGames;
        ArrayList<Controller> tempActiveGames;
        int j = 0;

        synchronized (activeGames){
            if(activeGames.size()==0) throw new addPlayerToGameException("there isn't active game try to create new one");
            tempActiveGames = activeGames;
        }

        synchronized (playersInGames){
            tempPlayersInGames = playersInGames;
        }



        while(j < tempActiveGames.size()){
            if(tempActiveGames.get(j).getMaxPlayerNumber() > tempPlayersInGames.get(j).length && tempActiveGames.get(j).getCurrentPlayer() == -1){
                addPlayerToGame(ID, connectionType, j);
                return activeGames.get(j).toString();
            }
            j++;
        }
        throw new addPlayerToGameException("not free game at the moment");
    }

    /**
     * @param ID ID of the player wishing to join a game
     * @param connectionType type of protocol the player is currently using
     * @param searchID the game to be joined must have this ID in the player list
     * @return returns a string indicating the controller of the game to be joined
     * @throws addPlayerToGameException different errors that may occur while searching for a game to join
     */
    /*
    the method searches for the game with the specified ID in the player list in the list of games that hasn't already started with space in the waiting room and adds the player to it
     */
    public synchronized String joinGame(String ID, ConnectionType connectionType, String searchID) throws addPlayerToGameException {
        ArrayList<String[]> tempPlayersInGames;
        ArrayList<Controller> tempActiveGames;

        int j = 0;

        synchronized (activeGames){
            if(activeGames.size()==0) throw new addPlayerToGameException("there isn't active game try to create new one");
            tempActiveGames = activeGames;
        }

        synchronized (playersInGames){
            tempPlayersInGames = playersInGames;
        }

        while(tempActiveGames.get(j) != null){
            for(String s : tempPlayersInGames.get(j)){
                if(s.equals(searchID)){
                    if(activeGames.get(j).getCurrentPlayer()==-1) {
                        addPlayerToGame(ID, connectionType, j);
                        return activeGames.get(j).toString();
                    }else {
                        throw new addPlayerToGameException("game just started");
                    }
                }
            }
            j++;
        }
        throw new addPlayerToGameException("ID not found");
    }

    /**
     * @param ID ID of the player that just joined a game
     * @param connectionType protocol of the player that just joined a game
     * @param gameNumber number indicating the game the player has joined
     * @throws addPlayerToGameException different errors that may occur while searching for a game to join
     */
    /*
    the method updates the collections keeping track if the players and the games they are in
     */
    public synchronized void addPlayerToGame(String ID, ConnectionType connectionType, int gameNumber) throws addPlayerToGameException {
        ArrayList<String[]> tempPlayersInGames;
        ArrayList<Controller> tempActiveGames;
        ArrayList<String> temp = new ArrayList<>();
        int j = gameNumber, i = 0;



        synchronized (playersInGames){
            tempPlayersInGames = playersInGames;
        }

        synchronized (activeGames){
            tempActiveGames = activeGames;
        }

        tempActiveGames.get(j).addNewPlayer(ID);

        Collections.addAll(temp, tempPlayersInGames.get(j));
        temp.add(ID);
        playersInGames.set(j, temp.toArray(new String[0]));

    }

    /**
     * @param ID ID of the player creating the game
     * @return returns a string indicating the controller of the game that has been created
     * @throws addPlayerToGameException possible errors
     */
    /*
    the method creates a game with the default maximum number of players
     */
    public synchronized String createGame(String ID) throws addPlayerToGameException {
        Controller controller = new Controller();
        controller.setLobby(this);
        createGameSupport(ID, controller);
        return controller.toString();
    }

    /**
     * @param ID ID of the player creating the game
     * @param maxPlayerNumber maximum number of players for the game
     * @return returns a string indicating the controller of the game that has been created
     * @throws addPlayerToGameException possible errors
     */
    /*
    the method creates a game with the specified maximum number of players
     */
    public synchronized String createGame(String ID, int maxPlayerNumber) throws addPlayerToGameException {
        Controller controller = new Controller(maxPlayerNumber);
        controller.setLobby(this);
        createGameSupport(ID, controller);
        return controller.toString();
    }

    /**
     * @param ID ID of the player creating the game
     * @param controller controller of the game just created
     * @throws addPlayerToGameException possible errors
     */
    /*
    the method updates the collection keeping track of active games and players in games while also launching the executor managing the controller of the new game
     */
    private synchronized void createGameSupport(String ID, Controller controller) throws addPlayerToGameException {

        int i = 0;
        int port = -1;
        ArrayList<String> temp = new ArrayList<>();

        temp.add(ID);

        controller.turnOnCnt(socket, rmi);

        activeGames.add(controller);
        executor = Executors.newCachedThreadPool();

        executor.submit(controller);

        playersInGames.add(temp.toArray(new String[0]));
        controller.addNewPlayer(ID);

    }




    public synchronized ArrayList<Controller> getActiveGames() {
        return activeGames;
    }

    public synchronized ArrayList<String[]> getPlayersInGames() {
        return playersInGames;
    }

    public void closeAllGames(){
        executor.shutdown();
    }


    /**
     * @param controller controller of the terminated game
     */
    /*
    the method removes the game and players from the collections keeping track of active games and players
     */
    public void setAllPlayersOffline(Controller controller){
        synchronized (playersInGames){
            synchronized (activeGames){
                for(int i = 0; i < activeGames.size(); i++){
                    if(activeGames.get(i).equals(controller)){
                        activeGames.remove(i);
                        playersInGames.remove(i);
                        System.out.println(TextColor.YELLOW.get() + "game N " + i + " is ended" + TextColor.DEFAULT.get());
                        return;
                    }
                }
            }
        }

    }

    /**
     * @param ID ID of the player trying to log in
     * @throws LoginException possible errors
     */
    /*
    the methods checks if a player trying to log in is already logged on on another device
     */
    private void checkPlayerLog(String ID) throws LoginException {
        synchronized (playersInGames){
            for(int i = 0; i<playersInGames.size();i++ ){
                for (String s: playersInGames.get(i))if(s.equals(ID))throw new LoginException("Player already logged on");
            }
        }
        /*
        synchronized (playersLoggedOn){
            for(int i = 0; i < playersLoggedOn.size(); i++){
                if(playersLoggedOn.get(i).equals(ID)){
                    throw new LoginException("Player already logged on");
                }
            }
            playersLoggedOn.add(ID);
        }*/
    }

}

