package it.polimi.ingsw.client.View;

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
import it.polimi.ingsw.shared.Connection.ConnectionType;
import it.polimi.ingsw.shared.JsonSupportClasses.Position;

import java.net.Socket;
import java.util.*;

import static it.polimi.ingsw.client.View.ColorCodes.*;
import static it.polimi.ingsw.shared.Connection.ConnectionType.*;


public class TUI {

    private Player player;

    private String printLine;

    private ConnectionManager connection;

    public TUI(Player player){
        this.player=player;
    }



    public void toRun(){
        System.out.println("WELCOME TO MYSHELFIE");
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
                connection = new ClientSOCKET(1234,"127.0.0.1");
            } catch (Exception e) {
                player.disconnectError("Server is offline");
            }
        }
        else{
            try {
                connection = new ClientRMI(1234,"127.0.0.1");
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
            user=sc.nextLine();
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

        MainBoard mainBoard;
        PlayerBoard playerBoard;

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
        Scanner sc = new Scanner(System.in);
        String selection;
        boolean success;

        success=false;
        do {
            System.out.println("Insert the number of players for this game (min 2, max 4)\nInsert /def to use default settings");
            selection = sc.nextLine();
            selection.toLowerCase();
            if (selection.equals("/def")) {
                success=((LobbyPlayer)player).createGame();
            } else if (Integer.parseInt(selection)>=2 && Integer.parseInt(selection)<=4) {
                success=((LobbyPlayer)player).createGame(Integer.parseInt(selection));
            }
        }while(!success);
    }

    /**
     * this method allows the player to join an existent game
     */
    public void joinGame(){
        Scanner sc = new Scanner(System.in);
        String selection;
        boolean success;

        success=false;
        do{
            System.out.println("Insert the name of a player to play with a friend, or /rand to join a random game");
            selection = sc.nextLine();
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

    public void takeCard(){
        Scanner sc = new Scanner(System.in);
        String selection;
        char sel;
        String[] picks;
        String[] tmpSel;
        String[] singlePick;
        Position[] positions;
        String tmpPos;

        do {
            System.out.println("Select the positions of the cards you want to take: x1,y1; [...]; xn,yn\n");
            selection = sc.nextLine();
            picks = selection.split(";");
        } while (picks.length<1 || picks.length>3);

        positions = new Position[picks.length];
        for(int i=0; i< picks.length;i++){
            singlePick=picks[i].split(",");
            positions[i]= new Position(Integer.parseInt(singlePick[0]),Integer.parseInt(singlePick[1]));
        }

        /** stampare carte selezionate */

        do {
            System.out.println("Do you want to change the order of the cards? Y/N");
            sel =charCommand();
        }while (sel!='Y' && sel!='N');

        if(sel=='Y'){
            do{
                System.out.println("Insert the new order, from the bottom to the top: bottom card, [...], top card\n");
                selection = sc.nextLine();
                tmpSel=selection.split(",");

                if(tmpSel.length==picks.length){
                    for(int i=0;i<picks.length;i++) {
                        tmpPos = picks[i];
                        picks[i]=picks[Integer.parseInt(tmpSel[i])];
                        picks[Integer.parseInt(tmpSel[i])]=tmpPos;
                    }
                }
                else printError("The number of tiles you want to reorder is different from the number of tiles you picked");

            }while(tmpSel.length!=picks.length);
        }
    }


    /**
     * @param err is the string to print on the standard error stream
     */
    public void printError(String err){
        System.err.println(err);
    }


    public char charCommand(){
        Scanner sc = new Scanner(System.in);
        return(Character.toUpperCase(sc.next().charAt(0)));
    }
}
