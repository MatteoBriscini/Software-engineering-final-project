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
import it.polimi.ingsw.shared.Cards.Card;
import it.polimi.ingsw.shared.Cards.CardColor;
import it.polimi.ingsw.shared.JsonSupportClasses.JsonUrl;
import it.polimi.ingsw.shared.JsonSupportClasses.Position;
import it.polimi.ingsw.shared.JsonSupportClasses.PositionWithColor;
import it.polimi.ingsw.shared.PlayerMode;
import it.polimi.ingsw.shared.TextColor;

import java.io.*;
import java.util.*;

import static it.polimi.ingsw.client.View.ColorCodes.*;


public class TUI implements UserInterface{

    MainBoard mainBoard;
    Scanner scCommand;
    PlayerBoard playerBoard;
    private Player player;

    private String printLine;

    private int minPlayers,maxPlayers,minPickable,maxPickable;

    private int socketPort,RMIPort;
    private String serverIP;
    //hide or show elements
    private boolean otherBoard = true;

    private String[] commonGoalsList;
    private int numTotCommonGoals;
    private boolean commonGoals = true;
    private boolean privateGoals = true;
    private ConnectionManager connection;

    public TUI(){

        try {
            jsonCreate();
        } catch (FileNotFoundException e) {
            System.out.println("TUI: JSON FILE NOT FOUND");
            throw new RuntimeException(e);
        }

        serverSelection();
        connectionSelection();
        userIdentification();

        Thread thread = new Thread(()->{this.toRun();});
        thread.start();
    }

    private void jsonCreate() throws FileNotFoundException{

        InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream(JsonUrl.getUrl("controllerConfig"));
        if(inputStream == null) throw new FileNotFoundException();
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

        InputStream inputStream1 = this.getClass().getClassLoader().getResourceAsStream(JsonUrl.getUrl("netConfig"));
        if(inputStream1 == null) throw new FileNotFoundException();
        BufferedReader bufferedReader1 = new BufferedReader(new InputStreamReader(inputStream1));

        InputStream inputStream2 = this.getClass().getClassLoader().getResourceAsStream(JsonUrl.getUrl("TUICommonGoalsConfig"));
        if(inputStream == null) throw new FileNotFoundException();
        BufferedReader bufferedReader2 = new BufferedReader(new InputStreamReader(inputStream2));

        JsonObject jsonObject = new Gson().fromJson(bufferedReader, JsonObject.class);
        this.minPlayers = jsonObject.get("minPlayerNumber").getAsInt();
        this.maxPlayers = jsonObject.get("maxPlayerNumber").getAsInt();
        this.minPickable = jsonObject.get("minTakeCard").getAsInt();
        this.maxPickable = jsonObject.get("maxTakeCard").getAsInt();

        jsonObject = new Gson().fromJson(bufferedReader1, JsonObject.class);
        this.serverIP = jsonObject.get("serverIP").getAsString();
        this.socketPort = jsonObject.get("defSocketPort").getAsInt();
        this.RMIPort = jsonObject.get("defRmiPort").getAsInt();

        jsonObject = new Gson().fromJson(bufferedReader2, JsonObject.class);
        this.numTotCommonGoals = jsonObject.get("tot").getAsInt();
        commonGoalsList=new String[numTotCommonGoals];
        for(int i=0;i<numTotCommonGoals;i++){
            this.commonGoalsList[i]=(jsonObject.get(String.valueOf(i)).getAsString());
        }
    }

    private int intParser(String s){
        try {
            return Integer.parseInt(s);
        }catch (RuntimeException e) {
            printError("Invalid command syntax");
            return -1;
        }
    }

    private void printTitle(){
        System.out.println(TextColor.YELLOW.get() +
                "███╗   ███╗██╗   ██╗    ███████╗██╗  ██╗███████╗██╗     ███████╗██╗███████╗\n" +
                "████╗ ████║╚██╗ ██╔╝    ██╔════╝██║  ██║██╔════╝██║     ██╔════╝██║██╔════╝\n" +
                "██╔████╔██║ ╚████╔╝     ███████╗███████║█████╗  ██║     █████╗  ██║█████╗  \n" +
                "██║╚██╔╝██║  ╚██╔╝      ╚════██║██╔══██║██╔══╝  ██║     ██╔══╝  ██║██╔══╝  \n" +
                "██║ ╚═╝ ██║   ██║       ███████║██║  ██║███████╗███████╗██║     ██║███████╗\n" +
                "╚═╝     ╚═╝   ╚═╝       ╚══════╝╚═╝  ╚═╝╚══════╝╚══════╝╚═╝     ╚═╝╚══════╝\n" +
                "                                                                           \n" +
                "\n\n\n\n\n" + TextColor.DEFAULT.get());
    }

    public void toRun(){
        char c;

        this.centerContent(4);
        this.printTitle();
        do {
            this.printRun();
            c=charCommand();

        }while (!runResponse(c));
    }
    private void printRun(){
        System.out.println(TextColor.YELLOW.get() + "Hi " + player.getPlayerID() + ", do you want to create a new game or join an existing one?"+ TextColor.LIGHTBLUE.get() + "\n[C] Create\n[J] Join" + TextColor.DEFAULT.get());
    }

    private boolean runResponse (char c){
        if (c!='C' && c!='J') {
            printError("Invalid selection, please try again");
            return false;
        }
        else if (c=='C'){
            createGame();
            this.centerContent(1);
            this.printTitle();
            System.out.println(TextColor.LIGHTBLUE.get() + "THIS IS THE WAITING ROOM... \n\n" + TextColor.DEFAULT.get());
        }
        else if (c=='J') {
            joinGame();
            if(((PlayingPlayer)player).getActivePlayer() == null){
                this.centerContent(1);
                this.printTitle();
                System.out.println(TextColor.LIGHTBLUE.get() + "THIS IS THE WAITING ROOM... \n\n" + TextColor.DEFAULT.get());
            }
        }
        return true;
    }

    synchronized private void centerContent(int contentSize){
        int ROWS;
        if(System.getenv("LINES")==null) ROWS = 24;
        else ROWS = Integer.parseInt(System.getenv("LINES"));
        ROWS = ROWS - contentSize;
        while (ROWS>0){
            System.out.println("\n");
            ROWS--;
        }
    }

    private void userInput(){
        scCommand = new Scanner(System.in);
        String s;
        char c;
        String msg;

        while (scCommand.hasNextLine()) {
            s = scCommand.nextLine().toLowerCase();
            if(player instanceof LobbyPlayer) {
                if(!runResponse(Character.toUpperCase(s.charAt(0)))) this.toRun();
                break;
            }
            if (s.equals("/start")) {
                ((PlayingPlayer) player).startGame();

            } else if (s.startsWith("/chat")) {
                msg = s.substring(6, s.length());
                try {
                    ((PlayingPlayer) player).sendMessage(msg);
                } catch (PlayerNotFoundException e) {
                    System.out.println(e.getMessage());
                }
            } else if (s.equals("/help")) {
                System.out.println(TextColor.YELLOW.get() + "/start" + TextColor.DEFAULT.get() + " to start the game");
                System.out.println(TextColor.YELLOW.get() + "/quit" + TextColor.DEFAULT.get() + " to quit the game");
                System.out.println(TextColor.YELLOW.get() + "/chat" + TextColor.DEFAULT.get() + " to send message to all the players in this game");
                System.out.println(TextColor.YELLOW.get() + "/chat --playerid:" + TextColor.DEFAULT.get() + " to send a message to a specif player id");
                System.out.println(TextColor.YELLOW.get() + "/pick [column;x,y;x,y...] " + TextColor.DEFAULT.get() + " to make a move (column, x and y have to be numbers)");
                System.out.println(TextColor.YELLOW.get() + "/hide" + TextColor.DEFAULT.get() + " to hide content of the ui (options: --board --commongoal --privategoal");
                System.out.println(TextColor.YELLOW.get() + "/show" + TextColor.DEFAULT.get() + " to show content of the ui (options: --board --commongoal --privategoal");
            } else if (s.startsWith("/pick")) {
                int index1 = s.indexOf('[');
                int index2 = s.indexOf(']');
                if (index1 == -1 || index2 == -1)
                    this.printError("Invalid command, please try again (/help to see the allowed commands)");
                else this.takeCard(s.substring(index1 + 1, index2).replaceAll("\\s+",""),
                        ((PlayingPlayer) player).getPlayerBoard(player.getPlayerID()).getColumns(),
                        ((PlayingPlayer) player).getMainBoard().getColumns(),
                        ((PlayingPlayer) player).getMainBoard().getRows());
            } else if (s.startsWith("/hide")) {
                this.changeSetUp(false, s.substring(5).replaceAll("\\s+",""));
            } else if(s.startsWith("/show")){
                this.changeSetUp(true, s.substring(5).replaceAll("\\s+",""));
            } else if (s.equals("/quit")) {
                do {
                    System.out.println("Are you sure? Y/N");
                    c=charCommand();

                    if(c=='Y')
                        ((PlayingPlayer) player).quitGame();
                    if(c!='Y' && c!='N')
                        this.printError("Invalid selection,please try again");
                }while (c!='Y' && c!='N');
            }   else this.printError("Invalid command, please try again (/help to see the allowed commands)");

        }
    }

    private void changeSetUp(boolean b, String command){
        int index = command.indexOf("--");
        boolean bol = true;
        String ex;
        if(index==-1){
            printError("Invalid command syntax");
            return;
        }
        if(player instanceof LobbyPlayer){
            printError("You can call this method only when the game is already started");
            return;
        }
        while (index !=-1) {
            command = command.substring(index + 2);
            index = command.indexOf("--");
            ex= command;
            if(index!=-1)ex = command.substring(0, index);
            switch (ex){
                case "board":
                    this.otherBoard = b;
                    break;
                case "commongoal":
                    this.commonGoals = b;
                    break;
                case "privategoal":
                    this.privateGoals = b;
                    break;
                default:
                    bol = false;
                    break;
            }
        }
        if(!bol)printError("Invalid param");
        else {
            this.updateAll();
            this.notifyNewActivePlayer();
        }
    }
    private void serverSelection(){
        Scanner sc = new Scanner(System.in);
        char c;
        String ip;

        this.centerContent(1);
        do {

            System.out.println(TextColor.YELLOW.get() + "Do you want to use the default server? ("+ serverIP + ")" + TextColor.LIGHTBLUE.get() + " Y/N" + TextColor.DEFAULT.get());
            c = charCommand();
            if (c!='Y' && c!='N')
                printError("Invalid selection, please try again");
        }while (c!='Y' && c!='N');

        if(c=='N') {
            System.out.println("insert server IP:");
            ip = sc.nextLine();
            this.serverIP = ip;
        }
        this.centerContent(3);
    }

    /**
     * this method allows the player to choose how to connect to the server (via Socket or RMI)
     */
    private void connectionSelection(){

        char c;

        do {

            System.out.println(TextColor.YELLOW.get() + "Select the type of the connection:"+ TextColor.LIGHTBLUE.get() + "\n[R] RMI\n[S] Socket" + TextColor.DEFAULT.get());
            c = charCommand();
            if(c!='R' && c!='S')
                printError("Invalid selection, please try again\n");
        }while(c!='R' && c!='S');

        if(c=='S'){
            try {
                connection = new ClientSOCKET(socketPort,serverIP);
            } catch (Exception e) {
                this.printError("Apparently the server is offline");
                System.out.println("Press enter to close the app");
                Scanner sc = new Scanner(System.in);
                sc.nextLine();
                System.exit(0);
            }
        }
        else{
            try {
                connection = new ClientRMI(RMIPort,serverIP);
            } catch (Exception e) {
                this.printError("Apparently the server is offline");
                System.out.println("Press enter to close the app");
                Scanner sc = new Scanner(System.in);
                sc.nextLine();
                System.exit(0);

            }
        }
    }

    /**
     * this method allows the player to log in or sign up
     */
    private void userIdentification() {

        this.centerContent(3);

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
                    System.out.println(TextColor.YELLOW.get() +  "What do you want to do?" + TextColor.LIGHTBLUE.get() + "\n[S]Sign up\n[L]Log in"+ TextColor.DEFAULT.get());
                    selection = charCommand();
                    if (selection != 'S' && selection != 'L')
                        printError("Invalid selection, please try again\n");
                } while (selection != 'S' && selection != 'L');
                this.centerContent(4);
            }

            logged=false;
            System.out.println(TextColor.YELLOW.get() + "Enter your username "+ TextColor.LIGHTBLUE.get() +"(or /back to return to the previous selection):" + TextColor.DEFAULT.get());
            user=stringCommand();
            if(!(user.equals("/back"))) {
                System.out.println(TextColor.YELLOW.get() + "Enter your password:"+ TextColor.DEFAULT.get());
                pwd = sc.nextLine();


                player = new LobbyPlayer(user, pwd, connection);
                if (selection == 'S') {
                    logged = ((LobbyPlayer) player).signUp();
                } else {
                    logged = ((LobbyPlayer) player).login();
                }
            }

            if(!logged) printError("Invalid selection, please try again\n");
        }while (!logged || user.equals("/back"));
        this.centerContent(3);
        player.setUi(this);
    }


    /**
     * this method allows the player to create a new game
     */
    private void createGame(){
        String selection;
        boolean success;
        int playerNumber;
        success=false;
        do {
            System.out.println(TextColor.YELLOW.get() +"Insert the number of players for this game (min " +minPlayers+ ", max" +maxPlayers+ ") "+ TextColor.LIGHTBLUE.get() +"\nInsert /def to use default settings"+ TextColor.DEFAULT.get());
            selection = stringCommand();
            if (selection.equals("/def")) {
                success=((LobbyPlayer)player).createGame();
            } else {
                playerNumber = this.intParser(selection);
                if (playerNumber >=minPlayers && playerNumber <=maxPlayers) {
                    success=((LobbyPlayer)player).createGame(playerNumber);
                }
            }
            if(!success)
                this.printError("Game creation failed, please try again");
        }while(!success);
    }

    /**
     * this method allows the player to join an existent game
     */
    private void joinGame(){
        String selection;
        boolean success;

        do{
            System.out.println(TextColor.YELLOW.get()+"Insert the name of a player to play with a friend\n"+ TextColor.LIGHTBLUE.get() +"Insert /rand to join a random game"+ TextColor.DEFAULT.get());
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
    private void sendMsg(){
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
        System.out.println("\n\n"+msg+"\n\n");
    }


    /**
     * this method calls the method on the player to start a new game
     */
    private void startGame(){
        ((PlayingPlayer)player).startGame();
    }


    /**
     * this method manage the entire process of taking one or more cards from the main board and inserting them into the player's board
     * the steps of the process are: 1)taking the cards from the main board (with a check on the validity of the move)
     *                               2)reordering the cards
     *                               3)selecting the column of the player's board in which the cards need to be inserted
     *                               4)calling the method to insert the cards on the player's board
     */
    private void takeCard(String cards, int maxPlayerColumns,int maxBoardColumns,int maxBoardsRows) {
        ArrayList<Position> positions = new ArrayList<>();
        int index = cards.indexOf(';');
        int index2, x, y;
        if (index != 1) {
            this.printError("Command pick has an invalid syntax (/help to see the allowed commands)");
            return;
        }
        int column = this.intParser(cards.substring(0, index));
        if (column <0 || column>((PlayingPlayer)player).getPlayerBoard(player.getPlayerID()).getColumns()) {
            this.printError("Invalid column value (/help to see the allowed commands)");
            return;
        }

        while (true) {
            cards = cards.substring(index + 1);
            index = cards.indexOf(';');
            index2 = cards.indexOf(',');
            x = this.intParser(cards.substring(0, index2));
            if (index == -1 || index2 == -1) {
                y = this.intParser(cards.substring(index2 + 1));
                if (!checkPick(x, y, maxBoardColumns, maxBoardsRows)) return;
                positions.add(new Position(x, y));
                break;
            }
            y = this.intParser(cards.substring(index2 + 1, index));
            if (!checkPick(x, y, maxBoardColumns, maxBoardsRows)) return;
            positions.add(new Position(x, y));
            if (index != 3 || cards.indexOf(',') != 1) {
                this.printError("Command pick has an invalid syntax (/help to see the allowed commands)");
                return;
            }
        }

        if (positions.size() < minPickable) {
            this.printError("You're trying to take too few cards(/help to see the allowed commands)");
            return;
        }
        if (positions.size() > maxPickable) {
            this.printError("You're trying to take too many cards(/help to see the allowed commands)");
            return;
        }
        char command;

        if(
                !((PlayingPlayer)player).checkMainBoardMove(positions.toArray(new Position[0])) ||
                !((PlayingPlayer)player).checkPlayerBoardMove(column, positions.size())
        ) return;

        if (positions.size()>1){
            ArrayList<Position> tmp = null;
            do {
                System.out.println(TextColor.YELLOW.get() + "Do you want to reorder the picked cards?" + TextColor.LIGHTBLUE.get() + " [y/n]" + TextColor.DEFAULT.get());
                command = Character.toUpperCase(new Scanner(System.in).next().charAt(0));
                if (command != 'Y' && command != 'N') {
                    printError("Invalid selection, please try again\n");
                }
                if (command == 'Y') tmp = this.reorderCards(positions);
                else break;
            } while (tmp == null);
            positions = tmp;
        }
        ((PlayingPlayer)player).takeCard(column,positions.toArray(new Position[0]));
    }
    private boolean checkPick(int x, int y,int maxBoardColumns,int maxBoardsRows){
        if(x>=maxBoardColumns || y>= maxBoardsRows || x<0 || y<0){
            this.printError("Not valid position on main board(/help to see the allowed commands)");
            return false;
        }
        return true;
    }

    /**
     * @return an array of Strings that contains the coordinates of the to be picked from the main board
     *          format: {[x1,y1],[...],[xn,yn]}
     */
    private String[] selectCardsFromBoard(){
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
    private ArrayList<Position> reorderCards(ArrayList<Position> picks){
        Scanner sc = new Scanner(System.in);
        ArrayList<Integer> order = new ArrayList<>();
        Integer i;
        int index;
        ArrayList<Position> tmp  = new ArrayList<>();

        System.out.println(TextColor.YELLOW.get() + "Type the new order"+ TextColor.LIGHTBLUE.get() +" (es: 1,2,0)" + TextColor.DEFAULT.get());
        String s = sc.nextLine().toLowerCase().replaceAll("\\s+","");
        for(int j=0; j<picks.size()-1;j++) {
            index = s.indexOf(',');
            if(index==-1){
                this.printError("Invalid syntax for reorderCards");
                return null;
            }
            i = this.intParser(s.substring(0,index));
            if(!checkReorder(i,picks.size(), order)) return null;
            order.add(i);
            tmp.add(picks.get(i));

            s = s.substring(index+1);
        }
        if(s.indexOf(',')!=-1){
            this.printError("Invalid syntax for reorderCards");
            return null;
        }
        i = this.intParser(s);
        if(!checkReorder(i,picks.size(), order)) return null;
        order.add(i);
        tmp.add(picks.get(i));

        return tmp;
    }

    private boolean checkReorder(int index, int size, ArrayList<Integer> order){
        if(order.contains(index)){
            this.printError("Can't type two time same number");
            return false;
        }
        if(index>=size){
            this.printError("You have typed a number that is too big");
            return false;
        }
        if(index<0) return false;
        return true;
    }

    /**
     * @return the index of the columns in which the cards have to be placed
     */
    private int selectColumn(){
        Scanner sc = new Scanner(System.in);
        int column;

        do {
            System.out.println("Select the column on your board:\n");
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
    @Override
    public void notifyNewActivePlayer(){
        System.out.println(TextColor.YELLOW.get() +"CURRENT PLAYER: " + ((PlayingPlayer)player).getActivePlayer() + TextColor.DEFAULT.get());
    }

    public void updateLastCommonGoal(){
        updateAll();
    }


    @Override
    public void updateAll(){
        JsonObject[] commons;
        String[][] toPrint = new String[((PlayingPlayer)player).getPlayerBoard(player.getPlayerID()).getColumns()][((PlayingPlayer)player).getPlayerBoard(player.getPlayerID()).getRows()];
        String[][] mainBoardToPrint = new String[((PlayingPlayer) player).getMainBoard().getColumns()][((PlayingPlayer) player).getMainBoard().getRows()];

        this.centerContent(20);

        if(privateGoals)playerBoardToString(((PlayingPlayer) player).getPlayerBoard(player.getPlayerID()),((PlayingPlayer) player).getPrivateGoal() , toPrint);
        else playerBoardToString(((PlayingPlayer) player).getPlayerBoard(player.getPlayerID()), toPrint);
        this.printBoard(toPrint, ((PlayingPlayer)player).getPlayerBoard(player.getPlayerID()).getColumns(), ((PlayingPlayer)player).getPlayerBoard(player.getPlayerID()).getRows(), player.getPlayerID());
        for(String id : ((PlayingPlayer)player).getPlayersID()) {
            if(!id.equals(player.getPlayerID()) && otherBoard){
                playerBoardToString(((PlayingPlayer) player).getPlayerBoard(id), toPrint);
                this.printBoard(toPrint, ((PlayingPlayer)player).getPlayerBoard(player.getPlayerID()).getColumns(), ((PlayingPlayer)player).getPlayerBoard(player.getPlayerID()).getRows(), id);
            }
        }





        if(commonGoals) System.out.println("\nCommon goals:\n1)"+ commonGoalsList[((PlayingPlayer) player).getCommonGoalID()[0]]+"\n2)"+ commonGoalsList[((PlayingPlayer) player).getCommonGoalID()[1]]);

        commons=((PlayingPlayer) player).getCommonGoalScored();
        if(commons!=null) {
            for (JsonObject obj : commons) {
                System.out.println("\n" + obj.get("playerID").getAsString() + ": " + obj.get("value").getAsInt());
            }
        }

        mainBoardToString(((PlayingPlayer) player).getMainBoard(),mainBoardToPrint);
        this.printBoard(mainBoardToPrint,((PlayingPlayer) player).getMainBoard().getColumns(),((PlayingPlayer) player).getMainBoard().getRows() , "main");
    }
    @Override
    public void updateMainBoard(PositionWithColor[] p){
        //updateAll();
    }
    @Override
    public void updatePlayerBoard(String id, int column, Card[] c){
        updateAll();
    }



    /**
     * this method sorts the final table of the game and displays it
     * @param tableJ is a JsonObject formatted this way: { "[name1]": points1, [...] , "[nameN]": pointsN}
     */
    public void finalResults(JsonObject tableJ){
        Scanner sc = new Scanner(System.in);
        char selection;

        int n=((PlayingPlayer)player).getPlayersNumber();
        String[][] table = new String[n][n];
        for(int i=0;i<n; i++){
            table[i][0]= ((PlayingPlayer)player).getPlayersID()[i];
            table[i][1]= tableJ.get(((PlayingPlayer)player).getPlayersID()[i]).getAsString();
        }

        Arrays.sort(table, Comparator.comparingInt(row->Integer.parseInt(row[1])));

        printTitle();
        System.out.println("FINAL RESULTS:");
        for(int i=0;i<n;i++){
            System.out.println("#"+(i+1)+": "+table[i][0]+"\tpoints: "+table[i][1]);
        }


        do{
            System.out.println("Do you want to start a new game? [Y/N]");
            selection=charCommand();
            if(selection!='Y' && selection!='N')
                printError("Invalid selection, please try again");
        }while (selection!='Y' && selection!='N');

        if(selection=='Y')
            connection.setPlayerAsLobby();

        if(selection=='N')
            System.exit(0);
    }


    /**
     * @param n is the number of players connected to the game
     */
    public void receiveNumPlayers(int n){
        System.out.println(TextColor.LIGHTBLUE.get()+ "Players connected: "+n + TextColor.DEFAULT.get());
    }

    @Override
    public void acceptingPlayingCommand(){
        Thread thread = new Thread(()->{this.userInput();});
        thread.start();
    }
    @Override
    public void setMode(PlayerMode m){
        player = connection.getPlayer();

        switch (m){
            case LOBBY :
                this.centerContent(4);
                this.printTitle();
                this.printRun();
                break;
            case PLAYING:
                break;
        }

    }

    /**
     * this method calls the quitGame method on the server and prints an error message if this procedure fails
     */
    private void quitGame(){
        if(!((PlayingPlayer)player).quitGame())
            printError("Failed to quit the game");
    }

    /**
     * @param err is the string to print on the standard error stream
     */
    public void printError(String err){
        System.err.println(TextColor.RED.get() + err + TextColor.DEFAULT.get());
    }


    /**
     * @return an upper case char
     */
    private char charCommand(){
        Scanner sc = new Scanner(System.in);
        return(Character.toUpperCase(sc.next().charAt(0)));
    }


    /**
     * @return a lower case String
     */
    private String stringCommand() {
        Scanner sc = new Scanner(System.in);
        return (sc.nextLine().toLowerCase());
    }

    private void mainBoardToString(MainBoard board, String[][] s){
        for(int x=0;x<board.getColumns();x++)
            for (int y=0;y<board.getRows();y++) {
                s[x][y] = ColorCodes.valueOf(board.getBoard()[x][y].getColor().toString()).get() + "   ";
            }
    }

    private void playerBoardToString(PlayerBoard board, String[][] s){
        for(int x=0;x<board.getColumns();x++)
            for (int y=0;y<board.getRows();y++){
                s[x][y]= ColorCodes.valueOf(board.getBoard()[x][y].getColor().toString()).get() + "   ";
            }
    }

    private void playerBoardToString(PlayerBoard board,PositionWithColor[] goal, String[][] s) {
        String color;


        for (int column = 0; column < board.getColumns(); column++) {
            for (int row = 0; row < board.getRows(); row++) {
                color = ColorCodes.valueOf(board.getBoard()[column][row].getColor().toString()).get() ;
                s[column][row] = color + "   ";
                if (color.equals(EMPTY.get())) {
                    for (PositionWithColor p : goal) {
                        if(p.getX()==column && p.getY() == row){
                            s[column][row] = TextColor.valueOf(p.getColor().toString()).get() + color + " X ";
                        }
                    }
                }
            }
        }

    }

    private void printBoard(String[][] board, int columns,int rows, String whosBoard){
        String printLine;

        if(whosBoard.equals("main")){
            System.out.println(TextColor.LIGHTBLUE.get() +"\n\n Main board: " + TextColor.DEFAULT.get());
        } else if (whosBoard.equals(player.getPlayerID())) {
            System.out.println(TextColor.YELLOW.get() +"\n\n Your board: " + TextColor.DEFAULT.get());
        }
        else System.out.println(TextColor.LIGHTBLUE.get() +"\n\n" + whosBoard+"'s board: " + TextColor.DEFAULT.get());

        for(int y=rows-1;y>=0;y--){

            printLine = BROWN.get() + "   ";
            for (int x = 0; x<columns; x++){
                printLine = printLine + "    ";
            }
            printLine = printLine + "  ";
            System.out.println(printLine + DEFAULT.get());

            printLine = EMPTY.get();
            for(int x =0 ;x<columns;x++){
                printLine = printLine + board[x][y] + BROWN.get() + " ";
            }
            System.out.println(BROWN.get() + " " + y + " " + DEFAULT.get() + printLine + BROWN.get() + "  " + DEFAULT.get());
        }

        printLine = BROWN.get() + "   ";
        for (int x = 0; x<columns; x++){
            printLine = printLine + " " + x + "  ";
        }
        printLine = printLine + "  ";
        System.out.println(printLine + DEFAULT.get());
    }

    private void printBoard() {



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

}
