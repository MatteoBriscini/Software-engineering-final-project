package it.polimi.ingsw.client.View;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import it.polimi.ingsw.client.Connection.ClientRMI;
import it.polimi.ingsw.client.Connection.ClientSOCKET;
import it.polimi.ingsw.client.Connection.ConnectionManager;
import it.polimi.ingsw.client.Exceptions.PlayerNotFoundException;
import it.polimi.ingsw.client.Game.MainBoard;
import it.polimi.ingsw.client.Game.PlayerBoard;
import it.polimi.ingsw.client.Player.LobbyPlayer;
import it.polimi.ingsw.client.Player.Player;
import it.polimi.ingsw.client.Player.PlayingPlayer;
import it.polimi.ingsw.shared.Cards.CardColor;
import it.polimi.ingsw.shared.JsonSupportClasses.JsonUrl;
import it.polimi.ingsw.shared.JsonSupportClasses.Position;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;

import static it.polimi.ingsw.client.View.ColorCodes.*;


public class TUI {

    MainBoard mainBoard;
    PlayerBoard playerBoard;
    private Player player;

    private String printLine;

    private JsonUrl jsonUrl;

    private int minPlayers,maxPlayers,minPickable,maxPickable;

    private int socketPort,RMIPort;
    private String serverIP;

    private ConnectionManager connection;

    public TUI(Player player){

        this.player=player;

        try {
            jsonCreate();
        } catch (FileNotFoundException e) {
            System.out.println("TUI: JSON FILE NOT FOUND");
            throw new RuntimeException(e);
        }

    }


    public void jsonCreate() throws FileNotFoundException{

        InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream(jsonUrl.getUrl("controllerConfig"));
        if(inputStream == null) throw new FileNotFoundException();
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

        InputStream inputStream1 = this.getClass().getClassLoader().getResourceAsStream(jsonUrl.getUrl("netConfig"));
        if(inputStream1 == null) throw new FileNotFoundException();
        BufferedReader bufferedReader1 = new BufferedReader(new InputStreamReader(inputStream1));

        JsonObject jsonObject = new Gson().fromJson(bufferedReader, JsonObject.class);
        this.minPlayers = jsonObject.get("minPlayerNumber").getAsInt();
        this.maxPlayers = jsonObject.get("maxPlayerNumber").getAsInt();
        this.minPickable = jsonObject.get("minTakeCard").getAsInt();
        this.maxPickable = jsonObject.get("maxTakeCard").getAsInt();

        jsonObject = new Gson().fromJson(bufferedReader1, JsonObject.class);
        this.serverIP = jsonObject.get("serverIP").getAsString();
        this.socketPort = jsonObject.get("defSocketPort").getAsInt();
        this.RMIPort = jsonObject.get("defRMIPort").getAsInt();
    }



    public void toRun(){
        System.out.println("WELCOME TO My Shelfie");
        connectionSelection();
        userIdentification();
    }

    /**
     * this method allows the player to choose how to connect to the server (via Socket or RMI)
     */
    public void connectionSelection(){

        char c;

        do {
            System.out.println("Select the type of the connection:\n[R] RMI\n[S] Socket");
            c = charCommand();
            if(c!='R' && c!='S')
                printError("Invalid selection, please try again\n");
        }while(c!='R' && c!='S');

        if(c=='S'){
            try {
                connection = new ClientSOCKET(socketPort,serverIP);
            } catch (Exception e) {
                player.disconnectError("Server is offline");
            }
        }
        else{
            try {
                connection = new ClientRMI(RMIPort,serverIP);
            } catch (Exception e) {
                player.disconnectError("Server is offline");
            }
        }
    }

    /**
     * this method allows the player to log in or sign up
     */
    public void userIdentification() {

        Scanner sc = new Scanner(System.in);
        String user;
        String pwd;
        char selection;
        boolean logged;

        user="/back";
        selection='n';
        do {
            if(user.equals("/back")) {
                do {
                    System.out.println("What do you want to do?\n[S]Sign up\n[L]Log in");
                    selection = charCommand();
                    if (selection != 'S' && selection != 'L')
                        printError("Invalid selection, please try again\n");
                } while (selection != 'S' && selection != 'L');
            }

            logged=false;
            System.out.println("Enter your username (or /back to return to the previous selection):");
            user=stringCommand();
            if(!(user.equals("/back"))) {
                System.out.println("Enter your password:");
                pwd = sc.nextLine();

                player = new LobbyPlayer(user, pwd, connection);
                if (selection == 'S') {
                    logged = ((LobbyPlayer) player).signUp();
                } else {
                    logged = ((LobbyPlayer) player).login();
                }
            }
        }while (!logged || user.equals("/back"));

    }

    public void printBoard() {


        /** stampare obiettivi*/


        for(String id : ((PlayingPlayer) player).getPlayersID()){
            playerBoard = ((PlayingPlayer)player).getPlayerBoard(id);
            for(int x=0;x< playerBoard.getColumns();x++) {
                printLine= EMPTY.get();
                for (int y = playerBoard.getRows() - 1; y >= 0; y--) {
                    for (int i = 0; i < CardColor.values().length; i++) {
                        if(playerBoard.getColor(x, y).toString().equals(CardColor.values()[i].toString())) {
                            printLine = printLine + ColorCodes.values()[i].get()+"  ";
                        }
                    }
                }
                System.out.println(printLine + " " + EMPTY.get());
            }
            if(id.equals(player.getPlayerID()))
                System.out.println("Your board");
            else
                System.out.println(id+"'s board");


            //System.out.println((player).getPlayerID().toString()+"'s board");


        }

        mainBoard=((PlayingPlayer)player).getMainBoard();
        for(int x=0;x< mainBoard.getColumns();x++) {
            printLine= EMPTY.get();
            for (int y = mainBoard.getRows() - 1; y >= 0; y--) {
                for (int i = 0; i < CardColor.values().length; i++) {
                    if(mainBoard.getColor(x, y).toString().equals(CardColor.values()[i].toString())) {
                        printLine = printLine + ColorCodes.values()[i].get()+"  ";
                    }
                }
            }
            System.out.println(printLine + " " + EMPTY.get());
        }
        System.out.println("Main board");

    }


    /**
     * this method allows the player to join create a new game
     */
    public void createGame(){
        String selection;
        boolean success;

        success=false;
        do {
            System.out.println("Insert the number of players for this game (min " +minPlayers+ ", max" +maxPlayers+ "\nInsert /def to use default settings");
            selection = stringCommand();
            if (selection.equals("/def")) {
                success=((LobbyPlayer)player).createGame();
            } else if (Integer.parseInt(selection)>=minPlayers && Integer.parseInt(selection)<=maxPlayers) {
                success=((LobbyPlayer)player).createGame(Integer.parseInt(selection));
            }
        }while(!success);
    }

    /**
     * this method allows the player to join an existent game
     */
    public void joinGame(){
        String selection;
        boolean success;

        do{
            System.out.println("Insert the name of a player to play with a friend, or /rand to join a random game");
            selection = stringCommand();
            if(selection.equals("/rand")){
                success=((LobbyPlayer)player).joinGame();
            } else {
                success=((LobbyPlayer)player).joinGame(selection);
            }
        }while (!success);


    }


    /**
     * this method reads the message from the standard input stream and calls the function on the player to send the message
     * if the message is directed to a not existent player, an error is printed on the standard error stream
     */
    public void sendMsg(){
        Scanner sc = new Scanner(System.in);
        try {
            ((PlayingPlayer) player).sendMessage(sc.nextLine());
        } catch (PlayerNotFoundException e){
            printError("CHAT ERROR: " + e.getMessage());
        }
    }


    /**
     * this method prints a message sent to the player
     * @param msg is the string that contains the message sent to the player
     */
    public void receiveMsg(String msg){
        System.out.println(msg);
    }


    /**
     * this method calls the method on the player to start a new game
     */
    public void startGame(){
        ((PlayingPlayer)player).startGame();
    }


    /**
     * this method manage the entire process of taking one or more cards from the main board and inserting them into the player's board
     * the steps of the process are: 1)taking the cards from the main board (with a check on the validity of the move)
     *                               2)reordering the cards
     *                               3)selecting the column of the player's board in which the cards need to be inserted
     *                               4)calling the method to insert the cards on the player's board
     */
    public void takeCard(){
        String[] picks;
        Position[] positions;
        int column;


        picks= selectCardsFromBoard();

        reorderCards(picks);

        column=selectColumn();

        positions = new Position[picks.length];
        intCoordinates(picks,positions);

        ((PlayingPlayer)player).takeCard(column,positions);

    }

    /**
     * @return an array of Strings that contains the coordinates of the to be picked from the main board
     *          format: {[x1,y1],[...],[xn,yn]}
     */
    public String[] selectCardsFromBoard(){
        Scanner sc = new Scanner(System.in);
        String selection;
        String[] picks;

        do {
            System.out.println("Select the positions of the cards you want to take: x1,y1; [...]; xn,yn\n");
            selection = sc.nextLine();
            picks = selection.split(";");
        } while (picks.length<minPickable || picks.length>maxPickable);

        return picks;
    }

    /**
     * @param picks is an array that contains coordinates formatted as Strings [x,y]
     *              This array will be reordered, based on player's choices
     */
    public void reorderCards(String[] picks){
        Scanner sc = new Scanner(System.in);
        String selection;
        String[] tmpPick;
        String tmpPos;
        char sel;

        do{

            /** stampare carte selezionate */

            do {
                System.out.println("Do you want to change the order of the cards? Y/N");
                sel = charCommand();
            }while (sel!='Y' && sel!='N');


            do{
                System.out.println("Insert the new order, from the bottom to the top: bottom card, [...], top card\n");
                selection = sc.nextLine();
                tmpPick=selection.split(",");

                if(tmpPick.length==picks.length){
                    for(int i=0;i<picks.length;i++) {
                        tmpPos = picks[i];
                        picks[i]=picks[Integer.parseInt(tmpPick[i])];
                        picks[Integer.parseInt(tmpPick[i])]=tmpPos;
                    }
                }
                else printError("The number of tiles you want to reorder is different from the number of tiles you picked, please try again\n");
            }while(tmpPick.length!=picks.length);

        }while (sel=='Y');

    }

    /**
     * @return the index of the columns in which the cards have to be placed
     */
    public int selectColumn(){
        Scanner sc = new Scanner(System.in);
        int column;

        do {
            System.out.println("select the column on your board:\n");
            column = Integer.parseInt(sc.nextLine());
            if(column<0 || column>= ((PlayingPlayer)player).getPlayerBoard(((PlayingPlayer)player).getPlayerID()).getColumns())
                printError("Invalid column, please try again");
        }while (column<0 || column>= ((PlayingPlayer)player).getPlayerBoard(((PlayingPlayer)player).getPlayerID()).getColumns());
        return column;
    }

    /**
     * @param picks is the array that contains the coordinates as Strings
     * @param positions is the array that will contain the coordinates as ints
     */
    private void intCoordinates(String[] picks,Position[] positions){
        String[] tmpPick;

        for(int i=0; i< picks.length;i++){
            tmpPick=picks[i].split(",");
            positions[i]= new Position(Integer.parseInt(tmpPick[0]),Integer.parseInt(tmpPick[1]));
        }
    }


    public void Update(){

    }

    /**
     * this method sorts the final table of the game and displays it
     * @param tableJ is a JsonObject formatted this way: { "[name1]": points1, [...] , "[nameN]": pointsN}
     */
    public void finalResults(JsonObject tableJ){
        int n=((PlayingPlayer)player).getPlayersNumber();
        String[][] table = new String[n][n];

        for(int i=0;i<n; i++){
            table[i][0]= ((PlayingPlayer)player).getPlayersID()[i];
            table[i][1]= tableJ.get(((PlayingPlayer)player).getPlayersID()[i]).getAsString();
        }

        Arrays.sort(table, Comparator.comparingInt(row->Integer.parseInt(row[1])));

        System.out.println("FINAL RESULTS:");
        for(int i=0;i<n;i++){
            System.out.println("#"+(i+1)+": "+table[i][0]+"\tpoints: "+table[i][1]);
        }

    }


    /**
     * @param n is the number of players connected to the game
     */
    public void receiveNumPlayers(int n){
        System.out.println("Players connected: "+n);
    }



    public void setMode(){

    }

    /**
     * this method calls the quitGame method on the server and prints an error message if this procedure fails
     */
    public void quitGame(){
        if(!((PlayingPlayer)player).quitGame())
            printError("Failed to quit the game");
    }

    /**
     * @param err is the string to print on the standard error stream
     */
    public void printError(String err){
        System.err.println(err);
    }


    /**
     * @return an upper case char
     */
    public char charCommand(){
        Scanner sc = new Scanner(System.in);
        return(Character.toUpperCase(sc.next().charAt(0)));
    }


    /**
     * @return a lower case String
     */
    public String stringCommand() {
        Scanner sc = new Scanner(System.in);
        return (sc.nextLine().toLowerCase());
    }
}
